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
    private final com.infra4u.billing_manager_service.config.multi_tenant.TenantPropertySource tenantPropertySource;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        final String clientID = req.getHeader(CLIENT_ID_HEADER);
        if (Objects.nonNull(clientID)) {
            logger.info("Client id: {}", clientID);
            TenantContext.setCurrentTenant(tenantPropertySource.getTenantByClientId(clientID));
        }
        chain.doFilter(request, response);
    }
}
