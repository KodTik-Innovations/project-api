package org.kodtik.ide.internal;

import java.io.File;
import java.util.Locale;
import org.apache.commons.io.FilenameUtils;

public class FileUtils {

  public static boolean hasExtension(File file, String extension) {
    return file.getPath().endsWith(extension);
  }

  public static boolean hasExtensionIgnoresCase(String fileName, String extension) {
    return endsWithIgnoreCase(fileName, extension);
  }

  private static boolean endsWithIgnoreCase(String subject, String suffix) {
    return subject.regionMatches(
        true, subject.length() - suffix.length(), suffix, 0, suffix.length());
  }

  public static File normalize(File src) {
    String path = src.getAbsolutePath();
    String normalizedPath = FilenameUtils.normalizeNoEndSeparator(path);
    if (normalizedPath != null) {
      return new File(normalizedPath);
    }
    File root = src;
    File parent = root.getParentFile();
    while (parent != null) {
      root = root.getParentFile();
      parent = root.getParentFile();
    }
    return root;
  }

  public static String withExtension(String filePath, String extension) {
    if (filePath.toLowerCase(Locale.ROOT).endsWith(extension)) {
      return filePath;
    }
    return removeExtension(filePath) + extension;
  }

  public static String removeExtension(String filePath) {
    int fileNameStart = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
    int extensionPos = filePath.lastIndexOf('.');

    if (extensionPos > fileNameStart) {
      return filePath.substring(0, extensionPos);
    }
    return filePath;
  }
}
