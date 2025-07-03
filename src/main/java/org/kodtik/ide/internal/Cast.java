package org.kodtik.ide.internal;

import javax.annotation.Nullable;

public abstract class Cast {

  public static <O, I> O cast(Class<O> outputType, I object) {
    try {
      return outputType.cast(object);
    } catch (ClassCastException e) {
      throw new ClassCastException(
          String.format(
              "Failed to cast object %s of type %s to target type %s",
              object, object.getClass().getName(), outputType.getName()));
    }
  }

  @Nullable
  public static <O, I> O castNullable(Class<O> outputType, @Nullable I object) {
    if (object == null) {
      return null;
    }
    return cast(outputType, object);
  }

  @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
  @Nullable
  public static <T> T uncheckedCast(@Nullable Object object) {
    return (T) object;
  }

  @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
  public static <T> T uncheckedNonnullCast(Object object) {
    return (T) object;
  }
}
