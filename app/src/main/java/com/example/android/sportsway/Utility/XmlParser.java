package com.example.android.sportsway.Utility;

import com.example.android.sportsway.Model.EventOnline;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlParser {
    private ArrayList<EventOnline> eventOnlines;
    private String data;

    public XmlParser(String text) {
        this.data = text;
        this.eventOnlines = new ArrayList<EventOnline>();
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public ArrayList<EventOnline> getEvents() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("event");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    EventOnline eventOnline = new EventOnline();
                    eventOnline.setTitle(getValue("title", element2));
                    eventOnline.setCity_name(getValue("city_name", element2));
                    eventOnline.setStart_time(getValue("start_time", element2));
                    eventOnlines.add(eventOnline);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventOnlines;
    }

}
