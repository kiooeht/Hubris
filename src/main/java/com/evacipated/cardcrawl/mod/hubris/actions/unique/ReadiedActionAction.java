package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ReadiedActionAction extends AbstractGameAction
{
    public ReadiedActionAction(AbstractCreature source, int amount)
    {
        setValues(AbstractDungeon.player, source, amount);
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update()
    {
        if (duration == 0.5f) {
            AbstractDungeon.handCardSelectScreen.open(RetainCardsAction.TEXT[0], amount, false, true, false, false, true);
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25f));
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (!c.isEthereal) {
                    c.retain = true;
                }
                c.setCostForTurn(c.costForTurn - 1);
                AbstractDungeon.player.hand.addToTop(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}
