/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package org.springframework.data.crate.query;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.crate.core.mapping.CratePersistentProperty;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.PersistentPropertyPath;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class CrateQueryCreator extends AbstractQueryCreator<MethodQuery, CriteriaQuery> {

    private final Set<String> roots;
    private final MappingContext<?, CratePersistentProperty> context;

    public CrateQueryCreator(PartTree tree,
                             Set<String> roots,
                             ParameterAccessor parameters,
                             MappingContext<?, CratePersistentProperty> context) {
        super(tree, parameters);
        this.roots = roots;
        this.context = context;
    }

    @Override
    protected CriteriaQuery create(Part part, Iterator<Object> iterator) {
        PersistentPropertyPath<CratePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
        return new CriteriaQuery(from(part,
                new Criteria(path.toDotPath(CratePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator));
    }

    @Override
    protected CriteriaQuery and(Part part, CriteriaQuery base, Iterator<Object> iterator) {
        if (base == null) {
            return create(part, iterator);
        }
        PersistentPropertyPath<CratePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
        return base.addCriteria(from(part,
                new Criteria(path.toDotPath(CratePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator));
    }

    @Override
    protected CriteriaQuery or(CriteriaQuery base, CriteriaQuery query) {
        return new CriteriaQuery(base.getCriteria().or(query.getCriteria()));
    }

    @Override
    protected MethodQuery complete(CriteriaQuery where, Sort sort) {
        return new SelectQuery.SelectQueryBuilder()
                .from(roots)
                .where(where)
                .build();
    }

    private Criteria from(Part part, Criteria instance, Iterator<?> parameters) {
        Part.Type type = part.getType();

        Criteria criteria = instance;
        if (criteria == null) {
            criteria = new Criteria();
        }

        switch (type) {
            case IS_NULL:
                return criteria.is(null);
            case TRUE:
                return criteria.is(true);
            case FALSE:
                return criteria.is(false);
            case SIMPLE_PROPERTY:
                return criteria.is(parameters.next());
            case NEGATING_SIMPLE_PROPERTY:
                return criteria.is(parameters.next()).not();
            case LIKE:
                return criteria.like(parameters.next().toString());
            case STARTING_WITH:
                return criteria.startsWith(parameters.next().toString());
            case ENDING_WITH:
                return criteria.endsWith(parameters.next().toString());
            case GREATER_THAN:
                return criteria.greaterThan(parameters.next());
            case GREATER_THAN_EQUAL:
                return criteria.greaterThanEqual(parameters.next());
            case LESS_THAN:
                return criteria.lessThan(parameters.next());
            case LESS_THAN_EQUAL:
                return criteria.lessThanEqual(parameters.next());
            default:
                throw new InvalidDataAccessApiUsageException(
                        String.format(Locale.ENGLISH, "Illegal criteria found: %s", type)
                );
        }
    }
}
