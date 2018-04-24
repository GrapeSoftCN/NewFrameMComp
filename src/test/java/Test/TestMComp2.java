package Test;

import common.java.security.codec;

public class TestMComp2 {
    public static void main(String[] args) {
        String encodeFastJSON = codec.encodeFastJSON("{\"companyName\":\"test123\",\"companyDesp\":\"qwe124\",\"companyURL\":\"asgdasg\",\"companyTele\":\"12345678\",\"companyMob\":\"13515626325\",\"companyEmail\":\"13515626325@163.com\",\"companyQQ\":\"222222\",\"companyPerson\":\"456546\"}");
        System.out.println(encodeFastJSON);
    }
}
