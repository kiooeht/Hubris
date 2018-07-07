package com.evacipated.cardcrawl.mod.hubris.actions.common;

import com.evacipated.cardcrawl.mod.hubris.powers.WarpPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Field;

public class SpawnWarpMonsterAction extends SpawnMonsterAction
{
    private static Field used;
    private static Field m;

    static {
        try {
            used = SpawnMonsterAction.class.getDeclaredField("used");
            used.setAccessible(true);

            m = SpawnMonsterAction.class.getDeclaredField("m");
            m.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private AbstractMonster owner;

    public SpawnWarpMonsterAction(AbstractMonster m, AbstractMonster owner)
    {
        super(m, true);

        this.owner = owner;
    }

    @Override
    public void update()
    {
        boolean usedPrev = false;
        try {
            usedPrev = used.getBoolean(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        super.update();

        try {
            if (used.getBoolean(this) && !usedPrev) {
                AbstractMonster monster = (AbstractMonster) m.get(this);
                monster.usePreBattleAction();
                monster.useUniversalPreBattleAction();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(monster, monster, new WarpPower(monster, owner)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
