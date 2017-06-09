package com.life.http.handler;

import com.life.api.ApiMessage;
import com.life.common.JsonUtils;
import com.life.socket.handler.HandlerController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class BaseHandler extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(BaseHandler.class);

    protected Map<String, Object> getRequestBody(HttpServletRequest request) {

        StringBuilder jsonBuff = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBuff.append(line);
            }
        } catch (Exception ex) {
            LOGGER.error("Read body", ex);
        }

        if (jsonBuff.length() > 0) {
            return JsonUtils.Instance.getMap(jsonBuff.toString());
        }

        return new HashMap<>();
    }

    protected void renderJson(HttpServletResponse resp, ApiMessage apiMessage) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(apiMessage.toString());
        out.flush();
    }

    protected int checkFilterAuthentication(HttpServletRequest request) {
        return (Integer) request.getAttribute("uid");
    }
}
