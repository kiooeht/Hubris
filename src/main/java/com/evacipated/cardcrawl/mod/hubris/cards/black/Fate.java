package com.evacipated.cardcrawl.mod.hubris.cards.black;

import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.FateAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.abstracts.BlackCard;
import infinitespire.patches.CardColorEnumPatch;

@CardIgnore
public class Fate extends BlackCard
{
    public static final String ID = "hubris:Fate";
    public static final String IMG = HubrisMod.BETA_SKILL;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;
    private static final int CHOICES = 3;
    private static final int UPGRADE_CHOICES = 2;

    public Fate()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.NONE);
        // Otherwise, BlackCard makes it "infinitespire:ID"
        cardID = ID;

        exhaust = true;
        magicNumber = baseMagicNumber = CHOICES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new FateAction(cardID, CardColorEnumPatch.CardColorPatch.INFINITE_BLACK, true, magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CHOICES);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Fate();
    }
}
