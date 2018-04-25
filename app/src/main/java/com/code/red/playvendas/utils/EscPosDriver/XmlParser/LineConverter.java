package com.code.red.playvendas.utils.EscPosDriver.XmlParser;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class LineConverter implements Converter{
    public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
        Line line = (Line) value;
        writer.addAttribute("align", line.getAlign());
        writer.addAttribute("font", line.getFont());
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Line line = new Line();
        if(reader.hasMoreChildren()){
            reader.moveDown();
            line.setText(reader.getValue());
            reader.moveUp();
        }else{
            line.setText(null);
        }

        line.setAlign(reader.getAttribute("align"));
        line.setFont(reader.getAttribute("font"));
        return line;
    }

    public boolean canConvert(Class clazz) {
        return clazz.equals(Line.class);
    }
}
