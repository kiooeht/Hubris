package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

public class BefuddledAction extends AbstractGameAction
{
    private AbstractCard c;

    public BefuddledAction(AbstractCard card)
    {
        c = card;
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.actionManager.cleanCardQueue();
            if (AbstractDungeon.player.hand.group.isEmpty()) {
                isDone = true;
                return;
            }

            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        }

        tickDuration();

        if (isDone) {
            int index = AbstractDungeon.player.hand.group.indexOf(c);
            System.out.println(index);
            AbstractDungeon.player.hand.removeCard(c);
            AbstractDungeon.transformCard(c);
            AbstractCard transformedCard = AbstractDungeon.getTransformedCard();
            transformedCard.current_x = transformedCard.target_x = c.current_x;
            transformedCard.current_y = transformedCard.target_y = c.current_y;
            transformedCard.setAngle(c.angle, true);
            AbstractDungeon.player.hand.group.add(index, transformedCard);
            AbstractDungeon.player.hand.refreshHandLayout();
        }
    }
}
