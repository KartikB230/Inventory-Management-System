package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.awt.print.*;


public class InventoryManagementSystem extends JFrame implements InventoryManager 
{
	private static final long serialVersionUID = 1L;

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> matchingItems = new ArrayList<Item>();
    private ArrayList<Item> cartItems = new ArrayList<>();
    private JList<String> itemList;
    private JList<String> cartItemList;
    private JTextField searchField; 
    static int count = 0; 
 
    
    public InventoryManagementSystem() 
    {
        setTitle("Inventory Management System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        try 
        {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } 
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e1) 
        {	
                System.out.println("UIManager not get loaded..");
        }
        
        //Icons and Font
        ImageIcon icon = new ImageIcon("C:/Users/KARTIK/Desktop/icon.png");
        ImageIcon icon1 = new ImageIcon("C:/Users/KARTIK/Desktop/icon1.png");
        ImageIcon icon2 = new ImageIcon("C:/Users/KARTIK/Desktop/icon2.png");
        Font font = new Font("Calibri",Font.BOLD,14);
        
        //Panels and ScrollPane
        JPanel contentPane = new JPanel();contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setBackground(Color.BLACK);
        contentPane.setLayout(new BorderLayout(0, 0));
        JPanel panel = new JPanel();
        JPanel panel_1 = new JPanel();
        JPanel panel_2 = new JPanel(new BorderLayout());
        
        //Labels
        JLabel lblNewLabel = new JLabel("AVAILABLE ITEMS");
        lblNewLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 00));
        lblNewLabel.setFont(font);
        JLabel lblNewLabel_1 = new JLabel("CART ITEMS");
        lblNewLabel_1.setFont(font);
        lblNewLabel_1.setBorder(BorderFactory.createEmptyBorder(0,650,0,650));
        
        //Buttons
        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setIcon(icon);
        btnAddToCart.setBackground(Color.WHITE);
        btnAddToCart.setFont(font);
        JButton btnRemoveFromCart = new JButton("Remove from Cart");
        btnRemoveFromCart.setFont(font);
        btnRemoveFromCart.setBackground(Color.WHITE);
        btnRemoveFromCart.setIcon(icon1);
        JButton btnBilling = new JButton("Billing");
        btnBilling.setFont(font);
        btnBilling.setBackground(Color.WHITE);
        btnBilling.setIcon(icon2);
        
        //JList
        itemList = new JList<>();
        cartItemList = new JList<>();
        
        
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //JTextField
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(100,30));
        searchField.setToolTipText("SearchBar");
        searchField.setForeground(Color.GRAY);
        searchField.setText("SearchBar");

        //focus listener to clear the hint text when the user clicks in the text field
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("SearchBar")) {
                	searchField.setText("");
                	searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            	count=count+1;
                if (searchField.getText().isEmpty()) {
                	searchField.setForeground(Color.GRAY);
                    searchField.setText("SearchBar");
                }

                if(count==1)
                {
                	for (Item i : items) {
                            matchingItems.add(i);
                    }
                	itemList.setListData(getMatchingItemNames());
                }	
                
            }
        });
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            	
                String searchText = searchField.getText().toLowerCase();
                matchingItems.clear();

                for (Item i : items) {
                    if (i.getName().toLowerCase().contains(searchText)) {
                        matchingItems.add(i);
                    }
                }
                itemList.setListData(getMatchingItemNames());
            }
        });  
        //ActionListeners
        btnAddToCart.addActionListener((ActionEvent e) -> {
            int selectedIndex = itemList.getSelectedIndex();
            if(selectedIndex != -1) {
                cartItems.add(matchingItems.get(selectedIndex));
                cartItemList.setListData(getCartItems());
            }
        });
        
        btnRemoveFromCart.addActionListener((ActionEvent e) -> {
            int selectedIndex = cartItemList.getSelectedIndex();
            if (selectedIndex != -1) 
            {
                cartItems.remove(selectedIndex);
                cartItemList.setListData(getCartItems());
            }
        });

        btnBilling.addActionListener((ActionEvent e) -> {
            if (cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(InventoryManagementSystem.this, "Your cart is empty.");
                return;
            }
            double total = 0.0;
            StringBuilder sb = new StringBuilder();
            sb.append("Items in your cart:\n");
            for (Item item : cartItems) {
                int quantity = item.getquantity();
                double price = item.getPrice();
                String Name = item.getName();
                sb.append("\n").append(Name).append(": Rs.").append(price).append("\n");
                total += price;

                
                item.setquantity(quantity - 1);
                DatabaseOperation(item);
                
            }
            sb.append("\nTotal: Rs.").append(total);
            int option=JOptionPane.showOptionDialog(InventoryManagementSystem.this,sb.toString(), "Billing Information", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"PRINT", "OK"}, "PRINT");
            if(option == JOptionPane.YES_OPTION){
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
                    if (pageIndex == 0) {
                        graphics.drawString(sb.toString(), 50, 50);
                        return Printable.PAGE_EXISTS;
                    } else {
                        return Printable.NO_SUCH_PAGE;
                    }
                });
                
                // show the PrintDialog
                if (printerJob.printDialog()) {
                    
                    try {
                        printerJob.print();
                    } catch (PrinterException ex) {
                        System.out.print("Printer Exception...");
                    }
                }
             
            }
            cartItems.clear();
            cartItemList.setListData(getCartItems());
        });

        
        //adding components and modifications
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(itemList);
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setViewportView(cartItemList);
        
        
        panel.add(lblNewLabel);
        panel.add(lblNewLabel_1);
        panel_1.add(btnAddToCart);
        panel_1.add(btnRemoveFromCart);
        panel_1.add(btnBilling);
        panel_2.add(searchField,BorderLayout.NORTH);
        panel_2.add(scrollPane,BorderLayout.AFTER_LINE_ENDS);
        
        
        contentPane.add(panel, BorderLayout.NORTH);
        
        contentPane.add(panel_2, BorderLayout.WEST);
        contentPane.add(scrollPane_1, BorderLayout.CENTER);
        contentPane.add(panel_1, BorderLayout.SOUTH);
        
        
        DatabaseOperation();
        itemList.setListData(getItemNames());
    }
    
@Override
public void DatabaseOperation() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "kartikbasrani");
            stmt = conn.createStatement();
           
            String sql = "SELECT * FROM items";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                int id = rs.getInt("id");
                int quantity = rs.getInt("quantity");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                Item item = new Item(id,name, price,quantity);
                items.add(item);
        }
    } catch (ClassNotFoundException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error in Database Connectivity", "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
public void DatabaseOperation(Item item) {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "kartikbasrani");
        
        if (item.getquantity() > 1) {
        	
            PreparedStatement stmt = conn.prepareStatement("UPDATE items SET quantity = quantity - 1 WHERE id = ?");
            stmt.setInt(1, item.getId());
            stmt.executeUpdate();
            stmt.close();
        } else {
        	
        	PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO selled_items values(?,?,?)");
            stmt1.setInt(1, item.getId());
            stmt1.setString(2, item.getName());
            stmt1.setDouble(3, item.getPrice());
            stmt1.executeUpdate();
            stmt1.close();
            
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id = ?");
            stmt.setInt(1, item.getId());
            stmt.executeUpdate();
            stmt.close();
            
           
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



@Override
public String[] getItemNames() {
    String[] names = new String[items.size()];
    for (int i = 0; i < items.size(); i++) {
        names[i] = items.get(i).getName();
    }
    return names;
}

@Override
public String[] getItemPrice() {
    String[] names = new String[items.size()];
    for (int i = 0; i < items.size(); i++) {
        names[i] = Double.toString(items.get(i).getPrice());
    }
    return names;
}

@Override
public String[] getCartItems() {
    String[] names = new String[cartItems.size()];
    for (int i = 0; i < cartItems.size(); i++) {
    	names[i] = String.format("%d. %-20s\t\t%.2f", i+1, cartItems.get(i).getName(), cartItems.get(i).getPrice());
    }
    return names;
}

@Override
public String[] getMatchingItemNames() {
    String[] names = new String[matchingItems.size()];
    for (int i = 0; i < matchingItems.size(); i++) {
        names[i] = matchingItems.get(i).getName();
    }
    return names;
}

public void maincall()
{
   EventQueue.invokeLater(() -> {
        try {
            InventoryManagementSystem frame = new InventoryManagementSystem();
            
            frame.setVisible(true);
        } 
        catch (Exception e) {
            System.out.println("Exception in Main...");
        }
    }); 
}


}



