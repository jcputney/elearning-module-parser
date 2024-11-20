package dev.jcputney.elearning.parser.input.scorm2004;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Represents the SCORM ADL Sequencing (ADLSeq) elements in the adlseq_v1p3 schema.
 *
 * <p>This class includes properties related to SCORM sequencing of learning activities,
 * such as objectives and mappings to global objectives, which allow for consistent progress
 * tracking and status reporting across different learning modules.</p>
 *
 * <p>The ADLSeq namespace is specified by {@link #NAMESPACE_URI}, and this class
 * is designed to conform to the SCORM 2004 3rd Edition specification.</p>
 *
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
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ADLSeq {

  public static final String NAMESPACE_URI = "http://www.adlnet.org/xsd/adlseq_v1p3";
}