package org.kodtik.ide.plugin.management.internal;

import org.kodtik.ide.api.Plugin;
import org.kodtik.ide.api.Project;

public interface PluginHandler {

  void id(Plugin<Project> plugin);
}
