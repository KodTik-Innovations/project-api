package org.kodtik.ide.api;

public enum Status {
  UP_TO_DATE("UP-TO-DATE"),
  NO_SOURCE("NO-SOURCE");

  private final String value;

  Status(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
