package ascendcorp.com.order.repository;

import ascendcorp.com.order.entity.VerifyOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyOrderRepository extends JpaRepository<VerifyOrderEntity, String> {

}
