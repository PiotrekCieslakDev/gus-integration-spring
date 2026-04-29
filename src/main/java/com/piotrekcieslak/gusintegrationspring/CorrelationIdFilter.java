package com.piotrekcieslak.gusintegrationspring;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(0)
public class CorrelationIdFilter implements Filter {
    private static final String MDC_KEY = "correlationId";
    private static final String HEADER_NAME = "X-Correlation-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        String correlationId = ((HttpServletRequest) request).getHeader(HEADER_NAME);
        if (correlationId == null) correlationId = UUID.randomUUID().toString();

        MDC.put(MDC_KEY, correlationId);
        ((HttpServletResponse) response).setHeader(HEADER_NAME, correlationId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }
}