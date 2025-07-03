package org.kodtik.ide.initialization;

import com.google.common.base.Objects;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.kodtik.ide.api.Project;
import org.kodtik.ide.api.initialization.ProjectDescriptor;
import org.kodtik.ide.api.internal.project.ProjectIdentifier;
import org.kodtik.ide.internal.Cast;
import org.kodtik.ide.internal.FileUtils;
import org.kodtik.ide.internal.file.PathToFileResolver;
import org.kodtik.ide.internal.scripts.DefaultScriptFileResolver;
import org.kodtik.ide.internal.scripts.ScriptFileResolver;
import org.kodtik.ide.util.Path;
import org.kodtik.ide.util.internal.NameValidator;

public class DefaultProjectDescriptor implements ProjectDescriptor, ProjectIdentifier {
  public static final String INVALID_NAME_IN_INCLUDE_HINT =
      "Set the 'rootProject.name' or adjust the 'include' statement.";

  public static final String BUILD_SCRIPT_BASENAME = "build";

  private String name;
  private boolean nameExplicitlySet;

  private final PathToFileResolver fileResolver;
  private final ScriptFileResolver scriptFileResolver;
  private File dir;
  private File canonicalDir;
  private final DefaultProjectDescriptor parent;
  private final Set<DefaultProjectDescriptor> children = new LinkedHashSet<>();
  private ProjectDescriptorRegistry projectDescriptorRegistry;
  private Path path;
  private String buildFileName;

  public DefaultProjectDescriptor(
      @Nullable DefaultProjectDescriptor parent,
      String name,
      File dir,
      ProjectDescriptorRegistry projectDescriptorRegistry,
      PathToFileResolver fileResolver) {
    this(parent, name, dir, projectDescriptorRegistry, fileResolver, null);
  }

  public DefaultProjectDescriptor(
      @Nullable DefaultProjectDescriptor parent,
      String name,
      File dir,
      ProjectDescriptorRegistry projectDescriptorRegistry,
      PathToFileResolver fileResolver,
      @Nullable ScriptFileResolver scriptFileResolver) {
    this.parent = parent;
    this.name = name;
    this.fileResolver = fileResolver;
    this.dir = dir;
    this.projectDescriptorRegistry = projectDescriptorRegistry;
    this.path = path(name);
    this.scriptFileResolver =
        scriptFileResolver != null ? scriptFileResolver : new DefaultScriptFileResolver();

    projectDescriptorRegistry.addProject(this);
    if (parent != null) {
      parent.getChildren().add(this);
    }
  }

  private Path path(String name) {
    if (isRootDescriptor()) {
      return path = Path.ROOT;
    } else {
      return parent.absolutePath(name);
    }
  }

  private Path absolutePath(String path) {
    return this.path.child(path);
  }

  private boolean isRootDescriptor() {
    return parent == null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    NameValidator.validate(name, "project name", INVALID_NAME_IN_INCLUDE_HINT);
    projectDescriptorRegistry.changeDescriptorPath(path, path(name));
    this.name = name;
    this.nameExplicitlySet = true;
  }

  public boolean isExplicitName() {
    return nameExplicitlySet;
  }

  @Override
  public File getProjectDir() {
    if (canonicalDir == null) {
      canonicalDir = fileResolver.resolve(dir);
    }
    return canonicalDir;
  }

  @Override
  public void setProjectDir(File dir) {
    this.canonicalDir = null;
    this.dir = dir;
  }

  @Override
  public DefaultProjectDescriptor getParent() {
    return parent;
  }

  @Override
  public ProjectIdentifier getParentIdentifier() {
    return parent;
  }

  @Override
  public Set<ProjectDescriptor> getChildren() {
    return Cast.uncheckedCast(children);
  }

  public Set<? extends DefaultProjectDescriptor> children() {
    return children;
  }

  @Override
  public String getPath() {
    return path.toString();
  }

  void setPath(Path path) {
    this.path = path;
  }

  @Override
  public String getBuildFileName() {
    return buildFile().getName();
  }

  @Override
  public void setBuildFileName(String name) {
    this.buildFileName = name;
  }

  @Override
  public File getBuildFile() {
    return FileUtils.normalize(buildFile());
  }

  private File buildFile() {
    if (buildFileName != null) {
      return new File(getProjectDir(), buildFileName);
    }
    File buildScriptFile =
        scriptFileResolver.resolveScriptFile(getProjectDir(), BUILD_SCRIPT_BASENAME);
    if (buildScriptFile != null) {
      return buildScriptFile;
    }
    return new File(getProjectDir(), Project.DEFAULT_BUILD_FILE);
  }

  public ProjectDescriptorRegistry getProjectDescriptorRegistry() {
    return projectDescriptorRegistry;
  }

  public void setProjectDescriptorRegistry(ProjectDescriptorRegistry projectDescriptorRegistry) {
    this.projectDescriptorRegistry = projectDescriptorRegistry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DefaultProjectDescriptor that = (DefaultProjectDescriptor) o;

    return Objects.equal(this.getParent(), that.getParent())
        && Objects.equal(this.getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getParent(), this.getName());
  }

  @Override
  public String toString() {
    return getPath();
  }

  public Path path() {
    return path;
  }
}
