package com.evacipated.cardcrawl.mod.hubris.cards.green;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AccuracyPower;

public class Sniper extends CustomCard
{
    public static final String ID = "hubris:Sniper";
    public static final String IMG = HubrisMod.BETA_SKILL;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = -2;
    private static final int ACCURACY = 2;
    private static final int UPGRADE_ACCURACY = 1;

    public Sniper()
    {
        super(ID, NAME, IMG,  COST, DESCRIPTION, CardType.SKILL, CardColor.GREEN, CardRarity.UNCOMMON, CardTarget.NONE);

        magicNumber = baseMagicNumber = ACCURACY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AccuracyPower(p, magicNumber), magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void triggerOnManualDiscard()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AccuracyPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_ACCURACY);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Sniper();
    }
}
