package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InsightAction extends AbstractGameAction
{
    private int getAmount;
    private boolean anyNumber;

    public InsightAction(int numCardsLook, int numCardsGet)
    {
        this(numCardsLook, numCardsGet, false);
    }

    public InsightAction(int numCardsLook, int numCardsGet, boolean anyNumber)
    {
        amount = numCardsLook;
        getAmount = numCardsGet;
        this.anyNumber = anyNumber;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        CardGroup tmpGroup;
        if (duration == startDuration) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                isDone = true;
                return;
            }

            tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (int i=0; i<Math.min(amount, AbstractDungeon.player.drawPile.size()); ++i) {
                tmpGroup.addToTop(AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
            }
            AbstractDungeon.gridSelectScreen.open(tmpGroup, getAmount, anyNumber, SeekAction.TEXT[getAmount == 1 ? 0 : 1]);
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.player.drawPile.moveToHand(c, AbstractDungeon.player.drawPile);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }
}
