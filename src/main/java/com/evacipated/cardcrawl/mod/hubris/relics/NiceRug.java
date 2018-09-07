package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class NiceRug extends HubrisRelic
{
    public static final String ID = "hubris:NiceRug";

    public NiceRug()
    {
        super(ID, "test4.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new NiceRug();
    }
}
