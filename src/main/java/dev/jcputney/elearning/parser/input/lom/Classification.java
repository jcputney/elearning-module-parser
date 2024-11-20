package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Purpose;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.TaxonPath;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.util.List;
import lombok.Data;

/**
 * Represents the classification information about a learning object in a Learning Object Metadata
 * (LOM) document. The classification provides information about the purpose and taxonomy paths of
 * the learning object, aiding in its categorization and retrieval.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="classification">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="purpose"/>
 *     <group ref="taxonPath"/>
 *     <group ref="description"/>
 *     <group ref="keyword"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:classification"/>
 * </complexType>
 * }</pre>
 */
@Data
public class Classification {

  /**
   * The purpose of this classification, typically represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="purpose">
   *   <complexContent>
   *     <extension base="purposeVocab">
   *       <attributeGroup ref="ag:purpose"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "purpose", useWrapping = false)
  @JacksonXmlProperty(localName = "purpose")
  private SourceValuePair<Purpose> purpose;

  /**
   * The taxonomy paths associated with this classification, allowing for hierarchical
   * categorization.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="taxonPath">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="source"/>
   *     <group ref="taxon"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:taxonPath"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "taxonPath", useWrapping = false)
  @JacksonXmlProperty(localName = "taxonPath")
  private List<TaxonPath> taxonPaths;

  /**
   * A description of this classification, represented as a language-specific string.
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

  /**
   * Keywords associated with this classification, represented as a list of language-specific
   * strings.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="keyword">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:keyword"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "keyword")
  private UnboundLangString keywords;
}