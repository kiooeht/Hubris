package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class FireCardAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FireCardAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractCard.CardType cardType;
    private List<AbstractCard> cannotPlay = new ArrayList<>();

    public FireCardAction(AbstractMonster target, AbstractCard.CardType type)
    {
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.WAIT;
        source = AbstractDungeon.player;
        this.target = target;
        cardType = type;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            if (!AbstractDungeon.player.hand.isEmpty()) {
                if (cardType != null) {
                    for (AbstractCard c : AbstractDungeon.player.hand.group) {
                        if (c.type != cardType) {
                            cannotPlay.add(c);
                        }
                    }
                }
                if (cannotPlay.size() == AbstractDungeon.player.hand.size()) {
                    isDone = true;
                    return;
                }

                AbstractDungeon.player.hand.group.removeAll(cannotPlay);

                if (AbstractDungeon.player.hand.size() == 1) {
                    fireCard(AbstractDungeon.player.hand.getTopCard());
                    returnCards();
                    isDone = true;
                    return;
                }

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                fireCard(c);
            }

            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            isDone = true;
        }
        tickDuration();
    }

    private void fireCard(AbstractCard c)
    {
        AbstractDungeon.player.hand.group.remove(c);
        AbstractDungeon.getCurrRoom().souls.remove(c);
        c.freeToPlayOnce = true;
        c.exhaustOnUseOnce = true;
        AbstractDungeon.player.limbo.group.add(c);
        if (!c.canUse(AbstractDungeon.player, (AbstractMonster) target)) {
            AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.limbo));
        } else {
            c.applyPowers();
            AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(c, target));
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(c));
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }
    }

    private void returnCards()
    {
        for (AbstractCard c : cannotPlay) {
            AbstractDungeon.player.hand.addToTop(c);
        }
        AbstractDungeon.player.hand.refreshHandLayout();
    }
}
