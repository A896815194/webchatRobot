package com.web.webchat.repository.gzh;

import com.web.webchat.entity.video.DanmuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DanmuRepository extends JpaRepository<DanmuEntity, Long> {
    List<DanmuEntity> findAll();

    void deleteAllById(String id);
}
