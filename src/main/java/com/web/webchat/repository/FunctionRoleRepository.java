package com.web.webchat.repository;

import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.entity.UserThing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FunctionRoleRepository extends JpaRepository<FunctionRoleEntity, Long> {

    List<FunctionRoleEntity> findAll();

    List<FunctionRoleEntity> findAllByIsOpen(Integer flag);

    List<FunctionRoleEntity> findAllByChatroomIdAndIsOpen(String chatRooms, Integer isOpen);

//    @Query(value = "select new com.web.webchat.entity.UserThing(" +
//            "a.id ," +
//            "a.wxidId," +
//            "a.entityId," +
//            "b.thingName," +
//            "b.thingType," +
//            "b.thingTemplate," +
//            "b.thingClass," +
//            "b.thingMethod, " +
//            "b.autoUse, " +
//            "a.startTime, " +
//            "a.endTime ," +
//            "a.useCount, " +
//            "b.useType " +
//            ")" +
//            "from UserBagEntity a " +
//            "left join " +
//            "ThingEntity b " +
//            "on a.entityId = b.id " +
//            "where " +
//            "a.entityType = 'thing' " +
//            "and a.isDelete = 0 " +
//            "and a.useCount > 0 " +
//            "and a.wxidId = :wxid")
//    List<UserThing> getUserThingsCanUse(@Param("wxid") String wxid);
}
