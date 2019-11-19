package com.highgreat.education.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：序列化map供Bundle传递map使用
 * 创建人：mac-likh
 * 创建时间：16/2/22 13:56
 * 修改人：mac-likh
 * 修改时间：16/2/22 13:56
 * 修改备注：
 */
public class SerializableList implements Serializable {

    /* private LinkedHashMap<Integer,Object> map;

     public LinkedHashMap<Integer, Object> getMap() {
         return map;
     }

     public void setMap(LinkedHashMap<Integer, Object> map) {
         this.map = map;
     }*/
    private List<String> list;

    public List<String> getPicList() {
        return list;
    }

    public void setPicList(List<String> list) {
        this.list = list;
    }
}

