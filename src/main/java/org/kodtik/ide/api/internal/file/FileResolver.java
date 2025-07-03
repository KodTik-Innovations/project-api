package org.kodtik.ide.api.internal.file;

import java.io.File;
import java.net.URI;
import org.kodtik.ide.api.PathValidation;
import org.kodtik.ide.internal.file.PathToFileResolver;
import org.kodtik.ide.internal.file.RelativeFilePathResolver;

public interface FileResolver extends RelativeFilePathResolver, PathToFileResolver {
  File resolve(Object path, PathValidation validation);

  URI resolveUri(Object path);

  @Override
  FileResolver newResolver(File baseDir);
}
