package com.infra4u.billing_manager_service.use_case.http.response;

import com.infra4u.billing_manager_service.use_case.http.request.PageFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public interface PageFilterStrategy {
    default Pageable map(PageFilter pageFilter){
        if (Objects.isNull(pageFilter.page()) && Objects.isNull(pageFilter.size())) {
            return Pageable.unpaged();
        }
        return PageRequest.of(pageFilter.page() <= 0  ? 0 : pageFilter.page() - 1, pageFilter.size());
    }
    default Pageable map(PageFilter pageFilter, Sort sort){
        if (Objects.isNull(pageFilter.page()) && Objects.isNull(pageFilter.size())) {
            return Pageable.unpaged(sort);
        }
        return PageRequest.of(pageFilter.page() <= 0  ? 0 : pageFilter.page() - 1, pageFilter.size());
    }
}
