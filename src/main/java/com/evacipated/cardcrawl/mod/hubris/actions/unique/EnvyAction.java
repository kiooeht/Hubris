package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.patches.CurseForTurnPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EnvyAction extends AbstractGameAction
{
    public EnvyAction()
    {
        setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.canUse(AbstractDungeon.player, null)) {
                    tmp.addToTop(c);
                }
            }
            if (!tmp.isEmpty()) {
                AbstractCard card = tmp.getRandomCard(AbstractDungeon.cardRng);
                card.color = AbstractCard.CardColor.CURSE;
                CurseForTurnPatch.Field.curseForTurn.set(card, true);
            }
        }
        tickDuration();
    }
}
