package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {
    private MainFrame mainFrame;

    public HomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        //顶部标题
        JLabel titleLabel = new JLabel("欢迎使用餐饮点餐系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        //中间内容区域
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton breakfastBtn = new JButton("早餐店");
        breakfastBtn.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        breakfastBtn.addActionListener(e -> mainFrame.switchPanel("breakfast"));

        JButton dailyBtn = new JButton("一日三餐好好吃饭店");
        dailyBtn.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        dailyBtn.addActionListener(e -> mainFrame.switchPanel("daily"));

        contentPanel.add(breakfastBtn);
        contentPanel.add(dailyBtn);
        add(contentPanel, BorderLayout.CENTER);

        //底部购物车和退出按钮
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton cartBtn = new JButton("购物车");
        cartBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        cartBtn.setPreferredSize(new Dimension(150, 40));
        cartBtn.addActionListener(e -> mainFrame.switchPanel("cart"));

        //添加退出登录按钮
        JButton logoutBtn = new JButton("退出登录");
        logoutBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        logoutBtn.setPreferredSize(new Dimension(150, 40));
        logoutBtn.addActionListener(e -> {
            mainFrame.setVisible(false);
            new LoginFrame(mainFrame).setVisible(true);
        });

        bottomPanel.add(cartBtn);
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}