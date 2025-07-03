package org.kodtik.ide.api.internal.file;

import java.io.File;
import org.kodtik.ide.internal.file.PathToFileResolver;

public class DefaultFileLookup implements FileLookup {
  private final IdentityFileResolver fileResolver = new IdentityFileResolver();

  @Override
  public FileResolver getFileResolver() {
    return fileResolver;
  }

  @Override
  public PathToFileResolver getPathToFileResolver() {
    return getFileResolver();
  }

  @Override
  public FileResolver getFileResolver(File baseDirectory) {
    return fileResolver.withBaseDir(baseDirectory);
  }

  @Override
  public PathToFileResolver getPathToFileResolver(File baseDirectory) {
    return getFileResolver(baseDirectory);
  }
}
