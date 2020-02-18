package com.evacipated.cardcrawl.mod.hubris.cards.EnhanceAbility;

import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@CardIgnore
public class BearsEndurance extends AbstractCard
{
    public static final String ID = "hubris:BearsEndurance";
    public static final String IMG = "colorless/power/panache";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private int amt;
    private int upgrade_amt;

    public BearsEndurance(int amount, int upgrade_amount)
    {
        super(ID, NAME, IMG, IMG, -2, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        magicNumber = baseMagicNumber = amount;
        amt = amount;
        upgrade_amt = upgrade_amount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption()
    {
        addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(upgrade_amt);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BearsEndurance(amt, upgrade_amt);
    }
}
