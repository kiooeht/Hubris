package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.NoDiscardField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CleavingFinishDeathCheckAction extends AbstractGameAction
{
    private static final float DURATION = 0.1f;
    private AbstractCard card;

    public CleavingFinishDeathCheckAction(AbstractCard card, AbstractMonster monster)
    {
        this.card = card;
        target = monster;
        actionType = ActionType.SPECIAL;
        duration = DURATION;
    }

    @Override
    public void update()
    {
        if (duration == DURATION && target != null) {
            if (target.isDying || target.currentHealth <= 0) {
                NoDiscardField.noDiscard.set(card, true);
                card.setCostForTurn(0);
            }
        }
        tickDuration();
    }
}
