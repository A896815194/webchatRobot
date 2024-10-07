package com.web.webchat.repository.gzh;

import com.web.webchat.entity.gzh.CardUserGzhEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardUserGzhRepository extends JpaRepository<CardUserGzhEntity, Long> {

    List<CardUserGzhEntity> findAll();

    List<CardUserGzhEntity> findByIsDeleteNotAndUserNameOrderByCreateTimeAsc(Integer isDelete, String userName);

    List<CardUserGzhEntity> findByIsDeleteNotAndUserNameAndCardTypeOrderByCreateTimeAsc(Integer isDelete, String userName,Integer cardType);

    List<CardUserGzhEntity> findByIsDeleteNotOrderByCreateTimeAsc(Integer isDelete);

}
