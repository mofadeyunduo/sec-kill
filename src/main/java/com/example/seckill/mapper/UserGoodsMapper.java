package com.example.seckill.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserGoodsMapper {

    @Insert("insert into user_goods values(#{userId}, #{goodsId})")
    void save(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);

}
