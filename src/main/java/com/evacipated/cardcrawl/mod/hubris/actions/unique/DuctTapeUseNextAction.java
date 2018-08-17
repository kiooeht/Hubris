package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class DuctTapeUseNextAction extends AbstractGameAction
{
    private List<AbstractCard> cards;
    private int cardIndex;
    private AbstractPlayer player;
    private AbstractMonster monster;

    public DuctTapeUseNextAction(List<AbstractCard> cards, int index, AbstractPlayer p, AbstractMonster m)
    {
        this.cards = cards;
        cardIndex = index;
        player = p;
        monster = m;
    }

    @Override
    public void update()
    {
        cards.get(cardIndex).calculateCardDamage(monster);
        cards.get(cardIndex).use(player, monster);

        ++cardIndex;
        if (cardIndex < cards.size()) {
            AbstractDungeon.actionManager.addToBottom(new DuctTapeUseNextAction(cards, cardIndex, player, monster));
        }

        isDone = true;
    }
}
