package org.springframework.data.crate.query;

import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;

import java.lang.reflect.Method;
import java.util.Locale;

public final class CrateQueryLookupStrategyFactory {

    private static class CrateAbstractLookupStrategy implements QueryLookupStrategy {

        protected final CrateOperations operations;
        protected final EvaluationContextProvider context;

        CrateAbstractLookupStrategy(CrateOperations operations, EvaluationContextProvider context) {
            this.operations = operations;
            this.context = context;
        }

        @Override
        public RepositoryQuery resolveQuery(Method method,
                                            RepositoryMetadata repositoryMetadata,
                                            ProjectionFactory projectionFactory,
                                            NamedQueries namedQueries) {
            throw new UnsupportedOperationException("Spring Data Crate does not support given query lookup strategy.");
        }

    }

    private static class DeclaredQueryLookupStrategy extends CrateAbstractLookupStrategy {

        DeclaredQueryLookupStrategy(CrateOperations operations, EvaluationContextProvider context) {
            super(operations, context);
        }

        @Override
        public RepositoryQuery resolveQuery(Method method,
                                            RepositoryMetadata metadata,
                                            ProjectionFactory factory,
                                            NamedQueries namedQueries) {
            CrateQueryMethod queryMethod = new CrateQueryMethod(method, metadata, factory);
            return CrateRepositoryQuery.fromAnnotationQuery(queryMethod, operations);
        }

    }

    private static class CreateIfNotFoundQueryLookupStrategy extends CrateAbstractLookupStrategy {

        CreateIfNotFoundQueryLookupStrategy(CrateOperations operations, EvaluationContextProvider context) {
            super(operations, context);
        }

        @Override
        public RepositoryQuery resolveQuery(Method method,
                                            RepositoryMetadata repositoryMetadata,
                                            ProjectionFactory projectionFactory,
                                            NamedQueries namedQueries) {
            QueryLookupStrategy lookupStrategy;
            try {
                lookupStrategy = new CreateQueryLookupStrategy(operations, context);
                return lookupStrategy.resolveQuery(method, repositoryMetadata, projectionFactory, namedQueries);
            } catch (Exception e) {
                lookupStrategy = new DeclaredQueryLookupStrategy(operations, context);
                return lookupStrategy.resolveQuery(method, repositoryMetadata, projectionFactory, namedQueries);
            }
        }
        
    }

    private static class CreateQueryLookupStrategy extends CrateAbstractLookupStrategy {

        CreateQueryLookupStrategy(CrateOperations operations,
                                  EvaluationContextProvider context) {
            super(operations, context);
        }

    }

    public static QueryLookupStrategy create(CrateOperations operations,
                                             QueryLookupStrategy.Key key,
                                             EvaluationContextProvider context) {
        if (key == null) {
            return new CreateQueryLookupStrategy(operations, context);
        }

        switch (key) {
            case CREATE:
                return new CreateQueryLookupStrategy(operations, context);
            case USE_DECLARED_QUERY:
                return new DeclaredQueryLookupStrategy(operations, context);
            case CREATE_IF_NOT_FOUND:
                return new CreateIfNotFoundQueryLookupStrategy(operations, context);
            default:
                throw new UnsupportedOperationException(
                        String.format(Locale.ENGLISH, "Spring Data Crate does not support %s query lookup strategy.", key)
                );
        }
    }

}
