package org.kodtik.ide.api;

public class UnknownDomainObjectException extends KodTikIdeException {
  public UnknownDomainObjectException(String message) {
    super(message);
  }

  public UnknownDomainObjectException(String message, Throwable cause) {
    super(message, cause);
  }
}
