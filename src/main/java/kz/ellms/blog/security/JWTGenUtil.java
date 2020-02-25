package kz.ellms.blog.security;

class JWTGenUtil {
    static final String HEADER = "Authorization";
    static final String BEARER = "Bearer ";
    static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    static final String SECRET = "$3cr3tk3y"; //sample secret key

    private JWTGenUtil(){}
}
