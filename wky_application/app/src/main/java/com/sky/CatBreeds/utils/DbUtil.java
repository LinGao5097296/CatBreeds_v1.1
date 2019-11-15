package com.sky.CatBreeds.utils;


import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sky.CatBreeds.database.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DbUtil {
    private Connection connection;

    //private String key="25f57c90f7bb1a6ae66c050f554915c7";
    private String key="a748acaf-39f9-4286-923d-8b12170ac1cb";
    //加载首页图书目录信息
    public List<Model> QueryModule()
    {
        List<Model> modelList=new ArrayList<Model>();
        Model model;
        model=new Model(0,"全部");
        modelList.add(model);
        HttpURLConnection connection = null;
        try {
            URL url=new URL("https://api.thecatapi.com/v1/breeds"+ "?key="+key);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            // 下面对获取到的输入流进行读取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("response=" + response.toString());
            String result=response.toString();
            JSONObject object = new JSONObject(result);
            String json_result = object.getString("result");
            JsonArray json = new JsonParser().parse(json_result).getAsJsonArray();

            for(int i =0;i<json.size();i++){

                JsonObject obj = json.get(i).getAsJsonObject(); //obj 为JSONObject数据
                Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+obj.get("id").getAsInt());
                Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+obj.get("catalog").getAsString());
                model=new Model(obj.get("id").getAsInt(),obj.get("catalog").getAsString());
                modelList.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelList;
    }
    //加载图书类别
    public List<Model> QueryContent(String catalog)
    {
        List<Model> modelList=new ArrayList<Model>();
        Model model;
        HttpURLConnection connection = null;
        try {
            URL url=new URL("https://api.thecatapi.com/v1/breeds"+ "?key="+key);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            // 下面对获取到的输入流进行读取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("response=" + response.toString());
            String result=response.toString();

            JSONArray jsonArray = new JSONArray(result);

            for(int i =0;i<jsonArray.length();i++){

                JSONObject obj = jsonArray.getJSONObject(i);

                    Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + obj.getString("name"));
                    Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + obj.getString("origin"));
                    model = new Model(i + 1, obj.getString("name"), obj.getString("origin"), obj.getString("description"),
                            obj.getString("vetstreet_url"), obj.getString("life_span"), obj.getString("id"));
                    modelList.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelList;
    }
}

