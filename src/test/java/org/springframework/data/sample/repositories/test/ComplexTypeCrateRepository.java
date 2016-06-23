package org.springframework.data.sample.repositories.test;

import org.springframework.data.crate.repository.CrateRepository;
import org.springframework.data.sample.entities.test.ComplexType;

public interface ComplexTypeCrateRepository extends CrateRepository<ComplexType, String> {
}
