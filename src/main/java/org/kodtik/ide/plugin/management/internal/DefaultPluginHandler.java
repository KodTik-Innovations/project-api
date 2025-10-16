package org.kodtik.ide.plugin.management.internal;

import org.kodtik.ide.api.Plugin;
import org.kodtik.ide.api.Project;

public class DefaultPluginHandler implements PluginHandler {

  private final Project project;

  public DefaultPluginHandler(Project project) {
    this.project = project;
  }

  @Override
  public void id(Plugin<Project> plugin) {
    project.apply(() -> plugin);
  }
}
