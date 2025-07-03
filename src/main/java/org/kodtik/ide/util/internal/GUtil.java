package org.kodtik.ide.util.internal;

import java.util.Collection;
import java.util.Comparator;
import javax.annotation.Nullable;
import org.kodtik.ide.internal.Factory;

public class GUtil {

  public static boolean isTrue(@Nullable Object object) {
    if (object == null) {
      return false;
    }
    if (object instanceof Collection) {
      return ((Collection) object).size() > 0;
    } else if (object instanceof String) {
      return ((String) object).length() > 0;
    }
    return true;
  }

  @Nullable
  public static <T> T getOrDefault(@Nullable T object, Factory<T> defaultValueSupplier) {
    return isTrue(object) ? object : defaultValueSupplier.create();
  }

  public static Comparator<String> caseInsensitive() {
    return new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        int diff = o1.compareToIgnoreCase(o2);
        if (diff != 0) {
          return diff;
        }
        return o1.compareTo(o2);
      }
    };
  }
}
