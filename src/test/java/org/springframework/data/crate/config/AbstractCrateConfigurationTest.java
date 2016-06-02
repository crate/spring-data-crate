/*
 * Copyright 2002-2014 the original author or authors.
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
package org.springframework.data.crate.config;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.data.crate.core.mapping.annotations.Table;
import org.springframework.data.crate.core.mapping.schema.CratePersistentEntitySchemaManager;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.crate.core.mapping.schema.SchemaExportOption.CREATE_DROP;

/**
 * @author Hasnain Javed
 * @since 1.0.0
 */
public class AbstractCrateConfigurationTest {

    @Test
    public void usesConfigClassPackageAsBaseMappingPackage() throws ClassNotFoundException {

        AbstractCrateConfiguration configuration = new SampleCrateConfiguration();
        assertThat(configuration.getMappingBasePackage(), is(SampleCrateConfiguration.class.getPackage().getName()));
        assertThat(configuration.getInitialEntitySet(), hasSize(1));
        assertThat(configuration.getInitialEntitySet(), hasItem(Entity.class));
    }

    @Table(name = "entity")
    static class Entity {
        String name;
    }

    class SampleCrateConfiguration extends AbstractCrateConfiguration {
        @Bean
        public CratePersistentEntitySchemaManager cratePersistentEntitySchemaManager() throws Exception {
            return new CratePersistentEntitySchemaManager(crateTemplate(), CREATE_DROP);
        }
    }
}
