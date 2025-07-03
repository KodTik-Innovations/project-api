package org.kodtik.ide.api.internal.project;

import java.io.File;
import java.util.Set;
import javax.annotation.Nullable;
import org.kodtik.ide.api.specs.Spec;

public interface ProjectRegistry<T extends ProjectIdentifier> {
  void addProject(T project);

  @Nullable
  T getRootProject();

  @Nullable
  T getProject(String path);

  @Nullable
  T getProject(File projectDir);

  int size();

  Set<T> getAllProjects();

  Set<T> getAllProjects(String path);

  Set<T> getSubProjects(String path);

  Set<T> findAll(Spec<? super T> constraint);
}
