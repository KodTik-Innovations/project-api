package org.kodtik.ide.api.plugins;

import org.kodtik.ide.api.Plugin;
import org.kodtik.ide.api.Project;

public class HelloWorldPlugin implements Plugin<Project> {

  @Override
  public void apply(final Project project) {
    System.out.println("Hello World!, from HelloWorldPlugin");
  }
}
