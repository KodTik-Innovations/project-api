package org.kodtik.ide.api.internal.project;

import javax.annotation.Nullable;
import org.kodtik.ide.api.initialization.ProjectDescriptor;
import org.kodtik.ide.api.internal.file.FileResolver;

public interface IProjectFactory {
  ProjectInternal createProject(
      ProjectDescriptor projectDescriptor,
      @Nullable ProjectInternal parent,
      FileResolver fileResolver,
      CrossProjectModelAccess crossAccess);
}
