package com.web.webchat.function.pokemon.model;

import lombok.Data;

import java.util.List;

@Data
public abstract class BasePokemon {
    //生命
    protected Integer life;
    //魔法
    protected Integer magic;
    //物理攻击力
    protected Integer physicalAttack;
    //魔法攻击力
    protected Integer magicAttack;
    //物理防御
    protected Integer physicalDefense;
    //魔法防御
    protected Integer magicDefense;
    //特性
    protected List<Integer> feature;
    //装备
    protected Object equip;
    //技能表
    protected List<Object> skills;
    //普通攻击
    abstract void attack();
    //技能攻击
    abstract void skillAttack();
}
