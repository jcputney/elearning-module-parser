package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.LOM;
import java.util.List;
import lombok.Data;

/**
 * Represents a taxon path in the Learning Object Metadata (LOM) schema. A taxon path defines a
 * hierarchical classification or categorization of a learning object, consisting of a source and a
 * list of taxa.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="taxonPath">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="source"/>
 *     <xs:group ref="taxon"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:taxonPath"/>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class TaxonPath {

  /**
   * The source describing the classification system or taxonomy from which the taxa are derived.
   * Often represented as a multilingual string.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="source">
   *   <xs:element name="source" type="LangString"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "source")
  private SingleLangString source;

  /**
   * A list of taxon elements representing nodes or concepts in the hierarchical classification.
   * Each taxon specifies a unique concept within the taxonomy.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="taxon">
   *   <xs:element name="taxon" type="Taxon" maxOccurs="unbounded"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "taxon")
  private List<Taxon> taxons;

  /**
   * Any custom elements or extensions defined within the taxon path, allowing for schema
   * extensibility.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="ex:customElements"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "customElement", namespace = LOM.NAMESPACE_URI + "/extend")
  private List<Object> customElements;
}