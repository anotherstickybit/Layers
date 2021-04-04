package tech.itpark.exception;

// FIXME: add parent exception to all application exceptions
public class UserNotExistsException extends RuntimeException {
  public UserNotExistsException() {
  }

  public UserNotExistsException(String message) {
    super(message);
  }

  public UserNotExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserNotExistsException(Throwable cause) {
    super(cause);
  }

  public UserNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
