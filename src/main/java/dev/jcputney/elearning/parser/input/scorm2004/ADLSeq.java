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

import java.io.Serializable;

/**
 * Represents the SCORM ADL Sequencing (ADLSeq) elements in the adlseq_v1p3 schema.
 *
 * <p>This class includes properties related to SCORM sequencing of learning activities,
 * such as objectives and mappings to global objectives, which allow for consistent progress
 * tracking and status reporting across different learning modules.</p>
 *
 * <p>The ADLSeq namespace is specified by {@link #NAMESPACE_URI}, and this class
 * is designed to conform to the SCORM 2004 3rd Edition specification.</p>
 * <p>
 * The following schema shows the structure of the ADLSeq element:
 * <pre>{@code
 *   <?xml version = "1.0"?>
 *   <xs:schema targetNamespace = "http://www.adlnet.org/xsd/adlseq_v1p3"
 *              xmlns:xs = "http://www.w3.org/2001/XMLSchema"
 *              xmlns = "http://www.adlnet.org/xsd/adlseq_v1p3"
 *              elementFormDefault = "qualified"
 *              version = "2.0">
 *      <xs:element name = "constrainedChoiceConsiderations" type = "constrainChoiceConsiderationsType"/>
 *      <xs:element name = "rollupConsiderations" type = "rollupConsiderationsType"/>
 *      <xs:element name = "objectives" type = "objectivesType"/>
 *      <xs:element name = "objective" type = "objectiveType"/>
 *      <xs:element name = "mapInfo" type = "mapInfoType"/>
 *
 *      <xs:complexType name = "rollupConsiderationsType">
 *         <xs:attribute name = "requiredForSatisfied" default = "always" type = "rollupConsiderationType"/>
 *         <xs:attribute name = "requiredForNotSatisfied" default = "always" type = "rollupConsiderationType"/>
 *         <xs:attribute name = "requiredForCompleted" default = "always" type = "rollupConsiderationType"/>
 *         <xs:attribute name = "requiredForIncomplete" default = "always" type = "rollupConsiderationType"/>
 *         <xs:attribute name = "measureSatisfactionIfActive" default = "true" type = "xs:boolean"/>
 *      </xs:complexType>
 *
 *      <xs:simpleType name = "rollupConsiderationType">
 *         <xs:restriction base = "xs:token">
 *            <xs:enumeration value = "always"/>
 *            <xs:enumeration value = "ifAttempted"/>
 *            <xs:enumeration value = "ifNotSkipped"/>
 *            <xs:enumeration value = "ifNotSuspended"/>
 *         </xs:restriction>
 *      </xs:simpleType>
 *
 *      <xs:complexType name = "constrainChoiceConsiderationsType">
 *         <xs:attribute name = "preventActivation" default = "false" type = "xs:boolean"/>
 *         <xs:attribute name = "constrainChoice" default = "false" type = "xs:boolean"/>
 *      </xs:complexType>
 *
 *      <xs:complexType name="objectivesType">
 *         <xs:sequence>
 *            <xs:element ref = "objective" minOccurs = "1" maxOccurs = "unbounded"/>
 *         </xs:sequence>
 *      </xs:complexType>
 *
 *      <xs:complexType name="objectiveType">
 *        <xs:sequence>
 *           <xs:element ref = "mapInfo" minOccurs = "1" maxOccurs = "unbounded"/>
 *        </xs:sequence>
 *        <xs:attribute name = "objectiveID" use = "required" type = "xs:anyURI"/>
 *      </xs:complexType>
 *
 *      <xs:complexType name="mapInfoType">
 *         <xs:attribute name="targetObjectiveID" use="required" type="xs:anyURI" />
 *         <xs:attribute name="readRawScore" default="true" type="xs:boolean" />
 *         <xs:attribute name="readMinScore" default="true" type="xs:boolean" />
 *         <xs:attribute name="readMaxScore" default="true" type="xs:boolean" />
 *         <xs:attribute name="readCompletionStatus" default="true" type="xs:boolean" />
 *         <xs:attribute name="readProgressMeasure" default="true" type="xs:boolean" />
 *         <xs:attribute name="writeRawScore" default="false" type="xs:boolean" />
 *         <xs:attribute name="writeMinScore" default="false" type="xs:boolean" />
 *         <xs:attribute name="writeMaxScore" default="false" type="xs:boolean" />
 *         <xs:attribute name="writeCompletionStatus" default="false" type="xs:boolean" />
 *         <xs:attribute name="writeProgressMeasure" default="false" type="xs:boolean" />
 *      </xs:complexType>
 *
 *      <xs:attribute name = "objectivesGlobalToSystem" default = "true" type = "xs:boolean" />
 *   </xs:schema>
 * }</pre>
 */
public class ADLSeq implements Serializable {

  /**
   * The namespace URI for the ADLSeq schema.
   */
  public static final String NAMESPACE_URI = "http://www.adlnet.org/xsd/adlseq_v1p3";

  /**
   * Default constructor for the ADLSeq class.
   */
  @SuppressWarnings("unused")
  private ADLSeq() {
    // Default constructor
  }
}