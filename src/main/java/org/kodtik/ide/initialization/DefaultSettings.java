package org.kodtik.ide.initialization;

import java.io.File;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.kodtik.ide.api.InvalidUserDataException;
import org.kodtik.ide.api.UnknownProjectException;
import org.kodtik.ide.api.initialization.ProjectDescriptor;
import org.kodtik.ide.api.initialization.Settings;
import org.kodtik.ide.api.internal.SettingsInternal;
import org.kodtik.ide.api.internal.file.FileResolver;
import org.kodtik.ide.api.internal.project.ProjectRegistry;
import org.kodtik.ide.internal.scripts.ScriptFileResolver;

public abstract class DefaultSettings implements SettingsInternal {

  private File settingsDir;

  private DefaultProjectDescriptor rootProjectDescriptor;

  private DefaultProjectDescriptor defaultProjectDescriptor;

  public DefaultSettings(File settingsDir) {
    this.settingsDir = settingsDir;
    this.rootProjectDescriptor =
        createProjectDescriptor(null, getProjectName(settingsDir), settingsDir);
  }

  private static String getProjectName(File settingsDir) {
    return settingsDir.getName();
  }

  @Override
  public String toString() {
    return "settings '" + rootProjectDescriptor.getName() + "'";
  }

  @Override
  public Settings getSettings() {
    return this;
  }

  public DefaultProjectDescriptor createProjectDescriptor(
      @Nullable DefaultProjectDescriptor parent, String name, File dir) {
    return new DefaultProjectDescriptor(
        parent,
        name,
        dir,
        getProjectDescriptorRegistry(),
        getFileResolver(),
        getScriptFileResolver());
  }

  @Override
  public DefaultProjectDescriptor findProject(String path) {
    return getProjectDescriptorRegistry().getProject(path);
  }

  @Override
  public DefaultProjectDescriptor findProject(File projectDir) {
    return getProjectDescriptorRegistry().getProject(projectDir);
  }

  @Override
  public DefaultProjectDescriptor project(String path) {
    DefaultProjectDescriptor projectDescriptor = getProjectDescriptorRegistry().getProject(path);
    if (projectDescriptor == null) {
      throw new UnknownProjectException(
          String.format("Project with path '%s' could not be found.", path));
    }
    return projectDescriptor;
  }

  @Override
  public DefaultProjectDescriptor project(File projectDir) {
    DefaultProjectDescriptor projectDescriptor =
        getProjectDescriptorRegistry().getProject(projectDir);
    if (projectDescriptor == null) {
      throw new UnknownProjectException(
          String.format("Project with path '%s' could not be found.", projectDir));
    }
    return projectDescriptor;
  }

  @Override
  public void include(Iterable<String> projectPaths) {
    int size = 0;
    for (String projectPath : projectPaths) {
      size++;
      if (size != 1) {
        throw new InvalidUserDataException(
            "Multiple projects are not supported. Path: " + projectPath);
      }
      String subPath = "";
      String[] pathElements = removeTrailingColon(projectPath).split(":");
      DefaultProjectDescriptor parentProjectDescriptor = rootProjectDescriptor;
      for (String pathElement : pathElements) {
        subPath = subPath + ":" + pathElement;
        DefaultProjectDescriptor projectDescriptor =
            getProjectDescriptorRegistry().getProject(subPath);
        if (projectDescriptor == null) {
          parentProjectDescriptor =
              createProjectDescriptor(
                  parentProjectDescriptor,
                  pathElement,
                  new File(parentProjectDescriptor.getProjectDir(), pathElement));
        } else {
          parentProjectDescriptor = projectDescriptor;
        }
      }
    }
  }

  private String removeTrailingColon(String projectPath) {
    if (projectPath.startsWith(":")) {
      return projectPath.substring(1);
    }
    return projectPath;
  }

  @Override
  public ProjectDescriptor getRootProject() {
    return rootProjectDescriptor;
  }

  public void setRootProjectDescriptor(DefaultProjectDescriptor rootProjectDescriptor) {
    this.rootProjectDescriptor = rootProjectDescriptor;
  }

  @Override
  public DefaultProjectDescriptor getDefaultProject() {
    return defaultProjectDescriptor;
  }

  @Override
  public void setDefaultProject(DefaultProjectDescriptor defaultProjectDescriptor) {
    this.defaultProjectDescriptor = defaultProjectDescriptor;
  }

  @Override
  public File getRootDir() {
    return rootProjectDescriptor.getProjectDir();
  }

  @Override
  public File getSettingsDir() {
    return settingsDir;
  }

  public void setSettingsDir(File settingsDir) {
    this.settingsDir = settingsDir;
  }

  @Inject
  public abstract ProjectDescriptorRegistry getProjectDescriptorRegistry();

  @Inject
  public abstract ScriptFileResolver getScriptFileResolver();

  @Override
  public ProjectRegistry<DefaultProjectDescriptor> getProjectRegistry() {
    return getProjectDescriptorRegistry();
  }

  @Inject
  protected abstract FileResolver getFileResolver();
}
