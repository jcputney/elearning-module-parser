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

package dev.jcputney.elearning.parser.input.scorm2004;

/**
 * Represents the navigation control elements within the SCORM ADL Navigation schema (adlnav_v1p3).
 * This class captures data related to navigation presentation and control within an LMS context.
 *
 * <p>The ADLNav class maps the navigation elements for controlling how the learner
 * interacts with the LMS user interface and navigation controls, providing access to elements such
 * as hideLMSUI and navigationInterface.</p>
 *
 * <p>This class follows the SCORM 2004 specification and ADL's extension
 * for sequencing and navigation of learning activities.</p>
 *
 * <p>The ADLNav namespace is specified by {@link #NAMESPACE_URI}, and this class
 * is designed to conform to the SCORM 2004 3rd Edition specification.</p>
 *
 * The following schema shows the structure of the ADLNav element:
 * <pre>{@code
 *   <?xml version = "1.0"?>
 *   <xs:schema targetNamespace = "http://www.adlnet.org/xsd/adlnav_v1p3"
 *              xmlns:xs = "http://www.w3.org/2001/XMLSchema"
 *              xmlns = "http://www.adlnet.org/xsd/adlnav_v1p3"
 *              elementFormDefault = "qualified"
 *              version = "1.0"> *
 *      <xs:element name = "presentation" type = "presentationType"/> *
 *      <xs:element name = "navigationInterface" type = "navigationInterfaceType"/> *
 *      <xs:element name = "hideLMSUI" type = "hideLMSUIType"/>
 *
 *      <xs:complexType name = "presentationType">
 *         <xs:sequence>
 *            <xs:element ref = "navigationInterface" minOccurs = "0" maxOccurs = "1"/>
 *         </xs:sequence>
 *      </xs:complexType>
 *
 *      <xs:complexType name = "navigationInterfaceType">
 *         <xs:sequence>
 *            <xs:element ref = "hideLMSUI" minOccurs = "0" maxOccurs = "unbounded"/>
 *         </xs:sequence>
 *      </xs:complexType>
 *
 *      <xs:simpleType name = "hideLMSUIType">
 *         <xs:restriction base = "xs:token">
 *            <xs:enumeration value = "abandon"/>
 *            <xs:enumeration value = "continue"/>
 *            <xs:enumeration value = "exit"/>
 *            <xs:enumeration value = "previous"/>
 *            <xs:enumeration value = "suspendAll"/>
 *            <xs:enumeration value = "exitAll"/>
 *            <xs:enumeration value = "abandonAll"/>
 *         </xs:restriction>
 *      </xs:simpleType>
 *   </xs:schema>
 * }</pre>
 */
public class ADLNav {

  /**
   * Default constructor for the ADLNav class.
   */
  public static final String NAMESPACE_URI = "http://www.adlnet.org/xsd/adlnav_v1p3";

  /**
   * Default constructor for the ADLNav class.
   */
  @SuppressWarnings("unused")
  private ADLNav() {
    // Default constructor
  }
}
