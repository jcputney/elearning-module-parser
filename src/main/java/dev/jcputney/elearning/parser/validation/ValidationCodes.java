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
 * Standard validation error and warning codes. Organized by eLearning standard.
 */
public final class ValidationCodes {

  // ===== SCORM 1.2 =====
  /**
   * Validation error code indicating that a referenced resource is missing in a SCORM 1.2
   * manifest.
   * <p>
   * This error occurs when the manifest references a resource which is not defined in the
   * {@code<resources>} section of the SCORM 1.2 manifest file. The absence of this resource
   * prevents the proper functioning of specific SCORM components or activities.
   * <p>
   * Error Code: SCORM12_MISSING_RESOURCE_REF
   * <p>
   * Severity: ERROR
   * <p>
   * Example use case includes validation of SCORM 1.2 module manifests where resource definitions
   * fail to align with their references.
   */
  public static final String SCORM12_MISSING_RESOURCE_REF = "SCORM12_MISSING_RESOURCE_REF";

  /**
   * Validation error code indicating that the launch URL is missing in a SCORM 1.2 manifest.
   * <p>
   * This error occurs when a SCORM 1.2 resource does not define a launch location for the activity
   * or content item, which is required for proper runtime interaction with the resource in a
   * compliant LMS.
   * <p>
   * Error Code: SCORM12_MISSING_LAUNCH_URL
   * <p>
   * Severity: ERROR
   */
  public static final String SCORM12_MISSING_LAUNCH_URL = "SCORM12_MISSING_LAUNCH_URL";

  /**
   * Validation error code indicating an invalid SCORM type attribute in a SCORM 1.2 resource.
   * <p>
   * This error occurs when a resource's adlcp:scormtype attribute contains an invalid value. Valid
   * SCORM types include "sco" (Sharable Content Object) and "asset".
   * <p>
   * Error Code: SCORM12_INVALID_SCORMTYPE
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.3.4.
   */
  public static final String SCORM12_INVALID_SCORMTYPE = "SCORM12_INVALID_SCORMTYPE";

  /**
   * Validation error code indicating an invalid default organization reference in a SCORM 1.2
   * manifest.
   * <p>
   * This error occurs when the organizations element's default attribute references an organization
   * identifier that does not exist in the manifest. The default organization determines which
   * content structure is presented to learners by default.
   * <p>
   * Error Code: SCORM12_INVALID_DEFAULT_ORG
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.3.2.
   */
  public static final String SCORM12_INVALID_DEFAULT_ORG = "SCORM12_INVALID_DEFAULT_ORG";

  /**
   * Validation error code indicating a referenced file cannot be found in a SCORM 1.2 package.
   * <p>
   * This error occurs when a resource references a file (via href or dependency) that does not
   * exist in the package's physical file structure. This prevents the content from being properly
   * delivered to learners.
   * <p>
   * Error Code: SCORM12_FILE_NOT_FOUND
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.3.4.
   */
  public static final String SCORM12_FILE_NOT_FOUND = "SCORM12_FILE_NOT_FOUND";

  /**
   * Validation error code indicating the organizations element is missing from a SCORM 1.2
   * manifest.
   * <p>
   * This error occurs when the required &lt;organizations&gt; element is not present in the
   * manifest. The organizations element defines the content structure and is mandatory in SCORM 1.2
   * manifests.
   * <p>
   * Error Code: SCORM12_MISSING_ORGANIZATIONS
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.3.2.
   */
  public static final String SCORM12_MISSING_ORGANIZATIONS = "SCORM12_MISSING_ORGANIZATIONS";

  /**
   * Validation error code indicating the resources element is missing from a SCORM 1.2 manifest.
   * <p>
   * This error occurs when the required &lt;resources&gt; element is not present in the manifest.
   * The resources element contains all content files and their metadata, and is mandatory in SCORM
   * 1.2 manifests.
   * <p>
   * Error Code: SCORM12_MISSING_RESOURCES
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.3.4.
   */
  public static final String SCORM12_MISSING_RESOURCES = "SCORM12_MISSING_RESOURCES";

  /**
   * Validation error code indicating an invalid or missing identifier attribute in a SCORM 1.2
   * manifest element.
   * <p>
   * This error occurs when a required identifier attribute is missing, empty, or contains invalid
   * characters. Identifiers must be unique within their scope and follow XML ID naming
   * conventions.
   * <p>
   * Error Code: SCORM12_INVALID_IDENTIFIER
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.1.
   */
  public static final String SCORM12_INVALID_IDENTIFIER = "SCORM12_INVALID_IDENTIFIER";

  /**
   * Validation error code indicating duplicate identifier values in a SCORM 1.2 manifest.
   * <p>
   * This error occurs when multiple elements (manifest, organizations, items, or resources) share
   * the same identifier value. All identifiers within a manifest must be unique to ensure
   * unambiguous references.
   * <p>
   * Error Code: SCORM12_DUPLICATE_IDENTIFIER
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.1.
   */
  public static final String SCORM12_DUPLICATE_IDENTIFIER = "SCORM12_DUPLICATE_IDENTIFIER";

  /**
   * Validation error code indicating a missing title element in a SCORM 1.2 manifest component.
   * <p>
   * This error occurs when a required &lt;title&gt; element is missing from an organization or
   * item. Titles provide human-readable labels for content structure elements and are required for
   * proper display in learning management systems.
   * <p>
   * Error Code: SCORM12_MISSING_TITLE
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.3.2.
   */
  public static final String SCORM12_MISSING_TITLE = "SCORM12_MISSING_TITLE";

  /**
   * Validation error code indicating a missing manifest identifier in a SCORM 1.2 package.
   * <p>
   * This error occurs when the required identifier attribute is missing from the manifest element.
   * The manifest identifier uniquely identifies the content package and is mandatory for SCORM 1.2
   * compliance.
   * <p>
   * Error Code: SCORM12_MISSING_MANIFEST_IDENTIFIER
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.1.
   */
  public static final String SCORM12_MISSING_MANIFEST_IDENTIFIER = "SCORM12_MISSING_MANIFEST_IDENTIFIER";

  /**
   * Validation error code indicating an organization has no launchable resources.
   * <p>
   * This error occurs when an organization structure contains only container items without any
   * items that reference actual content resources (items with identifierref attributes). At least
   * one launchable resource is required for the organization to be functional in a learning
   * management system.
   * <p>
   * Error Code: SCORM12_NO_LAUNCHABLE_RESOURCE
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 1.2 CAM specification section 2.3.3.
   */
  public static final String SCORM12_NO_LAUNCHABLE_RESOURCE = "SCORM12_NO_LAUNCHABLE_RESOURCE";

  // ===== SCORM 2004 =====

  /**
   * Validation error code indicating invalid sequencing rules in a SCORM 2004 manifest.
   * <p>
   * This error occurs when sequencing and navigation (S&amp;N) rules are malformed, contain logical
   * contradictions, or violate SCORM 2004 sequencing constraints. Sequencing controls the order and
   * conditions under which learners can access content.
   * <p>
   * Error Code: SCORM2004_INVALID_SEQUENCING
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 2004 4th Edition Sequencing and Navigation specification.
   */
  public static final String SCORM2004_INVALID_SEQUENCING = "SCORM2004_INVALID_SEQUENCING";

  /**
   * Validation error code indicating a missing resource reference in a SCORM 2004 manifest.
   * <p>
   * This error occurs when an item's identifierref attribute references a resource that does not
   * exist in the manifest's resources section. This prevents the content from being launched in the
   * learning management system.
   * <p>
   * Error Code: SCORM2004_MISSING_RESOURCE_REF
   * <p>
   * Severity: ERROR
   * <p>
   * Required by SCORM 2004 CAM specification section 2.3.
   */
  public static final String SCORM2004_MISSING_RESOURCE_REF = "SCORM2004_MISSING_RESOURCE_REF";

  // ===== AICC =====

  /**
   * Validation error code indicating an invalid Assignable Unit (AU) reference in AICC content.
   * <p>
   * This error occurs when a course structure element references an AU that is not defined in the
   * assignable unit (.au) file. AICC uses AUs as the primary learning objects that can be tracked
   * independently.
   * <p>
   * Error Code: AICC_INVALID_AU_REFERENCE
   * <p>
   * Severity: ERROR
   * <p>
   * Required by AICC CMI Guidelines specification.
   */
  public static final String AICC_INVALID_AU_REFERENCE = "AICC_INVALID_AU_REFERENCE";

  /**
   * Validation error code indicating a missing Course ID in AICC descriptor file.
   * <p>
   * This error occurs when the required Course_ID field is missing from the [Course_Description]
   * block in the .des (descriptor) file. The Course ID is mandatory and uniquely identifies the
   * AICC course.
   * <p>
   * Error Code: AICC_MISSING_COURSE_ID
   * <p>
   * Severity: ERROR
   * <p>
   * Required by AICC CMI Guidelines specification.
   */
  public static final String AICC_MISSING_COURSE_ID = "AICC_MISSING_COURSE_ID";

  // ===== cmi5 =====

  /**
   * Validation error code indicating a missing launch method in a cmi5 AU.
   * <p>
   * This error occurs when an Assignable Unit in a cmi5 course structure does not specify a
   * required launchMethod attribute. The launch method determines how the AU is presented to the
   * learner (e.g., "OwnWindow", "AnyWindow").
   * <p>
   * Error Code: CMI5_MISSING_LAUNCH_METHOD
   * <p>
   * Severity: ERROR
   * <p>
   * Required by cmi5 specification section 8.1.
   */
  public static final String CMI5_MISSING_LAUNCH_METHOD = "CMI5_MISSING_LAUNCH_METHOD";

  /**
   * Validation error code indicating an invalid launch URL in a cmi5 AU.
   * <p>
   * This error occurs when an Assignable Unit's launch URL is malformed, empty, or points to a
   * non-existent resource. The launch URL must be a valid URI that the LMS can use to launch the
   * content.
   * <p>
   * Error Code: CMI5_INVALID_LAUNCH_URL
   * <p>
   * Severity: ERROR
   * <p>
   * Required by cmi5 specification section 8.1.
   */
  public static final String CMI5_INVALID_LAUNCH_URL = "CMI5_INVALID_LAUNCH_URL";

  // ===== xAPI =====

  /**
   * Validation error code indicating an invalid Activity ID in xAPI/Tin Can content.
   * <p>
   * This error occurs when an activity's ID is not a valid IRI (Internationalized Resource
   * Identifier). Activity IDs must be absolute IRIs and are used to uniquely identify learning
   * activities in xAPI statements.
   * <p>
   * Error Code: XAPI_INVALID_ACTIVITY_ID
   * <p>
   * Severity: ERROR
   * <p>
   * Required by xAPI specification section 4.1.4.1.
   */
  public static final String XAPI_INVALID_ACTIVITY_ID = "XAPI_INVALID_ACTIVITY_ID";

  /**
   * Validation error code indicating a missing launch URL in xAPI/Tin Can content.
   * <p>
   * This error occurs when an activity definition does not include a required launch URL or the URL
   * is empty. The launch URL is needed for the LMS to present the activity to learners.
   * <p>
   * Error Code: XAPI_MISSING_LAUNCH_URL
   * <p>
   * Severity: ERROR
   * <p>
   * Required by xAPI specification and Tin Can packaging format.
   */
  public static final String XAPI_MISSING_LAUNCH_URL = "XAPI_MISSING_LAUNCH_URL";

  /**
   * Utility class containing constants for validation issue codes.
   * <p>
   * This class defines a set of predefined validation issue codes representing various problems or
   * inconsistencies that can occur during validation of e-learning module manifests (e.g., SCORM,
   * AICC, xAPI, and CMI5). These codes may be used to categorize and identify specific validation
   * issues.
   * <p>
   * The class is designed to be non-instantiable, as it only serves as a container for constants.
   * Attempting to instantiate the class directly will result in prevention by its private
   * constructor.
   */
  private ValidationCodes() {
    // Prevent instantiation
  }
}
