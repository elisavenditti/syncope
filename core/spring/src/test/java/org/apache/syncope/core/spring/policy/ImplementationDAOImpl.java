package org.apache.syncope.core.spring.policy;

import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.common.lib.types.ImplementationEngine;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.entity.Implementation;
import org.apache.syncope.core.provisioning.api.serialization.POJOHelper;

import java.util.List;

public class ImplementationDAOImpl implements ImplementationDAO {
    @Override
    public void refresh(Implementation entity) {

    }

    @Override
    public void detach(Implementation entity) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Implementation find(String key) {

        DefaultPasswordRule rule = new DefaultPasswordRule();
        DefaultPasswordRuleConf conf = new DefaultPasswordRuleConf();
        String body;
        Implementation impl = new ImplementationImpl();
        impl.setEngine(ImplementationEngine.JAVA);

        if(key.equalsIgnoreCase("rule1")) {

            conf.setMaxLength(10);
            conf.setMinLength(10);
            conf.setAlphanumericRequired(true);
            conf.setMustntEndWithDigit(true);
            conf.getWordsNotPermitted().add("password");
            rule.setConf(conf);

            body = POJOHelper.serialize(rule);
            impl.setBody(body);

        } else if(key.equalsIgnoreCase("rule2")) {

            conf.setMinLength(5);
            conf.getWordsNotPermitted().add("ingegneria");
            rule.setConf(conf);


            body = POJOHelper.serialize(rule);
            impl.setBody(body);

        } else if(key.equalsIgnoreCase("rule3")) {

            conf.setMaxLength(15);
            conf.setAlphanumericRequired(true);
            conf.getWordsNotPermitted().add("testing");
            rule.setConf(conf);

            body = POJOHelper.serialize(rule);
            impl.setBody(body);

        }
        return impl;

    }

    @Override
    public List<Implementation> findByType(String type) {
        return null;
    }

    @Override
    public List<Implementation> findAll() {
        return null;
    }

    @Override
    public Implementation save(Implementation implementation) {
        return null;
    }

    @Override
    public void delete(String key) {

    }
}
