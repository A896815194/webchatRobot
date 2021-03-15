package com.web.webchat.repository;

import com.web.webchat.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerInfoRepository extends JpaRepository<Demo, String> {
    @Override
    List<Demo> findAll();
}
