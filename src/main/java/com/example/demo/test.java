package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ludonglue
 * @date 2017/12/28
 */
public class test {
    public static void main(String[] args) {
        Map<String,List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("a");
        map.put("key",list);
        List<String> tmpList = map.get("key");
        tmpList.add("b");
        map.put("key",tmpList);
        System.out.println(map);
    }
}
