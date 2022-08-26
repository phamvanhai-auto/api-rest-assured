package model;

import io.restassured.http.Header;

import java.util.function.Function;

public interface RequestCapability {
    Header defaultHeader = new Header("Content-type", "application/json; charset=UTF-8");

    static Header getAuthenticatedHeader(String encodedCredStr){
        if(encodedCredStr == null){
            throw new  IllegalArgumentException("[Err] encodedCreStr can not be null");
        }
        return new Header("Authorization", "Basic " + encodedCredStr);
    }

    Function<String, Header> getAuthenticatedHeader = encodedCredStr -> {
        if(encodedCredStr == null){
            throw new  IllegalArgumentException("[Err] encodedCreStr can not be null");
        }
        return new Header("Authorization", "Basic " + encodedCredStr);
    };
}
