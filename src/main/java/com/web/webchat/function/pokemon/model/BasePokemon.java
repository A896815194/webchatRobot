package com.web.webchat.function.pokemon.model;

import com.web.webchat.util.Calculate;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public abstract class BasePokemon {
    //名字
    protected String name;
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
    //速度
    protected Integer speed;
    //性别
    protected String sex;
    //特性
    protected List<Integer> feature;
    //装备
    protected Object equip;
    //技能表
    protected List<Skill> skills;

    //普通攻击
    abstract void attack();

    //技能攻击
    abstract void skillAttack();

    // 132
    public BasePokemon createPokemon(String name, Integer allCount, String[] proportion) {
        if (Calculate.fixedPercentage(5)) {
            return perfectNumberPokeMon(name, allCount, proportion);
        }
        return randomNumberPokeMon(name, allCount, proportion);
    }

    public BasePokemon perfectNumberPokeMon(String name, Integer allCount, String[] proportion) {
        BigDecimal fenmuBig = new BigDecimal(0);
        for (String s : proportion) {
            fenmuBig = fenmuBig.add(new BigDecimal(s));
        }
        BigDecimal allCountBig = new BigDecimal(String.valueOf(allCount));
        BigDecimal lifeBig = new BigDecimal(String.valueOf(proportion[0]));
        BigDecimal physicalAttackBig = new BigDecimal(String.valueOf(proportion[1]));
        BigDecimal physicalDefenseBig = new BigDecimal(String.valueOf(proportion[2]));
        BigDecimal magicAttackBig = new BigDecimal(String.valueOf(proportion[3]));
        BigDecimal magicDefenseBig = new BigDecimal(String.valueOf(proportion[4]));
        BigDecimal speedBig = new BigDecimal(String.valueOf(proportion[5]));
        this.life = allCountBig.multiply(lifeBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue();
        this.magic = 100;
        this.physicalAttack = allCountBig.multiply(physicalAttackBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue();
        this.physicalDefense = allCountBig.multiply(physicalDefenseBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue();
        this.magicAttack = allCountBig.multiply(magicAttackBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue();
        this.magicDefense = allCountBig.multiply(magicDefenseBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue();
        this.speed = allCountBig.multiply(speedBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue();
        this.sex = Calculate.fixedPercentage(50) ? "男" : "女";
        this.name = name + "(闪光)";
        return this;
    }


    public BasePokemon randomNumberPokeMon(String name, Integer allCount, String[] proportion) {
        BigDecimal fenmuBig = new BigDecimal(0);
        for (String s : proportion) {
            fenmuBig = fenmuBig.add(new BigDecimal(s));
        }
        BigDecimal allCountBig = new BigDecimal(String.valueOf(allCount));
        BigDecimal lifeBig = new BigDecimal(String.valueOf(proportion[0]));
        BigDecimal physicalAttackBig = new BigDecimal(String.valueOf(proportion[1]));
        BigDecimal physicalDefenseBig = new BigDecimal(String.valueOf(proportion[2]));
        BigDecimal magicAttackBig = new BigDecimal(String.valueOf(proportion[3]));
        BigDecimal magicDefenseBig = new BigDecimal(String.valueOf(proportion[4]));
        BigDecimal speedBig = new BigDecimal(String.valueOf(proportion[5]));
        this.life = Calculate.randBewteewn(10, allCountBig.multiply(lifeBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue());
        this.magic = 100;
        this.physicalAttack = Calculate.randBewteewn(10, allCountBig.multiply(physicalAttackBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue());
        this.physicalDefense = Calculate.randBewteewn(10, allCountBig.multiply(physicalDefenseBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue());
        this.magicAttack = Calculate.randBewteewn(10, allCountBig.multiply(magicAttackBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue());
        this.magicDefense = Calculate.randBewteewn(10, allCountBig.multiply(magicDefenseBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue());
        this.speed = Calculate.randBewteewn(10, allCountBig.multiply(speedBig.divide(fenmuBig, BigDecimal.ROUND_HALF_UP)).toBigInteger().intValue());
        this.name = name;
        this.sex = Calculate.fixedPercentage(50) ? "男" : "女";
        return this;
    }

    public abstract BasePokemon createPokemon();

}