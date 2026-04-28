package ui;

import model.User;
import dao.UserDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        setVisible(true);
    }

    private void initUI() {
        setTitle("餐饮点餐系统 - 登录");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //标题区域
        JLabel titleLabel = new JLabel("用户登录", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        //表单区域
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel userLabel = new JLabel("用户名：");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        usernameField = new JTextField();
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        JLabel pwdLabel = new JLabel("密码：");
        pwdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(pwdLabel);
        formPanel.add(passwordField);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        //按钮区域（添加注册按钮）
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginBtn = new JButton("登录");
        loginBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        loginBtn.setPreferredSize(new Dimension(120, 40));

        JButton registerBtn = new JButton("注册");
        registerBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        registerBtn.setPreferredSize(new Dimension(120, 40));

        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        //登录按钮事件
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "用户名和密码不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserDao userDao = new UserDao();
            User user = userDao.login(username, password);

            if (user != null) {
                dispose();
                mainFrame.initPanels(user);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        //注册按钮事件
        registerBtn.addActionListener(e -> {
            JDialog registerDialog = new JDialog(LoginFrame.this, "用户注册", true);
            registerDialog.setSize(350, 300);
            registerDialog.setLocationRelativeTo(LoginFrame.this);

            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 20));
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            JLabel regUserLabel = new JLabel("用户名：");
            regUserLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            JTextField regUserField = new JTextField();

            JLabel regPwdLabel = new JLabel("密码：");
            pwdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            JPasswordField regPwdField = new JPasswordField();

            JLabel confirmLabel = new JLabel("确认密码：");
            confirmLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            JPasswordField confirmPwdField = new JPasswordField();

            panel.add(regUserLabel);
            panel.add(regUserField);
            panel.add(pwdLabel);
            panel.add(regPwdField);
            panel.add(confirmLabel);
            panel.add(confirmPwdField);

            JButton doRegisterBtn = new JButton("注册");
            doRegisterBtn.addActionListener(ae -> {
                String username = regUserField.getText().trim();
                String password = new String(regPwdField.getPassword()).trim();
                String confirm = new String(confirmPwdField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(registerDialog, "用户名和密码不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(registerDialog, "两次密码输入不一致", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                UserDao userDao = new UserDao();
                if (userDao.register(username, password)) {
                    JOptionPane.showMessageDialog(registerDialog, "注册成功，请登录", "成功", JOptionPane.INFORMATION_MESSAGE);
                    registerDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(registerDialog, "注册失败，用户名可能已存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            registerDialog.setLayout(new BorderLayout());
            registerDialog.add(panel, BorderLayout.CENTER);
            registerDialog.add(doRegisterBtn, BorderLayout.SOUTH);

            registerDialog.setVisible(true);
        });

        pack();
        setSize(450, 350);
    }
}
