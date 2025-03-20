package com.johnsoncskoo.shortify.repository;

import com.johnsoncskoo.shortify.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
    boolean existsByIdAndUrl(String id, String url);

    Link findByUrl(String url);
}
