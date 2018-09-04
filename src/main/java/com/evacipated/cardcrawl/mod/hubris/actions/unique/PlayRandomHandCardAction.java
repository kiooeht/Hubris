package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PlayRandomHandCardAction extends AbstractGameAction
{
    public PlayRandomHandCardAction(AbstractMonster target)
    {
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.WAIT;
        source = AbstractDungeon.player;
        this.target = target;
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

        AbstractCard card = tmp.getRandomCard(AbstractDungeon.cardRng);
        card.applyPowers();
        AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
        if (!Settings.FAST_MODE) {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        } else {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }

        isDone = true;
    }
}
