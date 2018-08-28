package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AstralHammer extends HubrisRelic
{
    public static final String ID = "hubris:AstralHammer";
    private static final int COST_DECREASE = -1;

    public AstralHammer()
    {
        super(ID, "astralHammer.png", RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (card.type == AbstractCard.CardType.ATTACK) {
            flash();
            if (card.cost > 0) {
                AbstractDungeon.actionManager.addToBottom(new ReduceCostAction(card, COST_DECREASE));
            } else {
                action.exhaustCard = true;
            }
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new AstralHammer();
    }
}
