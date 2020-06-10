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

public class PharmParser_hospital extends AppCompatActivity {
    final String TAG = "Map Activity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<PharmDTO_hospital> arrayList = xml_parse();
        Intent intent = new Intent(PharmParser_hospital.this, MapActivity.class);
        intent.putExtra("PharmDTO_hospital", arrayList);
        startActivity(intent);
    }

    public ArrayList<PharmDTO_hospital> xml_parse() {
        ArrayList<PharmDTO_hospital> arrayList = new ArrayList<PharmDTO_hospital>();
        InputStream inputStream = getResources().openRawResource(R.raw.hospital);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        XmlPullParser xmlPullParser = null;
        XmlPullParserFactory xmlPullParserFactory = null;

        try {
            xmlPullParserFactory = xmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(bufferedReader);

            PharmDTO_hospital pharmDTO_hospital = null;
            int event_type = xmlPullParser.getEventType();

            while(event_type != XmlPullParser.END_DOCUMENT) {
                switch (event_type) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml Start");
                        break;
                    case XmlPullParser.START_TAG:
                        String start_tag = xmlPullParser.getName();
                        Log.i(TAG,"start tag : " + start_tag);
                        if(start_tag.equals("Row")) {
                            pharmDTO_hospital = new PharmDTO_hospital();
                            Log.d(TAG, "data 추가");
                        }
                        else if(start_tag.equals("SIGUN_NM")) {
                            pharmDTO_hospital.setLocality(xmlPullParser.nextText());
                            Log.d(TAG, "시군명");
                        }
                        else if(start_tag.equals("BLZPLC_NM")) {
                            pharmDTO_hospital.setName(xmlPullParser.nextText());
                            Log.d(TAG, "사업장명");
                        }
                        else if(start_tag.equals("BSN_STATE_NM")) {
                            pharmDTO_hospital.setState(xmlPullParser.nextText());
                            Log.d(TAG, "영업상태");
                        }
                        else if(start_tag.equals("LOCPLC_FACLT_TELNO_DTLS")) {
                            pharmDTO_hospital.setNumber(xmlPullParser.nextText());
                            Log.d(TAG, "소재지시설전화번호");
                        }
                        else if(start_tag.equals("REFINE_ROADNM_ADDR")) {
                            pharmDTO_hospital.setAddress(xmlPullParser.nextText());
                            Log.d(TAG, "도로명주소");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String end_tag = xmlPullParser.getName();
                        Log.i(TAG, "end tag : " + end_tag);
                        if(end_tag.equals("row")) {
                            arrayList.add(pharmDTO_hospital);
                        }
                        break;
                }
                try {
                    event_type = xmlPullParser.next();
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
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
