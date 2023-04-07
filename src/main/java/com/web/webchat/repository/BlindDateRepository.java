package com.web.webchat.repository;

import com.web.webchat.entity.BindDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlindDateRepository extends JpaRepository<BindDateEntity, Long> {

    List<BindDateEntity> findAll();


}
