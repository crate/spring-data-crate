package org.springframework.data.crate.query;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.crate.annotations.Query;
import org.springframework.data.crate.core.CrateOperations;
import org.springframework.data.crate.core.mapping.event.User;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.sample.entities.SampleEntity;

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrateRepositoryQueryTest {

    @Test
    public void testReplacePlaceholders() throws Exception {
        CrateQueryMethod repositoryMethod = prepareQueryMethod("findByEmailAddress", User.class);
        RepositoryQuery repositoryQuery = CrateRepositoryQuery.fromAnnotationQuery(repositoryMethod, mock(CrateOperations.class));
    }

    public interface AnnotatedQueryRepository {

        @Query(value = "select u from User u where u.emailAddress = ?1")
        User findByEmailAddress(String emailAddress);


        @Query("select * from sys.nodes")
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
