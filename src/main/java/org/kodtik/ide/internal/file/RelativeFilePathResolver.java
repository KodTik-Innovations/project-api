package org.kodtik.ide.internal.file;

public interface RelativeFilePathResolver {
  String resolveAsRelativePath(Object path);

  String resolveForDisplay(Object path);
}
