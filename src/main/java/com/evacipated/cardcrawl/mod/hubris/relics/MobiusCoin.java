package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MobiusCoin extends HubrisRelic
{
    public static final String ID = "hubris:MobiusCoin";
    private static final int GOLD_AMOUNT = 50;

    public MobiusCoin()
    {
        super(ID, "mobiusCoin.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + GOLD_AMOUNT + DESCRIPTIONS[1];
    }

    @Override
    public void onTrigger()
    {
        flash();
        CardCrawlGame.sound.play("GOLD_GAIN");
        AbstractDungeon.player.gainGold(GOLD_AMOUNT);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new MobiusCoin();
    }
}
