package org.springframework.data.sample.entities.test;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.crate.core.mapping.annotations.Table;

import java.util.List;
import java.util.Set;

@Table(name = "obj_collection", numberOfReplicas = "0")
public class ComplexType {

    @Id
    public String id;
    public List<SimpleType> list;
    public Set<SimpleType> set;
    public SimpleType[] array;

    public ComplexType(String id, List<SimpleType> list, Set<SimpleType> set, SimpleType[] array) {
        this.id = id;
        this.list = list;
        this.set = set;
        this.array = array;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ComplexType)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        ComplexType that = (ComplexType) obj;

        return new EqualsBuilder().append(this.id, that.id)
                .append(this.list, that.list)
                .append(this.set, that.set)
                .append(this.array, that.array)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 11).append(id)
                .append(list)
                .append(array)
                .append(set)
                .toHashCode();
    }
}
