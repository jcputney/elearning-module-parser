package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.LOM;
import java.util.List;
import lombok.Data;

/**
 * Represents a taxon within a taxon path in the Learning Object Metadata (LOM) schema. A taxon is
 * used to identify a specific concept or category in a classification hierarchy.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="taxon">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="id"/>
 *     <xs:group ref="entryTaxon"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:taxon"/>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class Taxon {

  /**
   * The unique identifier for the taxon, typically used to reference the taxon in a classification
   * system.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="id">
   *   <xs:element name="id" type="CharacterString"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "id")
  private String id;

  /**
   * The entry providing a description or label for the taxon, often represented as a multilingual
   * string.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="entryTaxon">
   *   <xs:element name="entry" type="LangString"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "entry")
  private SingleLangString entry;

  /**
   * Any custom elements or extensions defined within the taxon, allowing for schema extensibility.
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