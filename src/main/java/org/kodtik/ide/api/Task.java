package org.kodtik.ide.api;

import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public interface Task {
  void dependsOn(String... strArr);

  void doFirst(Function1<? super Task, Unit> function1);

  void doLast(Function1<? super Task, Unit> function1);

  void execute();

  List<TaskAction> getActions();

  Set<String> getDependencies();

  String getDescription();

  String getGroup();

  String getName();

  String getPath();

  Project getProject();

  void setDescription(String str);

  void setGroup(String str);
}
