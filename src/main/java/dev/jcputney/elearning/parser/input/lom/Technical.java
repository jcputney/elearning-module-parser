package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.LomDuration;
import dev.jcputney.elearning.parser.input.lom.types.OrComposite;
import dev.jcputney.elearning.parser.input.lom.types.Requirement;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import java.util.List;
import lombok.Data;

/**
 * Represents the technical information about a learning object in a Learning Object Metadata (LOM)
 * document. Technical information includes details about formats, size, location, requirements, and
 * other platform-specific details.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="technical">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="format"/>
 *     <group ref="size"/>
 *     <group ref="location"/>
 *     <group ref="requirement"/>
 *     <group ref="installationRemarks"/>
 *     <group ref="otherPlatformRequirements"/>
 *     <group ref="duration"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:technical"/>
 * </complexType>
 * }</pre>
 */
@Data
public class Technical {

  /**
   * The list of MIME types representing the formats used by the learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="format">
   *   <simpleContent>
   *     <extension base="MimeType">
   *       <attributeGroup ref="ag:format"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "format", useWrapping = false)
  @JacksonXmlProperty(localName = "format")
  private List<String> format;

  /**
   * The size of the learning object, in bytes.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="size">
   *   <simpleContent>
   *     <extension base="Size">
   *       <attributeGroup ref="ag:size"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "size")
  private Integer size;

  /**
   * The list of locations where the learning object can be accessed. Each location is represented
   * as a URL or file path.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="location">
   *   <simpleContent>
   *     <extension base="CharacterString">
   *       <attributeGroup ref="ag:location"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "location", useWrapping = false)
  @JacksonXmlProperty(localName = "location")
  private List<String> location;

  /**
   * The list of requirements for the learning object, each represented as an {@link OrComposite}.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="requirement">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="orComposite"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:requirement"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "requirement", useWrapping = false)
  @JacksonXmlProperty(localName = "requirement")
  private List<Requirement> requirements;

  /**
   * Remarks or instructions about how to install the learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="installationRemarks">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:installationRemarks"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "installationRemarks")
  private SingleLangString installationRemarks;

  /**
   * Additional platform requirements, represented as a single {@link LangString}.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="otherPlatformRequirements">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:otherPlatformRequirements"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "otherPlatformRequirements")
  private SingleLangString otherPlatformRequirements;

  /**
   * The duration metadata of the learning object, including the duration value and an optional
   * description.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="duration">
   *   <complexContent>
   *     <extension base="Duration">
   *       <attributeGroup ref="ag:duration"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "duration")
  private LomDuration duration;

  /**
   * A placeholder for custom elements that extend the technical information. This allows for
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