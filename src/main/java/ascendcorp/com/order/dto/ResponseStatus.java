package ascendcorp.com.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseStatus implements Serializable {

  private static final long serialVersionUID = -4654525474520828105L;

  @JsonProperty("code")
  private String code;

  @JsonProperty("message")
  private String message;

}
