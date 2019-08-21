package ascendcorp.com.order.logger;


import java.io.Serializable;
import com.ascendcorp.logger.log4j.ACNLogger;
import org.apache.commons.lang3.StringUtils;


public class Logger implements Serializable {

  private static final long serialVersionUID = -8413250031672506886L;

  private static final String LOG_SEPARATOR = "*====*";

  private ACNLogger acnLogger;

  private String className;

  private Logger(ACNLogger logger, Class<?> clazz) {
    acnLogger = logger;
    className = clazz != null ? clazz.getSimpleName() : "";
  }

  public static Logger getInstance(Class<?> loggerName) {
    return new Logger(ACNLogger.create(loggerName), loggerName);
  }

  public void error(String message, Throwable throwable) {
    acnLogger.error(getMessageFormat(message), throwable);
  }

  public void error(String message, Object... var) {
    acnLogger.error(getMessageFormat(message), var);
  }

  public void info(String message) {
    acnLogger.info(getMessageFormat(message));
  }

  public void info(String message, Object... var) { acnLogger.info(getMessageFormat(message), var); }

  public void warn(String message) {
    acnLogger.warn(getMessageFormat(message));
  }

  public void catching(Throwable t) {
    acnLogger.catching(t);
  }

  public void warn(String message, Object... var) { acnLogger.warn(getMessageFormat(message), var); }

  private String getMessageFormat(String message) {
    String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
    int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
    return String.format("Thread: %s, Class: %s , Method: %s, Line: %s, %s", Thread.currentThread().getName(), className, methodName, lineNumber, message);
  }

  public String getStackTrace(Throwable throwable) {
    return StringUtils.join(throwable.getStackTrace(), LOG_SEPARATOR);
  }
}
