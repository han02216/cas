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

package org.jasig.cas.support.spnego.web.flow.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * A simple implementation of {@link BaseSpnegoKnownClientSystemsFilterAction} to allow / skip SPNEGO / KRB /
 * NTLM authentication based on a regex match against a reverse DNS lookup of the requesting
 * system.
 *
 * @author Sean Baker
 * @author Misagh Moayyed
 * @since 4.1
 */
@Component("hostnameSpnegoClientAction")
public class HostNameSpnegoKnownClientSystemsFilterAction extends BaseSpnegoKnownClientSystemsFilterAction {

    private  Pattern hostNamePatternString;

    /**
     * Instantiates a new Host name spnego known client systems filter action.
     */
    public HostNameSpnegoKnownClientSystemsFilterAction() {}

    /**
     * Instantiates a new hostname spnego known client systems filter action.
     *
     * @param hostNamePatternString the host name pattern string.
     *                              The pattern to match the retrieved hostname against.
     */
    @Autowired
    public HostNameSpnegoKnownClientSystemsFilterAction(@NotNull
                                                        @Value("${cas.spnego.hostname.pattern:something.+}")
                                                        final String hostNamePatternString) {
        super();
        this.hostNamePatternString = Pattern.compile(hostNamePatternString);
    }

    /**
     * {@inheritDoc}.
     * <p>
     * Checks whether the IP should even be paid attention to,
     * then does a reverse DNS lookup, and if it matches the supplied pattern, performs SPNEGO
     * else skips the process.
     *
     * @param remoteIp The remote ip address to validate
     */
    @Override
    protected boolean shouldDoSpnego(final String remoteIp) {
        final boolean ipCheck = ipPatternCanBeChecked(remoteIp);
        if(ipCheck && !ipPatternMatches(remoteIp)) {
            return false;
        }
        final String hostName = getRemoteHostName(remoteIp);
        logger.debug("Retrieved host name for the remote ip is {}", hostName);
        return this.hostNamePatternString.matcher(hostName).find();
    }
}
