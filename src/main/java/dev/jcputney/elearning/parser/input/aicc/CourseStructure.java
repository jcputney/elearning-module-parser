package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>courseStructure</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="courseStructure">
 *   <xs:all>
 *     <xs:element name="block" type="xs:string" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="member" type="xs:string" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class CourseStructure implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constant representing the key for identifying a block within the course structure. This key is
   * used for JSON serialization or deserialization and maps to the "block" field.
   */
  private static final String KEY_BLOCK = "block";

  /**
   * A constant key used to represent the "member" attribute in the course structure. It serves as
   * an identifier for mapping or processing data related to members within the context of the
   * CourseStructure class.
   */
  private static final String KEY_MEMBER = "member";

  /**
   * A constant key used to identify the "prerequisites" field in a course structure.
   * <p>
   * This key is typically utilized for accessing or manipulating the prerequisites associated with
   * a course, which might include specific conditions or requirements that need to be fulfilled
   * before progressing further in the course structure.
   **/
  private static final String KEY_PREREQUISITES = "prerequisites";

  /**
   * A constant representing the key name "attributes" used to identify and handle attributes
   * associated with a course structure. This key is primarily utilized in JSON mapping and
   * processing attribute-related data fields within the CourseStructure class.
   */
  private static final String KEY_ATTRIBUTES = "attributes";

  /**
   * A map used to store key-value pairs representing attributes of a course structure. The keys and
   * values are strings, with the map maintaining the insertion order to ensure consistent
   * iteration.
   * <p>
   * This map primarily holds additional data related to the course structure, allowing for dynamic
   * attributes to be associated with courses. Keys are expected to be normalized and unique within
   * the scope of this map.
   * <p>
   * The map is final to ensure thread-safe access and to maintain data consistency, preventing
   * reassignment of the map reference.
   */
  private final Map<String, String> attributes = new LinkedHashMap<>();

  /**
   * A map that holds additional column data for a course structure. The keys and values in this map
   * represent arbitrary additional attributes that are not part of the predefined fields in the
   * course structure, allowing for extensibility.
   * <p>
   * The map is implemented as a {@code LinkedHashMap} to maintain the insertion order of keys. This
   * ensures that the order in which elements are added to the map is preserved during iteration.
   * <p>
   * This field is immutable, meaning its reference cannot be changed after initialization. However,
   * the contents of the map (keys and values) can be modified.
   */
  private final Map<String, String> additionalColumns = new LinkedHashMap<>();

  /**
   * The block element of the course structure.
   */
  @JsonProperty(value = KEY_BLOCK, required = true)
  private String block;

  /**
   * The member element of the course structure.
   */
  @JsonProperty(value = KEY_MEMBER, required = true)
  private String member;

  /**
   * Raw prerequisites expression associated with the member.
   */
  @JsonProperty(value = KEY_PREREQUISITES)
  private String prerequisites;

  /**
   * Raw attribute string (e.g., CA=, CL=, CR=, MT=, UR=, RT= pairs).
   */
  private String attributesRaw;

  public CourseStructure() {
    // no-op
  }

  public CourseStructure(String block, String member) {
    this.block = block;
    this.member = member;
  }

  /**
   * Counts the number of occurrences of the '=' character in a given string.
   *
   * @param value the string to be scanned for occurrences of the '=' character. If null or empty,
   * the method will return 0.
   * @return the number of times the '=' character appears in the provided string.
   */
  private static int countEquals(String value) {
    int count = 0;
    for (int i = 0; i < value.length(); i++) {
      if (value.charAt(i) == '=') {
        count++;
      }
    }
    return count;
  }

  /**
   * Normalizes a given key by trimming any leading or trailing whitespace and converting it to
   * uppercase using the ROOT locale.
   *
   * @param key the key to normalize; must not be null.
   * @return the normalized key as a trimmed and uppercase string.
   */
  private static String normalizeKey(String key) {
    return key
        .trim()
        .toUpperCase(Locale.ROOT);
  }

  /**
   * Retrieves the block associated with the course structure.
   *
   * @return the block for the course structure as a string. The returned value may be null if the
   * block has not been set.
   */
  public String getBlock() {
    return this.block;
  }

  /**
   * Sets the block associated with the course structure.
   *
   * @param block the block to set for the course structure. Can be null. Represents a specific
   * aspect or grouping within the course structure.
   */
  public void setBlock(String block) {
    this.block = block;
  }

  /**
   * Retrieves the member associated with the course structure.
   *
   * @return the member for the course structure as a string. The returned value may be null if the
   * member has not been set.
   */
  public String getMember() {
    return this.member;
  }

  /**
   * Sets the member associated with the course structure.
   *
   * @param member the member to set for the course structure. Can be null. Represents a specific
   * member or entity within the course structure.
   */
  public void setMember(String member) {
    this.member = member;
  }

  /**
   * Retrieves the prerequisites associated with the course structure.
   *
   * @return the prerequisites for the course structure as a string. The returned value may be null
   * if the prerequisites have not been set.
   */
  public String getPrerequisites() {
    return this.prerequisites;
  }

  /**
   * Sets the prerequisites for the course structure. If the provided string is blank, this method
   * will set the prerequisites to null.
   *
   * @param prerequisites the prerequisites information for the course. If blank, it will default to
   * null.
   */
  public void setPrerequisites(String prerequisites) {
    this.prerequisites = StringUtils.defaultIfBlank(prerequisites, null);
  }

  /**
   * Retrieves the raw attributes string associated with the course structure.
   *
   * @return the raw attributes as a string. The returned value may be null if the raw attributes
   * have not been set or initialized.
   */
  public String getAttributesRaw() {
    return this.attributesRaw;
  }

  /**
   * Adds or updates an additional column with the provided key-value pair. If the key matches
   * specific predefined keys (block, member, prerequisites, attributes), it delegates the value to
   * the respective setter method. Otherwise, the key-value pair is stored in the additional columns
   * map and also added as an attribute.
   *
   * @param key the key for the column. If blank, the method does not perform any operations.
   * @param value the value for the column. Can be null.
   */
  @JsonAnySetter
  public void setAdditionalColumn(String key, String value) {
    if (StringUtils.isBlank(key)) {
      return;
    }
    String normalizedKey = key
        .trim()
        .toLowerCase(Locale.ROOT);
    switch (normalizedKey) {
      case KEY_BLOCK -> {
        setBlock(value);
        return;
      }
      case KEY_MEMBER -> {
        setMember(value);
        return;
      }
      case KEY_PREREQUISITES -> {
        setPrerequisites(value);
        return;
      }
      case KEY_ATTRIBUTES -> {
        setAttributes(value);
        return;
      }
      default -> {
        // continue
      }
    }
    this.additionalColumns.put(key, value);
    putAttribute(key, value);
  }

  /**
   * Retrieves an immutable view of the attributes map associated with the course structure. The
   * attributes represent key-value pairs related to the course structure.
   *
   * @return an unmodifiable map of attribute key-value pairs. The map is never null.
   */
  @JsonIgnore
  public Map<String, String> getAttributes() {
    return Map.copyOf(this.attributes);
  }

  /**
   * Sets the attributes for the course structure from a raw string. If the provided string is
   * blank, the attributes will be set to null. Otherwise, the string is processed to initialize
   * individual attribute key-value pairs by parsing it.
   *
   * @param attributes a raw attribute string containing key-value pairs separated by semicolons
   * (';'). If blank, the attributes will be set to null instead of being processed.
   */
  @JsonProperty(value = KEY_ATTRIBUTES)
  public void setAttributes(String attributes) {
    this.attributesRaw = StringUtils.defaultIfBlank(attributes, null);
    if (this.attributesRaw != null) {
      parseAttributeString(this.attributesRaw);
    }
  }

  /**
   * Retrieves the value of the attribute associated with the specified key. If the key is null, the
   * method returns null.
   *
   * @param key the key of the attribute to retrieve; may be null. If the key is null, null will be
   * returned.
   * @return the value of the attribute associated with the normalized key, or null if the key is
   * null or not found in the attributes.
   */
  public String getAttribute(String key) {
    if (key == null) {
      return null;
    }
    return this.attributes.get(normalizeKey(key));
  }

  /**
   * Retrieves an immutable view of the additional columns map associated with the course structure.
   * The additional columns represent key-value pairs that provide extended information not covered
   * by predefined fields like block, member, or prerequisites.
   *
   * @return an unmodifiable map of additional columns as key-value pairs. The map is never null.
   */
  public Map<String, String> getAdditionalColumns() {
    return Map.copyOf(this.additionalColumns);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CourseStructure that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getBlock(), that.getBlock())
        .append(getMember(), that.getMember())
        .append(getPrerequisites(), that.getPrerequisites())
        .append(getAttributesRaw(), that.getAttributesRaw())
        .append(getAttributes(), that.getAttributes())
        .append(getAdditionalColumns(), that.getAdditionalColumns())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getBlock())
        .append(getMember())
        .append(getPrerequisites())
        .append(getAttributesRaw())
        .append(getAttributes())
        .append(getAdditionalColumns())
        .toHashCode();
  }

  /**
   * Parses a raw attribute string and delegates individual segments to the parseAttributeSegment
   * method. The raw string is expected to have key-value pairs separated by semicolons (';'). If
   * the string is blank or null, the method simply returns without processing.
   *
   * @param raw the raw attribute string containing semicolon-separated key-value pairs to be
   * parsed. If null or blank, no operations are performed.
   */
  private void parseAttributeString(String raw) {
    if (StringUtils.isBlank(raw)) {
      return;
    }
    String[] segments = raw.split(";", -1);
    for (String segment : segments) {
      parseAttributeSegment(segment);
    }
  }

  /**
   * Parses a single segment of an attribute string, handling key-value pairs and their variations.
   * The method supports parsing segments containing multiple comma-separated key-value pairs and
   * recursively processes them if necessary. It delegates the storage of key-value pairs to the
   * `putAttribute` method.
   * <p>
   * If the segment is null or blank, the method does not perform any operations. If the segment
   * contains an equal sign (`=`), it separates the segment into a key and a value. If no equal sign
   * is found, the segment is treated as a key with a null value. For segments with multiple
   * key-value pairs separated by commas, each part is processed individually by recursive calls.
   *
   * @param segment a string representing an attribute segment to be parsed. It can be in the form
   * of a key-value pair (e.g., "key=value"), a standalone key (e.g., "key"), or multiple
   * comma-separated key-value pairs (e.g., "key1=value1,key2=value2"). If null or blank, the
   * segment is ignored.
   */
  private void parseAttributeSegment(String segment) {
    String trimmed = segment == null ? null : segment.trim();
    if (StringUtils.isBlank(trimmed)) {
      return;
    }
    int equalsCount = countEquals(trimmed);
    if (equalsCount > 1 && trimmed.contains(",")) {
      String[] parts = trimmed.split(",");
      for (String part : parts) {
        parseAttributeSegment(part);
      }
      return;
    }
    int idx = trimmed.indexOf('=');
    if (idx < 0) {
      putAttribute(trimmed, null);
      return;
    }
    String key = trimmed.substring(0, idx);
    String value = trimmed.substring(idx + 1);
    putAttribute(key, value);
  }

  /**
   * Stores a key-value pair into the attributes map after normalizing the key and trimming the
   * value. The key is normalized to ensure consistent handling, while the value is trimmed to
   * remove leading and trailing whitespace. If the key is blank, the method does nothing.
   *
   * @param key the key to be stored, normalized using the `normalizeKey` method. Must not be null
   * or blank.
   * @param value the value to be stored, trimmed of leading and trailing spaces. Can be null.
   */
  private void putAttribute(String key, String value) {
    if (StringUtils.isBlank(key)) {
      return;
    }
    String normalizedKey = normalizeKey(key);
    String normalizedValue = value == null ? null : value.trim();
    this.attributes.put(normalizedKey, normalizedValue);
  }
}
