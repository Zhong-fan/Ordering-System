package ui;

import dao.FoodDao;
import dao.OrderDao;
import model.Food;
import model.Order;
import model.OrderDetail;
import model.User;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private OrderDao orderDao = new OrderDao();
    private FoodDao foodDao = new FoodDao();

    //订单管理组件
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JTable detailTable;
    private DefaultTableModel detailTableModel;

    //添加菜品表单组件
    private JTextField nameField;
    private JTextField priceField;
    private JTextField imagePathField;
    private JComboBox<String> categoryCombo;
    private JTextArea descriptionArea;

    public AdminPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.currentUser = user;
        initUI();
        loadOrders();
        loadFoods();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //顶部标题和按钮
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("管理员后台", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("退出登录");
        logoutBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        logoutBtn.addActionListener(e -> {
            mainFrame.setVisible(false);
            new LoginFrame(mainFrame).setVisible(true);
        });
        topPanel.add(logoutBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        //中间使用选项卡面板切换订单管理和菜品管理
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        //1.订单管理面板
        JPanel orderPanel = createOrderPanel();
        tabbedPane.addTab("订单管理", orderPanel);

        //2.菜品管理面板
        JPanel foodPanel = createFoodPanel();
        tabbedPane.addTab("菜品管理", foodPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    //创建订单管理面板
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        //订单列表
        String[] orderColumns = {"订单ID", "用户ID", "总价", "下单时间"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        orderTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        orderTable.setRowHeight(25);
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
                    loadOrderDetails(orderId);
                }
            }
        });
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        //订单详情
        String[] detailColumns = {"菜品ID", "菜品名称", "数量", "小计"};
        detailTableModel = new DefaultTableModel(detailColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailTable = new JTable(detailTableModel);
        detailTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        detailTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        detailTable.setRowHeight(25);
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.add(new JLabel("订单详情：", JLabel.LEFT), BorderLayout.NORTH);
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);
        detailPanel.setPreferredSize(new Dimension(0, 200));
        panel.add(detailPanel, BorderLayout.SOUTH);

        return panel;
    }

    //创建菜品管理面板
    private JPanel createFoodPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        //顶部按钮
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("添加新菜品");
        addBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        addBtn.setPreferredSize(new Dimension(150, 40));
        btnPanel.add(addBtn);
        panel.add(btnPanel, BorderLayout.NORTH);

        //中间分割面板：左侧菜品列表，右侧添加表单
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500); // 初始分割位置

        //左侧：菜品列表
        String[] foodColumns = {"ID", "名称", "价格", "分类", "操作"};
        DefaultTableModel foodTableModel = new DefaultTableModel(foodColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        JTable foodTable = new JTable(foodTableModel);
        foodTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        foodTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        foodTable.setRowHeight(25);

        loadFoodsToTable(foodTableModel);

        foodTable.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton deleteBtn = new JButton("删除");
            deleteBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            deleteBtn.setForeground(Color.RED);
            int foodId = (int) foodTableModel.getValueAt(row, 0);
            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(AdminPanel.this,
                        "确定要删除该菜品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (foodDao.deleteFood(foodId)) {
                        JOptionPane.showMessageDialog(AdminPanel.this, "删除成功");
                        loadFoodsToTable(foodTableModel); // 刷新表格
                        mainFrame.refreshFoodPanels();
                    } else {
                        JOptionPane.showMessageDialog(AdminPanel.this, "删除失败");
                    }
                }
            });
            return deleteBtn;
        });

        splitPane.setLeftComponent(new JScrollPane(foodTable));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2; // 默认跨2列

        JLabel formTitle = new JLabel("添加新菜品");
        formTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("菜品名称：");
        nameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = new JTextField();
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        nameField.setPreferredSize(new Dimension(0, 35));
        formPanel.add(nameField, gbc);
        gbc.gridx = 0;

        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel priceLabel = new JLabel("菜品价格：");
        priceLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        formPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        priceField = new JTextField();
        priceField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        priceField.setPreferredSize(new Dimension(0, 35));
        formPanel.add(priceField, gbc);
        gbc.gridx = 0;

        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel imageLabel = new JLabel("图片路径：");
        imageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        formPanel.add(imageLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPanel imagePathPanel = new JPanel(new BorderLayout(5, 0));
        imagePathField = new JTextField();
        imagePathField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        imagePathField.setPreferredSize(new Dimension(0, 35));

        JButton browseBtn = new JButton("浏览");
        browseBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        browseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)");
                }

                @Override
                public String getDescription() {
                    return "图片文件 (*.jpg, *.jpeg, *.png, *.gif)";
                }
            });
            int result = fileChooser.showOpenDialog(AdminPanel.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                imagePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        imagePathPanel.add(imagePathField, BorderLayout.CENTER);
        imagePathPanel.add(browseBtn, BorderLayout.EAST);
        formPanel.add(imagePathPanel, gbc);
        gbc.gridx = 0;

        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel categoryLabel = new JLabel("菜品分类：");
        categoryLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        formPanel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        categoryCombo = new JComboBox<>(new String[]{"breakfast", "daily"});
        categoryCombo.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        categoryCombo.setPreferredSize(new Dimension(0, 35));
        formPanel.add(categoryCombo, gbc);
        gbc.gridx = 0;

        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel descLabel = new JLabel("菜品描述：");
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setPreferredSize(new Dimension(0, 100));
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitBtn = new JButton("添加菜品");
        submitBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        submitBtn.setPreferredSize(new Dimension(150, 40));
        submitBtn.addActionListener(e -> saveNewFood());
        formPanel.add(submitBtn, gbc);

        splitPane.setRightComponent(new JScrollPane(formPanel));
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadOrders() {
        orderTableModel.setRowCount(0);
        List<Order> orders = orderDao.getAllOrders();
        for (Order order : orders) {
            orderTableModel.addRow(new Object[]{
                    order.getId(),
                    order.getUserId(),
                    order.getTotalPrice(),
                    order.getCreateTime()
            });
        }
    }

    private void loadOrderDetails(int orderId) {
        detailTableModel.setRowCount(0);
        List<OrderDetail> details = orderDao.getOrderDetails(orderId);
        for (OrderDetail detail : details) {
            detailTableModel.addRow(new Object[]{
                    detail.getFoodId(),
                    detail.getFoodName(),
                    detail.getQuantity(),
                    detail.getSubtotal()
            });
        }
    }

    private void loadFoodsToTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Food> foods = foodDao.getAllFoods();
        for (Food food : foods) {
            model.addRow(new Object[]{
                    food.getId(),
                    food.getName(),
                    food.getPrice(),
                    food.getCategory(),
                    "删除"
            });
        }
    }

    private void loadFoods() {
    }

    private void saveNewFood() {
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            String imagePath = imagePathField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String description = descriptionArea.getText().trim();

            if (name.isEmpty() || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "名称和图片路径不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Food newFood = new Food(0, name, price, imagePath, category, description);

            boolean success = foodDao.addFood(newFood);
            if (success) {
                JOptionPane.showMessageDialog(this, "添加成功");
                clearFoodForm();
                JTabbedPane tabbedPane = (JTabbedPane) getComponent(1);
                JPanel foodPanel = (JPanel) tabbedPane.getComponentAt(1);
                JSplitPane splitPane = (JSplitPane) foodPanel.getComponent(1);
                JScrollPane leftScroll = (JScrollPane) splitPane.getLeftComponent();
                JTable foodTable = (JTable) leftScroll.getViewport().getView();
                loadFoodsToTable((DefaultTableModel) foodTable.getModel());

                mainFrame.refreshFoodPanels();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "价格请输入数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "添加失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFoodForm() {
        nameField.setText("");
        priceField.setText("");
        imagePathField.setText("");
        categoryCombo.setSelectedIndex(0);
        descriptionArea.setText("");
    }
}
