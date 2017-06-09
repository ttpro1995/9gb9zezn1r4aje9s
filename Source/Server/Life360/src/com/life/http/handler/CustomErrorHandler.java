package com.life.http.handler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class CustomErrorHandler extends ErrorHandler {

    @Override
    public void doError(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter writer = response.getWriter();
        writer.print("page not found!!!");
        writer.flush();

    }

}
