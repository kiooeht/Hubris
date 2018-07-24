package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BottledHeart extends AbstractRelic
{
    public static final String ID = "hubris:BottledHeart";

    public BottledHeart()
    {
        super(ID, "bottledHeart.png", RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        flash();
        setCounter(AbstractDungeon.player.maxHealth / 2);
        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth - 1);
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, counter));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BottledHeart();
    }
}
