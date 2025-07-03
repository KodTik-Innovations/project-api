package org.kodtik.ide.internal.file;

import java.io.File;

public interface PathToFileResolver {
  File resolve(Object path);

  PathToFileResolver newResolver(File baseDir);

  boolean canResolveRelativePath();
}
