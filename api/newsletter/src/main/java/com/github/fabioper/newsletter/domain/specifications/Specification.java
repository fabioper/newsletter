package com.github.fabioper.newsletter.domain.specifications;

public interface Specification<T> {
    boolean isSatisfiedBy(T candidate);
}
