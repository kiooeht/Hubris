package com.evacipated.cardcrawl.mod.hubris.cards.red;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.powers.ReadiedActionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReadiedAction extends CustomCard
{
    public static final String ID = "hubris:ReadiedAction";
    public static final String IMG = HubrisMod.BETA_SKILL;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;
    private static final int AMT = 1;
    private static final int UPGRADE_AMT = 1;

    public ReadiedAction()
    {
        super(ID, NAME, IMG,  COST, DESCRIPTION, AbstractCard.CardType.SKILL, CardColor.RED, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);

        magicNumber = baseMagicNumber = AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ReadiedActionPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMT);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new ReadiedAction();
    }
}
