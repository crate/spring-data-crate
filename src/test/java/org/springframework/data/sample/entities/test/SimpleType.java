package org.springframework.data.sample.entities.test;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Version;
import org.springframework.data.crate.core.mapping.annotations.Table;

@Table(numberOfReplicas = "0")
public class SimpleType {

    @Version
    public Long version;
    public String stringField;

    public SimpleType(Long version, String stringField) {
        this.version = version;
        this.stringField = stringField;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof SimpleType)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        SimpleType that = (SimpleType) obj;

        return new EqualsBuilder().append(this.stringField, that.stringField)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 9).append(stringField)
                .toHashCode();
    }
}
