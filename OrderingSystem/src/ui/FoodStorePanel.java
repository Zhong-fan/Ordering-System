package ui;

import model.Food;
import dao.FoodDao;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FoodStorePanel extends JPanel {
    private MainFrame mainFrame;
    private String storeName;
    private String category;

    public FoodStorePanel(MainFrame mainFrame, String storeName, String category) {
        this.mainFrame = mainFrame;
        this.storeName = storeName;
        this.category = category;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        //顶部面板
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(storeName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));

        JButton backBtn = new JButton("返回首页");
        backBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        backBtn.addActionListener(e -> mainFrame.switchPanel("home"));

        JButton cartBtn = new JButton("购物车");
        cartBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cartBtn.addActionListener(e -> mainFrame.switchPanel("cart"));

        //添加退出登录按钮
        JButton logoutBtn = new JButton("退出登录");
        logoutBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        logoutBtn.addActionListener(e -> {
            mainFrame.setVisible(false);
            new LoginFrame(mainFrame).setVisible(true);
        });

        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTopPanel.add(cartBtn);
        rightTopPanel.add(logoutBtn);
        topPanel.add(rightTopPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        //中间菜品列表
        refreshFoodList();
    }

    //刷新菜品列表
    public void refreshFoodList() {
        JPanel foodPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        foodPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        FoodDao foodDao = new FoodDao();
        List<Food> foods = foodDao.getFoodsByCategory(category);

        for (Food food : foods) {
            foodPanel.add(new FoodItemPanel(mainFrame, food));
        }

        add(new JScrollPane(foodPanel), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}