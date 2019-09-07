import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    /**
     * 创建Jwt
     */
    @Test
    public void testCreateJwt(){
        JwtBuilder builder= Jwts.builder()
                .setId("888")
                .setSubject("小白")
                .setIssuedAt(new Date())
             //   .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS256,"itheima");

        //自定义数据
        Map<String,Object>userinfo=new HashMap<String, Object>();
        userinfo.put("name","王五");
        userinfo.put("age",27);
        userinfo.put("address","黑马");
        builder.addClaims(userinfo);
        System.out.println(builder.compact());
    }

    /**
     * 解析Jwt令牌数据
     */
    @Test
    public void testParseJwt(){
        String compactJwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1NjU2MTY3MTEsImFkZHJlc3MiOiLpu5HpqawiLCJuYW1lIjoi546L5LqUIiwiYWdlIjoyN30.Z7E1sxLVYJrJEIcI0d7fE-jZuS4q_PkKXgUgX24T0_M";
        Claims claims=Jwts.parser().
                setSigningKey("itheima").
                parseClaimsJws(compactJwt).
                getBody();
        System.out.println(claims);
    }
}
