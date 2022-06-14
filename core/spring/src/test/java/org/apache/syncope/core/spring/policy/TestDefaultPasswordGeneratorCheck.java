package org.apache.syncope.core.spring.policy;

import junit.framework.TestCase;
import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.core.spring.security.DefaultPasswordGenerator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Test;

import java.util.*;


@RunWith(Parameterized.class)
public class TestDefaultPasswordGeneratorCheck extends TestCase {
    @Parameters
    public static Collection<Object[]> data() {


        DefaultPasswordRuleConf conf = new DefaultPasswordRuleConf();
        conf.setMinLength(5);
        conf.setMaxLength(10);
        conf.getWordsNotPermitted().add("ingegneria");

        DefaultPasswordRuleConf conf2 = new DefaultPasswordRuleConf();
        conf2.setMaxLength(1);
        conf2.setMinLength(2);
        conf2.setAlphanumericRequired(true);
        conf2.getWordsNotPermitted().add("testing");

        DefaultPasswordRuleConf conf3 = new DefaultPasswordRuleConf();
        conf3.setAlphanumericRequired(true);
        conf3.setNonAlphanumericRequired(true);

        DefaultPasswordRuleConf conf4 = new DefaultPasswordRuleConf();
        conf4.setLowercaseRequired(true);
        conf4.setUppercaseRequired(true);

        DefaultPasswordRuleConf conf5 = new DefaultPasswordRuleConf();
        conf5.setMustStartWithDigit(true);
        conf5.setMustntStartWithDigit(true);

        DefaultPasswordRuleConf conf6 = new DefaultPasswordRuleConf();
        conf6.setMustEndWithDigit(true);
        conf6.setMustntEndWithDigit(true);

        DefaultPasswordRuleConf conf7 = new DefaultPasswordRuleConf();
        conf7.setMustStartWithAlpha(true);
        conf7.setMustntStartWithAlpha(true);



        return Arrays.asList(new Object[][]{
                {null, false}, {conf, true}, {conf2, false}, {conf3, true},
                {conf4, true}, {conf5, false}, {conf6, false}, {conf7, false}
        });
    }


    private DefaultPasswordGenerator defaultPasswordGenerator;
    private DefaultPasswordRuleConf firstParam;
    private boolean expectedValidity;

    public TestDefaultPasswordGeneratorCheck(DefaultPasswordRuleConf configuration, boolean expectedValidity) {

        // method params configuration
        this.expectedValidity = expectedValidity;
        this.firstParam = configuration;

        // SUT
        this.defaultPasswordGenerator = new DefaultPasswordGenerator();
    }


    @Test
    public void testGenerate() {

        DefaultPasswordGeneratorChild temp = new DefaultPasswordGeneratorChild();
        boolean valid = false;
        try {
            temp.callCheck(this.firstParam);
            valid=true;
            assertEquals(valid, this.expectedValidity);
            System.out.println(1);
        } catch (NullPointerException e) {
            assertNull(this.firstParam);
            System.out.println(2);
        } catch (InvalidPasswordRuleConf e) {
            System.out.println(3);
        }
        assertEquals(valid, this.expectedValidity);
    }


    public class DefaultPasswordGeneratorChild extends DefaultPasswordGenerator {

        public void callCheck(DefaultPasswordRuleConf conf) throws InvalidPasswordRuleConf {
            check(conf);
        }


    }

}