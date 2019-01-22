package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.BefuddledAction;
import com.evacipated.cardcrawl.mod.hubris.powers.BefuddledPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class GrandSneckoEye extends HubrisRelic
{
    public static final String ID = "hubris:GrandSneckoEye";

    private static final int AMT = 2;

    public GrandSneckoEye()
    {
        super(ID, "grandSneckoEye.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public void atTurnStartPostDraw()
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new BefuddledAction(AMT));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new GrandSneckoEye();
    }
}
