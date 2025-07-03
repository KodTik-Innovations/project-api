package org.kodtik.ide.api.internal.file;

import java.io.File;

public class IdentityFileResolver extends AbstractFileResolver {
  @Override
  protected File doResolve(File path) {
    if (!path.isAbsolute()) {
      throw new UnsupportedOperationException(
          String.format("Cannot convert relative path %s to an absolute file.", path));
    }
    return path;
  }

  @Override
  public String resolveAsRelativePath(Object path) {
    throw new UnsupportedOperationException(
        String.format("Cannot convert path %s to a relative path.", path));
  }

  @Override
  public boolean canResolveRelativePath() {
    return false;
  }
}
