package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ReverseEnlightenmentAction extends AbstractGameAction
{
    public ReverseEnlightenmentAction()
    {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                c.costForTurn += 1;
                c.isCostModifiedForTurn = true;
                c.cost += 1;
                c.isCostModified = true;
            }
        }
        tickDuration();
    }
}
