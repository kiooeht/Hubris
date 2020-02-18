package com.evacipated.cardcrawl.mod.hubris.cards.EnhanceAbility;

import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

@CardIgnore
public class BullsStrength extends AbstractCard
{
    public static final String ID = "hubris:BullsStrength";
    public static final String IMG = "red/power/inflame";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static int amt;
    private static int upgrade_amt;

    public BullsStrength(int amount, int upgrade_amount)
    {
        super(ID, NAME, IMG, IMG, -2, DESCRIPTION, CardType.POWER, CardColor.RED, CardRarity.SPECIAL, CardTarget.SELF);

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
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, magicNumber), magicNumber));
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
        return new BullsStrength(amt, upgrade_amt);
    }
}
