package org.kodtik.ide.internal.scripts;

import static java.util.Collections.emptyList;
import static org.kodtik.ide.internal.FileUtils.hasExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class DefaultScriptFileResolver implements ScriptFileResolver {

  private static final String[] EXTENSIONS = ScriptFileUtil.getValidExtensions();

  @Nullable private final ScriptFileResolvedListener scriptFileResolvedListener;

  public DefaultScriptFileResolver(
      @Nullable ScriptFileResolvedListener scriptFileResolvedListener) {
    this.scriptFileResolvedListener = scriptFileResolvedListener;
  }

  public DefaultScriptFileResolver() {
    this.scriptFileResolvedListener = null;
  }

  @Override
  public File resolveScriptFile(File dir, String basename) {
    for (String extension : EXTENSIONS) {
      File candidate = new File(dir, basename + extension);
      if (isCandidateFile(candidate)) {
        return candidate;
      }
    }
    return null;
  }

  private boolean isCandidateFile(File candidate) {
    notifyListener(candidate);
    return candidate.isFile();
  }

  @Override
  public List<File> findScriptsIn(File dir) {
    File[] candidates = dir.listFiles();
    if (candidates == null || candidates.length == 0) {
      return emptyList();
    }
    List<File> found = new ArrayList<File>(candidates.length);
    for (File candidate : candidates) {
      if (candidate.isFile() && hasScriptExtension(candidate)) {
        found.add(candidate);
      }
    }
    return found;
  }

  private void notifyListener(File scriptFile) {
    if (scriptFileResolvedListener != null) {
      scriptFileResolvedListener.onScriptFileResolved(scriptFile);
    }
  }

  private boolean hasScriptExtension(File file) {
    for (String extension : EXTENSIONS) {
      if (hasExtension(file, extension)) {
        return true;
      }
    }
    return false;
  }
}
