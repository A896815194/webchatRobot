package com.web.webchat.repository;

import com.web.webchat.entity.ChatroomMemberSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface ChatroomMemberSignRepository extends JpaRepository<ChatroomMemberSign, Long> {

    List<ChatroomMemberSign> findAll();

    List<ChatroomMemberSign> findAllByWxidIdAndSignTimeBetween(String wxid, Date start, Date end);

    List<ChatroomMemberSign> findAllBySignTimeBetween(Date start, Date end);

    List<ChatroomMemberSign> findAllByChatroomIdIn(List<String>chatroomIds);
}
