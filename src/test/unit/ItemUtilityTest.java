package test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.logic.container.Container;
import domain.logic.item.Item;
import domain.logic.item.ItemUtility;

import static org.junit.jupiter.api.Assertions.*;
import java.util.function.Consumer;

public class ItemUtilityTest {
    private Container testContainer;
    private String existingItemName = "TestItem";
    private String nonExistingItemName = "NonExistingTestItem";

    void setUpContainer() {
        testContainer = new Container("TestContainer");
        testContainer.addNewItem(Item.getInstance(existingItemName, 1, "1-jan-2024"));
    }

    @Test
    void verifyDeleteExistingItem() {
        setUpContainer();
        boolean itemRemoved = testContainer.getItems().removeIf(item -> item.getName().equals(existingItemName));

        assertTrue(itemRemoved, "Item should be successfully deleted.");
    }

    @Test
    void verifyDeleteNonExistingItem() {
        setUpContainer();
        boolean itemRemoved = testContainer.getItems().removeIf(item -> item.getName().equals(nonExistingItemName));

        assertFalse(itemRemoved, "Item should not be deleted because it doesn't exist.");
    }

    @Test
    void testVerifyAddItemWithValidInput() {
        String validName = "Apple";
        String validQuantity = "10";
        String validExpiryDate = "2-oct-2024";

        Consumer<String> errorHandler = errorMsg -> fail("Should not get an error with valid input: " + errorMsg);

        boolean result = ItemUtility.verifyAddItem(validName, validQuantity, validExpiryDate, errorHandler);

        assertTrue(result, "Expected true for valid input parameters.");
    }

    @Test
    void testVerifyAddItemWithEmptyName() {
        String invalidName = "";
        String validQuantity = "5";
        String validExpiryDate = "20-june-2023";

        boolean[] errorOccurred = {false};
        Consumer<String> errorHandler = errorMsg -> errorOccurred[0] = true;

        boolean result = ItemUtility.verifyAddItem(invalidName, validQuantity, validExpiryDate, errorHandler);

        assertFalse(result, "Expected false due to empty item name.");
        assertTrue(errorOccurred[0], "Expected an error due to empty item name.");
    }

    @Test
    void testVerifyAddItemWithInvalidQuantity() {
        String validName = "Banana";
        String invalidQuantity = "not a number";
        String validExpiryDate = "5-may-2024";

        boolean[] errorOccurred = {false};
        Consumer<String> errorHandler = errorMsg -> errorOccurred[0] = true;

        boolean result = ItemUtility.verifyAddItem(validName, invalidQuantity, validExpiryDate, errorHandler);

        assertFalse(result, "Expected false due to invalid quantity.");
        assertTrue(errorOccurred[0], "Expected an error due to invalid quantity.");
    }

    @Test
    void testVerifyAddItemWithNegativeQuantity() {
        String validName = "Carrot";
        String invalidQuantity = "-1";
        String validExpiryDate = "10-aug-2024";

        boolean[] errorOccurred = {false};
        Consumer<String> errorHandler = errorMsg -> errorOccurred[0] = true;

        boolean result = ItemUtility.verifyAddItem(validName, invalidQuantity, validExpiryDate, errorHandler);

        assertFalse(result, "Expected false due to negative quantity.");
        assertTrue(errorOccurred[0], "Expected an error due to negative quantity.");
    }


}
