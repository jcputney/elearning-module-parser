/*
 * Copyright (c) 2024. Jonathan Putney
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
 * Represents the SCORM ADL Content Packaging (ADLCP) elements in the adlcp_v1p3 schema.
 *
 * <p>This class includes properties related to SCORM content packaging, such as
 * the SCORM type, data passed from the LMS, and completion threshold settings.</p>
 * <p>It is used within SCORM manifests to define content and completion behaviors.</p>
 *
 * <p>The ADLCP namespace is specified by {@link #NAMESPACE_URI}, and this class
 * is designed to conform to the SCORM 2004 3rd Edition specification.</p>
 *
 * The following schema snippet shows the structure of an "adlcp" element:
 * <pre>{@code
 *   <?xml version = "1.0"?>
 *   <xs:schema targetNamespace = "http://www.adlnet.org/xsd/adlcp_v1p3"
 *              xmlns:xs = "http://www.w3.org/2001/XMLSchema"
 *              xmlns = "http://www.adlnet.org/xsd/adlcp_v1p3"
 *              elementFormDefault = "qualified"
 *              version = "2.0">
 *      <xs:element name = "location" type = "locationType"/>
 *      <xs:element name = "dataFromLMS" type = "dataFromLMSType"/>
 *      <xs:element name = "timeLimitAction" type = "timeLimitActionType"/>
 *      <xs:element name = "completionThreshold" type = "completionThresholdType" />
 *      <xs:element name = "data" type = "dataType"/>
 *      <xs:element name = "map" type = "mapType"/>
 *
 *      <xs:attribute name = "scormType">
 *         <xs:simpleType>
 *            <xs:restriction base = "xs:string">
 *               <xs:enumeration value = "sco"/>
 *               <xs:enumeration value = "asset"/>
 *            </xs:restriction>
 *         </xs:simpleType>
 *      </xs:attribute>
 *
 *      <xs:simpleType name = "locationType">
 *         <xs:restriction base = "xs:anyURI"/>
 *      </xs:simpleType>
 *
 *      <xs:simpleType name = "dataFromLMSType">
 *         <xs:restriction base = "xs:string"/>
 *      </xs:simpleType>
 *
 *      <xs:simpleType name = "timeLimitActionType">
 *         <xs:restriction base = "xs:string">
 *            <xs:enumeration value = "exit,message"/>
 *            <xs:enumeration value = "exit,no message"/>
 *            <xs:enumeration value = "continue,message"/>
 *            <xs:enumeration value = "continue,no message"/>
 *         </xs:restriction>
 *      </xs:simpleType>
 *
 *      <xs:complexType name = "completionThresholdType">
 *         <xs:simpleContent>
 *            <xs:extension base="xs:string">
 *               <xs:attribute name = "completedByMeasure" default = "false" type = "xs:boolean" />
 *               <xs:attribute name = "minProgressMeasure" default = "1.0" type = "minProgressMeasureType" />
 *               <xs:attribute name = "progressWeight" default = "1.0" type = "progressWeightType" />
 *            </xs:extension>
 *         </xs:simpleContent>
 *      </xs:complexType>
 *
 *      <xs:simpleType name = "minProgressMeasureType">
 *         <xs:restriction base = "xs:decimal">
 *            <xs:minInclusive value = "0.0"/>
 *            <xs:maxInclusive value = "1.0"/>
 *         </xs:restriction>
 *      </xs:simpleType>
 *
 *      <xs:simpleType name = "progressWeightType">
 *         <xs:restriction base = "xs:decimal">
 *            <xs:minInclusive value = "0.0"/>
 *            <xs:maxInclusive value = "1.0"/>
 *         </xs:restriction>
 *      </xs:simpleType>
 *
 *      <xs:complexType name = "dataType">
 *         <xs:sequence>
 *            <xs:element ref = "map" minOccurs = "1" maxOccurs = "unbounded"/>
 *         </xs:sequence>
 *      </xs:complexType>
 *
 *      <xs:complexType name = "mapType">
 *         <xs:attribute name = "targetID" use = "required" type = "xs:anyURI"/>
 *         <xs:attribute name = "readSharedData" default = "true" type = "xs:boolean"/>
 *         <xs:attribute name = "writeSharedData" default = "false" type = "xs:boolean"/>
 *      </xs:complexType>
 *
 *      <xs:attribute name = "sharedDataGlobalToSystem" default = "true" type = "xs:boolean"/>
 *   </xs:schema>
 * }</pre>
 */
public class ADLCP {

  public static final String NAMESPACE_URI = "http://www.adlnet.org/xsd/adlcp_v1p3";

}