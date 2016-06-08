/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package org.springframework.data.crate.query.execution;

import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.crate.query.CrateRepositoryQuery;
import org.springframework.util.Assert;

public abstract class QueryExecution {

    protected final CrateOperations operations;

    QueryExecution(CrateOperations operations) {
        this.operations = operations;
    }

    public Object execute(CrateRepositoryQuery query, Object[] values) {
        Assert.notNull(query);
        Assert.notNull(values);

        Object result;

        try {
            result = doExecute(query, values);
        } catch (Exception e) {
            return null;
        }

        if (result == null) {
            return null;
        }

        // check if return type is the same with query returned object type
        return result;
    }

    protected abstract Object doExecute(CrateRepositoryQuery query, Object[] values);

}
