package org.kodtik.ide.internal.scripts;

import java.io.File;
import java.util.List;
import javax.annotation.Nullable;

public interface ScriptFileResolver {

  @Nullable
  File resolveScriptFile(File dir, String basename);

  List<File> findScriptsIn(File dir);
}
