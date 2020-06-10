package com.example.caps_project1;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class PharmParser_hospital {

    public final static String PHARM_URL = "https://openapi.gg.go.kr/Animalhosptl";
    public final static String SIGUM_NM = "용인시";

    public PharmParser_hospital() {
        try {
            apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PharmDTO_hospital> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        xmlPullParserFactory.setNamespaceAware(true);
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        xmlPullParser.setInput(bufferedInputStream, "utf-8");

        String tag = null;
        int event_type = xmlPullParser.getEventType();

        ArrayList<PharmDTO_hospital> list = new ArrayList<PharmDTO_hospital>();

        String locality = null, name = null;
        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xmlPullParser.getName();
            } else if (event_type == xmlPullParser.TEXT) {
                //병원 주소
                if (tag.equals("SIGUN_NM")) {
                    locality = xmlPullParser.getText();
                    System.out.println(SIGUM_NM);
                } else if (tag.equals("BLZPLC_NM")) {
                    name = xmlPullParser.getText();
                }
            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xmlPullParser.getName();
                if (tag.equals("row")) {
                    PharmDTO_hospital entity = new PharmDTO_hospital();
                    entity.setLocality(String.valueOf(locality));
                    entity.setName(name);

                    list.add(entity);
                }
            }
            event_type = xmlPullParser.next();
        }
        System.out.println(list.size());

        return list;
    }

    private String getURLParam(String search) {
        String url = PHARM_URL+"?SIGUN_NM="+SIGUM_NM;
        if(search != null) {
            url = url+"&BLZPLC_NM"+search;
        }
        return url;
    }
}
