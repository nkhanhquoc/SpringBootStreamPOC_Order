package ascendcorp.com.order.exception.message;

public interface ApplicationMessage {

  String getKey();

  String getMessage();

  String getDescription();

  default int getStatusCode() {
    return 200;
  }

  default ApplicationMessage get(String key) {
    return this.getKey().equalsIgnoreCase(key) ? this : null;
  }

}
