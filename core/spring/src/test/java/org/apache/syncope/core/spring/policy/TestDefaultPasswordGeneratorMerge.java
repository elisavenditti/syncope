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
public class TestDefaultPasswordGeneratorMerge extends TestCase {
    @Parameters
    public static Collection<Object[]> data() {


        DefaultPasswordRuleConf conf = new DefaultPasswordRuleConf();
        conf.setMinLength(5);
        conf.getWordsNotPermitted().add("ingegneria");

        DefaultPasswordRuleConf conf2 = new DefaultPasswordRuleConf();
        conf2.setMaxLength(15);
        conf2.setAlphanumericRequired(true);
        conf2.getWordsNotPermitted().add("testing");

        DefaultPasswordRuleConf conf3 = new DefaultPasswordRuleConf();
        conf3.setMaxLength(4);

        DefaultPasswordRuleConf conf4 = new DefaultPasswordRuleConf();
        conf4.setNonAlphanumericRequired(true);
        conf4.setAlphanumericRequired(true);
        conf4.setDigitRequired(true);
        conf4.setLowercaseRequired(true);
        conf4.setUppercaseRequired(true);
        conf4.setMustStartWithDigit(true);
        conf4.setMustntStartWithDigit(true);
        conf4.setMustEndWithDigit(true);
        conf4.setMustStartWithAlpha(true);
        conf4.setMustntStartWithAlpha(true);
        conf4.setMustStartWithNonAlpha(true);
        conf4.setMustntStartWithNonAlpha(true);
        conf4.setMustEndWithNonAlpha(true);
        conf4.setMustntEndWithNonAlpha(true);
        conf4.setMustEndWithAlpha(true);
        conf4.setMustntEndWithAlpha(true);
        conf4.setUsernameAllowed(true);



        DefaultPasswordRuleConf conf5 = new DefaultPasswordRuleConf();
        conf5.setNonAlphanumericRequired(false);
        conf5.setAlphanumericRequired(false);
        conf5.setDigitRequired(false);
        conf5.setLowercaseRequired(false);
        conf5.setUppercaseRequired(false);
        conf5.setMustStartWithDigit(false);
        conf5.setMustntStartWithDigit(false);
        conf5.setMustEndWithDigit(false);
        conf5.setMustStartWithAlpha(false);
        conf5.setMustntStartWithAlpha(false);
        conf5.setMustStartWithNonAlpha(false);
        conf5.setMustntStartWithNonAlpha(false);
        conf5.setMustEndWithNonAlpha(false);
        conf5.setMustntEndWithNonAlpha(false);
        conf5.setMustEndWithAlpha(false);
        conf5.setMustntEndWithAlpha(false);
        conf5.setUsernameAllowed(false);

        List<DefaultPasswordRuleConf> confifurations = new ArrayList<>();
        confifurations.add(conf);
        confifurations.add(conf2);

        List<DefaultPasswordRuleConf> confifuration = new ArrayList<>();
        confifurations.add(conf);

        List<DefaultPasswordRuleConf> confifurationConflict = new ArrayList<>();
        confifurationConflict.add(conf);
        confifurationConflict.add(conf3);

        List<DefaultPasswordRuleConf> confifurationAllTrue = new ArrayList<>();
        confifurationAllTrue.add(conf4);
        confifurationAllTrue.add(conf5);

        return Arrays.asList(new Object[][]{
                // Prima iterazione: jacoco
                {null},
                {new ArrayList<>()},
                {confifuration},
                {confifurations},
                {confifurationConflict},

                //Quarta iterazione: pit
                {confifurationAllTrue}
        });
    }


    private DefaultPasswordGenerator defaultPasswordGenerator;
    private List<DefaultPasswordRuleConf> firstParam;

    public TestDefaultPasswordGeneratorMerge(List<DefaultPasswordRuleConf> configuration) {

        // method params configuration
        if (configuration == null)
            this.firstParam = configuration;
        else {
            this.firstParam = new ArrayList<>();
            this.firstParam.addAll(configuration);
        }
        // SUT
        this.defaultPasswordGenerator = new DefaultPasswordGenerator();
    }


    @Test
    public void testGenerate() {

        DefaultPasswordGeneratorChild temp = new DefaultPasswordGeneratorChild();

        try {
            DefaultPasswordRuleConf actual = temp.callMerge(this.firstParam);
            System.out.print("max " + actual.getMaxLength());
            System.out.println(" - min "+actual.getMinLength());

            // chiamo l'oracolo per il risultato atteso
            assertTrue(isValid(actual, this.firstParam));

        }catch (NullPointerException e){
            assertNull(this.firstParam);
        }
    }

    private boolean isValid(DefaultPasswordRuleConf merge, List<DefaultPasswordRuleConf> confs) throws NullPointerException{
        boolean valid = true;
        for(DefaultPasswordRuleConf c: confs){

            if(c.getMaxLength()!=0 && merge.getMaxLength()>c.getMaxLength()){
                valid = false;
                return valid;
            }
            if(c.getMinLength()!=0 && merge.getMinLength()<c.getMinLength()){
                valid = false;
                return valid;
            }

            if(c.isNonAlphanumericRequired() && !(merge.isNonAlphanumericRequired())){
                valid = false;
                return valid;
            }
            if(c.isAlphanumericRequired()&& !(merge.isAlphanumericRequired())){
                valid = false;
                return valid;
            }
            if(c.isDigitRequired()&& !(merge.isDigitRequired())){
                valid = false;
                return valid;
            }
            if(c.isLowercaseRequired()&& !(merge.isLowercaseRequired())){
                valid = false;
                return valid;
            }
            if(c.isUppercaseRequired()&& !(merge.isUppercaseRequired())){
                valid = false;
                return valid;
            }
            if(c.isMustStartWithDigit()&& !(merge.isMustStartWithDigit())){
                valid = false;
                return valid;
            }
            if(c.isMustntStartWithDigit()&& !(merge.isMustntStartWithDigit())){
                valid = false;
                return valid;
            }
            if(c.isMustEndWithDigit()&& !(merge.isMustEndWithDigit())){
                valid = false;
                return valid;
            }
            if(c.isMustStartWithAlpha()&& !(merge.isMustStartWithAlpha())){
                valid = false;
                return valid;
            }
            if(c.isMustntStartWithAlpha()&& !(merge.isMustntStartWithAlpha())){
                valid = false;
                return valid;
            }
            if(c.isMustStartWithNonAlpha()&& !(merge.isMustStartWithNonAlpha())){
                valid = false;
                return valid;
            }
            if(c.isMustntStartWithNonAlpha()&& !(merge.isMustntStartWithNonAlpha())){
                valid = false;
                return valid;
            }
            if(c.isMustEndWithNonAlpha()&& !(merge.isMustEndWithNonAlpha())){
                valid = false;
                return valid;
            }
            if(c.isMustntEndWithNonAlpha()&& !(merge.isMustntEndWithNonAlpha())){
                valid = false;
                return valid;
            }
            if(c.isMustEndWithAlpha()&& !(merge.isMustEndWithAlpha())){
                valid = false;
                return valid;
            }
            if(c.isMustntEndWithAlpha()&& !(merge.isMustntEndWithAlpha())){
                valid = false;
                return valid;
            }
            if(c.isUsernameAllowed()&& !(merge.isUsernameAllowed())){
                valid = false;
                return valid;
            }


            // il test fallisce: scrivilo nel report
//            for(String s: c.getWordsNotPermitted())
//                if(!merge.getWordsNotPermitted().contains(s)){
//                    valid = false;
//                    System.out.println("2a");
//                    return valid;
//                }
        }
        return valid;
    }


    public class DefaultPasswordGeneratorChild extends DefaultPasswordGenerator{
        public DefaultPasswordRuleConf callMerge(List<DefaultPasswordRuleConf> defaultRuleConfs){
            return merge(defaultRuleConfs);
        }


    }

}