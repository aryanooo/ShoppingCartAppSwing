package ShoppingCartApp;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartApp {
    private JFrame frame;
    private ShoppingCart cart;
    private JTextField itemNameField;
    private JTextField itemPriceField;
    private JButton addButton;
    private JLabel totalPriceLabel;

    class ShoppingCart {
        private double totalPrice = 0.00;
        private List<Item> items;

        public ShoppingCart() {
            items = new ArrayList<>();
        }

        public synchronized void addItems(Item item) {
            items.add(item);
            totalPrice += item.getPrice();
        }

        public synchronized double getTotalPrice() {
            return totalPrice;
        }
    }

    class Item {
        private String name;
        private double price;

        public Item(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public double getPrice() {
            return price;
        }
    }

    public ShoppingCartApp() {
        cart = new ShoppingCart();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Shopping Cart App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        frame.setContentPane(panel);
        panel.setLayout(null);

        itemNameField = new JTextField(10);
        itemNameField.setBounds(50, 20, 150, 25);
        panel.add(itemNameField);

        itemPriceField = new JTextField(10);
        itemPriceField.setBounds(210, 20, 120, 25);
        panel.add(itemPriceField);

        addButton = new JButton("Add Item");
        addButton.setBounds(50, 60, 120, 25);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                double itemPrice;
                try {
                    itemPrice = Double.parseDouble(itemPriceField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid price. Please enter a valid numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Item newItem = new Item(itemName, itemPrice);
                cart.addItems(newItem);
                updateTotalPriceLabel();
            }
        });
        panel.add(addButton);

        JButton buyButton = new JButton("Buy");
        buyButton.setBounds(180, 60, 120, 25);
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BillPaymentWindow(cart);
                frame.dispose();
            }
        });
        panel.add(buyButton);

        totalPriceLabel = new JLabel("Total Price: $0.00");
        totalPriceLabel.setBounds(50, 100, 300, 15);
        panel.add(totalPriceLabel);

        frame.setVisible(true);
    }

    private void updateTotalPriceLabel() {
        totalPriceLabel.setText("Total Price: $" + cart.getTotalPrice());
    }

    class BillPaymentWindow {
        private JFrame billFrame;

        public BillPaymentWindow(ShoppingCart cart) {
            billFrame = new JFrame("Bill Payment");
            billFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            billFrame.setSize(400, 300);
            JPanel panel = new JPanel();

            panel.setLayout(new GridLayout(0, 2));
            for (Item item : cart.items) {
                JLabel itemNameLabel = new JLabel(item.name);
                JLabel itemPriceLabel = new JLabel("$" + item.price);

                itemNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
                itemPriceLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
    
                panel.add(itemNameLabel);
                panel.add(itemPriceLabel);
            }
            double cgst = cart.getTotalPrice() * 0.023;
        double sgst = cart.getTotalPrice() * 0.023;
        double totalAmount = cart.getTotalPrice() + cgst + sgst;

        JLabel totalAmt = new JLabel("Total Amount: $" + totalAmount);

        JLabel cgstLabel = new JLabel("CGST (2.3%):");
        JLabel sgstLabel = new JLabel("SGST (2.3%):");
        JLabel totalLabel = new JLabel("Total Amount:");

        JLabel cgstValue = new JLabel("$" + (cart.getTotalPrice() * 0.023));
        JLabel sgstValue = new JLabel("$" + (cart.getTotalPrice() * 0.023));

        cgstLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        sgstLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
       

        panel.add(cgstLabel);
        panel.add(cgstValue);
        panel.add(sgstLabel);
        panel.add(sgstValue);
        panel.add(totalLabel);
        panel.add(totalAmt);

        billFrame.add(panel);
        
        billFrame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ShoppingCartApp();
        });
    }
}
