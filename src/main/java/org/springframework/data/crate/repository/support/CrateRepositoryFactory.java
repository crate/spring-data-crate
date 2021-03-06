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

package org.springframework.data.crate.repository.support;

import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.util.Assert;

import java.io.Serializable;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

/**
 * Factory to create {@link org.springframework.data.crate.repository.CrateRepository}
 *
 * @author Rizwan Idrees
 */
public class CrateRepositoryFactory extends RepositoryFactorySupport {

    private final CrateOperations crateOperations;
    private final CrateEntityInformationCreator entityInformationCreator;

    public CrateRepositoryFactory(CrateOperations crateOperations) {
        Assert.notNull(crateOperations);
        this.crateOperations = crateOperations;
        this.entityInformationCreator = new CrateEntityInformationCreatorImpl(crateOperations.getConverter().getMappingContext());
    }

    private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
        return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }

    @Override
    public <T, ID extends Serializable> CrateEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return entityInformationCreator.getEntityInformation(domainClass);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Object getTargetRepository(RepositoryInformation repositoryInformation) {
        return new SimpleCrateRepository(getEntityInformation(repositoryInformation.getDomainType()), crateOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslRepository(metadata.getRepositoryInterface())) {
            throw new IllegalArgumentException("QueryDsl Support has not been implemented yet.");
        }
        return SimpleCrateRepository.class;
    }

    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider) {
        return CrateQueryLookupStrategyFactory.create(crateOperations, key, evaluationContextProvider);
    }

}
