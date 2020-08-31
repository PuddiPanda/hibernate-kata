package org.squashtest.hibernatekata.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.squashtest.hibernatekata.jpa.dto.FilmProjection;
import org.squashtest.hibernatekata.jpa.entities.Film;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    Optional<FilmProjection> findFilmProjectionById(Integer integer);
}
