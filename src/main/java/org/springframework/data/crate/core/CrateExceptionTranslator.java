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

import io.crate.action.sql.SQLActionException;
import io.crate.shade.org.elasticsearch.client.transport.NoNodeAvailableException;
import org.springframework.dao.*;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.crate.DuplicateTableException;
import org.springframework.data.crate.NoSuchTableException;
import org.springframework.data.crate.UncategorizedCrateException;

import static org.springframework.data.crate.core.CrateErrorCodes.*;

/**
 * {@link PersistenceExceptionTranslator} implementation for Crate. Converts the given runtime exception to an appropriate
 * exception from the {@code org.springframework.dao} hierarchy. Returns {@literal null} if no translation is
 * appropriate i.e any other exception may have resulted from user code, and should not be considered for translation.
 *
 * @author Hasnain Javed
 * @version 1.0.0
 */
public class CrateExceptionTranslator implements PersistenceExceptionTranslator {

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {

        if (ex instanceof NoNodeAvailableException) {
            return new DataAccessResourceFailureException(ex.getMessage(), ex);
        }

        if (ex instanceof SQLActionException) {

            int errorCode = ((SQLActionException) ex).errorCode();

            switch (errorCode) {
                case DUPLICATE_PRIMARY_KEY:
                    return new DuplicateKeyException(ex.getMessage(), ex);
                case VERSION_CONFLICT:
                    return new OptimisticLockingFailureException(ex.getMessage(), ex);
                case FIELD_VALIDATION_FAILED:
                    return new DataIntegrityViolationException(ex.getMessage(), ex);
                case DUPLICATE_TABLE_NAME:
                    return new DuplicateTableException(ex.getMessage(), ex);
                case UNKNOWN_TABLE:
                    return new NoSuchTableException(ex.getMessage(), ex);
                case INVALID_SQL_STATEMENT_OR_SYNTAX:
                case INVALID_TABLE_NAME:
                case UNKNOWN_ANALYZER:
                case UNKNOWN_COLUMN:
                case UNKNOWN_TYPE:
                case UNKNOWN_SCHEMA:
                case UNKNOWN_PARTITION:
                case COLUMN_ALIAS_AMBIGUOUS:
                case FEATURE_NOT_SUPPORTED_YET:
                case INVALID_ANALYZER_DEFINITION:
                case ALTER_TABLE_ALIAS_NOT_SUPPORTED:
                case TABLE_ALIAS_CONTAINS_TABLES_WITH_DIFFERENT_SCHEMA:
                    return new InvalidDataAccessResourceUsageException(ex.getMessage(), ex);
                case UNHANDLED_SERVER_ERROR:
                case TASKS_EXECUTION_FAILED:
                case SHARDS_NOT_AVAILABLE:
                case QUERY_FAILED_ON_SHARDS:
                    return new DataAccessResourceFailureException(ex.getMessage(), ex);
                default:
                    return new UncategorizedCrateException(ex.getMessage(), ex);
            }
        }

        return null;
    }
}
