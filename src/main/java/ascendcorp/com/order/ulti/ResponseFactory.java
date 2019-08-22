package ascendcorp.com.order.ulti;

import ascendcorp.com.order.dto.GeneralResponse;
import ascendcorp.com.order.dto.ResponseStatus;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseFactory {


  public static <T extends Serializable> ResponseEntity<GeneralResponse<T>> success() {
    ResponseStatus responseStatus = ResponseStatus.builder()
        .code("SUCCESS")
        .message("SUCCESS").build();
    GeneralResponse<T> generalResponse = GeneralResponse.<T>builder().status(responseStatus)
        .build();
    return ResponseEntity.ok(generalResponse);
  }



  public static ResponseEntity<GeneralResponse> error(HttpStatus httpStatus,
      String code, String message) {
    ResponseStatus responseStatus = ResponseStatus.builder()
        .code(code).message(message).build();
    GeneralResponse generalResponse = GeneralResponse.builder().status(responseStatus).data(null)
        .build();
    return ResponseEntity.status(httpStatus).body(generalResponse);
  }
}
