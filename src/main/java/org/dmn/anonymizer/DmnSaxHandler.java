package org.dmn.anonymizer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DmnSaxHandler extends DefaultHandler {

    private StringBuilder result = new StringBuilder();

    private boolean isDmnText = false;

    private Map<String, String> anonymizedValues = new HashMap<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        final StringBuilder attributesString = new StringBuilder();
        for (int i = 0; i < attributes.getLength(); i++) {

            final String attributeQName = attributes.getQName(i);
            final String attributeValue = attributes.getValue(i);

            attributesString
                    .append(" ")
                    .append(attributeQName)
                    .append("=\"");

            if (Objects.equals(attributeQName, "name") || Objects.equals(attributeQName, "locationURI")) {
                anonymizedValues.putIfAbsent(attributeValue, anonymizedValue(attributeValue));
                attributesString.append(anonymizedValues.get(attributeValue));
            } else if (Objects.equals(attributeQName, "typeRef")) {
                attributesString.append("Any");
            } else {
                attributesString.append(attributeValue);
            }

            attributesString.append("\"");
        }

        result
                .append("<")
                .append(qName)
                .append(attributesString.toString())
                .append(">");

        if (qName.contains("dmn:text")) {
            isDmnText = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        result
                .append("</")
                .append(qName)
                .append(">");

        if (qName.contains("dmn:text")) {
            isDmnText = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isDmnText) {
            final String oldValue = new String(ch).substring(start, start + length);
            anonymizedValues.putIfAbsent(oldValue, anonymizedValue(oldValue));
            result.append(anonymizedValues.get(oldValue));
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try (PrintWriter out = new PrintWriter(new FileWriter("anonym.dmn"))) {
            out.write(result.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String anonymizedValue(String value) {
        return "anonym_" + Math.abs(value.hashCode());
    }
}
