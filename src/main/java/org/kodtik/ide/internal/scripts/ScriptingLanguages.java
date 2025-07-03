package org.kodtik.ide.internal.scripts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.kodtik.ide.scripts.ScriptingLanguage;

public final class ScriptingLanguages {

  private static final List<ScriptingLanguage> ALL =
      Collections.unmodifiableList(Arrays.asList(scriptingLanguage(".json", null)));

  public static List<ScriptingLanguage> all() {
    return ALL;
  }

  private static ScriptingLanguage scriptingLanguage(
      final String extension, @Nullable final String scriptPluginFactory) {
    return new ScriptingLanguage() {
      @Override
      public String getExtension() {
        return extension;
      }

      @Override
      public String getProvider() {
        return scriptPluginFactory;
      }
    };
  }
}
