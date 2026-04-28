package model;

public class OrderDetail {
    private int id;
    private int orderId;
    private int foodId;
    private String foodName;
    private int quantity;
    private double subtotal;

    public OrderDetail() {
    }


    public OrderDetail(int id, int orderId, int foodId, String foodName, int quantity, double subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public int getDishId() {
        return getFoodId();
    }

    public void setDishId(int dishId) {
        setFoodId(dishId);
    }

    public String getDishName() {
        return getFoodName();
    }

    public void setDishName(String dishName) {
        setFoodName(dishName);
    }

    // 原有Getter和Setter方法保持不变
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
