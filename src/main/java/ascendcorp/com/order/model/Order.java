package ascendcorp.com.order.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class Order extends BaseModel{

  private String id;
  private String message;
  private String status;
  private Long value;
}
