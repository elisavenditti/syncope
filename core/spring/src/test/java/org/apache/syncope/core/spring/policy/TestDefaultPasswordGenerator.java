package org.apache.syncope.core.spring.policy;
import junit.framework.TestCase;
import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.core.persistence.api.entity.policy.PasswordPolicy;
import org.apache.syncope.core.spring.security.DefaultPasswordGenerator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Test;
import java.util.*;


@RunWith(Parameterized.class)
public class TestDefaultPasswordGenerator extends TestCase {
    @Parameters
    public static Collection<Object[]> data() {

        List<PasswordPolicy> policies = new ArrayList<>();
        PasswordPolicy p = new PasswordPolicyImpl();
        p.setAllowNullPassword(false);
        p.setHistoryLength(2);

        policies.add(p);

        return Arrays.asList(new Object[][]{
                {null}, {new ArrayList<>()}, {policies}
        });
    }


    private final DefaultPasswordGenerator defaultPasswordGenerator;
    private final List<PasswordPolicy> firstParam;

    public TestDefaultPasswordGenerator(List<PasswordPolicy> pwdPolicies) {

        // method params configuration
        if (pwdPolicies == null)
            this.firstParam = pwdPolicies;
        else {
            this.firstParam = new ArrayList<>();
            this.firstParam.addAll(pwdPolicies);
        }
        configure();

        // SUT
        this.defaultPasswordGenerator = new DefaultPasswordGenerator();


    }

    private void configure() {
        if (this.firstParam == null || this.firstParam.isEmpty()) return;

        //mock !!
//        ImplementationDAOImpl implementationDAO = new ImplementationDAOImpl();
//        this.firstParam.get(0).add(implementationDAO.find("rule1"));
//        this.firstParam.get(0).add(implementationDAO.find("rule2"));
//        this.firstParam.get(0).add(implementationDAO.find("rule3"));


    }


    @Test
    public void testGenerate() {
        String actual, expected;

        if (this.firstParam == null)
            expected = "exception";
        else {
            expected = "stringa conforme alla policy";
        }

        try {
            actual = this.defaultPasswordGenerator.generate(this.firstParam);
            if (actual != null) actual = "stringa conforme alla policy";
        } catch (NullPointerException e) {
            actual = "exception";
        } catch (PasswordPolicyException e) {
            actual = "exception 2";
        } catch (InvalidPasswordRuleConf e) {
            throw new RuntimeException(e);
        }

        assertEquals(expected, actual);

    }

}

