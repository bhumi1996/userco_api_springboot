package com.secui.mvc.config;


import com.secui.mvc.service.PortalInterface;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CORSFilter implements Filter {

    private final PortalInterface portalInterface;

    public CORSFilter(PortalInterface portalInterface) {
        this.portalInterface = portalInterface;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       HttpServletResponse response = (HttpServletResponse) servletResponse;
       HttpServletRequest request = (HttpServletRequest) servletRequest;
        String domain = request.getServerName();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers","Content-Type,Access-Control-Allow-Headers,Authorization,X-Requested-With,observe,x-api-secret,x-api-key");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials","true");
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            filterChain.doFilter(request,response);
        }

    }
}
