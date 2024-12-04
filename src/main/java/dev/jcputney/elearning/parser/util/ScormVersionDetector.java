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

package dev.jcputney.elearning.parser.util;

import static dev.jcputney.elearning.parser.enums.ModuleType.SCORM_12;
import static dev.jcputney.elearning.parser.enums.ModuleType.SCORM_2004;
import static dev.jcputney.elearning.parser.parsers.Scorm12Parser.MANIFEST_FILE;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScormVersionDetector {

  public static ModuleType detectScormVersion(FileAccess fileAccess) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(fileAccess.getFileContents(MANIFEST_FILE));

    // Check schema version
    Element schemaElement = (Element) document.getElementsByTagName("schema").item(0);
    Element versionElement = (Element) document.getElementsByTagName("schemaversion").item(0);
    if (schemaElement != null && versionElement != null) {
      String schema = schemaElement.getTextContent();
      String version = versionElement.getTextContent();

      if ("ADL SCORM".equalsIgnoreCase(schema)) {
        if ("1.2".equalsIgnoreCase(version)) {
          return SCORM_12;
        } else if (version.startsWith("2004")) {
          return SCORM_2004;
        }
      }
    }

    // Check for SCORM 2004-specific namespaces
    if (document.getDocumentElement().getAttribute("xmlns:adlcp").contains("adlcp_v1p3")) {
      return SCORM_2004;
    }

    // Default to SCORM 1.2 if uncertain
    return SCORM_12;
  }
}
