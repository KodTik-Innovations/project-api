package org.kodtik.ide.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class BuildLifecycle {
  private BuildPhase currentPhase = BuildPhase.INITIALIZATION;
  private final Map<BuildPhase, List<Function0<Unit>>> phaseListeners = new LinkedHashMap();

  public final void onPhaseStart(BuildPhase buildPhase, Function0<Unit> function0) {
    Object arrayList;
    Map map = this.phaseListeners;
    Object obj = map.get(buildPhase);
    if (obj == null) {
      arrayList = new ArrayList();
      map.put(buildPhase, arrayList);
    } else {
      arrayList = obj;
    }
    ((List) arrayList).add(function0);
  }

  public final void enterPhase(BuildPhase buildPhase) {
    this.currentPhase = buildPhase;
    List<Function0> list = (List) this.phaseListeners.get(buildPhase);
    if (list != null) {
      for (Function0 invoke : list) {
        invoke.invoke();
      }
    }
  }

  public final BuildPhase getCurrentPhase() {
    return this.currentPhase;
  }

  public final void setCurrentPhase(BuildPhase buildPhase) {
    this.currentPhase = buildPhase;
  }
}
