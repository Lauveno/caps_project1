package com.example.caps_project1;

import java.lang.System;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;




//이 json Parser는 공공 데이터를 요청하고 나온 값을 파싱
public class PharmParser_object{

    ArrayList<PharmDTO_object> PharmDTO_Objects = new ArrayList<PharmDTO_object>();

    private String CurrentLocation;
    private String Place;

    //StrinBuffer로 받아온 데이터를 문자열로 변경 후 json 파싱 필요
    private String NeedParsing;

    private JSONObject jsonObj;

    public PharmParser_object(String place, String current)
    {
        this.Place = place;
        this.CurrentLocation = current;

    }
    //Animalhosptl, 용인시

    public void RequestData()
    {

        String url = String.format("https://openapi.gg.go.kr/%s?SIGUN_NM=%s&Type=json",
                this.Place, this.CurrentLocation);
        URL TargetUrl = null;
        System.out.print(url);
        try{
            TargetUrl = new URL(url);
        } catch(MalformedURLException ex){
            throw new Error("I cannot deal with this problem");
        }

        //HttpURLConnection con = null;

        try{
            HttpURLConnection con = (HttpURLConnection)TargetUrl.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine).append("\n");
            }
            in.close();

            //StrinBuffer로 받아온 데이터를 문자열로 변경 후 json 파싱 필요
            this.NeedParsing = response.toString();

        } catch(IOException ex){
            throw new Error("I cannot deal with this problem");
        }
    }

    public ArrayList<PharmDTO_object> FileOpen()
    {
        try{
            StringBuffer content = new StringBuffer();

            /////////////////////// 아래 경로는 안드로이드 경로로
            String path = String.format("/app/src/main/assets/%s.json", this.Place);
            File file = new File(path);
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            while((line = bufReader.readLine()) != null){
                content.append(line);
            }
            this.NeedParsing = content.toString();
            bufReader.close();
            //System.out.print(this.NeedParsing);
        } catch(Exception ex){
            throw new Error("I cannot deal with this problem");
        }
        //내용 Parsing
        this.json_parser();
        return this.PharmDTO_Objects;
    }

    public void json_parser()
    {
        String[] PlaceLocal = {"SIGUN_NM"};
        String[] PlaceName = {"BIZPLC_NM", "ENTRPS_NM"};
        String[] PlaceTelno = {"ENTRPS_TELNO", "LOCPLC_FACLT_TELNO_DTLS", "TELNO", "ENTRPS_TELNO", "LOCPLC_FACLT_TELNO"};
        String[] PlaceAddr = {"REFINE_ROADNM_ADDR"};
        String[] PlaceLat = {"REFINE_WGS84_LAT"};
        String[] PlaceLogt = {"REFINE_WGS84_LOGT"};

        ArrayList<String[]> Buffer = new ArrayList<>();
        Buffer.add(PlaceLocal);
        Buffer.add(PlaceName);
        Buffer.add(PlaceTelno);
        Buffer.add(PlaceAddr);
        Buffer.add(PlaceLat);
        Buffer.add(PlaceLogt);

        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(this.NeedParsing);
            jsonObj = (JSONObject) obj;
            //jsonObj = new JSONObject(this.NeedParsing);
            JSONArray jArray = (JSONArray)jsonObj.get(this.Place);
            JSONObject row = (JSONObject)jArray.get(1);


            JSONArray obj2 = (JSONArray)row.get("row");
            Iterator i = obj2.iterator();

            ArrayList<String> str_buffer = new ArrayList<>();


            while(i.hasNext()){
                JSONObject result = (JSONObject)i.next();

                for(String[] item : Buffer){
                    for(String a : item){
                        if(result.containsKey(a)){
                            String test = this.CheckNull(result.get(a));
                            str_buffer.add(test);
                        } else{
                            continue;
                        }
                    }
                }

                PharmDTO_object model = new PharmDTO_object(
                        str_buffer.get(0),
                        str_buffer.get(1),
                        str_buffer.get(2),
                        str_buffer.get(3),
                        Float.parseFloat(str_buffer.get(4)),
                        Float.parseFloat(str_buffer.get(5))
                );
                this.PharmDTO_Objects.add(model);
            }

        } catch(Exception ex){
            throw new Error(ex.toString());
        }
    }

    public String CheckNull(Object str)
    {
        return (str != null) ? str.toString() : "알 수 없음";
    }
}