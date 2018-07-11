package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;

public class PeanutButter extends AbstractRelic
{
    public static final String ID = "hubris:PeanutButter";
    private static final int TEMP_HP_AMOUNT = 10;

    public PeanutButter()
    {
        super(ID, "peanutButter.png", RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + TEMP_HP_AMOUNT + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, TEMP_HP_AMOUNT));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new PeanutButter();
    }
}