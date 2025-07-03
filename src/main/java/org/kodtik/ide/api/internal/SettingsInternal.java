package org.kodtik.ide.api.internal;

import org.kodtik.ide.api.initialization.Settings;
import org.kodtik.ide.api.internal.project.ProjectRegistry;
import org.kodtik.ide.initialization.DefaultProjectDescriptor;

public interface SettingsInternal extends Settings {

  String BUILD_SRC = "buildSrc";

  ProjectRegistry<DefaultProjectDescriptor> getProjectRegistry();

  DefaultProjectDescriptor getDefaultProject();

  void setDefaultProject(DefaultProjectDescriptor defaultProject);

  default void include(String projectPath) {
    include(new String[] {projectPath});
  }
}
