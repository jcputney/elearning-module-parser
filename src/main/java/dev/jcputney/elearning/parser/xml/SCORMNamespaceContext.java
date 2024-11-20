/*
 * Copyright (c) 2024. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.xml;

import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

/**
 * Namespace context for SCORM 2004 XML parsing, specifically for handling SCORM's imsss namespace
 * in XPath expressions.
 * <p>
 * This class enables XPath expressions in {@link Scorm2004Parser} to accurately locate elements
 * within the imsss namespace, such as sequencing and navigation rules.
 * </p>
 */
public class SCORMNamespaceContext implements NamespaceContext {

  /**
   * Returns the namespace URI for the given prefix.
   *
   * @param prefix The namespace prefix (e.g., "imsss").
   * @return The associated namespace URI, or null if the prefix is not recognized.
   */
  @Override
  public String getNamespaceURI(String prefix) {
    if ("imsss".equals(prefix)) {
      return "http://www.imsglobal.org/xsd/imsss";
    }
    return null;
  }

  /**
   * Returns the prefix for the given namespace URI.
   *
   * @param namespaceURI The namespace URI to look up.
   * @return Null as this implementation does not use reverse prefix lookup.
   */
  @Override
  public String getPrefix(String namespaceURI) {
    return null; // Not required for this implementation
  }

  /**
   * Returns an iterator for all possible prefixes for a given namespace URI.
   *
   * @param namespaceURI The namespace URI to look up.
   * @return Null as this implementation does not use reverse prefix lookup.
   */
  @Override
  public Iterator<String> getPrefixes(String namespaceURI) {
    return null; // Not required for this implementation
  }
}
