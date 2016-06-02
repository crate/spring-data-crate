/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.crate.repository.support;

import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Spring {@link org.springframework.beans.factory.FactoryBean} implementation to ease container based configuration for
 * XML namespace and JavaConfig.
 *
 * @author Rizwan Idrees
 */
public class CrateRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
        RepositoryFactoryBeanSupport<T, S, ID> {

    private CrateOperations operations;

    /**
     * Configures the {@link CrateOperations} to be used to create Crate repositories.
     *
     * @param operations the operations to set
     */
    public void setCrateOperations(CrateOperations operations) {
        Assert.notNull(operations);
        this.operations = operations;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(operations, "CrateOperations must be configured!");
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new CrateRepositoryFactory(operations);
    }
}
