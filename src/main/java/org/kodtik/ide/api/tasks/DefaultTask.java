package org.kodtik.ide.api.tasks;

import java.util.*;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.kodtik.ide.api.*;

public class DefaultTask implements Task {
  private final String name;
  private final String path;
  private final Project project;

  private String group;
  private String description;
  private final Set<String> dependencies = new HashSet<>();
  private final List<TaskAction> actions = new ArrayList<>();

  public DefaultTask(String name, String path, Project project) {
    this.name = name;
    this.path = path;
    this.project = project;
  }

  @Override
  public void dependsOn(String... taskPaths) {
    for (String t : taskPaths) {
      if (t.startsWith(":")) {
        dependencies.add(t);
      } else if (":".equals(project.getPath())) {
        dependencies.add(":" + t);
      } else {
        dependencies.add(project.getPath() + ":" + t);
      }
    }
  }

  @Override
  public void doFirst(Function1<? super Task, Unit> action) {
    actions.add(new TaskAction(action, ActionType.DO_FIRST));
  }

  @Override
  public void doLast(Function1<? super Task, Unit> action) {
    actions.add(new TaskAction(action, ActionType.DO_LAST));
  }

  @Override
  public void execute() {
    for (TaskAction action : actions) {
      if (action.getType() == ActionType.DO_FIRST) {
        action.execute(this);
      }
    }
    for (TaskAction action : actions) {
      if (action.getType() == ActionType.DO_LAST) {
        action.execute(this);
      }
    }
  }

  @Override
  public List<TaskAction> getActions() {
    return actions;
  }

  @Override
  public Set<String> getDependencies() {
    return dependencies;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getGroup() {
    return group;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public Project getProject() {
    return project;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void setGroup(String group) {
    this.group = group;
  }
}
