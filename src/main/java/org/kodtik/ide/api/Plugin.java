package org.kodtik.ide.api;

public interface Plugin<T> {

  void apply(T target);
}
