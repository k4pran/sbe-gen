package com.company;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        XMLReader reader = new XMLReader("/home/ryan/IdeaProjects/sbe-gen/example-schema.xml");

        Set<String> messageNames = reader.findMessageNames();

        for (String messageName : messageNames) {
            List<Field> fields = reader.findFixedLengthFields(messageName);
            fields.addAll(reader.findVarLengthFields(messageName));
            fields.addAll(reader.findRepeatedGroups(messageName));

            MessageContext messageContext = new MessageContext("com.company.pojo");
            messageContext.setBase("com.company.PojoBase");
            messageContext.addInterface("com.company.PojoInterface");

            Message message = new Message(messageName, fields, messageContext);

            PojoWriter pojoWriter = new PojoWriter(message, "/home/ryan/IdeaProjects/sbe-gen/src/com/company/pojo");
            pojoWriter.writePojo();

            SerdeWriter serdeWriter = new SerdeWriter(message, "/home/ryan/IdeaProjects/sbe-gen/src/com/company/pojo");
            serdeWriter.writeSerde();
        }
    }
}
