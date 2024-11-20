package dev.jcputney.elearning.parser.util;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import java.io.IOException;

/**
 * Determines the type of eLearning module based on the files present in the file system.
 */
public class ModuleTypeDetector {

  private static final String IMSMANIFEST_XML = "imsmanifest.xml";
  private static final String IMSMANIFEST_XSD = "imsmanifest.xsd";
  private static final String AICC_AU_PREFIX = ".au";
  private static final String AICC_CRS_PREFIX = ".crs";
  private static final String CMI5_JSON = "cmi5.json";

  private final FileAccess fileAccess;

  public ModuleTypeDetector(FileAccess fileAccess) {
    this.fileAccess = fileAccess;
  }

  public ModuleType detectModuleType(String modulePath) throws ModuleDetectionException {
    try {
      // Check for SCORM 2004
      if (isScorm2004(modulePath)) {
        return ModuleType.SCORM_2004;
      }

      // Check for SCORM 1.2
      if (isScorm12(modulePath)) {
        return ModuleType.SCORM_12;
      }

      // Check for AICC
      if (isAicc(modulePath)) {
        return ModuleType.AICC;
      }

      // Check for cmi5
      if (fileAccess.fileExists(modulePath + "/" + CMI5_JSON)) {
        return ModuleType.CMI5;
      }

      // Unknown module type
      throw new ModuleDetectionException("Unknown module type");
    } catch (Exception e) {
      throw new ModuleDetectionException("Error detecting module type", e);
    }
  }

  private boolean isScorm2004(String modulePath) {
    return fileAccess.fileExists(modulePath + "/" + IMSMANIFEST_XML) &&
        fileAccess.fileExists(modulePath + "/" + IMSMANIFEST_XSD);
  }

  private boolean isScorm12(String modulePath) {
    return fileAccess.fileExists(modulePath + "/" + IMSMANIFEST_XML) &&
        !fileAccess.fileExists(modulePath + "/" + IMSMANIFEST_XSD);
  }

  private boolean isAicc(String modulePath) throws IOException {
    var files = fileAccess.listFiles(modulePath);
    return files
        .stream()
        .anyMatch(file -> file.endsWith(AICC_AU_PREFIX) &&
            files.stream().anyMatch(f -> f.endsWith(AICC_CRS_PREFIX)));
  }
}

