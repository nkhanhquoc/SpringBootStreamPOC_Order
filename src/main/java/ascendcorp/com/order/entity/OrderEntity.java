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
@Table(name = "torder")
@AllArgsConstructor
@Builder
public class OrderEntity {

  @Id
  private String id;

  private Long value;

  private String status;

  private String message;
}
