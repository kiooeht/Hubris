package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SoftwareUpdate extends HubrisRelic
{
    public static final String ID = "hubris:SoftwareUpdate";
    public static final int MAX_ORBS = 15;

    public SoftwareUpdate()
    {
        super(ID, "test5.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + MAX_ORBS + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new SoftwareUpdate();
    }
}
