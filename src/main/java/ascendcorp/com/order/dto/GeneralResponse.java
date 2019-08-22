package ascendcorp.com.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GeneralResponse<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = -3426043401746248097L;

  @JsonProperty("status")
  private ResponseStatus status;

  private T data;


}
