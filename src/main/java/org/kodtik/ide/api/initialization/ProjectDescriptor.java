package org.kodtik.ide.api.initialization;

import java.io.File;
import java.util.Set;
import javax.annotation.Nullable;

public interface ProjectDescriptor {
  String getName();

  void setName(String name);

  File getProjectDir();

  void setProjectDir(File dir);

  String getBuildFileName();

  void setBuildFileName(String name);

  File getBuildFile();

  @Nullable
  ProjectDescriptor getParent();

  Set<ProjectDescriptor> getChildren();

  String getPath();
}
