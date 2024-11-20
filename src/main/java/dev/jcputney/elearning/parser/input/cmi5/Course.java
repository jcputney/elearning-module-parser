package dev.jcputney.elearning.parser.input.cmi5;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import lombok.Data;

import java.util.List;

/**
 * Represents the root course element in a CMI5 course structure, including metadata such as title,
 * description, and optional custom extensions.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:element name="course">
 *   <xs:complexType>
 *     <xs:sequence>
 *       <xs:element name="title" type="textType"/>
 *       <xs:element name="description" type="textType"/>
 *       <xs:group ref="anyElement"/>
 *     </xs:sequence>
 *     <xs:attributeGroup ref="anyAttribute"/>
 *     <xs:attribute name="id" type="xs:anyURI" use="required"/>
 *   </xs:complexType>
 * </xs:element>
 * }</pre>
 */
@Data
public class Course {

  /**
   * The title of the course, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="title" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private TextType title;

  /**
   * The description of the course, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="description" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;

  /**
   * A list of additional custom elements (extensions) included in the course definition.
   *
   * <pre>{@code
   * <xs:group ref="anyElement"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "anyElement")
  private List<Object> customExtensions;

  /**
   * The unique identifier for the course, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="id" type="xs:anyURI" use="required"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  private String id;
}
