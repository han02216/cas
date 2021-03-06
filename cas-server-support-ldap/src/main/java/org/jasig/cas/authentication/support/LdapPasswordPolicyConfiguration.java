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
package org.jasig.cas.authentication.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * LDAP-specific password policy configuration container.
 *
 * @author Marvin S. Addison
 * @since 4.0.0
 */
@Component("ldapPasswordPolicyCOnfiguration")
public class LdapPasswordPolicyConfiguration extends PasswordPolicyConfiguration {

    /** Directory-specific account state handler component. */
    @NotNull
    private AccountStateHandler accountStateHandler;


    /**
     * @return  Account state handler component.
     */
    public AccountStateHandler getAccountStateHandler() {
        return accountStateHandler;
    }

    /**
     * Sets the directory-specific account state handler. If none is defined, account state handling is disabled,
     * which is the default behavior.
     *
     * @param accountStateHandler Account state handler.
     */
    @Autowired
    public void setAccountStateHandler(@Qualifier("accountStateHandler")
                                           final AccountStateHandler accountStateHandler) {
        this.accountStateHandler = accountStateHandler;
    }
}
