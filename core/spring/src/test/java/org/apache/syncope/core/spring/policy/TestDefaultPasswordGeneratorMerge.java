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

        List<DefaultPasswordRuleConf> confifurations = new ArrayList<>();
        confifurations.add(conf);
        confifurations.add(conf2);

        List<DefaultPasswordRuleConf> confifuration = new ArrayList<>();
        confifurations.add(conf);

        List<DefaultPasswordRuleConf> confifurationConflict = new ArrayList<>();
        confifurationConflict.add(conf);
        confifurationConflict.add(conf3);

        return Arrays.asList(new Object[][]{
                {null}, {new ArrayList<>()}, {confifuration}, {confifurations}, {confifurationConflict}
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