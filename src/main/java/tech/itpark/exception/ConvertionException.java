package tech.itpark.exception;

// FIXME: add parent exception to all application exceptions
public class ConvertionException extends RuntimeException {
  public ConvertionException() {
  }

  public ConvertionException(String message) {
    super(message);
  }

  public ConvertionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConvertionException(Throwable cause) {
    super(cause);
  }

  public ConvertionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
