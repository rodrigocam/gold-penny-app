package com.code.red.playvendas;

import com.code.red.playvendas.utils.EscPosDriver.XmlParser.Document;
import com.code.red.playvendas.utils.EscPosDriver.XmlParser.Line;
import com.code.red.playvendas.utils.EscPosDriver.XmlParser.LineConverter;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void main(){
        File xmlFile = new File("/home/shammyz/Documents/test.xml");
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("document", Document.class);
        xStream.alias("line", Line.class);

        xStream.aliasAttribute(Line.class, "align", "align");
        xStream.aliasAttribute(Line.class, "font", "font");
        xStream.addImplicitCollection(Document.class, "lines");

        xStream.registerConverter(new LineConverter());
        try{
            InputStream in = new FileInputStream("/home/shammyz/Documents/test2.xml");
            Document doc = (Document) xStream.fromXML(in);
            System.out.println(doc.lines.get(0).getText());
            System.out.println(doc.lines.get(0).getAlign());
            System.out.println(doc.lines.get(0).getFont());

            System.out.println(doc.lines.get(1).getText());
            System.out.println(doc.lines.get(1).getAlign());
            System.out.println(doc.lines.get(1).getFont());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}