package com.johnsoncskoo.shortify.repository;

import com.johnsoncskoo.shortify.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
    boolean existsByIdAndUrl(String id, String url);

    Link findByUrl(String url);

    Link findByIdIgnoreCase(String id);

    boolean existsByIdIgnoreCase(String newId);

    void deleteByIdIgnoreCase(String id);

    Link findByUrlIgnoreCase(String url);
}
