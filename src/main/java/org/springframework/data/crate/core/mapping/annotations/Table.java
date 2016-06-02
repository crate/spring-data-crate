/*
 * Copyright 2014 the original author or authors.
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

package org.springframework.data.crate.core.mapping.annotations;

import org.springframework.data.annotation.Persistent;
import org.springframework.data.crate.core.mapping.schema.ColumnPolicy;

import java.lang.annotation.*;

import static org.springframework.data.crate.core.mapping.schema.ColumnPolicy.DYNAMIC;

/**
 * Identifies a domain object to be persisted to Crate.
 *
 * @author Hasnain Javed
 * @since 1.0.0
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {

    String name() default "";

    String numberOfReplicas() default "1";

    int refreshInterval() default 1000;

    ColumnPolicy columnPolicy() default DYNAMIC;
}
