package org.kodtik.ide.api.artifacts.dsl;

import org.eclipse.aether.repository.ArtifactRepository;

public interface RepositoryHandler {

  ArtifactRepository gradlePluginPortal();

  ArtifactRepository mavenCentral();

  ArtifactRepository mavenLocal();

  ArtifactRepository google();
}
