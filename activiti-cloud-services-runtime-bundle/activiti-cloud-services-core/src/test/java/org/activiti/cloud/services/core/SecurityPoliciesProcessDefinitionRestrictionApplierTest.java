/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.core;

import java.util.Collections;
import java.util.Set;

import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SecurityPoliciesProcessDefinitionRestrictionApplierTest {

    private SecurityPoliciesProcessDefinitionRestrictionApplier restrictionApplier = new SecurityPoliciesProcessDefinitionRestrictionApplier();

    @Test
    public void restrictToKeysAddFilterOnGivenKeys() {
        //given
        ProcessDefinitionQuery initialQuery = mock(ProcessDefinitionQuery.class);
        Set<String> keys = Collections.singleton("procDef");

        ProcessDefinitionQuery restrictedQuery = mock(ProcessDefinitionQuery.class);
        given(initialQuery.processDefinitionKeys(keys)).willReturn(restrictedQuery);

        //when
        ProcessDefinitionQuery resultQuery = restrictionApplier.restrictToKeys(initialQuery,
                                                                                          keys);

        //then
        assertThat(resultQuery).isEqualTo(restrictedQuery);
    }

    @Test
    public void denyAllShouldAddUnmatchableFilter() {
        //given
        ProcessDefinitionQuery query = mock(ProcessDefinitionQuery.class);
        ProcessDefinitionQuery restrictedQuery = mock(ProcessDefinitionQuery.class);
        given(query.processDefinitionId(anyString())).willReturn(restrictedQuery);

        //when
        ProcessDefinitionQuery resultQuery = restrictionApplier.denyAll(query);

        //then
        assertThat(resultQuery).isEqualTo(restrictedQuery);
        verify(query).processDefinitionId(startsWith("missing-"));
    }
}