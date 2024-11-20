package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.ContributeMeta;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import java.util.List;
import lombok.Data;

/**
 * Represents the meta-metadata information about a learning object in a Learning Object Metadata
 * (LOM) document. Meta-metadata provides information about the metadata itself, including its
 * origin, purpose, and maintenance.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="metaMetadata">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="identifier"/>
 *     <group ref="contributeMeta"/>
 *     <group ref="metadataSchema"/>
 *     <group ref="language"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:metaMetadata"/>
 * </complexType>
 * }</pre>
 */
@Data
public class MetaMetadata {

  /**
   * The list of unique identifiers for the metadata. Each identifier typically includes a catalog
   * and an entry.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="identifier">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="catalog"/>
   *     <group ref="entry"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:identifier"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "identifier", useWrapping = false)
  @JacksonXmlProperty(localName = "identifier")
  private List<Identifier> identifier;

  /**
   * The list of contributors to the metadata, including their roles, entities, and contribution
   * dates.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="contributeMeta">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="roleMeta"/>
   *     <group ref="entityUnbounded"/>
   *     <group ref="date"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:contribute"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "contribute", useWrapping = false)
  @JacksonXmlProperty(localName = "contribute")
  private List<ContributeMeta> contribute;

  /**
   * The metadata schema or standard being used. Typically represented as a list of strings.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="metadataSchema">
   *   <simpleContent>
   *     <extension base="CharacterString">
   *       <attributeGroup ref="ag:metadataSchema"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "metadataSchema", useWrapping = false)
  @JacksonXmlProperty(localName = "metadataSchema")
  private List<String> metadataSchema;

  /**
   * The language used for the metadata content.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="language">
   *   <simpleContent>
   *     <extension base="LanguageId">
   *       <attributeGroup ref="ag:language"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "language")
  private String language;

  /**
   * A placeholder for custom elements that extend the meta-metadata information. This allows for
   * additional metadata to be included that is not part of the standard schema.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="customElements">
   *   <complexContent>
   *     <extension base="xs:anyType"/>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "customElements", useWrapping = false)
  @JacksonXmlProperty(localName = "customElements")
  private List<Object> customElements;
}