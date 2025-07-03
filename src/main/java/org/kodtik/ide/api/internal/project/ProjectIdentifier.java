package org.kodtik.ide.api.internal.project;

import java.io.File;
import javax.annotation.Nullable;

public interface ProjectIdentifier {
  String getName();

  String getPath();

  @Nullable
  ProjectIdentifier getParentIdentifier();

  File getProjectDir();

  File getBuildFile();
}
