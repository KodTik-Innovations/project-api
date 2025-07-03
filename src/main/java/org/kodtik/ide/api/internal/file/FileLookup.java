package org.kodtik.ide.api.internal.file;

import java.io.File;
import org.kodtik.ide.internal.file.PathToFileResolver;

public interface FileLookup {
  FileResolver getFileResolver();

  PathToFileResolver getPathToFileResolver();

  FileResolver getFileResolver(File baseDirectory);

  PathToFileResolver getPathToFileResolver(File baseDirectory);
}
