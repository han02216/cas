/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.monitor;

import org.ldaptive.Connection;
import org.ldaptive.pool.PooledConnectionFactory;
import org.ldaptive.pool.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * Monitors an ldaptive {@link PooledConnectionFactory}.
 *
 * @author Marvin S. Addison
 * @since 4.0.0
 */
@Component("pooledLdapConnectionFactoryMonitor")
public class PooledConnectionFactoryMonitor extends AbstractPoolMonitor {

    /** Source of connections to validate. */
    @Nullable
    @Autowired(required=false)
    @Qualifier("pooledConnectionFactoryMonitorConnectionFactory")
    private PooledConnectionFactory connectionFactory;

    /** Connection validator. */
    @Nullable
    @Autowired(required=false)
    @Qualifier("pooledConnectionFactoryMonitorValidator")
    private Validator<Connection> validator;


    /**
     * Instantiates a new Pooled connection factory monitor.
     */
    public PooledConnectionFactoryMonitor() {}

    /**
     * Creates a new instance that monitors the given pooled connection factory.
     *
     * @param  factory  Connection factory to monitor.
     * @param  validator  Validates connections from the factory.
     */
    public PooledConnectionFactoryMonitor(
            final PooledConnectionFactory factory, final Validator<Connection> validator) {
        this.connectionFactory = factory;
        this.validator = validator;
    }


    @Override
    protected StatusCode checkPool() throws Exception {
        if (this.connectionFactory != null && this.validator != null) {
            try (final Connection conn = this.connectionFactory.getConnection()) {
                return this.validator.validate(conn) ? StatusCode.OK : StatusCode.ERROR;
            }
        }
        return StatusCode.UNKNOWN;
    }

    @Override
    protected int getIdleCount() {
        return this.connectionFactory.getConnectionPool().availableCount();
    }

    @Override
    protected int getActiveCount() {
        return this.connectionFactory.getConnectionPool().activeCount();
    }
}
