package tech.itpark.exception;

// FIXME: add parent exception to all application exceptions
public class NoAppropriateConverter extends RuntimeException {
  public NoAppropriateConverter() {
  }

  public NoAppropriateConverter(String message) {
    super(message);
  }

  public NoAppropriateConverter(String message, Throwable cause) {
    super(message, cause);
  }

  public NoAppropriateConverter(Throwable cause) {
    super(cause);
  }

  public NoAppropriateConverter(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
