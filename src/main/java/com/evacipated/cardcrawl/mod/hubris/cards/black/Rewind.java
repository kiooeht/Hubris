package com.evacipated.cardcrawl.mod.hubris.cards.black;

import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.RewindAction;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.abstracts.BlackCard;

@CardIgnore
public class Rewind extends BlackCard
{
    public static final String ID = "hubris:Rewind";
    public static final String IMG = HubrisMod.BETA_SKILL;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 5;

    public Rewind()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.NONE);
        // Otherwise, BlackCard makes it "infinitespire:ID"
        cardID = ID;

        purgeOnUse = true;
        isInnate = true;
    }

    @Override
    public void useWithEffect(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new RewindAction());
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            isInnate = false;
            GraveField.grave.set(this, true);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Rewind();
    }
}
