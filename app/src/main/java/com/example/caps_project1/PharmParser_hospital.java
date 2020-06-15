package com.example.caps_project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class PharmParser_hospital extends AppCompatActivity{
    final String TAG = "MapActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<PharmDTO_object> arrayList = xml_parse();
        Intent intent = new Intent(PharmParser_hospital.this, MapActivity.class);
        intent.putExtra("PharmDTO_object", arrayList);
        startActivity(intent);
    }

    private ArrayList<PharmDTO_object> xml_parse() {
        ArrayList<PharmDTO_object> hospitals = new ArrayList<PharmDTO_object>();
        InputStream inputStream = getResources().openRawResource(R.raw.hospital);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        XmlPullParserFactory xmlPullParserFactory = null;
        XmlPullParser xmlPullParser = null;

        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(bufferedReader);

            PharmDTO_object pharmDTO_object = null;
            int eventType = xmlPullParser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml start");
                        break;
                    case XmlPullParser.START_TAG:
                        String startTag = xmlPullParser.getName();
                        Log.i(TAG, "Start TAG : " + startTag);
                        if(startTag.equals("row")) {
                            pharmDTO_object = new PharmDTO_object();
                            Log.d(TAG, "DTO 추가");
                        }
                        //시군명
                        else if(startTag.equals("SIGUN_NM")) {
                            pharmDTO_object.setLocality(xmlPullParser.nextText());
                        }
                        //사업장명
                        else if(startTag.equals("BIZPLC_NM") || startTag.equals("ENTRPS_NM")) {
                            pharmDTO_object.setName(xmlPullParser.nextText());
                        }
                        //영업상태명
                        else if(startTag.equals("BSN_STATE_NM")) {
                            pharmDTO_object.setState(xmlPullParser.nextText());
                        }
                        //전화번호
                        else if(startTag.equals("LOCPLC_FACLT_TELNO_DTLS") || startTag.equals("TELNO") || startTag.equals("ENTRPS_TELNO")) {
                            pharmDTO_object.setNumber(xmlPullParser.nextText());
                        }
                        //도로명주소
                        else if(startTag.equals("REFINE_ROADNM_ADDR")) {
                            pharmDTO_object.setAddress(xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xmlPullParser.getName();
                        Log.i(TAG, "End TAG : " + endTag);
                        if(endTag.equals("row")) {
                            hospitals.add(pharmDTO_object);
                        }
                        break;
                }
                try {
                    eventType = xmlPullParser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return hospitals;
    }
}