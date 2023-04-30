package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame implements ActionListener {
    
	private static final long serialVersionUID = 1L;
	// create components
    JLabel userLabel = new JLabel("Username:");
    JTextField userField = new JTextField(20);
    JLabel passLabel = new JLabel("Password:");
    JPasswordField passField = new JPasswordField(20);
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");

    public LoginPage() {
        // set window properties
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setResizable(false);

        // create panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(resetButton);

        add(panel);

        loginButton.addActionListener(this);
        resetButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            // check username and password
            if (user.equals("admin") && pass.equals("password")) {
                JOptionPane.showMessageDialog(null, "Login successful.");
                dispose();
                InventoryManagementSystem IMS = new InventoryManagementSystem();
                IMS.maincall();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        } else if (e.getSource() == resetButton) {
            userField.setText("");
            passField.setText("");
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
