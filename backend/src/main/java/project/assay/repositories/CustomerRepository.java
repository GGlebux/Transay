package project.assay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.assay.models.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    @Query(
            """
            SELECT (count(c) > 0) 
            FROM Customer c 
            WHERE c.email = :email 
            AND c.status = project.assay.models.enums.CustomerStatus.ACTIVE
            """
    )
    boolean existsActiveByEmail(@Param("email") String email);

    Optional<Customer> findByEmail(String email);


    @Query(
            """
            SELECT c 
            FROM Customer c 
            WHERE c.status = project.assay.models.enums.CustomerStatus.PENDING 
            AND c.createdAt < :yesterday
            """
    )
    List<Customer> findAllExpiredPending(@Param("yesterday") LocalDateTime yesterday);
}
