package ascendcorp.com.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Table(name = "verify_order")
@AllArgsConstructor
@Builder
public class VerifyOrderEntity {
  @Id
  private String id;
  private String oldStatus;
  private String newStatus;
}
