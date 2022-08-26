package model;

import com.google.gson.Gson;

public class BuildJsonModel {

    public static String parseJSONString(PostBody postBody){
        if(postBody == null) {
            throw new IllegalArgumentException("PostBody can not be null");
        }
        return new Gson().toJson(postBody);
    }
}
