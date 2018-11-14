package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Tomato extends HubrisRelic
{
    public static final String ID = "hubris:Tomato";
    private static final int MAX_HP = 20;

    public Tomato()
    {
        super(ID, "tomato.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + MAX_HP + LocalizedStrings.PERIOD;
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Tomato();
    }
}
