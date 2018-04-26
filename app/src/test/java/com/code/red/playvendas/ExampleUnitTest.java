package com.code.red.playvendas;

import android.content.Context;

import com.code.red.playvendas.utils.EscPosDriver.EscPosDriver;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

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
        EscPosDriver driver = new EscPosDriver();
        //try {
            //InputStream xmlFile = new FileInputStream("");
            //Context ctx
            //InputStream xmlFile = ctx.getResources().openRawResource(R.raw.template);
            //String path = getClass().getClassLoader().getResource(".").getPath();
            //System.out.println(path);
            //byte[] data = driver.xmlToEsc(xmlFile);
            //for(byte b: data){
                //System.out.print(b);
                //System.out.print(",");
            //}
        //}catch(IOException e){
            //e.printStackTrace();
        //}
    }
}