package com.dell.passer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listintuc;
    InputStream inputStream;
    BaiViet baiViet;
    String noidung;
    List<BaiViet> dsbaiviet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dsbaiviet=new ArrayList<BaiViet>();
       listintuc = (ListView) findViewById(R.id.listintuc);
        Laydulieu();

        Adapter adapter=new Adapter(this,R.layout.custum_list,dsbaiviet);
        listintuc.setAdapter(adapter);

    }

    private void Laydulieu(){

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url=new URL("http://vietnamnet.vn/rss/phap-luat.rss");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                     httpURLConnection.connect();

                    inputStream= httpURLConnection.getInputStream();

                    XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser=factory.newPullParser();
                    xmlPullParser.setInput(inputStream,null);

                    int event =xmlPullParser.getEventType();
                    while (event!=XmlPullParser.END_DOCUMENT){
                        switch (event){
                            case XmlPullParser.START_TAG:
                                String themo= xmlPullParser.getName();
                                if (themo.equals("item")){
                                    baiViet=new BaiViet();
                                }

                                break;

                            case  XmlPullParser.TEXT:
                                noidung =xmlPullParser.getText();
                                break;

                            case XmlPullParser.END_TAG:
                                String thedong =xmlPullParser.getName();

                                if (thedong.equals("title") && baiViet != null){
                                    baiViet.setTitle(noidung);
                                }
                                else if (thedong.equals("description")&& baiViet != null){
                                    baiViet.setDescription(noidung);
                                }
                                else if (thedong.equals("link")&& baiViet != null){
                                    baiViet.setLink(noidung);
                                }
                                else if (thedong.equals("pubDate")&& baiViet != null){
                                    baiViet.setPubdate(noidung);
                                }
                                else if (thedong.equals("image")&& baiViet != null){
                                    baiViet.setImage(noidung);
                                }
                                else if (thedong.equals("item")&& baiViet != null){
                                    dsbaiviet.add(baiViet);
                                }
                                break;

                        }
                        event=xmlPullParser.next();
                    }
                    for (int i=0; i<dsbaiviet.size(); i++){
                        Log.d("dulieu",dsbaiviet.get(i).getTitle());
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
}
