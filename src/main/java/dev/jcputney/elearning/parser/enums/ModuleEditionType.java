/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.enums;

/**
 * Enum representing the different types and editions of modules.
 * <p>
 * This enum extends the concept of {@link ModuleType} by providing
 * more granular edition information, particularly for SCORM 2004 modules
 * which have multiple editions (2nd, 3rd, and 4th).
 * </p>
 */
public enum ModuleEditionType {
  /**
   * SCORM 1.2 module type.
   */
  SCORM_12,

  /**
   * Generic SCORM 2004 module type (when edition is unknown or unspecified).
   */
  SCORM_2004,

  /**
   * SCORM 2004 2nd Edition module type.
   */
  SCORM_2004_2ND_EDITION,

  /**
   * SCORM 2004 3rd Edition module type.
   */
  SCORM_2004_3RD_EDITION,

  /**
   * SCORM 2004 4th Edition module type.
   */
  SCORM_2004_4TH_EDITION,

  /**
   * AICC (Aviation Industry Computer-Based Training Committee) module type.
   */
  AICC,

  /**
   * cmi5 (xAPI for cmi5) module type.
   */
  CMI5;

  /**
   * Determines if this edition type is a SCORM 2004 variant.
   *
   * @return true if this is any SCORM 2004 edition type, false otherwise
   */
  public boolean isScorm2004() {
    return this == SCORM_2004 
        || this == SCORM_2004_2ND_EDITION 
        || this == SCORM_2004_3RD_EDITION 
        || this == SCORM_2004_4TH_EDITION;
  }

  /**
   * Converts this edition type to the base {@link ModuleType}.
   *
   * @return the corresponding base module type
   */
  public ModuleType toModuleType() {
    switch (this) {
      case SCORM_12:
        return ModuleType.SCORM_12;
      case SCORM_2004:
      case SCORM_2004_2ND_EDITION:
      case SCORM_2004_3RD_EDITION:
      case SCORM_2004_4TH_EDITION:
        return ModuleType.SCORM_2004;
      case AICC:
        return ModuleType.AICC;
      case CMI5:
        return ModuleType.CMI5;
      default:
        throw new IllegalStateException("Unexpected edition type: " + this);
    }
  }

  /**
   * Creates a ModuleEditionType from a ModuleType and optional edition information.
   *
   * @param moduleType the base module type
   * @param edition optional edition string (e.g., "2nd", "3rd", "4th") for SCORM 2004
   * @return the corresponding ModuleEditionType
   */
  public static ModuleEditionType fromModuleType(ModuleType moduleType, String edition) {
    if (moduleType == ModuleType.SCORM_2004 && edition != null) {
      String normalizedEdition = edition.toLowerCase().trim();
      // Check for specific editions first
      if (normalizedEdition.contains("4th edition") || normalizedEdition.contains("4th")) {
        return SCORM_2004_4TH_EDITION;
      } else if (normalizedEdition.contains("3rd edition") || normalizedEdition.contains("3rd")) {
        return SCORM_2004_3RD_EDITION;
      } else if (normalizedEdition.contains("2nd edition") || normalizedEdition.contains("2nd") || normalizedEdition.equals("cam 1.3")) {
        return SCORM_2004_2ND_EDITION;
      }
    }
    
    switch (moduleType) {
      case SCORM_12:
        return SCORM_12;
      case SCORM_2004:
        return SCORM_2004;
      case AICC:
        return AICC;
      case CMI5:
        return CMI5;
      default:
        throw new IllegalArgumentException("Unknown module type: " + moduleType);
    }
  }
}