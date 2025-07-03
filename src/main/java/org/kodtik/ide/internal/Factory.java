package org.kodtik.ide.internal;

import javax.annotation.Nullable;

public interface Factory<T> {
  @Nullable
  T create();
}
