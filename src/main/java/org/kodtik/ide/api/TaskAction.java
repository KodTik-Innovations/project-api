package org.kodtik.ide.api;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class TaskAction implements Action<Task> {
  private final Function1<? super Task, Unit> action;
  private final ActionType type;

  public TaskAction(Function1<? super Task, Unit> function1, ActionType actionType) {
    this.action = function1;
    this.type = actionType;
  }

  @Override
  public void execute(Task task) {
    this.action.invoke(task);
  }

  public final Function1<? super Task, Unit> getAction() {
    return this.action;
  }

  public final ActionType getType() {
    return this.type;
  }
}
