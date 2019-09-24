package ascendcorp.com.order.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class VerifyOrder extends BaseModel{

  private String id;
  private String oldStatus;
  private String newStatus;

}
