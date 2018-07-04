package com.evacipated.cardcrawl.mod.hubris.cards.curses;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.CardNoUnlock;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.InescapableField;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;

public class NaturalOne extends CustomCard
{
    public static final String ID = "hubris:NaturalOne";
    public static final String IMG = "images/cards/icosahedron.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = -2;
    private static final int LOSE_ENERGY_AMT = 1;

    public NaturalOne()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.NONE);

        InescapableField.inescapable.set(this, true);
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

    @Override
    public void triggerWhenDrawn()
    {
        AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(LOSE_ENERGY_AMT));
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
        return new NaturalOne();
    }
}
