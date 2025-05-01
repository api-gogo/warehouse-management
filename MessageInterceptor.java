package com.ohgiraffers.warehousemanagement.wms.auth.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MessageInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        Object message = session.getAttribute("message");
        
        // 메시지가 있고 ModelAndView가 null이 아닌 경우에만 처리
        if (message != null && modelAndView != null) {
            modelAndView.addObject("message", message);
            // 메시지 처리 후 세션에서 제거
            session.removeAttribute("message");
        }
    }
}
