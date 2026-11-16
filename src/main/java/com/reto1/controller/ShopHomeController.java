package com.reto1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.reto1.dao.ProductImplementacionBD;
import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.CartItem;
import com.reto1.model.Customer;
import com.reto1.model.Product;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * Controller of the window Main View (shopHome.fxml).
 * Shop catalogue: search products and add them to the cart.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class ShopHomeController extends BaseController {

    @FXML
    private TableView<Product> tblProducts;

    @FXML
    private TableColumn<Product, String> colName;

    @FXML
    private TableColumn<Product, String> colBrand;

    @FXML
    private TableColumn<Product, Double> colPrice;

    @FXML
    private TableColumn<Product, Integer> colStock;

    @FXML
    private TextField txtSearchbox;

    @FXML
    private Label lblError;

    @FXML
    private TextField txtAmount;

    ArrayList<Product> products;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        lblError.setText("");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        try {
            products = getProducts();
        } catch (DatabaseException e) {
            lblError.setText("Internal error: " + e.getMessage());
            return;
        }
        loadProducts(products, "");
    }

    /**
     * Fills tblProducts with every product (name, brand, price, stock).
     */
    private void loadProducts(ArrayList<Product> products, String query) {
        if (products.isEmpty()) {
            lblError.setText("Error: there arent any products to show.");
            return;
        }
        ArrayList<Product> filteredProducts;
        if (!query.isEmpty()) {
            filteredProducts = new ArrayList<>(
                    products.stream()
                            .filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList()));
        } else {
            filteredProducts = products;
        }
        tblProducts.getItems().setAll(filteredProducts);
    }

    /**
     * gets the products from the database.
     */
    private ArrayList<Product> getProducts() throws DatabaseException {
        ProductImplementacionBD productsDb = new ProductImplementacionBD();
        ArrayList<Product> products = productsDb.findAll();
        return products;
    }

    /**
     * txtSearchbox (onKeyTyped): filters the table by the typed text.
     *
     * @param event key event
     */
    @FXML
    void search(KeyEvent event) {
        String query = txtSearchbox.getText();
        loadProducts(products, query);
    }

    /**
     * Add To Cart button: adds the selected product with the units of txtAmount.
     */
    @FXML
    private void addToCart() {
        // Validation
        Product selectedProduct = tblProducts.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            return;
        }
        if (!checkAmountValid()) {
            return;
        }

        if (loggedUser == null || !(loggedUser instanceof Customer)) {
            return;
        }

        // Create Cart Item
        int units = Integer.parseInt(txtAmount.getText());

        CartItem cartItem = new CartItem(selectedProduct, units);

        Customer customer = (Customer) loggedUser;

        List<CartItem> previousCart = customer.getCart();
        List<CartItem> updatedCart = new ArrayList<>(previousCart);

        updatedCart.add(cartItem);
        customer.setCart(updatedCart);

        try {
            UserDao userDao = new UserImplementacionBD();
            if (!userDao.update(customer)) {
                customer.setCart(previousCart);
                showError("Could not save your cart.", "Database Error");
                return;
            }
            showInfo("Items added sucessfuly to cart.", "Cart");
        } catch (DatabaseException e) {
            customer.setCart(previousCart);
            showError("Could not save your cart: " + e.getMessage(), "Database Error");
        }
    }

    @FXML
    private boolean checkAmountValid() {
        String amount = txtAmount.getText();
        try {
            int amountInt = Integer.parseInt(amount);
            if (amountInt < 1) {
                lblError.setText("Error: Product can not be negative.");
                txtAmount.setText("1");
                return false;
            }
        } catch (NumberFormatException e) {
            lblError.setText("Error: Product amount is not a valid value.");
            txtAmount.setText("1");
            return false;
        }
        return true;
    }
}
