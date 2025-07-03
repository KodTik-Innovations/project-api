package org.kodtik.ide.api.specs;

public interface Spec<T> {
  boolean isSatisfiedBy(T element);
}
