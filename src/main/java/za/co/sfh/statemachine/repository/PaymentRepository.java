package za.co.sfh.statemachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.sfh.statemachine.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
