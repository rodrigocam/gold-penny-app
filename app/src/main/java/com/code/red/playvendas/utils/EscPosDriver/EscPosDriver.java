package com.code.red.playvendas.utils.EscPosDriver;

import com.code.red.playvendas.utils.EscPosDriver.XmlParser.Document;
import com.code.red.playvendas.utils.EscPosDriver.XmlParser.Parser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.function.IntUnaryOperator;

public class EscPosDriver {

    private ByteArrayOutputStream byteData;
    private XStream xStream;
    private Parser parser;

    public EscPosDriver(){
        this.byteData = new ByteArrayOutputStream();
        this.xStream = new XStream(new DomDriver());
        this.parser = new Parser();
    }

    public byte[] getData(InputStream xmlStream){
        write(xmlStream);
        return this.byteData.toByteArray();
    }

    private void write(InputStream xmlStream){
        Document doc = this.parser.unmarshall(xmlStream);

        //esc-pos logic
    }
}
