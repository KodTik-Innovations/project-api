package org.kodtik.ide.api;

public class UnknownProjectException extends UnknownDomainObjectException {
  public UnknownProjectException(String message) {
    super(message);
  }

  public UnknownProjectException(String message, Throwable cause) {
    super(message, cause);
  }
}
