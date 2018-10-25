package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RunicObelisk extends HubrisRelic
{
    public static final String ID = "hubris:RunicObelisk";
    private static final int AMT = 3;

    public RunicObelisk()
    {
        super(ID, "runicObelisk.png", RelicTier.BOSS, LandingSound.MAGICAL);
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
        BaseMod.MAX_HAND_SIZE -= AMT;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
        BaseMod.MAX_HAND_SIZE += AMT;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new RunicObelisk();
    }
}
