package wso2.org.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by minudika on 1/1/16.
 */
public class XMLParser {
    private String xmlDefinition  = null;
    public XMLParser(String xmlDefinition){
        this.xmlDefinition = xmlDefinition;
    }

    public String parse(){
        String str=null;
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            xpp.setInput(new StringReader(xmlDefinition));

            int eventType = 0;
            try {
                eventType = xpp.getEventType();
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            }
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.TEXT) {
                   str=xpp.getText();
                    break;
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return str;
    }

    public String[] getData(){
        String str=parse();

        return str.split(":");
    }

    }



