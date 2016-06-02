/*
 * Copyright 2002-2014 the original author or authors.
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
package org.springframework.data.crate.core;

import org.springframework.data.crate.core.BulkActionResult.ActionResult;

import java.util.List;

/**
 * @param T
 * @author Hasnain Javed
 * @since 1.0.0
 */
public interface BulkOperartionResult<T> {

    public List<ActionResult<T>> getResults();

    public List<ActionResult<T>> getSuccesses();

    public List<ActionResult<T>> getFailures();
}
