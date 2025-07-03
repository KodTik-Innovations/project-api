package org.kodtik.ide.initialization;

import org.kodtik.ide.api.internal.project.ProjectRegistry;
import org.kodtik.ide.util.Path;

public interface ProjectDescriptorRegistry extends ProjectRegistry<DefaultProjectDescriptor> {
  void changeDescriptorPath(Path oldPath, Path newPath);
}
