package com.github.fabioper.newsletter.domain.edition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditionRepository extends JpaRepository<Edition, EditionId> {
}
