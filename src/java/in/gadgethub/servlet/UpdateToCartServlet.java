/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.gadgethub.servlet;

import in.gadgethub.dao.CartDAO;
import in.gadgethub.dao.impl.CartDAOImpl;
import in.gadgethub.dao.impl.DemandImpl;
import in.gadgethub.dao.impl.ProductDaoImpl;
import in.gadgethub.pojo.CartPojo;
import in.gadgethub.pojo.DemandPojo;
import in.gadgethub.pojo.ProductPojo;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author roysi
 */
public class UpdateToCartServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        String password = (String) session.getAttribute("password");
        String userType = (String) session.getAttribute("userType");
        if (userType == null || userName == null || password == null || !userType.equalsIgnoreCase("customer")) {
            response.sendRedirect("login.jsp?message=Access denied ! Please login first");
            return;
        }
        String prodId = request.getParameter("pid"); 
        int pQty = Integer.parseInt(request.getParameter("pQty")); 
        ProductDaoImpl prodDao = new ProductDaoImpl(); 
        CartDAO cartDao=new CartDAOImpl(); 
        ProductPojo product = prodDao.getProductDetails(prodId); 
        int availableQty = product.getProdQuantity(); 
        if (availableQty < pQty) { 
            String status = "Only " + availableQty + " no of " + product.getProdName() + " are available in the shop!.So we can add only " + availableQty + " products in your cart"; 
            DemandImpl demand = new DemandImpl(); 
            DemandPojo demandPojo = new DemandPojo(userName, prodId, pQty - availableQty); 
            boolean flag = demand.addProduct(demandPojo); 
            if (flag) { 
                status += "<br/>Later we will mail you when " + product.getProdName() + " will be available"; 
            } 
            RequestDispatcher rd = request.getRequestDispatcher("./CartDetailsServlet"); 
            request.setAttribute("message",status); 
            rd.forward(request, response); 
        }else{ 
            String status = cartDao.updateProductInCart(new CartPojo(userName, prodId, pQty)); 
            RequestDispatcher rd = request.getRequestDispatcher("./CartDetailsServlet"); 
            request.setAttribute("message",status); 
            rd.forward(request, response); 
    }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
