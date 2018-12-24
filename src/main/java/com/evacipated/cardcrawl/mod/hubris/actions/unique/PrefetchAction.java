package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.InnateOncePatch;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;

public class PrefetchAction extends AbstractGameAction
{
    public PrefetchAction()
    {
        setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (!c.isInnate && !c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado) {
                    AbstractCard masterDeckCard = StSLib.getMasterDeckEquivalent(c);
                    if (masterDeckCard != null) {
                        group.addToTop(masterDeckCard);
                    }
                }
            }

            if (group.size() == 0) {
                isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(group, amount, "TODO", false);
            tickDuration();
            return;
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() == amount) {
            for (int i=0; i<AbstractDungeon.gridSelectScreen.selectedCards.size(); ++i) {
                AbstractDungeon.gridSelectScreen.selectedCards.get(i).unhover();
                InnateOncePatch.Field.isInnateOnce.set(AbstractDungeon.gridSelectScreen.selectedCards.get(i), true);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }
}
