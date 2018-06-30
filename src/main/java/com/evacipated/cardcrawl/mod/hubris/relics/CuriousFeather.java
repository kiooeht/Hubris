package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CuriousFeather extends AbstractRelic
{
    public static final String ID = "hubris:Curious Feather";

    public CuriousFeather()
    {
        super(ID, "curiousFeather.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CuriosityPower(AbstractDungeon.player, 1)));
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new CuriousFeather();
    }
}
