package com.evacipated.cardcrawl.mod.hubris.cards.red;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.SharpeningStoneAction;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.SharpeningStoneRandomAction;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SharpeningStone extends CustomCard implements StartupCard
{
    public static final String ID = "hubris:SharpeningStone";
    public static final String IMG = HubrisMod.BETA_SKILL;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 0;
    private static final int AMT = 2;

    public SharpeningStone()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.RED, CardRarity.UNCOMMON, CardTarget.SELF);

        exhaust = true;
        magicNumber = baseMagicNumber = AMT;
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new SharpeningStoneRandomAction(AbstractDungeon.player, 1));
            return true;
        }
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new SharpeningStoneAction(p, magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SharpeningStone();
    }
}
