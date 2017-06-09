package com.life.http.filter;

import com.life.api.ApiMessage;
import com.life.common.HRequest;
import com.life.model.UserModel;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class FilterAuthentication extends BaseFilter {

    @Override
    public void init(FilterConfig fc) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        if (sr instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) sr;

            String token = httpRequest.getHeader("token");
            if (StringUtils.isBlank(token)) {
                renderJson(sr1, ApiMessage.INVALID_DATA);
                return;
            }
            int uid = UserModel.instance.checkToken(token);
            if (uid > 0) {
                sr.setAttribute("uid", uid);
                fc.doFilter(sr, sr1);
            } else {
                renderJson(sr1, ApiMessage.PERMISSION_DENY);
            }
        } else {
            renderJson(sr1, ApiMessage.UNKNOWN_EXCEPTION);
        }

    }

    @Override
    public void destroy() {

    }

}
