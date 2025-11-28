package com.infra4u.billing_manager_service.config;

import com.infra4u.billing_manager_service.config.multi_tenant.TenantContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

import static com.infra4u.billing_manager_service.use_case.http.request.HttpHeader.CLIENT_ID_HEADER;


@Component
@RequiredArgsConstructor
public class ClientIdFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ClientIdFilter.class);
    private static final String ACTUATOR_BASE_PATH = "/actuator";
    private final com.infra4u.billing_manager_service.config.multi_tenant.TenantPropertySource tenantPropertySource;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // Skip logging/tenant resolution for Actuator endpoints to avoid log pollution
        final String requestUri = req.getRequestURI();
        if (requestUri != null && (requestUri.equals(ACTUATOR_BASE_PATH) || requestUri.startsWith(ACTUATOR_BASE_PATH + "/"))) {
            chain.doFilter(request, response);
            return;
        }

        final String clientID = req.getHeader(CLIENT_ID_HEADER);
        if (Objects.nonNull(clientID)) {
            logger.info("Client id: {}", clientID);
            TenantContext.setCurrentTenant(tenantPropertySource.getTenantByClientId(clientID));
        }
        chain.doFilter(request, response);
    }
}
