package com.reto1.controller;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.dao.DiscountPackDao;
import com.reto1.dao.DiscountPackImplementacionBD;
import com.reto1.dao.ProductDao;
import com.reto1.dao.ProductImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.DiscountPack;
import com.reto1.model.Product;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Controller of the window Admin Offers (adminOffers.fxml).
 * Add, edit and remove discount packs.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminOffersController extends BaseController {

    /** Menu button USERS, hidden for workers that are not admins. */
    @FXML
    private Button btnUsers;
    /** Menu button LOG, hidden for workers that are not admins. */
    @FXML
    private Button btnLog;
    /** Table with every product; the selected rows are the products of the pack. */
    @FXML
    private TableView<Product> productsTable;
    /** Table with every discount pack. */
    @FXML
    private TableView<DiscountPack> offersTable;
    /** Field for the name of the pack. */
    @FXML
    private TextField tfName;
    /** Field for the units of the pack that can still be sold. */
    @FXML
    private TextField tfUnits;
    /** Field for the discount percentage of the pack, from 0 to 100. */
    @FXML
    private TextField tfDiscount;

    /** Data access object of the "discountPacks" collection. */
    private DiscountPackDao discountPackDao;
    /** Data access object of the "products" collection. */
    private ProductDao productDao;
    /** Pack loaded in the form with Edit offer, or null when a new pack is added. */
    private DiscountPack editingOffer;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        applyAdminPermissions(btnUsers, btnLog);
        configureTables();
        if (discountPackDao == null) {
            discountPackDao = new DiscountPackImplementacionBD();
        }
        if (productDao == null) {
            productDao = new ProductImplementacionBD();
        }
        loadProducts();
        loadOffers();
    }

    /**
     * Sets the DAO of the discount packs. The tests use it to pass a fake DAO.
     *
     * @param discountPackDao the DAO to use
     */
    public void setDiscountPackDao(DiscountPackDao discountPackDao) {
        this.discountPackDao = discountPackDao;
    }

    /**
     * Sets the DAO of the products. The tests use it to pass a fake DAO.
     *
     * @param productDao the DAO to use
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * Creates the columns of productsTable (name, brand, price and stock, with
     * multiple selection) and of offersTable (name, discount and units).
     */
    private void configureTables() {
        TableColumn<Product, String> productName = new TableColumn<>("Name");
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, String> brand = new TableColumn<>("Brand");
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Product, String> price = new TableColumn<>("Price");
        price.setCellValueFactory(new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Product, String> cell) {
                return Bindings.format("%.2f", cell.getValue().getPrice());
            }
        });
        TableColumn<Product, Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsTable.getColumns().setAll(productName, brand, price, stock);
        productsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<DiscountPack, String> offerName = new TableColumn<>("Name");
        offerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<DiscountPack, String> discount = new TableColumn<>("Discount");
        discount.setCellValueFactory(new Callback<CellDataFeatures<DiscountPack, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<DiscountPack, String> cell) {
                return Bindings.format("%.2f%%", cell.getValue().getDiscountPercentage());
            }
        });
        TableColumn<DiscountPack, Integer> units = new TableColumn<>("Units");
        units.setCellValueFactory(new PropertyValueFactory<>("remainingUnits"));
        offersTable.getColumns().setAll(offerName, discount, units);
    }

    
    /** Fills productsTable with every product of the database. */
    private void loadProducts() {
        try {
            productsTable.setItems(FXCollections.observableArrayList(productDao.findAll()));
        } catch (DatabaseException e) {
            logger.severe("Unable to load products for offers: " + e.getMessage());
            showError(e.getMessage(), "Products");
        }
    }
    /**
     * Fills tblOffers with every pack.
     */
    private void loadOffers() {
        try {
            offersTable.setItems(FXCollections.observableArrayList(discountPackDao.findAll()));
        } catch (DatabaseException e) {
            logger.severe("Unable to load offers: " + e.getMessage());
            showError(e.getMessage(), "Offers");
        }
    }

    /**
     * Click on a pack: selects its products in tblOfferProducts.
     */
    @FXML
    private void selectOffer() {
        DiscountPack selected = offersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        productsTable.getSelectionModel().clearSelection();
        for (Product product : productsTable.getItems()) {
            if (selected.getProductIds().contains(product.getId())) {
                productsTable.getSelectionModel().select(product);
            }
        }
    }

    /**
     * Add offer button: clears the form and changes to "new" mode.
     */
    @FXML
    private void addOffer() {
        editingOffer = null;
        offersTable.getSelectionModel().clearSelection();
        clearForm();
    }

    /**
     * Edit offer button: loads the selected pack in the form.
     */
    @FXML
    private void editOffer() {
        DiscountPack selected = offersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select an offer to edit.", "Offers");
            return;
        }
        editingOffer = selected;
        tfName.setText(selected.getName());
        tfUnits.setText(Integer.toString(selected.getRemainingUnits()));
        tfDiscount.setText(Double.toString(selected.getDiscountPercentage()));
        productsTable.getSelectionModel().clearSelection();
        for (Product product : productsTable.getItems()) {
            if (selected.getProductIds().contains(product.getId())) {
                productsTable.getSelectionModel().select(product);
            }
        }
    }

    /**
     * Remove offer button: deletes the selected pack.
     */
    @FXML
    private void removeOffer() {
        DiscountPack selected = offersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select an offer to remove.", "Offers");
            return;
        }
        if (!confirm("Are you sure you want to remove this offer?", "Remove offer")) {
            return;
        }
        try {
            if (!discountPackDao.delete(selected.getId())) {
                showError("The offer no longer exists.", "Offers");
                loadOffers();
                return;
            }
            loadOffers();
            clearForm();
        } catch (DatabaseException e) {
            logger.severe("Unable to remove offer: " + e.getMessage());
            showError(e.getMessage(), "Offers");
        }
    }

     /**
     * Confirm button: inserts or updates the pack of the form.
     */
    @FXML
    private void saveOffer() {
        if (!validateForm()) {
            return;
        }
        DiscountPack offer = editingOffer == null ? new DiscountPack() : editingOffer;
        offer.setName(tfName.getText().trim());
        offer.setRemainingUnits(Integer.parseInt(tfUnits.getText().trim()));
        offer.setDiscountPercentage(Double.parseDouble(tfDiscount.getText().trim().replace(',', '.')));
        List<ObjectId> productIds = new ArrayList<>();
        for (Product product : productsTable.getSelectionModel().getSelectedItems()) {
            productIds.add(product.getId());
        }
        offer.setProductIds(new ArrayList<>(productIds));
        try {
            if (editingOffer == null) {
                discountPackDao.insert(offer);
            } else if (!discountPackDao.update(offer)) {
                showError("The offer no longer exists.", "Offers");
                loadOffers();
                return;
            }
            loadOffers();
            clearForm();
            showInfo("Offer saved successfully.", "Offers");
        } catch (DatabaseException e) {
            logger.severe("Unable to save offer: " + e.getMessage());
            showError(e.getMessage(), "Offers");
        }
    }

    /**
     * Empties every field of the form and the product selection.
     */
    private void clearForm() {
        tfName.clear();
        tfUnits.clear();
        tfDiscount.clear();
        productsTable.getSelectionModel().clearSelection();
        editingOffer = null;
    }

    /**
     * Checks the form: name filled, discount between 0 and 100, units a whole
     * number >= 0, at least one product selected.
     *
     * @return {@code true} if the form is valid
     */
    private boolean validateForm() {
        if (tfName.getText() == null || tfName.getText().trim().isEmpty()) {
            showError("Enter an offer name.", "Invalid offer");
            return false;
        }
        if (productsTable.getSelectionModel().getSelectedItems().isEmpty()) {
            showError("Select at least one product.", "Invalid offer");
            return false;
        }
        try {
            int units = Integer.parseInt(tfUnits.getText().trim());
            double discount = Double.parseDouble(tfDiscount.getText().trim().replace(',', '.'));
            if (units < 0 || discount < 0 || discount > 100) {
                showError("Units must be zero or more and discount must be between 0 and 100.", "Invalid offer");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Enter whole units and a valid discount percentage.", "Invalid offer");
            return false;
        }
        return true;
    }
}
