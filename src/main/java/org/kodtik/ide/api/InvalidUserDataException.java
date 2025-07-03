package org.kodtik.ide.api;

public class InvalidUserDataException extends KodTikIdeException {
  public InvalidUserDataException() {}

  public InvalidUserDataException(String message) {
    super(message);
  }

  public InvalidUserDataException(String message, Throwable cause) {
    super(message, cause);
  }
}
