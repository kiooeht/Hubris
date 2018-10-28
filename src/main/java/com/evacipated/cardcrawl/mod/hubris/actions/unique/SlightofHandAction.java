package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Setup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SlightofHandAction extends AbstractGameAction
{
    public SlightofHandAction()
    {
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractCard c;
            if (p.hand.isEmpty()) {
                isDone = true;
                return;
            }
            if (p.hand.size() == 1) {
                c = p.hand.getTopCard();
                if (c.cost > 0) {
                    c.freeToPlayOnce = true;
                }
                p.hand.moveToDiscardPile(c);
                GameActionManager.incrementDiscard(false);
                c.triggerOnManualDiscard();
                AbstractDungeon.player.hand.refreshHandLayout();
                isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(Setup.TEXT[0], 1, false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c.cost > 0) {
                    c.freeToPlayOnce = true;
                }
                p.hand.moveToDiscardPile(c);
                GameActionManager.incrementDiscard(false);
                c.triggerOnManualDiscard();
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}
