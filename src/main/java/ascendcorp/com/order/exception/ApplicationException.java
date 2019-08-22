package ascendcorp.com.order.exception;

import ascendcorp.com.order.exception.message.ApplicationMessage;
import ascendcorp.com.order.exception.message.InternalServerMessage;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

  private static final long serialVersionUID = 8133784150850998787L;

  private final transient ApplicationMessage error;

  public ApplicationException() {
    super(InternalServerMessage.INTERNAL_SERVER_ERROR.getDescription());
    this.error = InternalServerMessage.INTERNAL_SERVER_ERROR;
  }

  public ApplicationException(String message) {
    super(message);
    this.error = InternalServerMessage.INTERNAL_SERVER_ERROR;
  }

  public ApplicationException(ApplicationMessage error) {
    super(error.getDescription());
    this.error = error;
  }

  public ApplicationException(Throwable cause, ApplicationMessage error) {
    super(error.getDescription(), cause);
    this.error = error;
  }

  public ApplicationException(String exceptionMessage, ApplicationMessage error) {
    super(exceptionMessage);
    this.error = error;
  }

  public ApplicationException(String message, Throwable cause,
      ApplicationMessage error) {
    super(message, cause);
    this.error = error;
  }
}
