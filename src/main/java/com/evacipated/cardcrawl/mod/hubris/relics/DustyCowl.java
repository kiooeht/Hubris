package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DustyCowl extends AbstractRelic
{
    public static final String ID = "hubris:DustyCowl";
    private static final int BLOCK = 3;

    public DustyCowl()
    {
        super(ID, "test2.png", RelicTier.COMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + BLOCK + DESCRIPTIONS[1];
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        if (card.isEthereal) {
            flash();
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK, true));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DustyCowl();
    }
}
