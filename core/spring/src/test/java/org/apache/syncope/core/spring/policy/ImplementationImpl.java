package org.apache.syncope.core.spring.policy;

import org.apache.syncope.common.lib.types.ImplementationEngine;
import org.apache.syncope.core.persistence.api.entity.Implementation;

public class ImplementationImpl implements Implementation {
    public static final String TABLE = "Implementation";

    private static final long serialVersionUID = 8700713975100295322L;

    private ImplementationEngine engine;

    private String type;

    private String body;

    @Override
    public ImplementationEngine getEngine() {
        return engine;
    }

    @Override
    public void setEngine(final ImplementationEngine engine) {
        this.engine = engine;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(final String body) {
        this.body = body;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }
}