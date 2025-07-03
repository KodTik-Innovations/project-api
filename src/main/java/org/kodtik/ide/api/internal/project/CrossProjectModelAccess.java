package org.kodtik.ide.api.internal.project;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.kodtik.ide.api.Project;

public interface CrossProjectModelAccess {

  @Nullable
  ProjectInternal findProject(ProjectInternal referrer, ProjectInternal relativeTo, String path);

  ProjectInternal access(ProjectInternal referrer, ProjectInternal project);

  Map<String, Project> getChildProjects(ProjectInternal referrer, ProjectInternal relativeTo);

  Set<? extends ProjectInternal> getSubprojects(
      ProjectInternal referrer, ProjectInternal relativeTo);

  Set<? extends ProjectInternal> getAllprojects(
      ProjectInternal referrer, ProjectInternal relativeTo);
}
