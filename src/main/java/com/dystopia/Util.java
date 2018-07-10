package com.dystopia;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {

  private Util() {
  }

  public static Path getContainingPath() {
    URL location = Util.class.getProtectionDomain().getCodeSource().getLocation();
    String path = location.getPath();
    return Paths.get(path);
  }
}
