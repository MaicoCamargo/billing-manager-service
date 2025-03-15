package com.infra4u.billing_manager_service.use_case.status;

import com.infra4u.billing_manager_service.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

}
