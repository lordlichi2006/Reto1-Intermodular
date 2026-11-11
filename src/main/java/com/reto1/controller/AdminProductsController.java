package com.reto1.controller;

import com.reto1.model.Product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn.CellEditEvent;

/**
 * Controller of the window Admin Products (adminProducts.fxml).
 * Add, edit and remove products and change their stock.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminProductsController extends BaseController {

    @FXML
    private Button btnUsers;
    @FXML
    private Button btnLog;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        applyAdminPermissions(btnUsers, btnLog);
        // TODO: applyAdminPermissions(btnUsers, btnLog), loadProducts()
        //       and loadSuppliers()
    }

    /**
     * Fills tblProducts with every product and its suppliers.
     */
    private void loadProducts() {
        // TODO: productDao.findAll()
    }

    /**
     * Fills cmbSupplier with every supplier.
     */
    private void loadSuppliers() {
        // TODO: supplierDao.findAll()
    }

    /**
     * Edit of a price cell in the table (onEditCommit).
     *
     * @param event cell edit event with the new value
     */
    @FXML
    private void editPrice(CellEditEvent<Product, Double> event) {
        // TODO: new value >= 0? productDao.update(product); if not, restore the cell
    }

    /**
     * Edit of a stock cell in the table (onEditCommit).
     *
     * @param event cell edit event with the new value
     */
    @FXML
    private void editStock(CellEditEvent<Product, Integer> event) {
        // TODO: new value >= 0? productDao.updateStock(id, stock); if not, restore
    }

    /**
     * Add product button: clears the form and changes to "new" mode.
     */
    @FXML
    private void addProduct() {
        // TODO: clearForm()
    }

    /**
     * Edit product button: loads the selected product in the form.
     */
    @FXML
    private void editProduct() {
        // TODO: product selected?
    }

    /**
     * Remove product button: deletes the selected product.
     */
    @FXML
    private void removeProduct() {
        // TODO: product selected? discountPackDao.existsByProduct(id) is false?
        //       confirm(...)? productDao.delete(id) and loadProducts()
    }

    /**
     * Confirm button: inserts or updates the product of the form.
     */
    @FXML
    private void saveProduct() {
        // TODO: if validateForm(): "new" mode -> productDao.insert(product),
        //       "edit" mode -> productDao.update(product); loadProducts()
    }

    /**
     * Empties every field of the form.
     */
    private void clearForm() {
        // TODO
    }

    /**
     * Checks the form: name, brand, price and stock filled, price and stock
     * numbers >= 0, a supplier chosen.
     *
     * @return {@code true} if the form is valid
     */
    private boolean validateForm() {
        boolean valid;

        // TODO
        valid = false;
        return valid;
    }
}
