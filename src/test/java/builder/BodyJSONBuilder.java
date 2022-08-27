package builder;

import com.google.gson.Gson;

public class BodyJSONBuilder {

    public static <T extends Object> String getJSONContent(T dataObject){

        return new Gson().toJson(dataObject);
    }
}
