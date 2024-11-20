package dev.jcputney.elearning.parser.input.scorm2004;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ADLNav {

  public static final String NAMESPACE_URI = "http://www.adlnet.org/xsd/adlnav_v1p3";

}