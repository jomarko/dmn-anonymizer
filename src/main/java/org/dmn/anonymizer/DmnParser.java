package org.dmn.anonymizer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class DmnParser {

    public static void main(String[] args) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {

            if (args.length != 1) {
                throw new IllegalArgumentException("You ned to provide just input file");
            }

            SAXParser saxParser = factory.newSAXParser();

            DmnSaxHandler handler = new DmnSaxHandler();
            saxParser.parse(new BufferedInputStream(new FileInputStream(args[0])), handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
