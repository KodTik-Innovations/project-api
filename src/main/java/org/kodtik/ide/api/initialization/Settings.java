package org.kodtik.ide.api.initialization;

import java.io.File;
import java.util.Arrays;
import javax.annotation.Nullable;
import org.kodtik.ide.api.UnknownProjectException;

public interface Settings {
  String DEFAULT_SETTINGS_FILE = "settings.json";

  default void include(String... projectPaths) {
    include(Arrays.asList(projectPaths));
  }

  void include(Iterable<String> projectPaths);

  Settings getSettings();

  File getSettingsDir();

  File getRootDir();

  ProjectDescriptor getRootProject();

  ProjectDescriptor project(String path) throws UnknownProjectException;

  @Nullable
  ProjectDescriptor findProject(String path);

  ProjectDescriptor project(File projectDir) throws UnknownProjectException;

  @Nullable
  ProjectDescriptor findProject(File projectDir);

  Object getSettingsModel();

  File getSettingsFile();
}
