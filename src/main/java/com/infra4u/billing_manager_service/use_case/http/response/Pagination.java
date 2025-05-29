package com.infra4u.billing_manager_service.use_case.http.response;

import org.springframework.data.domain.Page;

public record Pagination(
        int total,
        int lastPage,
        int prevPage,
        int nextPage,
        int perPage,
        int currentPage,
        int totalItemPage
) {
    public static Pagination of(Page<?> page){
        int prevPage = page.getNumber() > 0 ? page.getNumber() - 1 : 0;
        int nextPage = page.getTotalPages() > 0 ? page.getNumber() + 1 : page.getTotalPages();
        return new Pagination((int) page.getTotalElements(), page.getTotalPages(), prevPage, nextPage, page.getSize(), page.getTotalPages(), page.getNumberOfElements() );
    }
}