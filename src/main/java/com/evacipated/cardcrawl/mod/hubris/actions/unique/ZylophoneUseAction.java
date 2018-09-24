package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ZylophoneUseAction extends AbstractGameAction
{
    private AbstractCard card;
    private AbstractPlayer player;
    private AbstractMonster monster;
    private int usesLeft;

    public ZylophoneUseAction(AbstractCard card, AbstractPlayer player, AbstractMonster monster, int energyOnUse)
    {
        this.card = card;
        this.player = player;
        this.monster = monster;
        usesLeft = energyOnUse;
    }

    @Override
    public void update()
    {
        isDone = true;

        if (usesLeft > 0 && card.canUse(player, monster)) {
            card.use(player, monster);
            if (usesLeft > 1) {
                AbstractDungeon.actionManager.addToBottom(new ZylophoneUseAction(card, player, monster, usesLeft - 1));
            }
        }
    }
}
