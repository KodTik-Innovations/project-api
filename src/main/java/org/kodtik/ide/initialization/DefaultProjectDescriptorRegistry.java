package org.kodtik.ide.initialization;

import org.kodtik.ide.api.internal.project.DefaultProjectRegistry;
import org.kodtik.ide.util.Path;

public class DefaultProjectDescriptorRegistry
    extends DefaultProjectRegistry<DefaultProjectDescriptor> implements ProjectDescriptorRegistry {

  @Override
  public void changeDescriptorPath(Path oldPath, Path newPath) {
    DefaultProjectDescriptor projectDescriptor = removeProject(oldPath.toString());
    projectDescriptor.setPath(newPath);
    addProject(projectDescriptor);
  }
}
