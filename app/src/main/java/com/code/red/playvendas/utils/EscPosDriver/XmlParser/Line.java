package com.code.red.playvendas.utils.EscPosDriver.XmlParser;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Line {

    private String align;

    private String font;

    private String text;

    public String getAlign(){
        return this.align;
    }

    public String getFont(){
        return this.font;
    }

    public String getText(){
        return this.text;
    }

    public void setAlign(String align){
        this.align = align;
    }

    public void setFont(String font){
        this.font = font;
    }

    public void setText(String text){
        this.text = text;
    }

    @Override
    public String toString(){
        return this.align;
    }
}
