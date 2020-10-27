package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CustomJpaRepositoty<T, ID> extends JpaRepository<T, ID> {

    Optional<T> buscarPrimeiro();
}