package com.reto1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import com.reto1.dao.DiscountPackDao;
import com.reto1.dao.ProductDao;
import com.reto1.dao.ShipmentDao;
import com.reto1.dao.UserDao;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Customer;
import com.reto1.model.DiscountPack;
import com.reto1.model.Product;
import com.reto1.model.Role;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentStatus;
import com.reto1.model.User;
import com.reto1.model.Worker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

class AdminViewsTest extends ApplicationTest {

    private Parent offersRoot;
    private Parent usersRoot;
    private Parent profileRoot;
    private Parent shipmentsRoot;
    private Stage stage;
    private ScreenUserDao userDao;
    private TrackingShipmentDao shipmentDao;
    private Worker loggedAdmin;
    private Customer customerWithOpenShipment;
    private TestAdminShipmentsController shipmentsController;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        loggedAdmin = new Worker("Admin", "admin@example.com", "secret", "12345678A", Role.ADMIN);
        loggedAdmin.setId(new ObjectId());
        customerWithOpenShipment = new Customer();
        customerWithOpenShipment.setId(new ObjectId());
        userDao = new ScreenUserDao();
        userDao.users.add(loggedAdmin);
        userDao.users.add(customerWithOpenShipment);
        shipmentDao = new TrackingShipmentDao();
        shipmentDao.shipments.add(shipment(ShipmentStatus.IN_PROGRESS));
        shipmentDao.shipments.add(shipment(ShipmentStatus.IN_SHIPMENT));
        shipmentDao.shipments.add(shipment(ShipmentStatus.ARRIVED));
        shipmentDao.shipments.add(shipment(ShipmentStatus.CANCELLED));
        BaseController.loggedUser = loggedAdmin;
        offersRoot = loadView("/com/reto1/view/adminOffers.fxml");
        usersRoot = loadView("/com/reto1/view/adminUsers.fxml");
        shipmentsRoot = loadView("/com/reto1/view/adminShipments.fxml");
        BaseController.loggedUser = customerWithOpenShipment;
        profileRoot = loadView("/com/reto1/view/shopProfile.fxml");
        stage.setScene(new Scene(profileRoot));
        stage.show();
    }

    private Parent loadView(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerType) {
                if (controllerType == AdminOffersController.class) {
                    AdminOffersController controller = new AdminOffersController();
                    controller.setDiscountPackDao(new EmptyDiscountPackDao());
                    controller.setProductDao(new EmptyProductDao());
                    return controller;
                }
                if (controllerType == AdminUsersController.class) {
                    AdminUsersController controller = new AdminUsersController();
                    controller.setUserDao(userDao);
                    controller.setShipmentDao(shipmentDao);
                    return controller;
                }
                if (controllerType == AdminShipmentsController.class) {
                    shipmentsController = new TestAdminShipmentsController();
                    shipmentsController.setShipmentDao(shipmentDao);
                    return shipmentsController;
                }
                if (controllerType == ShopProfileController.class) {
                    ShopProfileController controller = new ShopProfileController();
                    controller.setUserDao(userDao);
                    return controller;
                }
                throw new IllegalArgumentException("Unexpected controller: " + controllerType.getName());
            }
        });
        return loader.load();
    }

    @AfterEach
    public void tearDown() throws TimeoutException {
        BaseController.loggedUser = null;
        FxToolkit.hideStage();
    }

    @Test
    public void managementViewsLoadWithTablesAndProfileControls() {
        assertEquals(4, ((TableView<?>) offersRoot.lookup("#productsTable")).getColumns().size());
        assertEquals(3, ((TableView<?>) offersRoot.lookup("#offersTable")).getColumns().size());
        assertEquals(4, ((TableView<?>) usersRoot.lookup("#userTable")).getColumns().size());
        assertEquals(2, ((TableView<?>) shipmentsRoot.lookup("#shipmentTable")).getItems().size());
        assertInstanceOf(ComboBox.class, usersRoot.lookup("#cmbType"));
        assertInstanceOf(ComboBox.class, usersRoot.lookup("#cmbRole"));
        assertInstanceOf(ListView.class, usersRoot.lookup("#lstAddresses"));
        assertInstanceOf(ComboBox.class, profileRoot.lookup("#cmbType"));
        assertInstanceOf(ListView.class, profileRoot.lookup("#lstAddresses"));
    }

    @Test
    public void loggedInUserCannotBeDeletedAndOpenShipmentBlocksCustomerDeletion() {
        TableView<User> usersTable = (TableView<User>) usersRoot.lookup("#userTable");
        interact(new Runnable() {
            @Override
            public void run() {
                stage.setScene(new Scene(usersRoot));
                usersTable.getSelectionModel().select(loggedAdmin);
            }
        });
        assertTrue(usersRoot.lookup("#btnDelete").isDisabled());

        interact(new Runnable() {
            @Override
            public void run() {
                usersTable.getSelectionModel().select(customerWithOpenShipment);
            }
        });
        assertFalse(usersRoot.lookup("#btnDelete").isDisabled());
        clickOn("#btnDelete");
        clickOn("OK");
        assertTrue(shipmentDao.checked);
        assertFalse(userDao.deleted);
    }

    @Test
    public void adminCanUpdateAndCancelActiveShipments() {
        TableView<Shipment> table = (TableView<Shipment>) shipmentsRoot.lookup("#shipmentTable");
        Shipment inProgress = table.getItems().get(0);
        Shipment inShipment = table.getItems().get(1);
        ComboBox<ShipmentStatus> status = (ComboBox<ShipmentStatus>) shipmentsRoot.lookup("#cmbStatus");

        interact(new Runnable() {
            @Override
            public void run() {
                stage.setScene(new Scene(shipmentsRoot));
                table.getSelectionModel().select(inProgress);
                status.getSelectionModel().select(ShipmentStatus.ARRIVED);
            }
        });
        clickOn("#btnConfirm");
        assertEquals(ShipmentStatus.ARRIVED, inProgress.getStatus());
        assertEquals(1, table.getItems().size());
        assertEquals("Shipment status updated successfully.", shipmentsController.infoMessage);

        interact(new Runnable() {
            @Override
            public void run() {
                table.getSelectionModel().select(inShipment);
            }
        });
        clickOn("#btnCancel");
        assertEquals(ShipmentStatus.CANCELLED, inShipment.getStatus());
        assertTrue(table.getItems().isEmpty());
        assertTrue(shipmentsController.confirmationRequested);
        assertNull(shipmentsController.errorMessage);
    }

    @Test
    public void editingWorkerShowsRolesAndEditingCustomerAllowsAddingAddress() {
        TableView<User> usersTable = (TableView<User>) usersRoot.lookup("#userTable");
        ComboBox<Role> roleCombo = (ComboBox<Role>) usersRoot.lookup("#cmbRole");
        ListView<String> addresses = (ListView<String>) usersRoot.lookup("#lstAddresses");
        TextField newAddress = (TextField) usersRoot.lookup("#tfNewAddress");

        interact(new Runnable() {
            @Override
            public void run() {
                stage.setScene(new Scene(usersRoot));
                usersTable.getSelectionModel().select(loggedAdmin);
            }
        });
        clickOn("#btnEdit");
        assertFalse(roleCombo.isDisabled());
        for (Role role : Role.values()) {
            assertTrue(roleCombo.getItems().contains(role));
        }

        interact(new Runnable() {
            @Override
            public void run() {
                usersTable.getSelectionModel().select(customerWithOpenShipment);
            }
        });
        clickOn("#btnEdit");
        interact(new Runnable() {
            @Override
            public void run() {
                newAddress.setText("New test address");
            }
        });
        clickOn("#btnAddAddress");
        assertTrue(addresses.getItems().contains("New test address"));
    }

    private Shipment shipment(ShipmentStatus status) {
        Shipment shipment = new Shipment(new ObjectId(), "Test address", new ArrayList<>(), 10.0, "Test carrier");
        shipment.setId(new ObjectId());
        shipment.setStatus(status);
        return shipment;
    }

    private static class TestAdminShipmentsController extends AdminShipmentsController {
        private String errorMessage;
        private String infoMessage;
        private boolean confirmationRequested;

        @Override
        protected void showError(String message, String header) {
            errorMessage = message;
        }

        @Override
        protected void showInfo(String message, String header) {
            infoMessage = message;
        }

        @Override
        protected boolean confirm(String message, String header) {
            confirmationRequested = true;
            return true;
        }
    }

    private static class EmptyProductDao implements ProductDao {
        @Override public ArrayList<Product> findAll() { return new ArrayList<>(); }
        @Override public Product findById(ObjectId id) { return null; }
        @Override public ArrayList<Product> findByName(String text) { return new ArrayList<>(); }
        @Override public void insert(Product product) { }
        @Override public boolean update(Product product) { return false; }
        @Override public boolean updateStock(ObjectId id, int stock) { return false; }
        @Override public boolean decreaseStock(ObjectId id, int units) { return false; }
        @Override public boolean delete(ObjectId id) { return false; }
    }

    private static class EmptyDiscountPackDao implements DiscountPackDao {
        @Override public ArrayList<DiscountPack> findAll() { return new ArrayList<>(); }
        @Override public DiscountPack findById(ObjectId id) { return null; }
        @Override public void insert(DiscountPack pack) { }
        @Override public boolean update(DiscountPack pack) { return false; }
        @Override public boolean delete(ObjectId id) { return false; }
        @Override public boolean existsByProduct(ObjectId productId) { return false; }
    }

    private static class TrackingShipmentDao implements ShipmentDao {
        private boolean checked;
        private final ArrayList<Shipment> shipments = new ArrayList<>();

        @Override public void insert(Shipment shipment) { }
        @Override public Shipment findById(ObjectId id) { return null; }
        @Override public ArrayList<Shipment> findAll() { return new ArrayList<>(shipments); }
        @Override public ArrayList<Shipment> findByCustomer(ObjectId customerId) { return new ArrayList<>(); }
        @Override public boolean updateStatus(ObjectId id, ShipmentStatus status) {
            for (Shipment shipment : shipments) {
                if (shipment.getId().equals(id)) {
                    shipment.setStatus(status);
                    return true;
                }
            }
            return false;
        }
        @Override public boolean existsInProgressByCustomer(ObjectId customerId) {
            checked = true;
            return true;
        }
    }

    private static class ScreenUserDao implements UserDao {
        private final ArrayList<User> users = new ArrayList<>();
        private boolean deleted;

        @Override public User login(String email, String password) { return null; }
        @Override public User findByEmail(String email) {
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    return user;
                }
            }
            return null;
        }
        @Override public boolean existsByEmail(String email) { return findByEmail(email) != null; }
        @Override public ArrayList<User> findAll() { return new ArrayList<>(users); }
        @Override public User findById(ObjectId id) {
            for (User user : users) {
                if (user.getId().equals(id)) {
                    return user;
                }
            }
            return null;
        }
        @Override public void insert(User user) { users.add(user); }
        @Override public boolean update(User user) { return true; }
        @Override public boolean delete(ObjectId id) {
            deleted = true;
            return true;
        }
    }
}
