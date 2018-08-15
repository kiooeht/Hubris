package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RGBLights extends AbstractRelic
{
    public static final String ID = "hubris:RGBLights";
    private static final int AMT = 3;
    private static final int MULTIPLIER = 2;

    public RGBLights()
    {
        super(ID, "rgbLights.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + AMT + DESCRIPTIONS[1] + MULTIPLIER + DESCRIPTIONS[2];
    }

    @Override
    public void setCounter(int c)
    {
        counter = c;

        if (counter == 2) {
            beginPulse();
            pulse = true;
        } else {
            pulse = false;
        }
    }

    @Override
    public void atTurnStart()
    {
        setCounter(0);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb)
    {
        setCounter(counter + 1);
        if (counter == 3) {
            if (!AbstractDungeon.player.orbs.isEmpty() && !(AbstractDungeon.player.orbs.get(0) instanceof EmptyOrbSlot)) {
                flash();
                AbstractDungeon.player.orbs.get(0).onEvoke();
            }
            setCounter(0);
        }
    }

    @Override
    public void onVictory()
    {
        setCounter(-1);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new RGBLights();
    }
}
