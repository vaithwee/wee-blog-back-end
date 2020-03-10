package xyz.vaith.weeblogbackend.authority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;

import java.util.Date;

public class AuthorityJWTUtil {
    private static final String PICEA_JWT_KEY = "123adf123123faewfwe";

    public static String createToken(String username, int valid) {

        if (valid == 0) {
            valid =  30 * 60 * 1000;
        }

        return JWT
                .create()
                .withAudience(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + valid))
                .sign(Algorithm.HMAC256(PICEA_JWT_KEY));
    }

    public static boolean valid(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(PICEA_JWT_KEY)).build();
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsername(String token) {
      return JWT.decode(token).getAudience().get(0);
    }
}
