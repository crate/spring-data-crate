package org.springframework.data.crate.repository.support;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.repository.query.DefaultEvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;

public class QueryLookUpStrategyTest {

    @Mock
    private CrateOperations crateOperations;

    private final CrateRepositoryFactory factory = new CrateRepositoryFactory(crateOperations);

    @Test
    public void testDeclaredQueryLookupStrategy() {
        QueryLookupStrategy lookupStrategy = factory.getQueryLookupStrategy(
                QueryLookupStrategy.Key.USE_DECLARED_QUERY,
                DefaultEvaluationContextProvider.INSTANCE
        );
        // assert instance of
    }

}
