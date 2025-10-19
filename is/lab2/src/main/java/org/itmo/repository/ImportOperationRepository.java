package org.itmo.repository;

import org.itmo.model.ImportOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportOperationRepository extends JpaRepository<ImportOperation, Long> {
    List<ImportOperation> findByUsernameOrderByCreatedAtDesc(String username);
    List<ImportOperation> findAllByOrderByCreatedAtDesc();
}
