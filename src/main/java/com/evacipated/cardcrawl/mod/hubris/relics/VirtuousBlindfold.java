package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class VirtuousBlindfold extends AbstractRelic
{
    public static final String ID = "hubris:VirtuousBlindfold";

    public VirtuousBlindfold()
    {
        super(ID, "virtuousBlindfold.png", RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new VirtuousBlindfold();
    }
}
