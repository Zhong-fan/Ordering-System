package ui;

import model.Food;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FoodItemPanel extends JPanel {
    private MainFrame mainFrame;
    private Food food;
    private JSpinner quantitySpinner;

    public FoodItemPanel(MainFrame mainFrame, Food food) {
        this.mainFrame = mainFrame;
        this.food = food;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setPreferredSize(new Dimension(200, 300));

        //食物图片
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(0, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //加载图片
        try {
            File imageFile = new File(food.getImagePath());
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(food.getImagePath());
                //缩放图片以适应标签
                Image scaledImage = icon.getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("图片未找到");
            }
        } catch (Exception e) {
            imageLabel.setText("加载失败");
        }
        add(imageLabel, BorderLayout.NORTH);

        //食物信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //食物名称
        JLabel nameLabel = new JLabel(food.getName());
        nameLabel.setFont(new Font("宋体", Font.BOLD, 16));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));

        //食物价格
        JLabel priceLabel = new JLabel("价格: ¥" + food.getPrice());
        priceLabel.setForeground(Color.RED);
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));

        //食物描述
        String description = food.getDescription();
        if (description.length() > 30) {
            description = description.substring(0, 30) + "...";
        }
        JLabel descLabel = new JLabel("描述: " + description);
        descLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        //数量选择和添加按钮
        JPanel controlPanel = new JPanel(new BorderLayout());

        //数量选择器
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantitySpinner.setPreferredSize(new Dimension(60, 30));
        controlPanel.add(quantitySpinner, BorderLayout.WEST);

        //添加到购物车按钮
        JButton addBtn = new JButton("加入购物车");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = (Integer) quantitySpinner.getValue();
                mainFrame.addToCart(food, quantity);
                JOptionPane.showMessageDialog(
                        FoodItemPanel.this,
                        "已添加 " + food.getName() + " x " + quantity + " 到购物车",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        controlPanel.add(addBtn, BorderLayout.CENTER);

        infoPanel.add(controlPanel);
        add(infoPanel, BorderLayout.CENTER);
    }
}
