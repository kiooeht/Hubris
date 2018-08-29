package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OldNail extends HubrisRelic
{
    public static final String ID = "hubris:OldNail";

    public OldNail()
    {
        super(ID, "oldNail.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new OldNail();
    }
}
