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

package org.springframework.data.sample.repositories;

import org.springframework.data.crate.annotations.Query;
import org.springframework.data.crate.repository.CrateRepository;
import org.springframework.data.sample.entities.person.Person;

import java.util.List;

public interface PersonRepository extends CrateRepository<Person, Integer> {

    @Query("select * from person order by name")
    List<Person> getAll();

    @Query("select * from person where name = $1")
    List<Person> findByName(String name);
}
