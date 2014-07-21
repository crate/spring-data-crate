/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.data.crate.core.mapping;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.SimpleTypeHolder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Crate specific {@link org.springframework.data.mapping.PersistentProperty} implementation processing
 *
 * @author Rizwan Idrees
 */
public class SimpleCratePersistentProperty extends
        AnnotationBasedPersistentProperty<CratePersistentProperty> implements CratePersistentProperty {

	private static final Set<String> SUPPORTED_ID_PROPERTY_NAMES = new HashSet<String>();

	static {
		SUPPORTED_ID_PROPERTY_NAMES.add("id");
	}

	public SimpleCratePersistentProperty(Field field, PropertyDescriptor propertyDescriptor,
                                         PersistentEntity<?, CratePersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(field, propertyDescriptor, owner, simpleTypeHolder);
	}

	@Override
	public String getFieldName() {
		return field.getName();
	}

	@Override
	public boolean isIdProperty() {
		return super.isIdProperty() || (field != null && SUPPORTED_ID_PROPERTY_NAMES.contains(getFieldName()));
	}

	@Override
	protected Association<CratePersistentProperty> createAssociation() {
		throw new UnsupportedOperationException("@Reference is not supported!");
	}
}