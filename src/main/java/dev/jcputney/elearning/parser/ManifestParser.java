package dev.jcputney.elearning.parser;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ManifestParseException;

public abstract class ManifestParser {

  protected final FileAccess fileAccess;

  public ManifestParser(FileAccess fileAccess) {
    this.fileAccess = fileAccess;
  }

  public abstract ModuleType getModuleType();

  public abstract void parseManifest(String manifestPath) throws ManifestParseException;
}
