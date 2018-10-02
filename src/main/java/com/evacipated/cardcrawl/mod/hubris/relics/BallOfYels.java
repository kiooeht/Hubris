package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BallOfYels extends HubrisRelic implements OnChannelRelic
{
    public static final String ID = "hubris:BallOfYels";

    public BallOfYels()
    {
        super(ID, "ballOfYels.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onChannel(AbstractOrb orb)
    {
        if (orb instanceof Lightning) {
            flash();
            orb.onEndOfTurn();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BallOfYels();
    }
}
