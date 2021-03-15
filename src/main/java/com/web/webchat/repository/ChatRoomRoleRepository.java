package com.web.webchat.repository;

import com.web.webchat.entity.ChatRoomRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatRoomRoleRepository extends JpaRepository<ChatRoomRoleEntity, Long> {

    List<ChatRoomRoleEntity> findAll();
    List<ChatRoomRoleEntity> findAllByIsOpen(Integer flag);
}
