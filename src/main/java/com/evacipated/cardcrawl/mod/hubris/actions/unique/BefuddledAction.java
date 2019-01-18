package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.List;

public class BefuddledAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("BefuddledAction");
    public static final String[] TEXT = uiStrings.TEXT;

    private int amount;
    private List<AbstractCard> cannotTransform = new ArrayList<>();

    public BefuddledAction(int amount)
    {
        this.amount = amount;
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        if (duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.group.isEmpty()) {
                isDone = true;
                return;
            }

            for (AbstractCard c : p.hand.group) {
                if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) {
                    cannotTransform.add(c);
                }
            }
            if (cannotTransform.size() == p.hand.size()) {
                isDone = true;
                return;
            }

            p.hand.group.removeAll(cannotTransform);
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], Math.min(amount, p.hand.size()),
                    false, false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                // Transform
                AbstractDungeon.srcTransformCard(c);
                AbstractCard transformedCard = AbstractDungeon.getTransformedCard();
                transformedCard.current_x = transformedCard.target_x = c.current_x;
                transformedCard.current_y = transformedCard.target_y = c.current_y;
                transformedCard.superFlash();
                AbstractDungeon.player.hand.addToTop(transformedCard);
                AbstractDungeon.player.hand.refreshHandLayout();
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.player.hand.glowCheck();
            isDone = true;
        }

        tickDuration();
    }

    private void returnCards()
    {
        for (AbstractCard c : cannotTransform) {
            AbstractDungeon.player.hand.addToTop(c);
        }
        AbstractDungeon.player.hand.refreshHandLayout();
    }
}
