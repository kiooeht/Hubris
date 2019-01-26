package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.hubris.vfx.RelicSpeechBubble;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ClockworkCow extends HubrisRelic
{
    public static final String ID = "hubris:ClockworkCow";

    public ClockworkCow()
    {
        super(ID, "clockworkCow.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new ClockworkCow();
    }
}
