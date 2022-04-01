package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XMLReader {

    private static final String MESSAGE_TAG = "message";
    private static final String MESSAGE_NAME_ATTRIBUTE = "name";
    private static final String MESSAGE_TYPE_ATTRIBUTE = "type";

    private static final String FIELD_XPATH = "//message[@name=\"%s\"]/field";
    private static final String FIELD_RELATIVE_XPATH = "/field";
    private static final String DATA_XPATH = "//message[@name=\"%s\"]/data";
    private static final String DATA_RELATIVE_XPATH = "/data";
    private static final String GROUP_XPATH = "//message[@name=\"%s\"]/group";
    private static final String GROUP_RELATIVE_XPATH = "/group";

    private XPath xPath;

    private Document document;

    public XMLReader(String location) throws IOException, ParserConfigurationException, SAXException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        this.xPath = xPathfactory.newXPath();

        readXml(location);
    }

    private void readXml(String location) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(location);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        this.document = db.parse(file);
    }

    public Set<String> findMessageNames()  {
        Set<String> messageNames = new HashSet<>();
        NodeList messages = document.getElementsByTagName(MESSAGE_TAG);
        for (int i = 0; i < messages.getLength(); i++) {
            String name = getAttributeValue(messages.item(i), MESSAGE_NAME_ATTRIBUTE);
            messageNames.add(name);
        }
        return messageNames;
    }

    private List<Field> extractDetail(NodeList nodes, boolean isVarLength) {
        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String fieldName = getAttributeValue(node, MESSAGE_NAME_ATTRIBUTE);
            String fieldType = getAttributeValue(node, MESSAGE_TYPE_ATTRIBUTE);
            fields.add(new Field(fieldName, asFieldType(fieldType), isVarLength));
        }
        return fields;
    }

    public List<Field> findFixedLengthFields(String messageName) throws XPathExpressionException {
        XPathExpression expr = xPath.compile(String.format(FIELD_XPATH, messageName));
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        return extractDetail(nodes, false);
    }

    public List<Field> findVarLengthFields(String messageName) throws XPathExpressionException {
        XPathExpression expr = xPath.compile(String.format(DATA_XPATH, messageName));
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        return extractDetail(nodes, true);
    }

    public List<Field> findRepeatedGroups(String messageName) throws XPathExpressionException {
        List<Field> fields = new ArrayList<>();
        XPathExpression expr = xPath.compile(String.format(GROUP_XPATH, messageName));

        NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            String fieldName = getAttributeValue(node, MESSAGE_NAME_ATTRIBUTE);
            Field group = new Field(fieldName, FieldType.GROUP, true);
            group.addChildren(handleRepeatingGroups(node));
            fields.add(group);
        }

        return fields;
    }

    private List<Field> handleRepeatingGroups(Node node) throws XPathExpressionException {
        XPathExpression fieldExpr = xPath.compile(FIELD_RELATIVE_XPATH);
        NodeList fieldNodes = (NodeList) fieldExpr.evaluate(node, XPathConstants.NODESET);
        List<Field> fields = new ArrayList<>(extractDetail(fieldNodes, false));

        XPathExpression dataExpr = xPath.compile(DATA_RELATIVE_XPATH);
        NodeList dataNodes = (NodeList) dataExpr.evaluate(node, XPathConstants.NODESET);
        fields.addAll(extractDetail(dataNodes, true));

        return fields;
    }

    public String getAttributeValue(Node node, String attribute) {
        return node.getAttributes().getNamedItem(attribute).getNodeValue();
    }

    private FieldType asFieldType(String fieldTypeRep) {


        switch (fieldTypeRep) {

            case "uint8":
            case "uint16":
            case "uint32":
            case "someNumbers":
            case "discountedModel":
                return FieldType.INT;

            case "uint64":
                return FieldType.LONG;

            case "BooleanType":
                return FieldType.BOOL;

            case "varStringEncoding":
            case "varAsciiEncoding":
            case "VehicleCode":
            case "Model":
            case "OptionalExtras":
            case "Engine":
                return FieldType.STRING;

            default:
                return FieldType.findByRepr(fieldTypeRep);
        }

    }
}
