package com.noobisoftcontrolcenter.tokemon.repositories;

import com.noobisoftcontrolcenter.tokemon.models.TokemonModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokemonRepository extends JpaRepository<TokemonModel, Long> {
    // Custom query methods if needed
}



