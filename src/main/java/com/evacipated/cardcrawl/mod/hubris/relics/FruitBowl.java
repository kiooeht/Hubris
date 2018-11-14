package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FruitBowl extends HubrisRelic
{
    public static final String ID = "hubris:FruitBowl";
    private static final float PERCENT_INCREASE = 0.5f;

    public FruitBowl()
    {
        super(ID, "fruitBowl.png", RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + (int) (PERCENT_INCREASE * 100) + DESCRIPTIONS[1];
    }

    public int onMaxHPChange(int amount)
    {
        flash();
        return (int) (amount * (1.0f + PERCENT_INCREASE));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new FruitBowl();
    }
}
