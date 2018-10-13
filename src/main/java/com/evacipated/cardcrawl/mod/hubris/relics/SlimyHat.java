package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SlimyHat extends HubrisRelic
{
    public static final String ID = "hubris:SlimyHat";
    private static final int AMT = 2;

    public SlimyHat()
    {
        super(ID, "slimyHat.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip()
    {
        BaseMod.MAX_HAND_SIZE += AMT;
    }

    @Override
    public void onUnequip()
    {
        BaseMod.MAX_HAND_SIZE -= AMT;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new SlimyHat();
    }
}
