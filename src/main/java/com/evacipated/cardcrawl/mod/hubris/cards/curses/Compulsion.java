package com.evacipated.cardcrawl.mod.hubris.cards.curses;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.ChooseMustPlayCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class Compulsion extends CustomCard
{
    public static final String ID = "hubris:Compulsion";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = -2;

    private AbstractCard mustPlay = null;

    public Compulsion()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.hasRelic(BlueCandle.ID)) {
            useBlueCandle(p);
        } else {
            AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
        }
    }

    public void setCompulsion(AbstractCard card)
    {
        mustPlay = card;
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f,
                EXTENDED_DESCRIPTION[0] + mustPlay.name + EXTENDED_DESCRIPTION[1], true));
    }

    @Override
    public boolean canPlay(AbstractCard card)
    {
        if (mustPlay != null) {
            if (mustPlay.equals(card) || mustPlay.equals(this)) {
                return true;
            }
            card.cantUseMessage = EXTENDED_DESCRIPTION[0] + mustPlay.name + EXTENDED_DESCRIPTION[1];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed)
    {
        if (cardPlayed != null && cardPlayed.equals(mustPlay)) {
            mustPlay = null;
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        mustPlay = this;
        AbstractDungeon.actionManager.addToBottom(new ChooseMustPlayCardAction(this));
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Compulsion();
    }
}
