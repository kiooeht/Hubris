package com.evacipated.cardcrawl.mod.hubris.actions.common;

import com.evacipated.cardcrawl.mod.hubris.vfx.cardManip.AutoplayCardEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

public class AutoplayCardAction extends AbstractGameAction
{
    private AbstractCard card;
    private CardGroup group;

    public AutoplayCardAction(AbstractCard card, CardGroup group)
    {
        this.card = card;
        this.group = group;
    }

    @Override
    public void update()
    {
        for (AbstractGameEffect effect : AbstractDungeon.topLevelEffects) {
            if (effect instanceof PlayerTurnEffect || effect instanceof BattleStartEffect) {
                return;
            }
        }
        isDone = true;
        group.removeCard(card);
        AbstractDungeon.effectList.add(new AutoplayCardEffect(card));
    }
}
