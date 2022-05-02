package com.web.webchat.repository;

import com.web.webchat.entity.FunctionRoleCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FunctionRoleCommandRepository extends JpaRepository<FunctionRoleCommand, Long> {

    List<FunctionRoleCommand> findAll();


}
