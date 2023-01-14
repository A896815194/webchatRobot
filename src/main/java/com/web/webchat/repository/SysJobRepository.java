package com.web.webchat.repository;

import com.web.webchat.entity.SysJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysJobRepository extends JpaRepository<SysJob, Long> {

    List<SysJob> findAll();

    List<SysJob> findAllByJobStatus(Integer flag);
}
