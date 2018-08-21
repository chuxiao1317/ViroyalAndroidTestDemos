package com.chuxiao.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chuxiao.networktest.entity.App;
import com.chuxiao.networktest.handler.ContentHandler;
import com.chuxiao.networktest.util.HttpCallbackListener;
import com.chuxiao.networktest.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequestBtn = findViewById(R.id.send_request_btn);
        responseText = findViewById(R.id.response_tv);
        sendRequestBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request_btn) {

            //发送HttpURLConnection网络请求
            //sendRequestWithHttpURLConnection();


            //发送OkHttp网络请求
//            sendRequestWithOkHttp();
//            Toast.makeText(this, "请用内网连接主机，并在控制台查看打印信息", Toast.LENGTH_SHORT).show();


            /**
             * 发送HttpURLConnection网络请求
             * */
//            HttpUtil.sendHttpRequest("http://www.baidu.com", new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                  //TODO:返回内容执行具体的逻辑
//                }
//
//                @Override
//                public void onError(Exception e) {
//                  //TODO：对异常情况进行处理
//                }
//            });


            /**
             * 最佳实践：发送OkHttp网络请求
             * */
            HttpUtil.sendOkHttpRequest("http://www.baidu.com", new okhttp3.Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    //处理异常
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到服务器返回的具体内容
                    String responseData = response.body().string();
                    //GSON解析JSON
                    parseJSONWithGSON(responseData);
                }
            });
        }
    }

    /**
     * 发送OkHttp网络请求
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
//                            .url("http://www.baidu.com")
//                            .url("http://192.168.2.16:8080/get_data.xml")
                            .url("http://192.168.2.16:8080/get_data.json")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();

//                    showResponse(responseData);

                    // Pull解析XML
//                    parseXMLWithPull(responseData);

                    // SAX解析XML
//                    parseXMLWithSAX(responseData);

                    //JSONObject解析JSON
                    parseJSONWithJSONObject(responseData);

                    //GSON解析JSON
                    parseJSONWithGSON(responseData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 使用GSON方式解析JSON
     */
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>() {
        }.getType());
        for (App app : appList) {
            Log.d("MainActivity", "id 是 " + app.getId());
            Log.d("MainActivity", "name 是 " + app.getName());
            Log.d("MainActivity", "version 是 " + app.getVersion());
        }
    }

    /**
     * 使用JSONObject方式解析JSON
     */
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d("MainActivity", "id is " + id);
                Log.d("MainActivity", "name is " + name);
                Log.d("MainActivity", "version is " + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用SAX方式解析XML
     */
    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            // 将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);
            //开始解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用Pull方式解析XML
     */
    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity", "id is " + id);
                            Log.d("MainActivity", "name is " + name);
                            Log.d("MainActivity", "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 发送HttpURLConnection网络请求
//     */
//    private void sendRequestWithHttpURLConnection() {
//        // 开启线程
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL("https://www.baidu.com");
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream inputStream = connection.getInputStream();
//                    // 对获取到的输入流进行读取
//                    reader = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    showResponse(response.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }
//            }
//        }).start();
//    }

    /**
     * 安卓不允许在子线程中进行UI操作，需要通过此方法将线程切回主线程才能进行UI更新
     */
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作
                responseText.setText(response);
            }
        });
    }

}
