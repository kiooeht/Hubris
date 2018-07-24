package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SphereOfDissonance extends AbstractRelic
{
    public static final String ID = "hubris:SphereOfDissonance";
    private static final int AMT = 3;

    public SphereOfDissonance()
    {
        super(ID, "test6.png", RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(m, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, AMT, false), AMT, true));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new SphereOfDissonance();
    }
}
