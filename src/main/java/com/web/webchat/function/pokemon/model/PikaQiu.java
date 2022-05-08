package com.web.webchat.function.pokemon.model;


public class PikaQiu extends BasePokemon {

//    public PikaQiu() {
//        BasePokemon a = createPokemon("皮卡丘", 132, new Integer[]{3, 4, 3, 3, 3, 6});
//        BeanUtils.copyProperties(a,);
//    }

    @Override
    void attack() {

    }

    @Override
    void skillAttack() {

    }

    public BasePokemon createPokemon() {
        return createPokemon("皮卡丘", 132, new String[]{"18.0", "24.0", "18.0", "18.0", "18.0", "36.0"});
    }
}
