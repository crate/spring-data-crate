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

package org.springframework.data.crate.query;

import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.crate.query.execution.CollectionExecutor;
import org.springframework.data.crate.query.execution.QueryExecution;
import org.springframework.data.crate.query.execution.SingleEntityExecutor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;

public class CrateRepositoryQuery implements RepositoryQuery {

    private final CrateQueryMethod queryMethod;
    private final CrateOperations crateOperations;
    private final String query;

    private CrateRepositoryQuery(String query, CrateQueryMethod queryMethod, CrateOperations crateOperations) {
        this.queryMethod = queryMethod;
        this.crateOperations = crateOperations;
        this.query = query;
    }

    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

    @Override
    public Object execute(Object[] parameters) {
        return getExecution().execute(this, parameters);
    }

    private QueryExecution getExecution() {
        if (queryMethod.isCollectionQuery()) {
            return new CollectionExecutor(crateOperations);
        } else {
            return new SingleEntityExecutor(crateOperations);
        }
    }

    public String getSource() {
        return query;
    }

    public static CrateRepositoryQuery buildFromAnnotation(CrateQueryMethod queryMethod, CrateOperations crateOperations) {
        if (queryMethod.getAnnotatedQuery().isPresent()) {
            return new CrateRepositoryQuery(queryMethod.getAnnotatedQuery().get(), queryMethod, crateOperations);
        }
        throw new IllegalArgumentException("cannot create annotated query if annotation doesn't contain a query");
    }

}
