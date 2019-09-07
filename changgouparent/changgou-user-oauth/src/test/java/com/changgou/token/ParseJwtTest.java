package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: shenkunlin
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoib2F1dGgiLCJpZCI6IjEiLCJhdXRob3JpdGllcyI6WyJhZG1pbiJdfQ.JAHjFQwGjUIqSkK_pJoYInl4BMcyCNfJIMKp-YzXFbJHyBzfFkZtU_FcGKZ1_uxxbH2sg0F5RMvZya5rGPU1r-aFHVxbsWUGuCV6yrPXLUmnWAvpeSEMlC-2F-fdYpBYL51iKhw1iAx0JC2wh5ulv0XShfpewXj2uj7p9-7HOoT2ERyQngTEfju10N4ojpORSIoEzq-wrQOctGmM5MxlRffOL52Mr5PT49LgAu_dYtK8u3pW6xa5r0_GV45Cy65j84ygzEdSF_rkv-tnKTUn76lqpsFtqmwYNRMGdRG8yN5s09xn4BuJAHHB7X6lsvswiXU5h7J0giFz-U1G8HvfVA";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApoc/NH3Am82JeCk199geuyiehUZ4/LMEGUH+IWNKlyvx3s+PAlV8CW5srrKY+aZXN2PWKH6hVY9wqtAKVBT7TtljEKtio6DOiJAqCAPNrSFrc9HTpY3Ltp7roI3zAG1D7hAYNqHWm+8BPxGoLq6u2N96rYn6zRymT8GlHtH2avS48T+yMT/y77Th+9hyZmj3CW52oRlahfrNo93yFf6HSq56YRFbMvYPkZpTiSiqaiyxobZgYEZ3K+iVnFNFIHXA5yPimaojccWJP54OClXPgFVptBJKBtu5CJcGlVxOv7yFQhqVSBoD1D2XSK86U41RDwKNlhdqrEyGRkPInhWCGQIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
  //      System.out.println(encoded);
    }
}
