package com.reto1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.Role;
import com.reto1.model.Worker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

/**
 * TestFX tests of the login window (loginView.fxml and {@link LoginController}).
 * The window works with a {@link FakeUserDao}, so MongoDB is not needed.
 */
public class LoginControllerTest extends ApplicationTest {

    /**
     * Error message shown when the credentials are wrong.
     */
    private static final String WRONG_CREDENTIALS = "Incorrect Password or Email.";

    /**
     * Error message shown when the database is not available.
     */
    private static final String SERVER_ERROR = "Server Error, please try again later.";

    /**
     * Fake data access object passed to the controller in each test.
     */
    private FakeUserDao fakeDao;

    /**
     * Opens the login window before each test, with a new fake DAO.
     *
     * @param stage the stage created by TestFX
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader;
        Parent root;
        LoginController controller;

        loader = new FXMLLoader(getClass().getResource("/com/reto1/view/loginView.fxml"));
        root = loader.load();
        controller = loader.getController();
        fakeDao = new FakeUserDao();
        controller.setUserDao(fakeDao);
        stage.setScene(new Scene(root, 270, 340));
        stage.show();
    }

    /**
     * Closes the window and releases keys and mouse buttons after each test.
     *
     * @throws TimeoutException if the window does not close in time
     */
    @AfterEach
    public void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /**
     * Types the email and the password in the login fields.
     *
     * @param email text for the email field
     * @param password text for the password field
     */
    private void writeCredentials(String email, String password) {
        clickOn("#txtEmail").write(email);
        clickOn("#txtPassword").write(password);
    }

    /**
     * The error label starts empty when the window opens.
     */
    @Test
    public void testErrorLabelIsEmptyAtStart() {
        verifyThat("#lblError", hasText(""));
    }

    /**
     * The window sends to the DAO exactly the email and password typed.
     */
    @Test
    public void testTypedCredentialsAreSentToDao() {
        writeCredentials("mikel@gozoshop.com", "1234");
        clickOn("#btnLogin");
        assertEquals("mikel@gozoshop.com", fakeDao.getLastEmail());
        assertEquals("1234", fakeDao.getLastPassword());
    }

    /**
     * Wrong email or password shows the error message.
     */
    @Test
    public void testWrongCredentialsShowError() {
        fakeDao.setUserToReturn(null);
        writeCredentials("nobody@gozoshop.com", "wrong");
        clickOn("#btnLogin");
        verifyThat("#lblError", hasText(WRONG_CREDENTIALS));
    }

    /**
     * Pressing Login with the fields empty shows the error message.
     */
    @Test
    public void testEmptyFieldsShowError() {
        clickOn("#btnLogin");
        verifyThat("#lblError", hasText(WRONG_CREDENTIALS));
    }

    /**
     * A customer goes to the shop window.
     */
    @Test
    public void testCustomerLoginOpensShop() {
        fakeDao.setUserToReturn(new Customer("Mikel", "mikel@gozoshop.com", "1234", "12345678Z",
                new ArrayList<String>(), CustomerType.INDIVIDUAL));
        writeCredentials("mikel@gozoshop.com", "1234");
        clickOn("#btnLogin");
        verifyThat("#tblProducts", isVisible());
    }

    /**
     * A worker goes to the management windows (Admin Shipments).
     */
    @Test
    public void testWorkerLoginOpensAdminPanel() {
        fakeDao.setUserToReturn(new Worker("Alain", "alain@gozoshop.com", "1234", "12345678Z", Role.ADMIN));
        writeCredentials("alain@gozoshop.com", "1234");
        clickOn("#btnLogin");
        verifyThat("#shipmentTable", isVisible());
    }

    /**
     * A worker with the ADMIN role can see the USERS and LOG buttons.
     */
    @Test
    public void testAdminSeesUsersAndLogButtons() {
        fakeDao.setUserToReturn(new Worker("Alain", "alain@gozoshop.com", "1234", "12345678Z", Role.ADMIN));
        writeCredentials("alain@gozoshop.com", "1234");
        clickOn("#btnLogin");
        verifyThat("#btnUsers", isVisible());
        verifyThat("#btnLog", isVisible());
    }

    /**
     * A worker with the WORKER role cannot see the USERS and LOG buttons.
     */
    @Test
    public void testRegularWorkerDoesNotSeeUsersAndLogButtons() {
        fakeDao.setUserToReturn(new Worker("Jon", "jon@gozoshop.com", "1234", "87654321X", Role.WORKER));
        writeCredentials("jon@gozoshop.com", "1234");
        clickOn("#btnLogin");
        verifyThat("#btnUsers", isInvisible());
        verifyThat("#btnLog", isInvisible());
    }

    /**
     * If MongoDB is not available, the window shows the server error.
     */
    @Test
    public void testDatabaseErrorShowsServerError() {
        fakeDao.setDatabaseDown(true);
        writeCredentials("mikel@gozoshop.com", "1234");
        clickOn("#btnLogin");
        verifyThat("#lblError", hasText(SERVER_ERROR));
    }
}
