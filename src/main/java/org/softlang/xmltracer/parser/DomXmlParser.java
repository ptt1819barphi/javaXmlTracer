package org.softlang.xmltracer.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.*;
import org.softlang.xmltracer.data.CollectionElement;
import org.softlang.xmltracer.data.Element;
import org.softlang.xmltracer.data.ObjectElement;
import org.softlang.xmltracer.data.PrimitiveElement;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DomXmlParser {

    private final ObjectElement rootObjectElement;

    /**
     * Constructor for the class DomXmlParser.
     *
     * @param file The File to be parsed.
     * @throws ParserConfigurationException If a DocumentBuilder cannot be created which satisfies the configuration requested in the constructor.
     * @throws SAXException If any parse errors occur.
     * @throws IOException If any IO errors occur.
     */
    public DomXmlParser(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        org.w3c.dom.Element root = docBuilder.parse(file).getDocumentElement();
        root.normalize();
        this.rootObjectElement = parseToObject(root);
    }

    /**
     * Constructor for the class DomXmlParser.
     *
     * @param path The path to the File to be parsed.
     * @throws ParserConfigurationException If a DocumentBuilder cannot be created which satisfies the configuration requested in the constructor.
     * @throws SAXException If any parse errors occur.
     * @throws IOException If any IO errors occur.
     */
    public DomXmlParser(String path) throws ParserConfigurationException, SAXException, IOException {
        this(new File(path));
    }

    /**
     * Method for returning the parsed ObjectElement.
     * @return The parsed ObjectElement.
     */
    public ObjectElement getParseResult() {
        return rootObjectElement;
    }
    
    /**
     * Helper method for helper methods.
     *
     * @param node The DomXml Node to be parsed.
     * @return The DomXml Node parsed to the corresponding Element type.
     */
    private Element parseToElement(Node node) {
        if (hasSubnodes(node)) {
            return parseToObject(node);
        } else {
            return parseToPrimitive(node);
        }
    }

    /**
     * Helper method to check if a given Node has Subnodes (Children).
     *
     * @param node The Node to be checked.
     * @return True if the given Node has Subnoded, else false.
     */
    private boolean hasSubnodes(Node node) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper method for parsing to a PrimitiveElement.
     *
     * @param node The DomXml Node to be parsed.
     * @return The parsed PrimitiveElement.
     */
    private PrimitiveElement parseToPrimitive(Node node) {
        return new PrimitiveElement(node.getTextContent());
    }

    /**
     * Helper method for parsing to an ObjectElement.
     *
     * @param node The DomXml Node to be parsed.
     * @return The prased ObjectElement.
     */
    private ObjectElement parseToObject(Node node) {
        Map<String, List<Element>> tempMap = new HashMap<>();
        Map<String, Element> objectMap = new HashMap<>();

        NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);

            if (tempNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            tempMap.computeIfAbsent(tempNode.getNodeName(), k -> new ArrayList<>()).add(parseToElement(tempNode));
        }

        for (Map.Entry<String, List<Element>> entry : tempMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                objectMap.put(entry.getKey(), new CollectionElement(entry.getValue()));
            } else {
                objectMap.put(entry.getKey(), entry.getValue().get(0));
            }
        }

        return new ObjectElement(objectMap);
    }
}
