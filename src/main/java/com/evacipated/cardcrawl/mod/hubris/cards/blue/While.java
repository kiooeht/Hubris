package com.evacipated.cardcrawl.mod.hubris.cards.blue;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.WhileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class While extends CustomCard
{
    public static final String ID = "hubris:While";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;

    public While()
    {
        super(ID, NAME, null,  COST, DESCRIPTION, CardType.SKILL, CardColor.BLUE, CardRarity.RARE, CardTarget.SELF);

        exhaust = true;
        rawDescription = DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new WhileAction());
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new While();
    }
}
