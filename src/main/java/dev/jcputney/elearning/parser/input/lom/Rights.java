package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.CopyrightAndOtherRestrictions;
import dev.jcputney.elearning.parser.input.lom.types.Cost;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import lombok.Data;

/**
 * Represents the rights information about a learning object in a Learning Object Metadata (LOM)
 * document. Rights information includes details about costs, copyright restrictions, and any
 * related descriptions.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="rights">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="cost"/>
 *     <group ref="copyrightAndOtherRestrictions"/>
 *     <group ref="description"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:rights"/>
 * </complexType>
 * }</pre>
 */
@Data
public class Rights {

  /**
   * The cost information associated with the learning object, represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="cost">
   *   <complexContent>
   *     <extension base="costVocab">
   *       <attributeGroup ref="ag:cost"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "cost", useWrapping = false)
  @JacksonXmlProperty(localName = "cost")
  private SourceValuePair<Cost> cost;

  /**
   * Copyright and other restrictions associated with the learning object, represented as a
   * source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="copyrightAndOtherRestrictions">
   *   <complexContent>
   *     <extension base="copyrightAndOtherRestrictionsVocab">
   *       <attributeGroup ref="ag:copyrightAndOtherRestrictions"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "copyrightAndOtherRestrictions", useWrapping = false)
  @JacksonXmlProperty(localName = "copyrightAndOtherRestrictions")
  private SourceValuePair<CopyrightAndOtherRestrictions> copyrightAndOtherRestrictions;

  /**
   * Descriptions of the rights information, represented as a list of language-specific strings.
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
  private UnboundLangString descriptions;
}