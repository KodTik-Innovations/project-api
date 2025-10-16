package org.kodtik.ide.api.artifacts.dsl;

import org.eclipse.aether.repository.ArtifactRepository;
import org.kodtik.ide.api.Project;

public class DefaultRepositoryHandler implements RepositoryHandler {

  private final Project project;

  public DefaultRepositoryHandler(Project project) {
    this.project = project;
  }

  @Override
  public ArtifactRepository gradlePluginPortal() {
    return null;
  }

  @Override
  public ArtifactRepository mavenCentral() {
    return null;
  }

  @Override
  public ArtifactRepository mavenLocal() {
    return null;
  }

  @Override
  public ArtifactRepository google() {
    return null;
  }
}
