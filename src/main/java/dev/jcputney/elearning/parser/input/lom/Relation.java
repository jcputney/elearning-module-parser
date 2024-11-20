package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Kind;
import dev.jcputney.elearning.parser.input.lom.types.Resource;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import java.util.List;
import lombok.Data;

/**
 * Represents the relationship information about a learning object in a Learning Object Metadata
 * (LOM) document. Relations describe connections between the current learning object and other
 * resources.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="relation">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="kind"/>
 *     <group ref="resource"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:relation"/>
 * </complexType>
 * }</pre>
 */
@Data
public class Relation {

  /**
   * The kind of relationship, represented as a source-value pair, specifying the type of connection
   * between the current learning object and another resource.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="kind">
   *   <complexContent>
   *     <extension base="kindVocab">
   *       <attributeGroup ref="ag:kind"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "kind")
  private SourceValuePair<Kind> kind;

  /**
   * The resource information that describes the target of the relationship. A resource can include
   * identifiers and descriptions for the related learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="resource">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="identifier"/>
   *     <group ref="description"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:resource"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "resource", useWrapping = false)
  @JacksonXmlProperty(localName = "resource")
  private List<Resource> resource;
}