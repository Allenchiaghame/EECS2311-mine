package domain.logic.item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.table.DefaultTableModel;

import database.DB;
import domain.logic.container.Container;
import gui.home.HomeView;

/**
 * Utility class for operations related to items in the inventory such as
 * adding, deleting, and updating item details. This class holds logic that GUI
 * classes can use.
 */
public class ItemUtility {
	/**
	 * Verifies the deletion of an item from the specified container
	 *
	 * @param itemName  The name of the item to verify and delete.
	 * @param container The container from which to delete the item.
	 * @param database The database object
	 * @return Boolean indicating the success or failure of the item deletion.
	 */
	public static Boolean verifyDeleteItem(String itemName, Container container, DB database) {
		if (database.getItem(container, itemName) != null) {
			database.removeItem(container, itemName);
			return true;
		} else
			return false;

	}

	/**
	 * Validates the input data for a new item and, if valid, returns true. In case
	 * of validation errors, it utilizes a Consumer to handle the error message and
	 * returns false.
	 *
	 * @param name         The name of the new item.
	 * @param quantityStr  The quantity of the new item as a string.
	 * @param expiryDate   The expiry date of the new item.
	 * @param errorHandler A Consumer that handles error messages.
	 */
	public static boolean verifyAddItem(String name, String quantityStr, String expiryDate,
										Consumer<String> errorHandler) {
		name = name.trim();
		quantityStr = quantityStr.trim();
		expiryDate = expiryDate.trim();

		// Initialize counters for validation
		boolean nameIsEmpty = name.isEmpty();
		boolean quantityIsEmpty = quantityStr.isEmpty();
		boolean expiryIsEmpty = expiryDate.isEmpty();
		int expiryValidationResult = validateExpiryDate(expiryDate);
		int invalidInputsCount = 0;

		if (nameIsEmpty || name.length() > 50) invalidInputsCount++;
		if (quantityIsEmpty || !isQuantityValid(quantityStr)) invalidInputsCount++;
		if (expiryIsEmpty || expiryValidationResult != 0) invalidInputsCount++;

		if (invalidInputsCount > 1) {
			errorHandler.accept("There are empty and/or invalid inputs, no item added.");
			return false;
		} else if (nameIsEmpty) {
			errorHandler.accept("Item name cannot be empty.");
			return false;
		} else if (name.length() > 50) {
			errorHandler.accept("Item Name exceeds character length (50).");
			return false;
		} else if (quantityIsEmpty || !isQuantityValid(quantityStr)) {
			errorHandler.accept("Please enter a valid number for quantity (has to be an integer greater than 0).");
			return false;
		} else if (expiryIsEmpty) {
			errorHandler.accept("Date cannot be empty.");
			return false;
		} else if (expiryValidationResult == -1) {
			errorHandler.accept("Please enter the expiry date in the correct format (dd-MMM-yyyy).");
			return false;
		} else if (expiryValidationResult == 1) {
			errorHandler.accept("Expired item, please discard.");
			return false;
		}

		return true;
	}

	private static boolean isQuantityValid(String quantityStr) {
		try {
			int quantity = Integer.parseInt(quantityStr);
			return quantity > 0;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private static int validateExpiryDate(String expiryDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		sdf.setLenient(false);
		try {
			Date expiry = sdf.parse(expiryDate);

			// Set expiry time to the end of the day (23:59:59)
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(expiry);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			expiry = calendar.getTime();

			Date current = new Date();
			if (expiry.before(current)) {
				return 1;
			}
			return 0;
		} catch (ParseException ex) {
			return -1;
		}
	}

	/**
	 * Updates the specified item's food group tag based on the provided new value and
	 * column index. The update is only performed if the item exists in the
	 * container.
	 *
	 * @param container The container where the item resides.
	 * @param itemName  The name of the item to be updated.
	 * @param newValue  The new value to be set for the item's property.
	 * @param column    The column index corresponding to the property to be
	 *                  updated.
	 * @param database The database object
	 * @return true if the item was successfully updated, false otherwise.
	 */
	public static void updateItemFoodGroupTag(Container container, String itemName, Object newValue, int column, DB database) {
		if (column == 3 && newValue instanceof FoodGroup) {
			database.updateItemFoodGroup(container, itemName, (FoodGroup) newValue);
		}
	}

	/**
	 * Retrieves and initializes the rows in ItemsListViews from the database for a
	 * specified container.
	 * 
	 * @param c          Container object to initialize the items for
	 * @param tableModel the table object to initialize the rows for
	 * @param database The database object
	 */
	public static void initItems(Container c, DefaultTableModel tableModel, DB database) {
		List<Item> items = database.retrieveItems(c);
		tableModel.setRowCount(0);
		for (Item item : items) {
			String tag = database.getItemTag(item.getName());
			tableModel.addRow(new Object[] { item.getName(), item.getQuantity(), dateFormat(item.getExpiryDate()),
					item.getFoodGroupTag(), item.getFoodFreshnessTag(), tag });
		}
	}

	/**
	 * Formats a Date object into a string representation with the format
	 * "yyyy-MM-dd". This format is often used for SQL date columns.
	 *
	 * @param expiryDate The date to be formatted.
	 * @return A string representation of the date in "yyyy-MM-dd" format.
	 */
	public static String dateFormat(Date expiryDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = formatter.format(expiryDate);
		return format;
	}

	/**
	 * Assigns FoodFreshness tags to items based on their expiry dates.
	 * 
	 * @param container The container whose items' freshness will be updated.
	 * @param database The database object
	 */
	public static void assignFoodFreshness(Container container, DB database) {
		database.batchUpdateItemFreshness(container);
	}

	/**
	 * Retrieves storage tips for a specific food item from the database. This
	 * method queries the database through its interface to
	 * find storage tips associated with the given food name. If a tip is found, it
	 * is returned as a string.
	 *
	 * @param foodName The name of the food item for which storage tips are being
	 *                 retrieved.
	 * @param database The database object
	 * @return A string containing storage tips for the specified food item. Returns
	 *         {@code null} if no tips are found or if there's an error in
	 *         retrieving the data.
	 */
	public static String retrieveStorageTip(String foodName, DB database) {
		return database.getStorageTip(foodName);

	}

	/**
	 * Verifies and updates the quantity of an item in the list.
	 * 
	 * @param val             The user inputed value.
	 * @param database            The database object
	 * @param c               The container that belongs to the item
	 * @param item            The name of the item which needs the quantity edited
	 * @param errorHandler    A Consumer thsat handles error messages.
	 * @param successCallback A Runnable that is executed upon successful addition.
	 */
	public static void verifyEditQuantity(String val, DB database, Container c, String item, Consumer<String> errorHandler,
			Runnable successCallback) {
		try {
			if (val == null) {
				return;
			}
			val = val.trim();
			if (val.isEmpty()) {
				errorHandler.accept("Quantity cannot be empty!");
				return;
			}
			int o = Integer.parseInt(val);

			if (o < 0) {
				errorHandler.accept("You can't have a negative quantity!");
				return;
			} else if (o == 0) {
				verifyDeleteItem(item, c, database);
				successCallback.run();

			} else {
				database.updateQuantity(item, o, c);
				successCallback.run();
			}

		} catch (NumberFormatException e) {
			errorHandler.accept("Not a valid number!");
			return;
		}

	}
	
	public static void updateFreshness(DB database) {

		 List<String> s =  database.retrieveContainers();
	       Container c;
	       for (String containerName: s) {
	    	   
	    	   c = new Container(containerName);
			   database.batchUpdateItemFreshness(c);
	       }
	}
	
}
