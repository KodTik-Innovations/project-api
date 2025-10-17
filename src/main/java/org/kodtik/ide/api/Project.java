package org.kodtik.ide.api;

import java.io.File;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.kodtik.ide.util.Path;

public interface Project extends Comparable<Project> {
  String DEFAULT_BUILD_FILE = "build.json";
  String PATH_SEPARATOR = ":";
  String DEFAULT_BUILD_DIR_NAME = "build";

  Project getRootProject();

  File getRootDir();

  @Deprecated
  File getBuildDir();

  @Deprecated
  void setBuildDir(File path);

  @Deprecated
  void setBuildDir(Object path);

  File getBuildFile();

  @Nullable
  Project getParent();

  String getName();

  Map<String, Project> getChildProjects();

  Project getProject();

  Set<Project> getAllprojects();

  Set<Project> getSubprojects();

  String getPath();

  @Nullable
  Project findProject(String path);

  Project project(String path) throws UnknownProjectException;

  Path projectPath(String name);

  String relativeProjectPath(String path);

  int depthCompare(Project otherProject);

  int getDepth();

  void evaluate(Function1<? super Project, Unit> function1);

  Task getTask(String str);

  Map<String, Task> getTasks();

  void setState(ProjectState state);

  ProjectState getState();

  Object getBuildModel();
}
