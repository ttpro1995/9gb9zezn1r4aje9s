package com.life.http.filter;

import com.life.api.ApiMessage;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.ServletResponse;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public abstract class BaseFilter implements Filter {

    protected void renderJson(ServletResponse resp, ApiMessage apiMessage) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(apiMessage.toString());
        out.flush();
    }

}
