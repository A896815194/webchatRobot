package com.web.webchat.repository;

import com.web.webchat.entity.UserBagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserBagRepository extends JpaRepository<UserBagEntity, Long> {

    List<UserBagEntity> findAll();

    List<UserBagEntity> findAllByWxidIdAndIsDelete(String wxid, Integer isDelete);

    List<UserBagEntity> findAllByWxidIdAndEntityIdAndIsDelete(String wxid, String entityId, Integer isDelete);
}
