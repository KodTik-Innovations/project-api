package org.kodtik.ide.api.internal.file;

import java.io.File;
import java.nio.file.Path;
import org.kodtik.ide.util.internal.GUtil;

public class BaseDirFileResolver extends AbstractFileResolver {
  private final File baseDir;

  public BaseDirFileResolver(File baseDir) {
    if (!GUtil.isTrue(baseDir)) {
      throw new IllegalArgumentException(
          String.format("baseDir may not be null or empty string. basedir='%s'", baseDir));
    }
    if (!baseDir.isAbsolute()) {
      throw new IllegalArgumentException(
          String.format("base dir '%s' is not an absolute file.", baseDir));
    }
    this.baseDir = baseDir;
  }

  @Override
  public String resolveAsRelativePath(Object path) {
    Path baseDir = this.baseDir.toPath();
    Path file = resolve(path).toPath();
    if (file.equals(baseDir)) {
      return ".";
    } else {
      return baseDir.relativize(file).toString();
    }
  }

  @Override
  public String resolveForDisplay(Object path) {
    Path file = resolve(path).toPath();
    Path baseDir = this.baseDir.toPath();
    if (file.equals(baseDir)) {
      return ".";
    }
    Path parent = baseDir.getParent();
    if (parent == null) {
      parent = baseDir;
    }
    if (file.startsWith(parent)) {
      return baseDir.relativize(file).toString();
    } else {
      return file.toString();
    }
  }

  @Override
  protected File doResolve(File file) {
    if (!file.isAbsolute()) {
      return new File(baseDir, file.getPath());
    }
    return file;
  }

  @Override
  public boolean canResolveRelativePath() {
    return true;
  }

  public File getBaseDir() {
    return baseDir;
  }
}
