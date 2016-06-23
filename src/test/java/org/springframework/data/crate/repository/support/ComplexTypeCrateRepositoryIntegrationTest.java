package org.springframework.data.crate.repository.support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.crate.client.CrateClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.crate.CrateIntegrationTest;
import org.springframework.data.crate.config.TestCrateConfiguration;
import org.springframework.data.crate.core.mapping.schema.CratePersistentEntitySchemaManager;
import org.springframework.data.crate.repository.config.EnableCrateRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.sample.entities.test.ComplexType;
import org.springframework.data.sample.entities.test.SimpleType;
import org.springframework.data.sample.repositories.test.ComplexTypeCrateRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.data.crate.core.mapping.schema.SchemaExportOption.CREATE_DROP;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComplexTypeCrateRepositoryIntegrationTest.TestConfig.class})
public class ComplexTypeCrateRepositoryIntegrationTest extends CrateIntegrationTest {

    @Autowired
    ComplexTypeCrateRepository repository;

    @After
    public void teardown() throws InterruptedException {
        repository.deleteAll();
    }

    private static List<ComplexType> prepareComplexTypes() {
        SimpleType simpleType = new SimpleType(1L, "CrateSimpleType");
        ComplexType complexType = new ComplexType(
                "CrateComplexType",
                ImmutableList.of(simpleType),
                ImmutableSet.of(simpleType),
                new SimpleType[]{simpleType}
        );
        return ImmutableList.of(complexType);
    }

    @Test
    public void testSimpleAnnotation() {
        repository.bulkInsert(prepareComplexTypes());
        assertThat(repository.findAll().size(), is(1));
    }

    @Configuration
    @EnableCrateRepositories(basePackages = "org.springframework.data.sample.repositories.test",
            queryLookupStrategy = QueryLookupStrategy.Key.USE_DECLARED_QUERY)
    static class TestConfig extends TestCrateConfiguration {

        @Bean
        public CrateClient crateClient() {
            return new CrateClient(String.format(Locale.ENGLISH, "%s:%d", server.crateHost(), server.transportPort()));
        }

        @Bean
        public CratePersistentEntitySchemaManager cratePersistentEntitySchemaManager() throws Exception {
            return new CratePersistentEntitySchemaManager(crateTemplate(), CREATE_DROP);
        }

        @Override
        protected String getMappingBasePackage() {
            return "org.springframework.data.sample.entities.test";
        }
    }
}
