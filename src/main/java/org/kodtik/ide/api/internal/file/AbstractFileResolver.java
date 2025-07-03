package org.kodtik.ide.api.internal.file;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.kodtik.ide.api.InvalidUserDataException;
import org.kodtik.ide.api.PathValidation;
import org.kodtik.ide.internal.FileUtils;
import org.kodtik.ide.internal.os.OperatingSystem;

public abstract class AbstractFileResolver implements FileResolver {
  private static final Pattern ENCODED_URI = Pattern.compile("%([0-9a-fA-F]{2})");

  protected AbstractFileResolver() {}

  public FileResolver withBaseDir(Object path) {
    return new BaseDirFileResolver(resolve(path));
  }

  @Override
  public FileResolver newResolver(File baseDir) {
    return new BaseDirFileResolver(baseDir);
  }

  @Override
  public File resolve(Object path) {
    return resolve(path, PathValidation.NONE);
  }

  @Override
  public String resolveForDisplay(Object path) {
    return resolveAsRelativePath(path);
  }

  @Override
  public File resolve(Object path, PathValidation validation) {
    File maybeRelativeFile = (File) parseNotation(path, "File");
    return resolveFile(maybeRelativeFile, validation);
  }

  @Override
  public URI resolveUri(Object uri) {
    return (URI) parseNotation(uri, "URI");
  }

  private Object parseNotation(Object path, String hint) {
    if (hint.equals("File")) {
      if (path instanceof File) {
        return (File) path;
      }
      if (path instanceof Path) {
        return ((Path) path).toFile();
      }
      if (path instanceof URL) {
        try {
          path = ((URL) path).toURI();
        } catch (URISyntaxException e) {
          throw new UncheckedIOException(e.getMessage(), null);
        }
      }
      if (path instanceof URI) {
        URI uri = (URI) path;
        if ("file".equals(uri.getScheme())) {
          try {
            return new File(uri);
          } catch (IllegalArgumentException ex) {
            throw new InvalidUserDataException(
                String.format("Cannot convert URI '%s' to a file.", uri), ex);
          }
        }
      }
      if (path instanceof CharSequence) {
        String notationString = path.toString();
        if (notationString.startsWith("file:")) {
          try {
            URI uri = new URI(notationString);
            try {
              return new File(uri);
            } catch (IllegalArgumentException ignored) {
            }
          } catch (URISyntaxException ignored) {
          }
          return new File(fallbackUrlDecode(notationString));
        }
        return new File(notationString);
      }
    } else if (hint.equals("URI")) {
      if (path instanceof URL) {
        try {
          path = ((URL) path).toURI();
        } catch (URISyntaxException e) {
          throw invalidUserDataException(path, e);
        }
      }
      if (path instanceof URI) {
        URI uri = (URI) path;
        return uri;
      }
      if (path instanceof CharSequence) {
        String notationString = path.toString();
        if (notationString.startsWith("file:")) {
          return null;
        }
        if (notationString.startsWith("http://") || notationString.startsWith("https://")) {
          try {
            return new URI(notationString);
          } catch (URISyntaxException e) {
            throw invalidUserDataException(notationString, e);
          }
        }
        try {
          URI uri = new URI(notationString);
          if (uri.getScheme() == null || isWindowsRootDirectory(uri.getScheme())) {
            return null;
          }
          return uri;
        } catch (URISyntaxException e) {
        }
      }
    }
    return path;
  }

  protected abstract File doResolve(File path);

  private File resolveFile(File maybeRelativeFile, PathValidation validation) {
    File file = doResolve(maybeRelativeFile);
    file = FileUtils.normalize(file);
    validate(file, validation);
    return file;
  }

  protected void validate(File file, PathValidation validation) {
    switch (validation) {
      case NONE:
        break;
      case EXISTS:
        if (!file.exists()) {
          throw new InvalidUserDataException(String.format("File '%s' does not exist.", file));
        }
        break;
      case FILE:
        if (!file.exists()) {
          throw new InvalidUserDataException(String.format("File '%s' does not exist.", file));
        }
        if (!file.isFile()) {
          throw new InvalidUserDataException(String.format("File '%s' is not a file.", file));
        }
        break;
      case DIRECTORY:
        if (!file.exists()) {
          throw new InvalidUserDataException(String.format("Directory '%s' does not exist.", file));
        }
        if (!file.isDirectory()) {
          throw new InvalidUserDataException(
              String.format("Directory '%s' is not a directory.", file));
        }
        break;
    }
  }

  @Nonnull
  private static InvalidUserDataException invalidUserDataException(
      Object notation, URISyntaxException e) {
    return new InvalidUserDataException(
        String.format("Cannot convert '%s' to a URI.", notation), e);
  }

  private static boolean isWindowsRootDirectory(@Nullable String scheme) {
    return scheme != null
        && scheme.length() == 1
        && Character.isLetter(scheme.charAt(0))
        && OperatingSystem.current().isWindows();
  }

  private static String fallbackUrlDecode(String fullPath) {
    String path = fullPath.substring(5);
    StringBuffer builder = new StringBuffer();
    Matcher matcher = ENCODED_URI.matcher(path);
    while (matcher.find()) {
      String val = matcher.group(1);
      matcher.appendReplacement(builder, String.valueOf((char) Integer.parseInt(val, 16)));
    }
    matcher.appendTail(builder);
    return builder.toString();
  }
}
