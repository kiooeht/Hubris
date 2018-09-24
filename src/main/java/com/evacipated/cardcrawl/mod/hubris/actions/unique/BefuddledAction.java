package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BefuddledAction extends AbstractGameAction
{
    private AbstractCard c;

    public BefuddledAction(AbstractCard card)
    {
        c = card;
        AbstractDungeon.player.limbo.addToBottom(c);
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
            c.setAngle(0);
            c.target_x = Settings.WIDTH / 2.0f;
            c.target_y = Settings.HEIGHT / 2.0f;
            c.targetTransparency = c.transparency = 1.0f;
        }

        if (c.current_x == c.target_x && c.current_y == c.target_y) {
            tickDuration();
        }

        if (isDone) {
            int index = AbstractDungeon.player.hand.group.indexOf(c);
            if (index < 0) {
                return;
            }
            AbstractDungeon.player.hand.removeCard(c);
            AbstractDungeon.player.limbo.removeCard(c);
            AbstractDungeon.srcTransformCard(c);
            AbstractCard transformedCard = AbstractDungeon.getTransformedCard();
            transformedCard.current_x = transformedCard.target_x = c.current_x;
            transformedCard.current_y = transformedCard.target_y = c.current_y;
            AbstractDungeon.player.hand.group.add(index, transformedCard);
            transformedCard.beginGlowing();
            transformedCard.targetTransparency = transformedCard.transparency = 1.0f;
            AbstractDungeon.player.hand.refreshHandLayout();
        }
    }
}
