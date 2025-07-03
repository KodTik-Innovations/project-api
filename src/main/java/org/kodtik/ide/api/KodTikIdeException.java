package org.kodtik.ide.api;

import javax.annotation.Nullable;

public class KodTikIdeException extends RuntimeException {
  public KodTikIdeException() {
    super();
  }

  public KodTikIdeException(String message) {
    super(message);
  }

  public KodTikIdeException(String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
