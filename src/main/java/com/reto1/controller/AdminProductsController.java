package com.reto1.controller;

import java.util.ArrayList;
import java.util.List;

import com.reto1.dao.DiscountPackDao;
import com.reto1.dao.DiscountPackImplementacionBD;
import com.reto1.dao.ProductDao;
import com.reto1.dao.ProductImplementacionBD;
import com.reto1.dao.SupplierDao;
import com.reto1.dao.SupplierImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Product;
import com.reto1.model.ProductSupplier;
import com.reto1.model.Supplier;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Controller of the window Admin Products (adminProducts.fxml).
 * Add, edit and remove products. A product can only be removed if it is
 * not in any discount pack. The price and the stock can also be edited in
 * the table: double click on the cell, type the new value and press Enter.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminProductsController extends BaseController {

    /** Menu button USERS, hidden for workers that are not admins. */
    @FXML
    private Button btnUsers;
    /** Menu button LOG, hidden for workers that are not admins. */
    @FXML
    private Button btnLog;
    /** Table with every product. */
    @FXML
    private TableView<Product> productTable;
    /** Column with the name of the product. */
    @FXML
    private TableColumn<Product, String> colName;
    /** Column with the brand of the product. */
    @FXML
    private TableColumn<Product, String> colBrand;
    /** Column with the price of the product. */
    @FXML
    private TableColumn<Product, Double> colPrice;
    /** Column with the stock of the product. */
    @FXML
    private TableColumn<Product, Integer> colStock;
    /** Column with the names of the suppliers of the product. */
    @FXML
    private TableColumn<Product, String> colSupplier;
    /** Field for the name. */
    @FXML
    private TextField tfName;
    /** Field for the brand. */
    @FXML
    private TextField tfBrand;
    /** Field for the price. */
    @FXML
    private TextField tfPrice;
    /** Field for the stock. */
    @FXML
    private TextField tfStock;
    /** Field for the description. */
    @FXML
    private TextArea taDesc;
    /** Combo with every supplier of the "suppliers" collection. */
    @FXML
    private ComboBox<Supplier> cmbSupplier;

    /** Data access object of the products. */
    private ProductDao productDao;
    /** Data access object of the suppliers. */
    private SupplierDao supplierDao;
    /** Data access object of the discount packs. */
    private DiscountPackDao packDao;
    /** Product loaded in the form with Edit product, or null when adding a new one. */
    private Product editingProduct;

    /**
     * Runs when the window opens: prepares the columns and the supplier combo
     * and loads the suppliers and the products.
     */
    @FXML
    private void initialize() {
        applyAdminPermissions(btnUsers, btnLog);
        productDao = new ProductImplementacionBD();
        supplierDao = new SupplierImplementacionBD();
        packDao = new DiscountPackImplementacionBD();
        editingProduct = null;

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colSupplier.setCellValueFactory(
                new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Product, String> data) {
                        return new SimpleStringProperty(supplierNames(data.getValue()));
                    }
                });
        configureEditableCells();
        cmbSupplier.setConverter(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier supplier) {
                String text;

                text = "";
                if (supplier != null) {
                    text = supplier.getName();
                }
                return text;
            }

            @Override
            public Supplier fromString(String text) {
                return null;
            }
        });

        loadSuppliers();
        loadProducts();
    }

    /**
     * Makes the price and stock cells of productTable editable with a
     * TextFieldTableCell. When the user presses Enter, the onEditCommit
     * event calls {@link #savePrice} or {@link #saveStock}.
     */
    private void configureEditableCells() {
        productTable.setEditable(true);
        colPrice.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                String text;

                text = "";
                if (value != null) {
                    text = String.valueOf(value);
                }
                return text;
            }

            @Override
            public Double fromString(String text) {
                Double value;

                try {
                    value = Double.valueOf(text.trim().replace(',', '.'));
                } catch (NumberFormatException e) {
                    value = null;
                }
                return value;
            }
        }));
        colStock.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                String text;

                text = "";
                if (value != null) {
                    text = String.valueOf(value);
                }
                return text;
            }

            @Override
            public Integer fromString(String text) {
                Integer value;

                try {
                    value = Integer.valueOf(text.trim());
                } catch (NumberFormatException e) {
                    value = null;
                }
                return value;
            }
        }));
        colPrice.setOnEditCommit(new EventHandler<CellEditEvent<Product, Double>>() {
            @Override
            public void handle(CellEditEvent<Product, Double> event) {
                savePrice(event.getRowValue(), event.getNewValue());
            }
        });
        colStock.setOnEditCommit(new EventHandler<CellEditEvent<Product, Integer>>() {
            @Override
            public void handle(CellEditEvent<Product, Integer> event) {
                saveStock(event.getRowValue(), event.getNewValue());
            }
        });
    }

    /**
     * onEditCommit of colPrice: checks the new price and saves the product
     * with {@link ProductDao#update}. If the price is not valid or the
     * database fails, the product keeps its old price and an error is shown.
     *
     * @param product  product of the edited row
     * @param newPrice price typed in the cell, or null if it is not a number
     */
    private void savePrice(Product product, Double newPrice) {
        double oldPrice;

        oldPrice = product.getPrice();
        if (newPrice == null || newPrice < 0 || newPrice.isNaN() || newPrice.isInfinite()) {
            showError("The price must be a number greater than or equal to 0.", "Invalid price");
        } else {
            product.setPrice(newPrice);
            try {
                if (productDao.update(product)) {
                    logger.info("Admin Products: price of " + product.getName() + " changed from "
                            + oldPrice + " to " + newPrice);
                    if (product == editingProduct) {
                        tfPrice.setText(String.valueOf(newPrice));
                    }
                } else {
                    product.setPrice(oldPrice);
                    showError("The product no longer exists.", "Product not saved");
                    loadProducts();
                }
            } catch (DatabaseException e) {
                product.setPrice(oldPrice);
                logger.severe("Admin Products: error changing the price: " + e.getMessage());
                showError(e.getMessage(), "Database Error");
            }
        }
        productTable.refresh();
    }

    /**
     * onEditCommit of colStock: checks the new stock and saves it with
     * {@link ProductDao#updateStock}. If the stock is not valid or the
     * database fails, the product keeps its old stock and an error is shown.
     *
     * @param product  product of the edited row
     * @param newStock stock typed in the cell, or null if it is not a whole number
     */
    private void saveStock(Product product, Integer newStock) {
        int oldStock;

        oldStock = product.getStock();
        if (newStock == null || newStock < 0) {
            showError("The stock must be a whole number greater than or equal to 0.", "Invalid stock");
        } else {
            try {
                if (productDao.updateStock(product.getId(), newStock)) {
                    product.setStock(newStock);
                    logger.info("Admin Products: stock of " + product.getName() + " changed from "
                            + oldStock + " to " + newStock);
                    if (product == editingProduct) {
                        tfStock.setText(String.valueOf(newStock));
                    }
                } else {
                    showError("The product no longer exists.", "Product not saved");
                    loadProducts();
                }
            } catch (DatabaseException e) {
                logger.severe("Admin Products: error changing the stock: " + e.getMessage());
                showError(e.getMessage(), "Database Error");
            }
        }
        productTable.refresh();
    }

    /**
     * Fills productTable with every product of the database.
     */
    private void loadProducts() {
        try {
            productTable.setItems(FXCollections.observableArrayList(productDao.findAll()));
        } catch (DatabaseException e) {
            logger.severe("Admin Products: error loading products: " + e.getMessage());
            showError(e.getMessage(), "Database Error");
        }
    }

    /**
     * Fills cmbSupplier with every supplier of the database.
     */
    private void loadSuppliers() {
        try {
            cmbSupplier.setItems(FXCollections.observableArrayList(supplierDao.findAll()));
        } catch (DatabaseException e) {
            logger.severe("Admin Products: error loading suppliers: " + e.getMessage());
            showError(e.getMessage(), "Database Error");
        }
    }

    /**
     * Joins the names of the suppliers of a product, separated by commas.
     *
     * @param product product of the row
     * @return the names, or an empty text if it has no suppliers
     */
    private String supplierNames(Product product) {
        StringBuilder names;
        List<ProductSupplier> suppliers;

        names = new StringBuilder();
        suppliers = product.getSuppliers();
        if (suppliers != null) {
            for (ProductSupplier supplier : suppliers) {
                if (names.length() > 0) {
                    names.append(", ");
                }
                names.append(supplier.getName());
            }
        }
        return names.toString();
    }

    /**
     * Looks in cmbSupplier for the first supplier of a product, by its _id.
     * The loop stops as soon as it is found thanks to the variable found.
     *
     * @param product product loaded in the form
     * @return the supplier of the combo, or null if it is not there
     */
    private Supplier findSupplier(Product product) {
        Supplier result;
        boolean found;
        int i;

        result = null;
        found = false;
        i = 0;
        if (product.getSuppliers() != null && !product.getSuppliers().isEmpty()) {
            while (i < cmbSupplier.getItems().size() && !found) {
                if (cmbSupplier.getItems().get(i).getId()
                        .equals(product.getSuppliers().get(0).getSupplierId())) {
                    result = cmbSupplier.getItems().get(i);
                    found = true;
                }
                i++;
            }
        }
        return result;
    }

    /**
     * Add product button: clears the form and changes to "new" mode.
     */
    @FXML
    private void addProduct() {
        clearForm();
        tfName.requestFocus();
        logger.info("Admin Products: new product mode");
    }

    /**
     * Edit product button: loads the selected product in the form and
     * changes to "edit" mode.
     */
    @FXML
    private void editProduct() {
        Product selected;

        selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a product in the table.", "No product selected");
        } else {
            editingProduct = selected;
            tfName.setText(selected.getName());
            tfBrand.setText(selected.getBrand());
            tfPrice.setText(String.valueOf(selected.getPrice()));
            tfStock.setText(String.valueOf(selected.getStock()));
            taDesc.setText(selected.getDescription() == null ? "" : selected.getDescription());
            cmbSupplier.getSelectionModel().select(findSupplier(selected));
            logger.info("Admin Products: editing product " + selected.getName());
        }
    }

    /**
     * Remove product button: deletes the selected product after asking for
     * confirmation. It is not deleted if it is in a discount pack.
     */
    @FXML
    private void removeProduct() {
        Product selected;

        selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a product in the table.", "No product selected");
        } else {
            try {
                if (packDao.existsByProduct(selected.getId())) {
                    showError("The product is in a discount pack, remove it from the pack first.",
                            "Product in an offer");
                } else if (confirm("Do you want to remove " + selected.getName() + "?", "Remove product")) {
                    productDao.delete(selected.getId());
                    logger.info("Admin Products: product removed " + selected.getName());
                    clearForm();
                    loadProducts();
                }
            } catch (DatabaseException e) {
                logger.severe("Admin Products: error removing product: " + e.getMessage());
                showError(e.getMessage(), "Database Error");
            }
        }
    }

    /**
     * Confirm button: inserts the product of the form ("new" mode) or
     * updates the product being edited ("edit" mode).
     */
    @FXML
    private void saveProduct() {
        Product product;
        Supplier supplier;
        ArrayList<ProductSupplier> suppliers;
        boolean saved;

        if (validateForm()) {
            supplier = cmbSupplier.getSelectionModel().getSelectedItem();
            suppliers = new ArrayList<>();
            suppliers.add(new ProductSupplier(supplier.getId(), supplier.getName(), supplier.getCif()));
            product = new Product(tfName.getText().trim(), tfBrand.getText().trim(), readPrice(),
                    Integer.parseInt(tfStock.getText().trim()), taDesc.getText().trim(), suppliers);
            try {
                if (editingProduct == null) {
                    productDao.insert(product);
                    saved = true;
                } else {
                    product.setId(editingProduct.getId());
                    saved = productDao.update(product);
                }
                if (saved) {
                    logger.info("Admin Products: product saved " + product.getName());
                    showInfo("The product " + product.getName() + " has been saved.", "Product saved");
                    clearForm();
                    loadProducts();
                } else {
                    showError("The product no longer exists.", "Product not saved");
                }
            } catch (DatabaseException e) {
                logger.severe("Admin Products: error saving product: " + e.getMessage());
                showError(e.getMessage(), "Database Error");
            }
        }
    }

    /**
     * Reads the price of the form. A comma is accepted as decimal separator.
     *
     * @return the price typed by the user
     * @throws NumberFormatException if the text is not a number
     */
    private double readPrice() {
        return Double.parseDouble(tfPrice.getText().trim().replace(',', '.'));
    }

    /**
     * Empties every field of the form and goes back to "new" mode.
     */
    private void clearForm() {
        editingProduct = null;
        tfName.clear();
        tfBrand.clear();
        tfPrice.clear();
        tfStock.clear();
        taDesc.clear();
        cmbSupplier.getSelectionModel().clearSelection();
        productTable.getSelectionModel().clearSelection();
    }

    /**
     * Checks the form: name, brand, price and stock filled, price and stock
     * numbers greater than or equal to 0, and a supplier chosen. Shows a
     * popup with the first error found.
     *
     * @return {@code true} if the form is valid
     */
    private boolean validateForm() {
        boolean valid;
        double price;
        int stock;

        valid = true;
        if (tfName.getText().trim().isEmpty() || tfBrand.getText().trim().isEmpty()
                || tfPrice.getText().trim().isEmpty() || tfStock.getText().trim().isEmpty()) {
            showError("Fill in the name, brand, price and stock.", "Missing fields");
            valid = false;
        } else {
            try {
                price = readPrice();
                stock = Integer.parseInt(tfStock.getText().trim());
                if (price < 0 || stock < 0 || Double.isNaN(price) || Double.isInfinite(price)) {
                    valid = false;
                }
            } catch (NumberFormatException e) {
                valid = false;
            }
            if (!valid) {
                showError("The price and the stock must be numbers greater than or equal to 0.",
                        "Invalid price or stock");
            } else if (cmbSupplier.getSelectionModel().getSelectedItem() == null) {
                showError("Choose a supplier.", "Missing supplier");
                valid = false;
            }
        }
        return valid;
    }
}
