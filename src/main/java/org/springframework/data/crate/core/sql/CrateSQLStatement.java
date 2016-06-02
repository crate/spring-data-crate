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
package org.springframework.data.crate.core.sql;

/**
 * @author Hasnain Javed
 * @since 1.0.0
 */
public interface CrateSQLStatement {

    String SPACE = " ";
    String COMMA = ",";
    String OPEN_BRACE = "(";
    String CLOSE_BRACE = ")";
    String AS = "AS";
    String PRIMARY_KEY = "PRIMARY KEY";
    String CREATE_TABLE = "CREATE TABLE";
    String DROP_TABLE = "DROP TABLE";
    String ALTER_TABLE = "ALTER TABLE";
    String ADD_COLUMN = "ADD COLUMN";
    String INSERT_INTO = "INSERT INTO";
    String WHERE = "WHERE";
    String VALUES = "VALUES";
    String WITH = "WITH";
    String SET = "SET";
    String REFRESH_TABLE = "REFRESH TABLE";
    String NO_OF_REPLICAS = "number_of_replicas";
    String REFRESH_INTERVAL = "refresh_interval";
    String COLUMN_POLICY = "column_policy";

    String createStatement();
}
