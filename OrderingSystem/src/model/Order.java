package model;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private User user;
    private double totalPrice;
    private Date createTime;
    private List<OrderDetail> details;

    public Order(int id, int userId, User user, double totalPrice, Date createTime, List<OrderDetail> details) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.totalPrice = totalPrice;
        this.createTime = createTime;
        this.details = details;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public List<OrderDetail> getDetails() { return details; }
    public void setDetails(List<OrderDetail> details) { this.details = details; }
}
