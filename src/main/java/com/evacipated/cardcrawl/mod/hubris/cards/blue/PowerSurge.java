package com.evacipated.cardcrawl.mod.hubris.cards.blue;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;

public class PowerSurge extends CustomCard implements StartupCard
{
    public static final String ID = "hubris:PowerSurge";
    public static final String IMG = HubrisMod.BETA_POWER;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;
    private static final int FOCUS_AMT = 2;
    private static final int UPGRADE_FOCUS_AMT = 1;

    public PowerSurge()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.SELF);

        if (HubrisMod.hasBard) {
            tags.add(BuffNote.TAG);
            tags.add(DebuffNote.TAG);
        }
        GraveField.grave.set(this, true);
        AutoplayField.autoplay.set(this, true);
        magicNumber = baseMagicNumber = FOCUS_AMT;
        // Stop this card generating mid-combat
        tags.add(CardTags.HEALING);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FocusPower(p, magicNumber), magicNumber));
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FocusPower(p, -magicNumber), -magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_FOCUS_AMT);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new PowerSurge();
    }
}
