package org.eam.code.vmixapp.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DOMParser {


    public List<String> readXML(String xmlResponse) {
        List<String> cameraNames = new ArrayList<>();
        try {
            InputStream xmlInput = new ByteArrayInputStream(xmlResponse.getBytes());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlInput);
            doc.getDocumentElement().normalize();

            NodeList inputList = doc.getElementsByTagName("input");

            for (int i = 0; i < inputList.getLength(); i++) {
                Node node = inputList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String shortTitle = element.getAttribute("shortTitle");
//                    String title = element.getAttribute("title");
                    cameraNames.add(shortTitle);
//                    System.out.println("ShortTitle: " + shortTitle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cameraNames;
    }
}


