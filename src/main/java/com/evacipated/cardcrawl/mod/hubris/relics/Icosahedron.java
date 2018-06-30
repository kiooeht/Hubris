package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Icosahedron extends AbstractRelic
{
    public static final String ID = "hubris:Icosahedron";

    public Icosahedron()
    {
        super(ID, "icosahedron.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new com.evacipated.cardcrawl.mod.hubris.cards.Icosahedron(), 1, false));
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Icosahedron();
    }
}
