package com.evacipated.cardcrawl.mod.hubris.relics;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class BloodyCrown extends HubrisRelic
{
    public static final String ID = "hubris:BloodyCrown";

    public BloodyCrown()
    {
        super(ID, "test5.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 2;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 2;
    }

    @Override
    public void atPreBattle()
    {
        //if (AbstractDungeon.getCurrRoom().monsters.monsters.size() > 1) {
            AbstractMonster monsterToDuplicate = null;

            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m.type != AbstractMonster.EnemyType.BOSS && m.type != AbstractMonster.EnemyType.ELITE) {
                    monsterToDuplicate = m;
                    break;
                }
            }

            if (monsterToDuplicate != null) {
                System.out.println(monsterToDuplicate.getClass().getCanonicalName());

                //*
                float offsetX = (monsterToDuplicate.drawX - Settings.WIDTH * 0.75f) / Settings.scale;
                offsetX -= (2.0f * monsterToDuplicate.hb_w);
                float offsetY = (monsterToDuplicate.drawY - AbstractDungeon.floorY) / Settings.scale;

                final Class<?>[] wantedParams = {float.class, float.class};
                Object instance = null;
                //AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(monsterToDuplicate, this));
                Constructor<?>[] constructors = monsterToDuplicate.getClass().getConstructors();
                for (Constructor<?> ctor : constructors) {
                    System.out.println("ctor");
                    for (Class<?> paramType : ctor.getParameterTypes()) {
                        System.out.println(paramType.getCanonicalName());
                    }

                    try {
                        if (ctor.getParameterTypes().length == 0) {
                            instance = ctor.newInstance();
                            break;
                        } else if (Arrays.equals(ctor.getParameterTypes(), wantedParams)) {
                            instance = ctor.newInstance(0, 0);
                            break;
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                if (instance != null) {
                    AbstractMonster monster = (AbstractMonster) instance;
                    monster.drawX = Settings.WIDTH * 0.75f + offsetX * Settings.scale;
                    monster.drawY = AbstractDungeon.floorY + offsetY * Settings.scale;

                    AbstractDungeon.getCurrRoom().monsters.addMonster(0, monster);

                    monster.showHealthBar();
                    monster.healthBarUpdatedEvent();
                    monster.usePreBattleAction();
                    monster.useUniversalPreBattleAction();
                    monster.rollMove();
                }
                //*/
            }
        //}
    }

    private static class IntentAdapter extends TypeAdapter<AbstractMonster.Intent>
    {

        @Override
        public void write(JsonWriter jsonWriter, AbstractMonster.Intent intent) throws IOException
        {

        }

        @Override
        public AbstractMonster.Intent read(JsonReader jsonReader) throws IOException
        {
            return null;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BloodyCrown();
    }
}
