package org.springframework.data.crate.query;

import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;

import java.util.Locale;

public class CrateRepositoryQuery implements RepositoryQuery {

    private final CrateQueryMethod queryMethod;
    private final CrateOperations crateOperations;
    private final String query;

    private CrateRepositoryQuery(String query, CrateQueryMethod queryMethod, CrateOperations crateOperations) {
        this.query = query;
        this.queryMethod = queryMethod;
        this.crateOperations = crateOperations;
    }

    @Override
    public Object execute(Object[] parameters) {
//        ParametersParameterAccessor parameterAccessor = new ParametersParameterAccessor(queryMethod.getParameters(), parameters);
//        prepare query
//        pageble queries

        if (queryMethod.isCollectionQuery()) {
            // execute for multiple resutls
        } else if (queryMethod.isModifyingQuery()) {
            // execute for update/delete..
        } else {
            // single result exec
        }
        return null;
    }

    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

    public static RepositoryQuery fromAnnotationQuery(CrateQueryMethod queryMethod, CrateOperations crateOperations) {
        String query = queryMethod.getAnnotatedQuery();
        if (query != null) {
            return new CrateRepositoryQuery(query, queryMethod, crateOperations);
        }
        throw new IllegalArgumentException(String.format(Locale.ENGLISH,
                "Cannot create an annotated query for the query method: %s", queryMethod.toString()));
    }

}
