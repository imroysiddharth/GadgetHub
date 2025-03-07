/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.gadgethub.dao.impl;

import in.gadgethub.dao.CartDAO;
import in.gadgethub.pojo.CartPojo;
import in.gadgethub.pojo.DemandPojo;
import in.gadgethub.utility.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author roysi
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public String addProductToCart(CartPojo cart) {
        String status = "Failed to add to cart";
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps1 = null;
        ResultSet rs = null;

        try {
            ps1 = conn.prepareStatement("Select * from USERCART where prodid=? and useremail=?");
            ps1.setString(1, cart.getProdId());
            ps1.setString(2, cart.getUseremail());
            rs = ps1.executeQuery();
            if (rs.next()) {
                ProductDaoImpl prodDao = new ProductDaoImpl();

                prodDao.getProductQuantity(cart.getProdId());
                int stockQty = prodDao.getProductQuantity(cart.getProdId());
                int newQty = cart.getQuantity() + rs.getInt("quantity");
                if (stockQty < newQty) {
                    cart.setQuantity(stockQty);
                    updateProductInCart(cart);
                    status = "Only" + stockQty + "no' of items that are avlbl in our stock is less than your demand so we are adding " + stockQty + "in your cart";
                    DemandPojo demandPojo = new DemandPojo();
                    demandPojo.setProdId(cart.getProdId());
                    demandPojo.setUseremail(cart.getUseremail());
                    demandPojo.setDemandQuantity(newQty - stockQty);
                    DemandImpl demandDao = new DemandImpl();
                    boolean result = demandDao.addProduct(demandPojo);
                    if (result == true) {
                        status = "We will email you when" + (newQty - stockQty) + "number of item available";
                    }
                } else {
                    cart.setQuantity(newQty);
                    updateProductInCart(cart);
                }
            }
        } catch (SQLException ex) {
            status = "Addition failed due to Exception";
            System.out.println("Error in addProductToCart : " + ex);
            ex.printStackTrace();
        }
        DBUtil.closeStatement(ps1);

        DBUtil.closeResultSet(rs);
        return status;
    }

    
    @Override
    public String updateProductInCart(CartPojo cart) {
        String status = "Failed to add to cart";
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
//        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int ans = 0;

        try {
            ps1 = conn.prepareStatement("Select * from USERCART where prodid=? and useremail=?");
            ps1.setString(1, cart.getProdId());
            ps1.setString(2, cart.getUseremail());
            rs = ps1.executeQuery();
            if (rs.next()) {
                int qty = cart.getQuantity();
                if (qty > 0) {
                    ps2 = conn.prepareStatement("Update USERCART set quantity=? where useremail=? and prodid=?");
                    ps2.setInt(1, cart.getQuantity());
                    ps2.setString(2, cart.getUseremail());
                    ps2.setString(3, cart.getProdId());
                    ans = ps2.executeUpdate();
                    if (ans > 0) {
                        status = "Product successfully updated in the cart";
                    }
                } else if (qty == 0) {
                    ps2 = conn.prepareStatement("Delete from USERCART where useremail=? and prodid=?");
                    ps2.setString(1, cart.getUseremail());
                    ps2.setString(2, cart.getProdId());
                    ans = ps2.executeUpdate();
                    if (ans > 0) {
                        status = "Product successfully updated in the cart";
                    }
                    else{
                         status = "Could not update the product!";    
                    }
                }
                
            }else{
                ps2 = conn.prepareStatement("Insert into USERCART values(?,?,?)");
                ps2.setString(1, cart.getUseremail());
                ps2.setString(2, cart.getProdId());
                ps2.setInt(3, cart.getQuantity());
                ans = ps2.executeUpdate();
                if (ans > 0) {
                    status = "Product successfully added to cart";
                }else{
                    status= "Could not add the product";
                }
            }
        } catch (SQLException ex) {
            status = "Updation failed due to Exception";
            System.out.println("Error in updateProductInCart : " + ex);
            ex.printStackTrace();
        }
        
        DBUtil.closeStatement(ps1);
        DBUtil.closeStatement(ps2);
        DBUtil.closeResultSet(rs);
        
        return status;
    }

    @Override
    public List<CartPojo> getAllCartItems(String userId) {
        List<CartPojo> itemList = new ArrayList<>();
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select * from usercart where useremail=?");
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                CartPojo cart = new CartPojo();
                cart.setUseremail(rs.getString("useremail"));
                cart.setProdId(rs.getString("prodId"));
                cart.setQuantity(rs.getInt("quantity"));
                itemList.add(cart);
            }
        } catch (SQLException ex) {

            System.out.println("Error in getAllCartItems : " + ex);
            ex.printStackTrace();
        }

        DBUtil.closeStatement(ps);
        DBUtil.closeResultSet(rs);
        return itemList;
    }

    @Override
    public int getCartItemCount(String userId, String itemId) {
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs =null ;
        int count = 0 ;
        try{
            ps = conn.prepareStatement("select quantity from usercart where useremail=? and prodid=? ");
            ps.setString(1,userId);
            ps.setString(2, itemId);
            rs = ps.executeQuery();
            if(rs.next()){
              count = rs.getInt(1);
            }
        }catch (SQLException ex) {
           
            System.out.println("Error in removeProductFromCart : " + ex);
            ex.printStackTrace();
        }
        DBUtil.closeStatement(ps);
        DBUtil.closeResultSet(rs);
        return count;
    }

    @Override
    public String removeProductFromCart(String userId, String prodId) {
        String status = "Failed to remove from cart";
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            ps1 = conn.prepareStatement("Select * from USERCART where prodid=? and useremail=?");
            ps1.setString(1, prodId);
            ps1.setString(2, userId);
            rs = ps1.executeQuery();
            if (rs.next()) {
                int prodQuantity = rs.getInt("quantity");
                prodQuantity -= 1;
                if (prodQuantity > 0) {
                    ps2 = conn.prepareStatement("Update usercart set quantity=? where useremail=? and prodid=?");
                    ps2.setInt(1, prodQuantity);
                    ps2.setString(2, userId);
                    ps2.setString(3, prodId);
                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product successfully removed from cart";

                    }
                } else {
                    ps2 = conn.prepareStatement("Delete from usercart where useremail=? and prodid=?");
                    ps2.setInt(1, prodQuantity);
                    ps2.setString(2, userId);
                    ps2.setString(3, prodId);
                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product successfully removed from cart";
                    }
                }
            }
        } catch (SQLException ex) {
            status = "removal failed due to exception(in removeProductFromCart)";
            System.out.println("Error in removeProductFromCart : " + ex);
            ex.printStackTrace();
        }
        DBUtil.closeStatement(ps1);
        DBUtil.closeStatement(ps2);
        DBUtil.closeResultSet(rs);

        return status;
    }

    @Override
    public boolean removeAProduct(String userId, String prodId) {
        boolean status = false;
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("Delete from usercart where useremail=? and prodid=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            int k = ps.executeUpdate();
            if (k > 0) {
                status = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error in removeAProductFromCart : " + ex);
            ex.printStackTrace();
        }
        DBUtil.closeStatement(ps);
        return status;
    }

}
