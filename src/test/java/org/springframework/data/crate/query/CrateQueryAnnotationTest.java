package org.springframework.data.crate.query;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.crate.annotations.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.sample.entities.SampleEntity;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrateQueryAnnotationTest {

    @Test
    public void testAnnotatedQuery() throws Exception {
        CrateQueryMethod repositoryMethod = prepareQueryMethod("a", SampleEntity.class);
        assertEquals("select * from sys.cluster", repositoryMethod.getAnnotatedQuery());
        assertEquals(repositoryMethod.isModifyingQuery(), false);
    }

    @Test
    public void testModifyingAnnotatedQuery() throws Exception {
        CrateQueryMethod repositoryMethod = prepareQueryMethod("b", SampleEntity.class);
        assertEquals("delete from foo", repositoryMethod.getAnnotatedQuery());
        assertEquals(repositoryMethod.isModifyingQuery(), true);
    }

    public interface AnnotatedQueryRepository {

        @Query(value = "select * from sys.cluster")
        List<SampleEntity> a();

        @Query("delete from foo")
        List<SampleEntity> b();

    }

    private CrateQueryMethod prepareQueryMethod(String methodName, Class<?> entityClass) throws Exception {

        RepositoryMetadata repositoryMetadata = Mockito.mock(RepositoryMetadata.class);
        when(repositoryMetadata.getDomainType()).thenReturn((Class) entityClass);

        Method testMethod = AnnotatedQueryRepository.class.getMethod(methodName);
        when(repositoryMetadata.getReturnedDomainClass(testMethod)).thenReturn((Class) entityClass);

        return new CrateQueryMethod(testMethod, repositoryMetadata, mock(ProjectionFactory.class));
    }
}
