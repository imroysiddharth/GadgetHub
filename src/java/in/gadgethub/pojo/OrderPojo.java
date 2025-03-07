
package in.gadgethub.pojo;


public class OrderPojo {
    private String orderid ;
    private String productid;
    private int quantity;
    private double amount ;
    private int shipped;

    public OrderPojo() {
    }

    public OrderPojo(String orderid, String productid, int quantity, double amount, int shipped) {
        this.orderid = orderid;
        this.productid = productid;
        this.quantity = quantity;
        this.amount = amount;
        this.shipped = shipped;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getShipped() {
        return shipped;
    }

    public void setShipped(int shipped) {
        this.shipped = shipped;
    }

    @Override
    public String toString() {
        return "OrderPojo{" + "orderid=" + orderid + ", productid=" + productid + ", quantity=" + quantity + ", amount=" + amount + ", shipped=" + shipped + '}';
    }
    
    
    
}
