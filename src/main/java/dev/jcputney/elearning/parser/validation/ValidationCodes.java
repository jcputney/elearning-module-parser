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

package dev.jcputney.elearning.parser.validation;

/**
 * Standard validation error and warning codes.
 * Organized by eLearning standard.
 */
public final class ValidationCodes {

    // ===== SCORM 1.2 =====
    public static final String SCORM12_MISSING_RESOURCE_REF = "SCORM12_MISSING_RESOURCE_REF";
    public static final String SCORM12_MISSING_LAUNCH_URL = "SCORM12_MISSING_LAUNCH_URL";
    public static final String SCORM12_INVALID_SCORMTYPE = "SCORM12_INVALID_SCORMTYPE";
    public static final String SCORM12_INVALID_DEFAULT_ORG = "SCORM12_INVALID_DEFAULT_ORG";
    public static final String SCORM12_FILE_NOT_FOUND = "SCORM12_FILE_NOT_FOUND";
    public static final String SCORM12_MISSING_ORGANIZATIONS = "SCORM12_MISSING_ORGANIZATIONS";
    public static final String SCORM12_MISSING_RESOURCES = "SCORM12_MISSING_RESOURCES";
    public static final String SCORM12_INVALID_IDENTIFIER = "SCORM12_INVALID_IDENTIFIER";
    public static final String SCORM12_DUPLICATE_IDENTIFIER = "SCORM12_DUPLICATE_IDENTIFIER";
    public static final String SCORM12_MISSING_TITLE = "SCORM12_MISSING_TITLE";

    // ===== SCORM 2004 =====
    public static final String SCORM2004_INVALID_SEQUENCING = "SCORM2004_INVALID_SEQUENCING";
    public static final String SCORM2004_MISSING_RESOURCE_REF = "SCORM2004_MISSING_RESOURCE_REF";

    // ===== AICC =====
    public static final String AICC_INVALID_AU_REFERENCE = "AICC_INVALID_AU_REFERENCE";
    public static final String AICC_MISSING_COURSE_ID = "AICC_MISSING_COURSE_ID";

    // ===== cmi5 =====
    public static final String CMI5_MISSING_LAUNCH_METHOD = "CMI5_MISSING_LAUNCH_METHOD";
    public static final String CMI5_INVALID_LAUNCH_URL = "CMI5_INVALID_LAUNCH_URL";

    // ===== xAPI =====
    public static final String XAPI_INVALID_ACTIVITY_ID = "XAPI_INVALID_ACTIVITY_ID";
    public static final String XAPI_MISSING_LAUNCH_URL = "XAPI_MISSING_LAUNCH_URL";

    private ValidationCodes() {
        // Prevent instantiation
    }
}
