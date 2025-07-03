package org.kodtik.ide.util;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.join;

import com.google.common.base.Strings;
import com.google.common.collect.AbstractIterator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.kodtik.ide.api.InvalidUserDataException;
import org.kodtik.ide.util.internal.GUtil;

public class Path implements Comparable<Path> {
  public static final Path ROOT = new Path(new String[0], true);

  private static final Comparator<String> STRING_COMPARATOR = GUtil.caseInsensitive();
  public static final String SEPARATOR = ":";

  public static Path path(@Nullable String path) {
    validatePath(path);
    if (SEPARATOR.equals(path)) {
      return ROOT;
    } else {
      return parsePath(path);
    }
  }

  public static void validatePath(@Nullable String path) {
    if (Strings.isNullOrEmpty(path)) {
      throw new InvalidUserDataException("A path must be specified!");
    }
  }

  private static Path parsePath(String path) {
    String[] segments = StringUtils.split(path, SEPARATOR);
    boolean absolute = path.startsWith(SEPARATOR);
    return new Path(segments, absolute);
  }

  private final String[] segments;
  private final boolean absolute;
  private final int hashCode;
  private volatile String fullPath;

  private Path(String[] segments, boolean absolute) {
    this.segments = segments;
    this.absolute = absolute;

    this.hashCode = computeHashCode(absolute, segments);
  }

  @Override
  public String toString() {
    return getPath();
  }

  public Path append(Path path) {
    if (path.segments.length == 0) {
      return this;
    }
    String[] concat = new String[segments.length + path.segments.length];
    System.arraycopy(segments, 0, concat, 0, segments.length);
    System.arraycopy(path.segments, 0, concat, segments.length, path.segments.length);
    return new Path(concat, absolute);
  }

  public String getPath() {
    if (fullPath == null) {
      fullPath = createFullPath();
    }
    return fullPath;
  }

  private String createFullPath() {
    StringBuilder path = new StringBuilder();
    if (absolute) {
      path.append(SEPARATOR);
    }
    return path.append(join(segments, SEPARATOR)).toString();
  }

  public List<String> segments() {
    return asList(segments);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Path path = (Path) o;

    if (absolute != path.absolute) {
      return false;
    }
    return Arrays.equals(segments, path.segments);
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  private static int computeHashCode(boolean absolute, String[] segments) {
    int result = 0;
    for (Object element : segments) {
      result = 31 * result + element.hashCode();
    }
    result = 31 * result + (absolute ? 1 : 0);
    return result;
  }

  @Override
  public int compareTo(Path other) {
    if (absolute && !other.absolute) {
      return 1;
    }
    if (!absolute && other.absolute) {
      return -1;
    }
    for (int i = 0; i < Math.min(segments.length, other.segments.length); i++) {
      int diff = STRING_COMPARATOR.compare(segments[i], other.segments[i]);
      if (diff != 0) {
        return diff;
      }
    }
    int lenDiff = segments.length - other.segments.length;
    if (lenDiff > 0) {
      return 1;
    }
    if (lenDiff < 0) {
      return -1;
    }
    return 0;
  }

  @Nullable
  public Path getParent() {
    if (segments.length == 0) {
      return null;
    }
    if (segments.length == 1) {
      return absolute ? ROOT : null;
    }
    String[] parentPath = new String[segments.length - 1];
    System.arraycopy(segments, 0, parentPath, 0, parentPath.length);
    return new Path(parentPath, absolute);
  }

  @Nullable
  public String getName() {
    if (segments.length == 0) {
      return null;
    }
    return segments[segments.length - 1];
  }

  public Path child(String name) {
    String[] childSegments = new String[segments.length + 1];
    System.arraycopy(segments, 0, childSegments, 0, segments.length);
    childSegments[segments.length] = name;
    return new Path(childSegments, absolute);
  }

  public String absolutePath(String path) {
    return absolutePath(path(path)).getPath();
  }

  public Path absolutePath(Path path) {
    if (path.absolute) {
      return path;
    }
    return append(path);
  }

  public boolean isAbsolute() {
    return absolute;
  }

  public String relativePath(String path) {
    return relativePath(path(path)).getPath();
  }

  public Path relativePath(Path path) {
    if (path.absolute != absolute) {
      return path;
    }
    if (path.segments.length < segments.length) {
      return path;
    }
    for (int i = 0; i < segments.length; i++) {
      if (!path.segments[i].equals(segments[i])) {
        return path;
      }
    }
    if (path.segments.length == segments.length) {
      return path;
    }
    return new Path(
        Arrays.copyOfRange(path.segments, segments.length, path.segments.length), false);
  }

  public int segmentCount() {
    return segments.length;
  }

  public Path removeFirstSegments(int n) {
    if (n == 0) {
      return this;
    } else if (n == segments.length && absolute) {
      return ROOT;
    } else if (n < 0 || n >= segments.length) {
      throw new IllegalArgumentException("Cannot remove " + n + " segments from path " + getPath());
    }

    return new Path(Arrays.copyOfRange(segments, n, segments.length), absolute);
  }

  public String segment(int index) {
    if (index < 0 || index >= segments.length) {
      throw new IllegalArgumentException(
          "Segment index " + index + " is invalid for path " + getPath());
    }

    return segments[index];
  }

  public Path takeFirstSegments(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("Taken path segment count must be >= 1.");
    }
    if (n >= segmentCount()) {
      return this;
    }
    return new Path(Arrays.copyOfRange(segments, 0, Math.min(n, segmentCount())), absolute);
  }

  public Iterable<Path> ancestors() {
    return new Iterable<Path>() {
      @Override
      public Iterator<Path> iterator() {
        return new AbstractIterator<Path>() {
          private int segmentsToInclude = absolute ? 0 : 1;

          @Nullable
          @Override
          protected Path computeNext() {
            if (segmentsToInclude >= segments.length) {
              return endOfData();
            }
            if (segmentsToInclude == 0) {
              segmentsToInclude++;
              return Path.ROOT;
            }
            int segments = segmentsToInclude;
            segmentsToInclude++;
            return takeFirstSegments(segments);
          }
        };
      }
    };
  }
}
