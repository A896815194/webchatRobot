package com.web.webchat.repository;

import com.web.webchat.entity.ShopEntity;
import com.web.webchat.entity.ShopThing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

    List<ShopEntity> findAll();

    List<ShopEntity> findAllByThingId(String thingId);

    @Query(value = "select new com.web.webchat.entity.ShopThing(" +
            "  b.id," +
            "  b.thingName," +
            "  a.thingCount," +
            "  a.thingPrice," +
            "  a.isDelete," +
            "  a.isOne," +
            "  b.thingDesc)" +
            " from ShopEntity a " +
            "left join ThingEntity b " +
            "on a.thingId = b.id where a.isDelete = 0 ")
    List<ShopThing> getShopThings();

}
