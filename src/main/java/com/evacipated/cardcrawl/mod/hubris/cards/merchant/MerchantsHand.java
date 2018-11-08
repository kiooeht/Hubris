package com.evacipated.cardcrawl.mod.hubris.cards.merchant;

import com.evacipated.cardcrawl.mod.hubris.CardNoSeen;
import com.megacrit.cardcrawl.actions.unique.GreedAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@CardNoSeen
public class MerchantsHand extends AbstractCard
{
    public static final String ID = "hubris:MerchantsHand";
    public static final String IMG = "colorless/attack/handOfGreed";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int ATTACK_DMG = 12;
    private static final int UPGRADE_ATTACK_DMG = 4;
    private static final int GOLD_AMT = 8;
    private static final int UPGRADE_GOLD_AMT = 2;

    public MerchantsHand()
    {
        super(ID, NAME, IMG, IMG, COST, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.BASIC, CardTarget.ENEMY);

        baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = GOLD_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new GreedAction(m, new DamageInfo(p, damage, damageTypeForTurn), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_ATTACK_DMG);
            upgradeMagicNumber(UPGRADE_GOLD_AMT);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new MerchantsHand();
    }
}
