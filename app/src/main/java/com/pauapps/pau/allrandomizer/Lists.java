package com.pauapps.pau.allrandomizer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Pau on 14/10/2018.
 */

public class Lists {

    protected String name = "list";
    public String title;
    public String item;
    public int max;

    public Lists(String title, String item) {
        this.title = title;
        this.item = item;
    }

    public Lists() {

    }

    public int getMax() {
        return max;
    }

    public void setMax() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;

            Document doc = builder.parse(new File("value.xml"));
            Node integer = doc.getFirstChild();
            Node max_lists = doc.getElementsByTagName("max_lists").item(0);

            this.max = Integer.parseInt(max_lists.getTextContent());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
