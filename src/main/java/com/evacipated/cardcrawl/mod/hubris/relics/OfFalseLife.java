package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;

public class OfFalseLife extends AbstractRelic
{
    public static final String ID = "hubris:False Life";
    private static final int TEMP_HP_AMOUNT = 10;

    public OfFalseLife()
    {
        super(ID, "goldenCompass.png", RelicTier.UNCOMMON, LandingSound.MAGICAL);
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
        return new OfFalseLife();
    }

    public static int getTemporaryHealth(AbstractCreature creature)
    {
        try {
            Field f = AbstractCreature.class.getDeclaredField("temporaryHealth");
            return (int) f.get(creature);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
