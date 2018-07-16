package com.evacipated.cardcrawl.mod.hubris.cards.red;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StunningStrike extends CustomCard
{
    public static final String ID = "hubris:StunningStrike";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int STUN_LENGTH = 1;

    public StunningStrike()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardColor.RED, CardRarity.RARE, CardTarget.ENEMY);

        exhaust = true;
        baseDamage = DAMAGE;
        baseMagicNumber = STUN_LENGTH;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, baseDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(m, p));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new StunningStrike();
    }
}
