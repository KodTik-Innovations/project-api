package org.kodtik.ide.api;

import java.io.File;
import javax.annotation.Nullable;

public class KodTikIdeException extends RuntimeException {

  private final long timeTaken;
  private final File file;
  private final int line;

  public KodTikIdeException() {
    this(null, null, 0, null, 0);
  }

  public KodTikIdeException(String message) {
    this(message, null, 0, null, 0);
  }

  public KodTikIdeException(String message, @Nullable Throwable cause) {
    this(message, cause, 0, null, 0);
  }

  public KodTikIdeException(
      String message,
      @Nullable Throwable cause,
      long timeTaken,
      @Nullable File file,
      @Nullable int line) {
    super(message, cause);
    this.timeTaken = timeTaken;
    this.file = file;
    this.line = line;
  }

  public long getTimeTaken() {
    return timeTaken;
  }

  @Nullable
  public File getFile() {
    return file;
  }

  public int getLine() {
    return line;
  }
}
