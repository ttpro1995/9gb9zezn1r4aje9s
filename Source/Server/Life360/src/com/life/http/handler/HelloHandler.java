package com.life.http.handler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class HelloHandler extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
        PrintWriter writer = resp.getWriter();
        System.out.println("hello get");
        writer.write("Hello!!!");
    }
    
    
    

}
