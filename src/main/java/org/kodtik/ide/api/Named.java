package org.kodtik.ide.api;

public interface Named {

  String getName();

  class Namer implements org.kodtik.ide.api.Namer<Named> {

    public static final org.kodtik.ide.api.Namer<Named> INSTANCE = new Namer();

    @Override
    public String determineName(Named object) {
      return object.getName();
    }

    @SuppressWarnings("unchecked")
    public static <T> org.kodtik.ide.api.Namer<? super T> forType(Class<? extends T> type) {
      if (Named.class.isAssignableFrom(type)) {
        return (org.kodtik.ide.api.Namer<T>) INSTANCE;
      } else {
        throw new IllegalArgumentException(
            String.format(
                "The '%s' cannot be used with FactoryNamedDomainObjectContainer without specifying"
                    + " a Namer as it does not implement the Named interface.",
                type));
      }
    }
  }
}
