package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Date;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import lombok.Data;

/**
 * Represents the annotation information about a learning object in a Learning Object Metadata (LOM)
 * document. Annotations provide additional comments, instructions, or explanations related to the
 * learning object.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="annotation">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="entity"/>
 *     <group ref="date"/>
 *     <group ref="description"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:annotation"/>
 * </complexType>
 * }</pre>
 */
@Data
public class Annotation {

  /**
   * The entity that provided the annotation, typically represented as a vCard.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="entity">
   *   <simpleContent>
   *     <extension base="VCard">
   *       <attributeGroup ref="ag:entity"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "entity")
  private String entity;

  /**
   * The date when the annotation was created or last modified.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="date">
   *   <complexContent>
   *     <extension base="DateTime">
   *       <attributeGroup ref="ag:date"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "date")
  private Date date;

  /**
   * A description of the annotation, represented as a language-specific string.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="description">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:description"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private SingleLangString description;
}