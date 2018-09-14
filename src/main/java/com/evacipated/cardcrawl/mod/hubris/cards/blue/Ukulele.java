package com.evacipated.cardcrawl.mod.hubris.cards.blue;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class Ukulele extends CustomCard
{
    public static final String ID = "hubris:Ukulele";
    public static final String IMG = HubrisMod.BETA_ATTACK;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 1;

    public Ukulele()
    {
        super(ID, NAME, IMG,  COST, DESCRIPTION, CardType.ATTACK, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        baseMagicNumber = 0;
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof Lightning) {
                ++baseMagicNumber;
            }
        }
        magicNumber = baseMagicNumber;

        for (int i=0; i<baseMagicNumber; ++i) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            AbstractDungeon.effectList.add(new LightningEffect(m.drawX, m.drawY));
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
        }
    }

    private void setDescription()
    {
        if (baseMagicNumber == 1) {
            rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        } else {
            rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        baseMagicNumber = 0;
        magicNumber = 0;
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof Lightning) {
                ++baseMagicNumber;
            }
        }
        if (baseMagicNumber > 0) {
            setDescription();
        }
    }

    @Override
    public void onMoveToDiscard()
    {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m)
    {
        super.calculateCardDamage(m);
        if (baseMagicNumber > 0) {
            setDescription();
        }
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
        return new Ukulele();
    }
}
