package com.web.webchat.repository;

import com.web.webchat.entity.FunctionRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FunctionRoleRepository extends JpaRepository<FunctionRoleEntity, Long> {

    List<FunctionRoleEntity> findAll();

    List<FunctionRoleEntity> findAllByIsOpen(Integer flag);

    List<FunctionRoleEntity> findAllByChatroomIdAndIsOpen(String chatRooms, Integer isOpen);

}
