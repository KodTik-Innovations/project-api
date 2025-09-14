package org.kodtik.ide.api.internal.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;
import javax.inject.Inject;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.kodtik.ide.api.Plugin;
import org.kodtik.ide.api.Project;
import org.kodtik.ide.api.ProjectState;
import org.kodtik.ide.api.Task;
import org.kodtik.ide.api.UnknownProjectException;
import org.kodtik.ide.api.internal.file.FileResolver;
import org.kodtik.ide.internal.Cast;
import org.kodtik.ide.util.Path;

public abstract class DefaultProject implements ProjectInternal {

  private final ProjectInternal rootProject;

  private final File projectDir;

  private final File buildFile;
  private File buildDir;
  private Path projectPath;

  private final List<Plugin<?>> plugins;
  private ProjectState state;
  private final Map<String, Task> tasks;

  @Nullable private final ProjectInternal parent;

  private final String name;

  private final int depth;

  public DefaultProject(
      String name, @Nullable ProjectInternal parent, File projectDir, File buildFile) {
    this.rootProject = parent != null ? parent.getRootProject() : this;
    this.projectDir = projectDir;
    this.buildFile = buildFile;
    this.parent = parent;
    this.name = name;

    this.tasks = new LinkedHashMap();
    this.plugins = new ArrayList();
    this.state = ProjectState.NOT_LOADED;

    this.projectPath = path(name);

    if (parent == null) {
      depth = 0;
    } else {
      depth = parent.getDepth() + 1;
    }
  }

  private Path path(String name) {
    if (isRootDescriptor()) {
      return Path.ROOT;
    } else {
      return parent.getProjectPath().child(name);
    }
  }

  private boolean isRootDescriptor() {
    return parent == null;
  }

  @Override
  public ProjectInternal getRootProject() {
    return getRootProject(this);
  }

  @Override
  public ProjectInternal getRootProject(ProjectInternal referrer) {
    return getCrossProjectModelAccess().access(referrer, rootProject);
  }

  @Override
  public File getBuildFile() {
    return buildFile;
  }

  @Override
  public File getRootDir() {
    return rootProject.getProjectDir();
  }

  @Override
  @Nullable
  public ProjectInternal getParent() {
    return getParent(this);
  }

  @Nullable
  @Override
  public ProjectInternal getParent(ProjectInternal referrer) {
    if (parent == null) {
      return null;
    }
    return getCrossProjectModelAccess().access(referrer, parent);
  }

  @Nullable
  @Override
  public ProjectIdentifier getParentIdentifier() {
    return parent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Map<String, Project> getChildProjectsUnchecked() {
    Map<String, Project> map = new TreeMap<>();
    for (Project p : getSubprojects()) {
      map.put(p.getName(), p);
    }
    return map;
  }

  @Override
  public Map<String, Project> getChildProjects() {
    return getChildProjects(this);
  }

  @Override
  public Map<String, Project> getChildProjects(ProjectInternal referrer) {
    return getCrossProjectModelAccess().getChildProjects(referrer, this);
  }

  @Inject
  @Override
  public abstract FileResolver getFileResolver();

  @Override
  public int getDepth() {
    return depth;
  }

  @Override
  public String getPath() {
    return this.getProjectPath().toString();
  }

  @Override
  public String absoluteProjectPath(String path) {
    return getProjectPath().absolutePath(path);
  }

  @Override
  public Path getProjectPath() {
    return this.projectPath;
  }

  @Override
  public Path projectPath(String name) {
    return getProjectPath().child(name);
  }

  @Override
  public String relativeProjectPath(String path) {
    return getProjectPath().relativePath(path);
  }

  @Inject
  protected abstract CrossProjectModelAccess getCrossProjectModelAccess();

  @Override
  public int depthCompare(Project otherProject) {
    return Integer.compare(this.getDepth(), otherProject.getDepth());
  }

  @Override
  public int compareTo(Project otherProject) {
    int depthCompare = depthCompare(otherProject);
    if (depthCompare == 0) {
      return this.getPath().compareTo(otherProject.getPath());
    } else {
      return depthCompare;
    }
  }

  @Override
  public ProjectInternal project(String path) {
    return project(this, path);
  }

  @Override
  public ProjectInternal project(ProjectInternal referrer, String path)
      throws UnknownProjectException {
    ProjectInternal project = getCrossProjectModelAccess().findProject(referrer, this, path);
    if (project == null) {
      throw new UnknownProjectException(
          String.format("Project with path '%s' could not be found in %s.", path, this));
    }
    return project;
  }

  @Override
  public ProjectInternal findProject(String path) {
    return findProject(this, path);
  }

  @Nullable
  @Override
  public ProjectInternal findProject(ProjectInternal referrer, String path) {
    return getCrossProjectModelAccess().findProject(referrer, this, path);
  }

  @Override
  public Set<Project> getAllprojects() {
    return Cast.uncheckedCast(getAllprojects(this));
  }

  @Override
  public Set<? extends ProjectInternal> getAllprojects(ProjectInternal referrer) {
    return getCrossProjectModelAccess().getAllprojects(referrer, this);
  }

  @Override
  public Set<Project> getSubprojects() {
    return Cast.uncheckedCast(getSubprojects(this));
  }

  @Override
  public Set<? extends ProjectInternal> getSubprojects(ProjectInternal referrer) {
    return getCrossProjectModelAccess().getSubprojects(referrer, this);
  }

  @Override
  public ProjectInternal getProject() {
    return this;
  }

  @Override
  public File getProjectDir() {
    return projectDir;
  }

  @Override
  @Deprecated
  public File getBuildDir() {
    return this.buildDir;
  }

  @Override
  @Deprecated
  public void setBuildDir(File path) {
    setBuildDir((Object) path);
  }

  @Override
  @Deprecated
  public void setBuildDir(Object path) {
    this.buildDir = getFileResolver().resolve(path);
  }

  @Override
  public void evaluate(Function1<? super Project, Unit> function1) {
    function1.invoke(this);
  }

  @Override
  public void setState(ProjectState state) {
    this.state = state;
  }

  @Override
  public ProjectState getState() {
    return this.state;
  }

  @Override
  public boolean hasPlugin(Class<? extends Plugin<?>> cls) {
    List<Plugin<?>> iterable = this.plugins;
    if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
      return false;
    }
    for (Plugin isInstance : iterable) {
      if (cls.isInstance(isInstance)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void apply(Map<String, ? extends Object> map) {
    Object obj = map.get("plugin");
    if (obj != null) {}
  }

  @Override
  public Task getTask(String str) {
    return this.tasks.get(str);
  }

  @Override
  public Map<String, Task> getAllTasks() {
    return this.tasks;
  }

  @Override
  public List<Plugin<?>> getAppliedPlugins() {
    return this.plugins;
  }
}
