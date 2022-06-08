package org.apache.syncope.core.spring.security;

import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.spring.policy.DefaultPasswordRule;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDummy {

    @Test
    public void testUltimaSperanza() throws UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        Encryptor encryptor = Encryptor.getInstance("abc");
        String s = encryptor.encode("ciao", CipherAlgorithm.AES);
        System.out.println(s);
        assertEquals(s,s);

        //probabilmente devo creare un mock
//        User user = new JPAUser();
//        user.setPassword("ciaoamico");
//        System.out.println(user.getClearPassword());

        DefaultPasswordRule rule = new DefaultPasswordRule();
        DefaultPasswordRuleConf conf = new DefaultPasswordRuleConf();


//        conf.setDigitRequired(false);
//        conf.setLowercaseRequired(false);
        conf.setMaxLength(10);
        conf.setMinLength(10);
//        conf.setMustEndWithDigit(false);
//        conf.setMustEndWithNonAlpha(false);
//        conf.setMustStartWithDigit(false);
//        conf.setMustStartWithNonAlpha(false);
        conf.setAlphanumericRequired(true);
        conf.setMustntEndWithDigit(true);
        conf.getWordsNotPermitted().add("password");
//        conf.setMustntEndWithNonAlpha(false);
//        conf.setMustntStartWithDigit(false);
//        conf.setMustntStartWithNonAlpha(false);
//        conf.setNonAlphanumericRequired(false);
//        conf.setUppercaseRequired(false);
//        conf.setMustStartWithAlpha(false);
//        conf.setMustntEndWithAlpha(false);
//        conf.setMustntStartWithAlpha(false);
        rule.setConf(conf);

        System.out.println(conf.getWordsNotPermitted().get(0));
//        AuthDataAccessor authDataAccessor = new AuthDataAccessor();

    }
}
