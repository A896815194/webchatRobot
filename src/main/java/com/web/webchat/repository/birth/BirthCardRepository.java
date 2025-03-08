package com.web.webchat.repository.birth;

import com.web.webchat.entity.birth.BirthCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BirthCardRepository extends JpaRepository<BirthCardEntity, Long> {

    List<BirthCardEntity> findAll();

    BirthCardEntity findTopBySendAndCardType(String send,String cardType);

    List<BirthCardEntity> findAllBySendAndCardType(String send,String cardType);

    List<BirthCardEntity> findAllByCardType(String cardType);
}
