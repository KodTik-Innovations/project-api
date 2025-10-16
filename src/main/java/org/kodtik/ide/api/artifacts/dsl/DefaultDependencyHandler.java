package org.kodtik.ide.api.artifacts.dsl;

import org.kodtik.ide.api.Project;

public class DefaultDependencyHandler implements DependencyHandler {

  private final Project project;

  public DefaultDependencyHandler(Project project) {
    this.project = project;
  }
}
