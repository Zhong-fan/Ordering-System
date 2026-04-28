package ui;

import model.CartItem;
import model.Food;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;

    public CartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        refreshCart();
    }


    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("我的购物车", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        String[] tableColumns = {"菜品名称", "单价(¥)", "数量", "小计(¥)", "操作"};
        cartTableModel = new DefaultTableModel(tableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cartTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        cartTable.setRowHeight(28);


        cartTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 2) {
                int row = e.getFirstRow();
                if (row >= 0 && row < cartTableModel.getRowCount()) {
                    try {
                        CartItem item = getCartItemByRow(row);
                        if (item != null) {
                            int newQuantity = Integer.parseInt(
                                    cartTableModel.getValueAt(row, 2).toString().trim()
                            );
                            mainFrame.updateCartItemQuantity(item.getFood().getId(), newQuantity);
                            refreshCart();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "请输入有效数字", "错误", JOptionPane.ERROR_MESSAGE);
                        refreshCart();
                    }
                }
            }
        });

        add(new JScrollPane(cartTable), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 10));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("总价：¥0.00");
        totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        totalLabel.setForeground(Color.RED);
        totalPanel.add(totalLabel);
        bottomPanel.add(totalPanel, BorderLayout.CENTER);

        //按钮区域
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton backBtn = new JButton("返回首页");
        backBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        backBtn.setPreferredSize(new Dimension(100, 35));

        JButton checkoutBtn = new JButton("结算");
        checkoutBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        checkoutBtn.setPreferredSize(new Dimension(100, 35));
        checkoutBtn.setBackground(Color.RED);
        checkoutBtn.setForeground(Color.WHITE);

        btnPanel.add(backBtn);
        btnPanel.add(checkoutBtn);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> mainFrame.showHomePanel());
        checkoutBtn.addActionListener(e -> mainFrame.checkout());
    }


    public void refreshCart() {
        cartTableModel.setRowCount(0);

        List<CartItem> cartItems = mainFrame.getCartItems();

        if (cartItems.isEmpty()) {
            cartTableModel.addRow(new Object[]{"购物车为空", "", "", "", ""});
            totalLabel.setText("总价：¥0.00");
            return;
        }

        for (CartItem item : cartItems) {
            Food food = item.getFood();
            int quantity = item.getQuantity();
            double subtotal = food.getPrice() * quantity;

            JButton deleteBtn = new JButton("删除");
            deleteBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            deleteBtn.setForeground(Color.RED);
            deleteBtn.addActionListener(e -> {
                mainFrame.removeFromCart(food.getId());
            });

            cartTableModel.addRow(new Object[]{
                    food.getName(),
                    food.getPrice(),
                    quantity,
                    subtotal,
                    deleteBtn
            });
        }

        totalLabel.setText("总价：¥" + mainFrame.calculateTotal());
    }

    private CartItem getCartItemByRow(int row) {
        List<CartItem> cartItems = mainFrame.getCartItems();
        if (row >= 0 && row < cartItems.size()) {
            return cartItems.get(row);
        }
        return null;
    }
}