/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rs.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.rs.cache.Cache;
import com.rs.game.item.Item;
import com.rs.game.player.content.Shop;
import com.rs.json.GsonHandler;
import com.rs.json.impl.ShopsLoader;

/**
 *
 * @author Tyluur
 */
@SuppressWarnings("serial")
public class ShopEditor extends JFrame {

	/**
	 * Creates new form ShopEditor
	 */
	public ShopEditor() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		shopItemsPanel = new javax.swing.JPanel();
		shopItemsScrollPane = new javax.swing.JScrollPane();
		shopItemsTable = new javax.swing.JTable();
		jLayeredPane1 = new javax.swing.JLayeredPane();
		gameShopsScrollPane = new javax.swing.JScrollPane();
		gameShopsJList = new javax.swing.JList<String>();
		addShopButton = new javax.swing.JButton();
		deleteButton = new javax.swing.JButton();
		addItemButton = new javax.swing.JButton();

		refreshShopsList();
		gameShopsJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (gameShopsJList.getSelectedIndex() != -1) {
					updateTable(gameShopsJList.getSelectedIndex());
				}
			}
		});

		addShopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String name = null;
				try {
					name = JOptionPane.showInputDialog("What is the name of the shop you wish to create?");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "You entered an invalid name!", "Invalid Input", JOptionPane.ERROR_MESSAGE, null);
					e.printStackTrace();
					return;
				}
				if (name == null || name.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "You entered an invalid name!", "Invalid Input", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				Shop shop = new Shop(name, 995, new Item[] {}, false);
				shopLoader.addShopToDatabase(shop);
				shopLoader.initialize();
				refreshShopsList();
			}
		});

		addItemButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedShop = gameShopsJList.getSelectedIndex();
				if (selectedShop != -1) {
					Shop shop = getShopByIndex(selectedShop);
					int id = 0;
					int amount = 0;
					try {
						id = Integer.parseInt(JOptionPane.showInputDialog("What is the id of the item you wish to add?"));
						amount = Integer.parseInt(JOptionPane.showInputDialog("How much of this item do you wish to add:"));
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "You entered an invalid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE, null);
						return;
					}
					shop.addItem(new Item(id, amount));
					save(shop);
					updateTable(selectedShop);
				} else {
					System.out.println("Please select a shop first before adding items to it.");
				}
			}
		});

		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				int selectedShop = gameShopsJList.getSelectedIndex();
				int selectedItemRow = shopItemsTable.getSelectedRow();
				int selectedItemColumn = shopItemsTable.getSelectedColumn();

				if (selectedItemRow == -1 && selectedItemColumn == -1) { // deletes
					// the
					// current
					// selected
					// shop
					// entirely
					if (selectedShop != -1) {
						Shop shop = getShopByIndex(selectedShop);
						System.out.println(shop.getName());
						removeShop(shop);
						updateTable(selectedShop);
						shopLoader.getShops().clear();
						shopLoader.initialize();
						refreshShopsList();
					} else {
						System.out.println("You have to be selecting a shop in order to edit its items.");
					}
				} else { // deletes the current selected item
					Shop shop = getShopByIndex(selectedShop);
					Item item = shop.getMainStock()[selectedItemRow];
					shop.removeItem(item);
					save(shop);
					updateTable(selectedShop);
				}
			}
		});

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		shopItemsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Shop Items"));

		shopItemsTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {

		}));
		shopItemsScrollPane.setViewportView(shopItemsTable);

		javax.swing.GroupLayout shopItemsPanelLayout = new javax.swing.GroupLayout(shopItemsPanel);
		shopItemsPanel.setLayout(shopItemsPanelLayout);
		shopItemsPanelLayout.setHorizontalGroup(shopItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(shopItemsPanelLayout.createSequentialGroup().addContainerGap().addComponent(shopItemsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE).addContainerGap()));
		shopItemsPanelLayout.setVerticalGroup(shopItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(shopItemsPanelLayout.createSequentialGroup().addComponent(shopItemsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE).addContainerGap()));

		jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Shops"));

		gameShopsScrollPane.setViewportView(gameShopsJList);

		gameShopsScrollPane.setBounds(10, 20, 640, 130);
		jLayeredPane1.add(gameShopsScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

		addShopButton.setText("Add Shop");
		addShopButton.setBounds(533, 160, 120, 23);
		jLayeredPane1.add(addShopButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

		deleteButton.setText("Delete");
		deleteButton.setBounds(10, 160, 130, 23);
		jLayeredPane1.add(deleteButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

		addItemButton.setText("Add Item");
		addItemButton.setBounds(380, 160, 130, 23);
		jLayeredPane1.add(addItemButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLayeredPane1).addComponent(shopItemsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(shopItemsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE).addContainerGap()));

		pack();
	}// </editor-fold>

	private void removeShop(Shop shop) {
		List<Shop> shops = shopLoader.load();
		ListIterator<Shop> it = shops.listIterator();
		while (it.hasNext()) {
			Shop shop1 = it.next();
			if (shop1 == null || shop1.getName() == null) {
				continue;
			}
			if (shop1.getName().equals(shop.getName())) {
				it.remove();
			}
		}
		reload(shops);
	}

	protected void updateTable(int index) {
		final Shop shop = getShopByIndex(index);
		DefaultTableModel model = new DefaultTableModel() {
			/**
			 *
			 */
			private static final long serialVersionUID = 4890109891417882803L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0 ? false : true;
			}
		};
		model.addColumn("Name");
		model.addColumn("Id");
		model.addColumn("Amount");
		if (shop != null && shop.getMainStock() != null) {
			for (Item item : shop.getMainStock()) {
				if (item == null) {
					continue;
				}
				model.addRow(new Object[] { item.getDefinitions().getName(), item.getDefinitions().id, item.getAmount() });
			}
		}
		if (shop != null) {
			shopItemsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(shop.getName()));
		}
		shopItemsTable.setModel(model);
		setTableModelListener();
		shopItemsTable.scrollRectToVisible(shopItemsTable.getCellRect(shopItemsTable.getRowCount() - 1, shopItemsTable.getColumnCount(), true));
	}

	private void setTableModelListener() {
		shopItemsTable.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					Shop shop = getShopByIndex(gameShopsJList.getSelectedIndex());
					Item item = shop.getMainStock()[e.getFirstRow()];
					if (e.getColumn() == 1) {
						item.setId((short) Integer.parseInt((String) shopItemsTable.getModel().getValueAt(e.getFirstRow(), e.getColumn())));
					} else {
						item.setAmount(Integer.parseInt((String) shopItemsTable.getModel().getValueAt(e.getFirstRow(), e.getColumn())));
					}
					save(shop);
					updateTable(gameShopsJList.getSelectedIndex());
				}
			}
		});
	}

	private void save(Shop shop) {
		List<Shop> shops = shopLoader.load();
		ListIterator<Shop> it = shops.listIterator();
		while (it.hasNext()) {
			Shop shop1 = it.next();
			if (shop1 == null || shop1.getName() == null) {
				continue;
			}
			if (shop1.getName().equals(shop.getName())) {
				it.remove();
			}
		}
		shops.add(shop);
		reload(shops);
	}

	private void reload(List<Shop> shops) {
		shopLoader.save(shops);
		shopLoader.initialize();
		shopLoader.sortShops();
	}

	private void refreshShopsList() {
		gameShopsJList.setModel(new javax.swing.AbstractListModel<String>() {

			@Override
			public int getSize() {
				return shopLoader.getShops().size();
			}

			@Override
			public String getElementAt(int i) {
				return getShopByIndex(i).getName();
			}
		});
	}

	private Shop getShopByIndex(int index) {
		Iterator<Shop> it = shopLoader.getSortedShops().iterator();
		int count = -1;
		while (it.hasNext()) {
			Shop shop = it.next();
			count++;
			if (count == index) {
				return shop;
			}
		}
		return null;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GsonHandler.initialize();
		shopLoader = GsonHandler.getJsonLoader(ShopsLoader.class);
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ShopEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ShopEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ShopEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ShopEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ShopEditor().setVisible(true);
			}
		});
	}

	private static ShopsLoader shopLoader = null;

	// Variables declaration - do not modify
	private javax.swing.JButton addItemButton;
	private javax.swing.JButton addShopButton;
	private javax.swing.JButton deleteButton;
	private javax.swing.JList<String> gameShopsJList;
	private javax.swing.JScrollPane gameShopsScrollPane;
	private javax.swing.JLayeredPane jLayeredPane1;
	private javax.swing.JPanel shopItemsPanel;
	private javax.swing.JScrollPane shopItemsScrollPane;
	private javax.swing.JTable shopItemsTable;
	// End of variables declaration
}