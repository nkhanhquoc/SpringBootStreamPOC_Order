package ascendcorp.com.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Table(name = "torder")
@AllArgsConstructor
@Builder
@Data
public class OrderEntity extends BaseEntity {

  @Id
  private String id;

  private Long value;

  private String status;

  private String message;
}
