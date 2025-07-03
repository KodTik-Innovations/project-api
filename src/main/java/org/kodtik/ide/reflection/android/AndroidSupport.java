package org.kodtik.ide.reflection.android;

public abstract class AndroidSupport {

  public static boolean isDalvik() {
    return System.getProperty("java.vm.name", "").contains("Dalvik");
  }
}
