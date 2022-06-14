package org.apache.syncope.core.spring.policy;
import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.persistence.api.entity.user.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import junit.framework.TestCase;
import org.junit.Test;//.jupiter.api.Test;
import java.util.*;

@RunWith(Parameterized.class)
public class TestDefaultPasswordRule extends TestCase {



    @Parameters
    public static Collection<Object[]> data(){


        return Arrays.asList(new Object[][]{
                {"nullPwdUser", null}, {"nullPwdUser", null}, {"validPwdUser", "AAAAAAAAAA"},
                {"invalidPwdUser","AAAAAAAAA1"}, {null, "1234"}, {null, "AAAAAAAAAA"}, {"usernameAA", "usernameAA"},
                {"username", "passwordAA"},{"123456789AAA", null}, {null, null}, {"123456789AAA", "username"}
        });
    }


    private DefaultPasswordRule defaultPasswordRule; // tested object
    private User firstParam;

    public TestDefaultPasswordRule(String username, String password){

        // method params configuration
        User user = new UserImpl();
        user.setCipherAlgorithm(CipherAlgorithm.AES);
        user.setUsername(username);
        user.setPassword(password);
        this.firstParam = user;

        // SUT configuration
        configure();

    }

    private void configure(){

        DefaultPasswordRule rule = new DefaultPasswordRule();
        DefaultPasswordRuleConf conf = new DefaultPasswordRuleConf(); // password like: XXXXXXXXXY (X alpha numeric, Y not digit)
        conf.setMaxLength(10);
        conf.setMinLength(10);
        conf.setAlphanumericRequired(true);
        conf.setMustntEndWithDigit(true);
        conf.getWordsNotPermitted().add("password");
        rule.setConf(conf);
        this.defaultPasswordRule = rule;
    }


    private boolean isValid(String password, String username){
        boolean valid = true;
        DefaultPasswordRuleConf conf = (DefaultPasswordRuleConf) this.defaultPasswordRule.getConf();
        int max = conf.getMaxLength();
        int min = conf.getMinLength();
        int len = password.length();
        List<String> dontUse = conf.getWordsNotPermitted();
        if(password.equals(username) || len>max || len<min){
            valid = false;
            return valid;
        }
        for (String str: dontUse){
            if(password.contains(str)){
                valid = false;
                return valid;
            }
        }

        if(conf.isMustntEndWithDigit() && Character.isDigit(password.charAt(len-1))){
            valid = false;
            return valid;
        }
        if(conf.isAlphanumericRequired()){
            for(int i=0; i<len; i++){
                char c = password.charAt(i);
                if(!(Character.isAlphabetic(c) || Character.isDigit(c))){
                    valid = false;
                    return valid;
                }
            }
        }

        return valid;
    }

    @Test
    public void testEnforce(){

        int actualCode, expectedCode;

        // calcolo del valore di ritorno atteso
        if(this.firstParam == null)
            expectedCode = -1;
        else {
            String pwd = this.firstParam.getClearPassword();
            String username = this.firstParam.getUsername();

            if (pwd == null || isValid(pwd, username))
                expectedCode = 0;
            else
                expectedCode = -2;

        }

        try {
            this.defaultPasswordRule.enforce(this.firstParam);
            actualCode = 0;
        } catch (NullPointerException e){
            actualCode = -1;
        } catch (PasswordPolicyException e){
            actualCode = -2;
        }

        System.out.println(expectedCode);
        assertEquals(expectedCode, actualCode);

    }
}