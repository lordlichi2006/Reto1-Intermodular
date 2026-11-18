package com.reto1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import com.reto1.model.Product;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

class ShopHomeTest extends ApplicationTest {

    private Parent homeRoot;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/com/reto1/view/shopHome.fxml"));
        homeRoot = homeLoader.load();

        stage.setScene(new Scene(homeRoot, 860, 540));
        stage.show();
    }

    @AfterEach
    public void tearDown() throws TimeoutException {
        BaseController.loggedUser = null;
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    void shopHomeDisplaysProductionProductTable() {
        TableView<?> productsTable = (TableView<?>) homeRoot.lookup("#tblProducts");
        assertEquals(4, productsTable.getColumns().size());
        verifyThat("#tblProducts", isVisible());
    }

    @Test
    void searchFiltersLoadedProductsByName() {
        TableView<?> productsTable = (TableView<?>) homeRoot.lookup("#tblProducts");
        assumeFalse(productsTable.getItems().isEmpty(),
                "Product search requires products in the configured MongoDB.");

        String firstProductName = ((Product) productsTable.getItems().get(0)).getName();
        assertFalse(firstProductName.isEmpty());
        String query = firstProductName.substring(0, Math.min(3, firstProductName.length())).toLowerCase();
        clickOn("#txtSearchbox").write(query);

        assertFalse(productsTable.getItems().isEmpty());
        assertTrue(productsTable.getItems().stream()
                .allMatch(product -> ((Product) product).getName().toLowerCase().contains(query)));
    }

    @Test
    void rejectsZeroAmountAndResetsField() {
        submitAmount("0");
        verifyThat("#lblError", hasText("Error: Product can not be negative."));
        assertEquals("1", ((TextField) homeRoot.lookup("#txtAmount")).getText());
    }

    @Test
    void rejectsNonNumericAmountAndResetsField() {
        submitAmount("not a number");
        verifyThat("#lblError", hasText("Error: Product amount is not a valid value."));
        assertEquals("1", ((TextField) homeRoot.lookup("#txtAmount")).getText());
    }

    /** Fires the FXML text field's action handler with the supplied amount. */
    private void submitAmount(String value) {
        interact(() -> {
            TextField amount = (TextField) homeRoot.lookup("#txtAmount");
            amount.setText(value);
            amount.fireEvent(new ActionEvent());
        });
    }
}
