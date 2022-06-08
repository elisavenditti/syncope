package org.apache.syncope.core.persistence.jpa;
import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.persistence.api.entity.user.*;
import org.apache.syncope.core.persistence.jpa.entity.user.JPAUser;
import org.apache.syncope.core.spring.policy.DefaultPasswordRule;
import org.apache.syncope.core.spring.policy.PasswordPolicyException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;

@RunWith(Parameterized.class)
public class TestDefaultPasswordRule extends TestCase {



    @Parameters
    public static Collection<Object[]> data(){
        // Return a set (testedInstance, firstParam, expectedResult)

        User userNullPwd = new JPAUser();
        User userValidPwd = new JPAUser();
        User userInvalidPwd = new JPAUser();

        userNullPwd.setCipherAlgorithm(CipherAlgorithm.AES);
        userNullPwd.setUsername("nullPwdUser");
        userNullPwd.setPassword(null);              // --> PROBLEMA

        userValidPwd.setCipherAlgorithm(CipherAlgorithm.AES);
        userValidPwd.setUsername("validPwdUser");
        userValidPwd.setPassword("AAAAAAAAAA");

        userInvalidPwd.setCipherAlgorithm(CipherAlgorithm.AES);
        userInvalidPwd.setUsername("invalidPwdUser");
        userInvalidPwd.setPassword("AAAAAAAAA1");




        return Arrays.asList(new Object[][]{
                {null, -1}, {userNullPwd, -2}, {userValidPwd, 0}, {userInvalidPwd, -2}
        });
    }


    private final DefaultPasswordRule defaultPasswordRule; // tested object
    private final User firstParam;
    private final int expectedResultCode;

    public TestDefaultPasswordRule(User user, int expectedResultCode){

        // method patams configuration
        this.firstParam = user;
        this.expectedResultCode = expectedResultCode;


        // SUT configuration
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

    private boolean isValid(String password){
        boolean valid = true;
        DefaultPasswordRuleConf conf = (DefaultPasswordRuleConf) this.defaultPasswordRule.getConf();
        int max = conf.getMaxLength();
        int min = conf.getMinLength();
        int len = password.length();
        List<String> dontUse = conf.getWordsNotPermitted();

        if(len>max || len<min){
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
        else if(this.firstParam.getClearPassword() == null || isValid(this.firstParam.getClearPassword()) ){
            expectedCode = 0;
        }else{
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


        assertEquals(expectedCode, actualCode);

    }
}
