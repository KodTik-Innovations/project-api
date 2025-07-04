package org.kodtik.ide.api;

public interface Namer<T> {

  String determineName(T object);

  class Comparator<T> implements java.util.Comparator<T> {

    private final Namer<? super T> namer;

    public Comparator(Namer<? super T> namer) {
      this.namer = namer;
    }

    @Override
    public int compare(T o1, T o2) {
      return namer.determineName(o1).compareTo(namer.determineName(o2));
    }

    @Override
    public boolean equals(Object obj) {
      return getClass().equals(obj.getClass()) && namer.equals(((Comparator) obj).namer);
    }

    @Override
    public int hashCode() {
      return 31 * namer.hashCode();
    }
  }
}
