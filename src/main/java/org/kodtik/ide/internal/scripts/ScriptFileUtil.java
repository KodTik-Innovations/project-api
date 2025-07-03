package org.kodtik.ide.internal.scripts;

import java.util.List;
import org.kodtik.ide.scripts.ScriptingLanguage;

public class ScriptFileUtil {

  public static final String SETTINGS_FILE_BASE_NAME = "settings";

  public static final String BUILD_FILE_BASE_NAME = "build";

  public static String[] getValidSettingsFileNames() {
    return getFileNames(SETTINGS_FILE_BASE_NAME);
  }

  public static String[] getValidBuildFileNames() {
    return getFileNames(BUILD_FILE_BASE_NAME);
  }

  public static String[] getValidExtensions() {
    List<ScriptingLanguage> scriptingLanguages = ScriptingLanguages.all();
    String[] extensions = new String[scriptingLanguages.size()];
    for (int i = 0; i < scriptingLanguages.size(); i++) {
      extensions[i] = scriptingLanguages.get(i).getExtension();
    }
    return extensions;
  }

  private static String[] getFileNames(String basename) {
    List<ScriptingLanguage> scriptingLanguages = ScriptingLanguages.all();
    String[] fileNames = new String[scriptingLanguages.size()];
    for (int i = 0; i < scriptingLanguages.size(); i++) {
      fileNames[i] = basename + scriptingLanguages.get(i).getExtension();
    }
    return fileNames;
  }
}
