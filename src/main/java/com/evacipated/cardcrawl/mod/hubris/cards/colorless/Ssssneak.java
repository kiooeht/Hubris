package com.evacipated.cardcrawl.mod.hubris.cards.colorless;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SneckoField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Ssssneak extends CustomCard
{
    public static final String ID = "hubris:Ssssneak";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = -1;
    private static final int EXTRA_DRAW = 1;
    private static final int UPGRADE_EXTRA_DRAW = 1;

    public Ssssneak()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        SneckoField.snecko.set(this, true);
        magicNumber = baseMagicNumber = EXTRA_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int draw = magicNumber;
        if (cost >= 0) {
            draw += cost;
            if (upgraded) {
                draw += cost;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, draw));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_EXTRA_DRAW);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Ssssneak();
    }
}
