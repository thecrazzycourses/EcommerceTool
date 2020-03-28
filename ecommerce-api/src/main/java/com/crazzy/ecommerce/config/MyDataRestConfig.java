package com.crazzy.ecommerce.config;

import com.crazzy.ecommerce.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.rest.core.config.*;
import org.springframework.data.rest.webmvc.config.*;
import org.springframework.http.*;

import javax.persistence.*;
import javax.persistence.metamodel.*;
import java.util.*;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] theUnsupportedMethods = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        // Disable for Products
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedMethods));

        // Disable for ProductCategory
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedMethods));

        // call an internal helper method
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // get list of all entites
        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();

        // create an array of entity type
        List<Class> entiClasses = new ArrayList<>();

        // get the entity type for entities
        for (EntityType entityType : entityTypes) {
            entiClasses.add(entityType.getJavaType());
        }

        // expose the entity ids for the array of entity types
        Class[] domainTypes = entiClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);

    }
}
