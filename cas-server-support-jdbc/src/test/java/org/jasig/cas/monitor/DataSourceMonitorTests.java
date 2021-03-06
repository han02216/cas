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

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * Unit test for {@link DataSourceMonitor}.
 *
 * @author Marvin S. Addison
 * @since 3.5.1
 */
public class DataSourceMonitorTests {

    private DataSource dataSource;

    @Before
    public void setup() {
        final ClassPathXmlApplicationContext ctx = new
            ClassPathXmlApplicationContext("classpath:/jpaTestApplicationContext.xml");
        this.dataSource = ctx.getBean("dataSource", DataSource.class);
    }

    @Test
    public void verifyObserve() throws Exception {
        final DataSourceMonitor monitor = new DataSourceMonitor(this.dataSource);
        monitor.setExecutor(Executors.newSingleThreadExecutor());
        monitor.setValidationQuery("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS");
        final PoolStatus status = monitor.observe();
        assertEquals(StatusCode.OK, status.getCode());
    }
}
