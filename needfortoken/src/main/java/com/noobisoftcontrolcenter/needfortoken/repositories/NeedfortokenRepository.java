package com.noobisoftcontrolcenter.needfortoken.repositories;

import com.noobisoftcontrolcenter.needfortoken.models.NeedfortokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeedfortokenRepository extends JpaRepository<NeedfortokenModel, Long> {
    // Custom query methods if needed
}


