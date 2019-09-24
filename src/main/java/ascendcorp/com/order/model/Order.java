package ascendcorp.com.order.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class Order implements Serializable {

  private String id;
  private String message;
  private String status;
  private Long value;
}
