package org.apache.syncope.core.spring.policy;
import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.persistence.api.entity.user.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;

import static org.apache.syncope.core.spring.policy.TestDefaultPasswordRule.isValid;

@RunWith(Parameterized.class)
public class TestDefaultPasswordRule2 extends TestCase {



    @Parameters
    public static Collection<Object[]> data(){


        return Arrays.asList(new Object[][]{

                // Terza iterazione: badua
                {"username","AAAAAAAAAA", true, false}, // DEVE ANDARE A BUON FINE
                {"username","AAAAAAAAA%", true, false}, // NON DEVE ANDARE A BUON FINE
                {"username","AAAAAAAAAA", false, true}, // NON DEVE ANDARE A BUON FINE
                {"username","AAAAAAAAA%", false, true},  // DEVE ANDARE A BUON FINE

                // Quarta iterazione: pit
                {"username","123456789AAA", false, false},  // caso password troppo lunga
                {"username","%%%%%%%%%%", false, false}     // caso password non alfanumerica



        });
    }


    private DefaultPasswordRule defaultPasswordRule;
    private User firstParam;

    public TestDefaultPasswordRule2(String username, String clearPassword, boolean isMustntEndWithNonAlpha, boolean isMustEndWithNonAlpha){

        // method params configuration
        User user = new UserImpl();
        user.setCipherAlgorithm(CipherAlgorithm.AES);
        user.setUsername(username);
        user.setPassword(clearPassword);

        this.firstParam = user;
        // SUT configuration
        configure(isMustntEndWithNonAlpha, isMustEndWithNonAlpha);

    }

    private void configure(boolean isMustntEndWithNonAlpha, boolean isMustEndWithNonAlpha){

        DefaultPasswordRule rule = new DefaultPasswordRule();
        DefaultPasswordRuleConf conf = new DefaultPasswordRuleConf(); // password like: XXXXXXXXXY (X alpha numeric, Y not digit)
        conf.setMaxLength(10);
        conf.setMinLength(10);
        conf.setAlphanumericRequired(true);
        conf.setMustntEndWithDigit(true);
        conf.getWordsNotPermitted().add("password");
        if(isMustntEndWithNonAlpha) conf.setMustntEndWithNonAlpha(true);
        if(isMustEndWithNonAlpha) conf.setMustEndWithNonAlpha(true);
        rule.setConf(conf);
        this.defaultPasswordRule = rule;
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

            if (pwd == null || isValid(pwd, username, (DefaultPasswordRuleConf) this.defaultPasswordRule.getConf()))
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
