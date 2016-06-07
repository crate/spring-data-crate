package org.springframework.data.crate.query;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.crate.annotations.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class CrateQueryMethod extends QueryMethod {

    private final Query query;

    public CrateQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
        super(method, metadata, factory);
        this.query = method.getAnnotation(Query.class);
    }

    public String getAnnotatedQuery() {
        String value = String.valueOf(AnnotationUtils.getValue(query, "value"));
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value;
    }

}
