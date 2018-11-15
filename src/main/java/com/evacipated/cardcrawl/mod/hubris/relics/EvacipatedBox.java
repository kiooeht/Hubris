package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EvacipatedBox extends HubrisRelic
{
    public static final String ID = "hubris:EvacipatedBox";

    public EvacipatedBox()
    {
        super(ID, "evacipatedBox.png", RelicTier.SPECIAL, LandingSound.HEAVY);
    }
    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new EvacipatedBox();
    }
}
