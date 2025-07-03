package org.kodtik.ide.api.internal.project;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.kodtik.ide.api.Project;
import org.kodtik.ide.api.UnknownProjectException;
import org.kodtik.ide.api.internal.file.FileResolver;
import org.kodtik.ide.util.Path;

public interface ProjectInternal extends Project, ProjectIdentifier {

  @Nullable
  @Override
  ProjectInternal getParent();

  @Nullable
  ProjectInternal getParent(ProjectInternal referrer);

  @Override
  ProjectInternal getRootProject();

  ProjectInternal getRootProject(ProjectInternal referrer);

  @Override
  ProjectInternal project(String path) throws UnknownProjectException;

  ProjectInternal project(ProjectInternal referrer, String path) throws UnknownProjectException;

  @Override
  @Nullable
  ProjectInternal findProject(String path);

  @Nullable
  ProjectInternal findProject(ProjectInternal referrer, String path);

  Set<? extends ProjectInternal> getSubprojects(ProjectInternal referrer);

  @Override
  Map<String, Project> getChildProjects();

  Map<String, Project> getChildProjects(ProjectInternal referrer);

  Map<String, Project> getChildProjectsUnchecked();

  Set<? extends ProjectInternal> getAllprojects(ProjectInternal referrer);

  Path getProjectPath();

  String absoluteProjectPath(String path);

  FileResolver getFileResolver();
}
