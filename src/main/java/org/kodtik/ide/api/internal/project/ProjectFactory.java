package org.kodtik.ide.api.internal.project;

import org.kodtik.ide.api.initialization.ProjectDescriptor;
import org.kodtik.ide.api.internal.file.FileResolver;

public class ProjectFactory implements IProjectFactory {

  @Override
  public ProjectInternal createProject(
      ProjectDescriptor projectDescriptor,
      ProjectInternal parent,
      FileResolver fileResolver,
      CrossProjectModelAccess crossAccess) {

    DefaultProject project =
        new DefaultProject(
            projectDescriptor.getName(),
            parent,
            projectDescriptor.getProjectDir(),
            projectDescriptor.getBuildFile()) {
          @Override
          public FileResolver getFileResolver() {
            return fileResolver;
          }

          @Override
          protected CrossProjectModelAccess getCrossProjectModelAccess() {
            return crossAccess;
          }
        };

    return project;
  }
}
