package com.example.demo.websocket;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class dataee {


    public static String data(String pho) {
        String phoes = "";
        String  aa =pho.substring( pho.indexOf("{") + 1, pho.lastIndexOf("}"));
        String  bb ="{"+aa+"}";
        Map maps = (Map) JSON.parse(bb);

        if(maps.get("code").equals("M0")){
            phoes = String.valueOf(maps.get("numArray"));
        }
        return phoes;
    }
}
