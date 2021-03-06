/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.security.sasl.util;

import static org.wildfly.common.Assert.checkNotNullParam;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import javax.security.sasl.SaslServerFactory;

/**
 * A SaslServerFactory allowing the user to add properties
 *
 * @author Kabir Khan
 */
public class PropertiesSaslServerFactory extends AbstractDelegatingSaslServerFactory {

    private final Map<String, ?> properties;

    /**
     * Constructor
     * @param delegate the underlying SaslServerFactory
     * @param properties the properties
     */
    public PropertiesSaslServerFactory(SaslServerFactory delegate, Map<String, ?> properties) {
        super(delegate);
        this.properties = new HashMap<>(checkNotNullParam("properties", properties));
    }

    @Override
    public String[] getMechanismNames(Map<String, ?> props) {
        return delegate.getMechanismNames(combine(props, properties));
    }

    @Override
    public SaslServer createSaslServer(String mechanism, String protocol, String serverName, Map<String, ?> props, CallbackHandler cbh) throws SaslException {
        return delegate.createSaslServer(mechanism, protocol, serverName, combine(props, properties), cbh);
    }

    private static Map<String, ?> combine(Map<String, ?> provided, Map<String, ?> configured) {
        Map<String, Object> combined = new HashMap<>(provided);
        combined.putAll( configured);

        return combined;
    }
}
