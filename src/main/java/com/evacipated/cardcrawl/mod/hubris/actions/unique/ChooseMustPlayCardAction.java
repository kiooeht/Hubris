package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.cards.curses.Compulsion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChooseMustPlayCardAction extends AbstractGameAction
{
    final Compulsion compulsion;

    public ChooseMustPlayCardAction(Compulsion compulsion)
    {
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.WAIT;
        source = AbstractDungeon.player;
        this.compulsion = compulsion;
    }

    @Override
    public void update()
    {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.canUse(AbstractDungeon.player, (AbstractMonster) target)) {
                tmp.addToTop(c);
            }
        }

        if (tmp.size() == 0) {
            isDone = true;
            return;
        }

        compulsion.setCompulsion(tmp.getRandomCard(AbstractDungeon.cardRng));

        isDone = true;
    }
}
