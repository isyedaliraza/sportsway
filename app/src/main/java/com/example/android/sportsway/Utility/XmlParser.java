package com.example.android.sportsway.Utility;

import com.example.android.sportsway.Model.Event;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Syed Ali Raza on 12/2/2017.
 */

public class XmlParser {
    private ArrayList<Event> events;
    private String data;

    public XmlParser(String text) {
        this.data = text;
        this.events = new ArrayList<Event>();
    }

    public ArrayList<Event> getEvents() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("event");

            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Event event = new Event();
                    event.setTitle(getValue("title", element2));
                    event.setCity_name(getValue("city_name", element2));
                    event.setStart_time(getValue("start_time", element2));
                    events.add(event);
                }
            }

        } catch (Exception e) {e.printStackTrace();}

        return events;
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}
