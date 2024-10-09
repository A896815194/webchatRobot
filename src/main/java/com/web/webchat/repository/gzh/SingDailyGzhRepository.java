package com.web.webchat.repository.gzh;

import com.web.webchat.entity.gzh.SingDailyGzhEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface SingDailyGzhRepository extends JpaRepository<SingDailyGzhEntity, Long> {

    List<SingDailyGzhEntity> findAll();

    List<SingDailyGzhEntity> findAllByYearAndMonthAndDayOrderByCreateTimeAsc(Integer year, Integer month, Integer day);

    List<SingDailyGzhEntity> findAllByCreateTimeBetweenOrderByCreateTimeAsc(Date start, Date end);

    List<SingDailyGzhEntity> findAllByYearAndMonthAndDayAndSongName(Integer year, Integer month, Integer day, String songName);

}
