package utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class AuthenticationHandler {

    public static String encodedCredStr(String email, String token){
        String cred = email.concat(":").concat(token);
        byte[] encodeCred = Base64.encodeBase64(cred.getBytes(StandardCharsets.UTF_8));
        return new String(encodeCred);
    }
}
