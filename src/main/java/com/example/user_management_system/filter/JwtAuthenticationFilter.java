package com.example.user_management_system.filter;

import com.example.user_management_system.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        if (method.equals("OPTIONS")) {
            return true;
        }

        if (method.equals("POST") && path.equals("/admin/admins")) {
            return true;
        }

        if (method.equals("POST") && path.equals("/admin/login")) {
            return true;
        }

        if (method.equals("POST") && path.equals("/portal/register")) {
            return true;
        }

        if (method.equals("POST") && path.equals("/portal/login")) {
            return true;
        }

        if (method.equals("POST") && path.equals("/portal/forgot-password")) {
            return true;
        }

        if (method.equals("POST") && path.equals("/portal/reset-password")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Thiếu hoặc sai định dạng header Authorization\"}");
            return;
        }

        String token = authHeader.substring(7);

        boolean isValid = jwtUtil.isTokenValid(token);

        if (!isValid) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Token không hợp lệ hoặc đã hết hạn\"}");
            return;
        }

        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        request.setAttribute("authenticatedUser", username);
        request.setAttribute("authenticatedRole", role);

        String path = request.getRequestURI();

        if (path.startsWith("/admin/") && !role.equals("ADMIN")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Chỉ ADMIN mới được truy cập khu vực này\"}");
            return;
        }

        if (path.startsWith("/portal/") && !role.equals("USER")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Chỉ USER mới được truy cập khu vực này\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
