package com.web.webchat.repository;

import com.web.webchat.entity.ThingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThingRepository extends JpaRepository<ThingEntity, Long> {

    List<ThingEntity> findAll();

}
