package org.kodtik.ide.api.plugins;

import kotlin.Unit;
import org.kodtik.ide.api.Plugin;
import org.kodtik.ide.api.Project;
import org.kodtik.ide.api.internal.project.DefaultProject;

public class HelloWorldPlugin implements Plugin<Project> {

  @Override
  public void apply(final Project project) {
    System.out.println("Hello World!, from HelloWorldPlugin");

    ((DefaultProject) project)
        .createTask(
            "helloworld",
            task -> {
              task.setGroup("test");
              task.setDescription("Hello World test");

              task.doLast(
                  t -> {
                    System.out.println("helloworld done.");
                    return Unit.INSTANCE;
                  });

              return Unit.INSTANCE;
            });
  }
}
