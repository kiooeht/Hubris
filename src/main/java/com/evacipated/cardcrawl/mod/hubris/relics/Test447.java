package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Test447 extends AbstractRelic
{
    public static final String ID = "test447";

    public Test447()
    {
        super(ID, "test447.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(Backtick.ID)) {
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(Backtick.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Test447();
    }
}
