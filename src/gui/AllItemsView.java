package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import domain.logic.container.Container;
import domain.logic.item.Item;
import domain.logic.item.ItemUtility;
import gui.home.HomeView;

public class AllItemsView extends JPanel implements ActionListener {
	
	/**
	 * Button to go from AllItemsView to HomeView
	 */
	private JButton backButton = new JButton("Back");
	
	/**
	 * Table model that contains the information for tableModel
	 */
	private DefaultTableModel tableModel; 
		
	/**
	 * Displaying JTable following the tableModel model
	 */
	private JTable allItemsTable;
	
	private JPanel topPanel;
	
	/**
	 * The font and size that texts in the view will have
	 */
	private Font font;
	
	private JLabel viewInfo;
	
	/**
	 * Hash map containing the list of all the containers and their corresponding buttons from the database
	 */
	private static ConcurrentHashMap<JButton, Container> containerMap;
		
	/**
	 * Constructor method that initializes all of the objects and tables on the page
	 */
	private AllItemsView() {
		
		setLayout(new BorderLayout());
		setBackground(new Color(253, 241, 203));
		topPanel = new JPanel(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		
		backButton.addActionListener(this);
		viewInfo = new JLabel("Click on an item to go to its container");	
		
		topPanel.add(backButton, BorderLayout.WEST);
		topPanel.add(viewInfo, BorderLayout.CENTER);
		
		displayAllItems();
		rowClickTransition();
				
		add(new JScrollPane(allItemsTable), BorderLayout.CENTER);
		addFonts();
	}
	
	/**
	 * Returns an instance of the AllItemsView object
	 * @return an instance of the AllItemsView object
	 */
	public static AllItemsView getInstance() {
		return new AllItemsView();
	}
	
	/**
	 * Method that initializes the JTable by calling and storing information about each container
	 * and its items into the table model
	 */
	private void displayAllItems() {
		
		containerMap = HomeView.getContainerMap();
		String[] columnNames = {"Name", "Quantity", "Expiry Date", "Food Group", "Food Freshness", "Container"};
		
		tableModel = new DefaultTableModel(null, columnNames) {
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
		};
		
		containerMap.forEach((button, container) -> {
			ItemUtility.assignFoodFreshness(container, HomeView.data);
			
			List<Item> items = HomeView.data.retrieveItems(container);
			
			for (Item item : items) {
				tableModel.addRow(new Object[] { item.getName(), item.getQuantity(), ItemUtility.dateFormat(item.getExpiryDate()),
						item.getFoodGroupTag(), item.getFoodFreshnessTag(), container.getName() });
			}
		});
		
		allItemsTable = new JTable(tableModel);
	}
	
	/**
	 * Sets the visibility of the AllItemsView GUI depending on the boolean passed
	 * @param b the value of whether the visibility is true or not
	 */
	public static void setAllItemsViewVisibility(boolean b) {
		JFrame frame = HomeView.getFrame();
		
		if (b) {
			frame.getContentPane().removeAll();
			AllItemsView allItemsView = AllItemsView.getInstance();
			HomeView.getFrame().add(allItemsView);
		
		} else {
			frame.getContentPane().removeAll();
			HomeView.getHomeView().setHomeViewVisibility(true);
		}
		
		frame.revalidate();
		frame.repaint();
		
	}

	/**
	 * Handles action events triggered by GUI components.
	 *
	 * @param e The ActionEvent object containing details about the event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			setAllItemsViewVisibility(false);
		}
		
	}
	
	/**
	 * Provides access to the JTable
	 * @return the current JTable object
	 */
	public JTable getTable() {
		return this.allItemsTable;
	}
	
	/**
	 * Provides access to the DefaultTableModel
	 * @return the current DefaultTableModel object
	 */
	public DefaultTableModel getTableModel() {
		return this.tableModel;
	}
	
	/**
	 * Add fonts to components
	 */
	public void addFonts() {
		Font f = new Font("Lucida Grande", Font.PLAIN, HomeView.getSettings().getFontSize());
		getTable().setFont(f);
		getTable().setRowHeight(getTable().getRowHeight()+10);
		backButton.setFont(f);
		viewInfo.setFont(f);
	}
	
	/**
	 * Implements clickable functionality to the JTable so that when a row is clicked
	 * the view is changed to the container associated with the row
	 */
	private void rowClickTransition() {
		
		allItemsTable.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				int row = getTable().getSelectedRow();
				if (row != -1) {
					String name = getTableModel().getValueAt(row, 5).toString();
					
					HomeView.getContainerMap().forEach((button, container) -> {
						if (name.equals(container.getName())) {
							container.getGUI();
						}
							
					});
				}
			}
		});
	}
}
