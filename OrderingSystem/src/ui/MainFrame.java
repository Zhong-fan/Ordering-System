package ui;

import model.CartItem;
import model.Food;
import model.User;
import dao.OrderDao;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private List<CartItem> cartItems = new ArrayList<>();

    private HomePanel homePanel;
    private AdminPanel adminPanel;
    private CartPanel cartPanel;
    private FoodStorePanel breakfastPanel;
    private FoodStorePanel dailyPanel;

    public MainFrame() {
        initFrame();
    }

    private void initFrame() {
        setTitle("餐饮点餐系统");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(false);
    }

    public void initPanels(User user) {
        try {
            this.currentUser = user;
            cartItems.clear();

            System.out.println("登录用户: " + user.getUsername() + " (管理员: " + user.isAdmin() + ")");

            if (user.isAdmin()) {

                adminPanel = new AdminPanel(this, user);
                setContentPane(adminPanel);
            } else {

                initUserPanels();
                setContentPane(homePanel);
            }

            revalidate();
            repaint();
            setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "面板初始化失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void initUserPanels() {
        if (homePanel == null) homePanel = new HomePanel(this);
        if (breakfastPanel == null) breakfastPanel = new FoodStorePanel(this, "早餐店", "breakfast");
        if (dailyPanel == null) dailyPanel = new FoodStorePanel(this, "一日三餐好好吃饭店", "daily");
        if (cartPanel == null) cartPanel = new CartPanel(this);
    }

    public void switchPanel(String panelName) {
        try {
            JPanel targetPanel = null;
            switch (panelName) {
                case "home":
                    targetPanel = homePanel;
                    break;
                case "breakfast":
                    targetPanel = breakfastPanel;
                    break;
                case "daily":
                    targetPanel = dailyPanel;
                    break;
                case "cart":
                    if (cartPanel != null) cartPanel.refreshCart();
                    targetPanel = cartPanel;
                    break;
                default:
                    targetPanel = homePanel;
            }

            if (targetPanel != null) {
                setContentPane(targetPanel);
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "面板未初始化: " + panelName, "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "切换面板失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void refreshFoodPanels() {
        if (breakfastPanel != null) breakfastPanel.refreshFoodList();
        if (dailyPanel != null) dailyPanel.refreshFoodList();
    }

    public void showHomePanel() {
        if (homePanel != null) {
            setContentPane(homePanel);
            revalidate();
            repaint();
        }
    }

    public void showAdminPanel() {
        if (adminPanel != null) {
            setContentPane(adminPanel);
            revalidate();
            repaint();
        }
    }

    public List<CartItem> getCartItems() { return cartItems; }

    public void updateCartItemQuantity(int foodId, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getFood().getId() == foodId) {
                if (newQuantity <= 0) cartItems.remove(item);
                else item.setQuantity(newQuantity);
                break;
            }
        }
    }

    public double calculateTotal() {
        double total = 0.0;
        for (CartItem item : cartItems) total += item.getFood().getPrice() * item.getQuantity();
        return Math.round(total * 100.0) / 100.0;
    }

    public void addToCart(Food food, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getFood().getId() == food.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(food, quantity));
    }

    public void removeFromCart(int foodId) {
        cartItems.removeIf(item -> item.getFood().getId() == foodId);
        if (cartPanel != null) cartPanel.refreshCart();
    }

    public void clearCart() {
        cartItems.clear();
        if (cartPanel != null) cartPanel.refreshCart();
    }

    public void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "购物车为空，无法结算！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrderDao orderDao = new OrderDao();
        int result = orderDao.createOrder(currentUser, cartItems);
        boolean success = result > 0;


        if (success) {
            JOptionPane.showMessageDialog(this, "订单创建成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearCart();
            showHomePanel();
        } else {
            JOptionPane.showMessageDialog(this, "订单创建失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User getCurrentUser() { return currentUser; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
