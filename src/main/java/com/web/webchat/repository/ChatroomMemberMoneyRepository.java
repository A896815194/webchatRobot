package com.web.webchat.repository;

import com.web.webchat.entity.ChatroomMemberMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatroomMemberMoneyRepository extends JpaRepository<ChatroomMemberMoney, Long> {

    List<ChatroomMemberMoney> findAll();

    List<ChatroomMemberMoney> findAllByWxidId(String wxidId);

    List<ChatroomMemberMoney> findAllByWxidIdIn(List<String> wxidIds);
}
