package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.OnChannelRelic;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BallOfEels extends HubrisRelic implements OnChannelRelic
{
    public static final String ID = "hubris:BallOfEels";

    public BallOfEels()
    {
        super(ID, "ballOfEels.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onChannel(AbstractOrb orb)
    {
        orb.onEndOfTurn();
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BallOfEels();
    }
}
