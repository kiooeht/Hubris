package com.evacipated.cardcrawl.mod.hubris.cards.curses;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.CardNoUnlock;
import com.evacipated.cardcrawl.mod.hubris.actions.common.AutoplayCardAction;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.InescapableField;
import com.evacipated.cardcrawl.mod.hubris.powers.SlothPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

@CardNoUnlock
public class Sloth extends CustomCard
{
    public static final String ID = "hubris:Sloth";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;

    public Sloth()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.SELF);

        InescapableField.inescapable.set(this, true);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(AbstractDungeon.player.energy.energy));
        AbstractDungeon.player.energy.energy = 0;
        if (!p.hasPower(ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SlothPower(p)));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p , AbstractMonster m)
    {
        return true;
    }

    @Override
    public void triggerWhenDrawn()
    {
        AbstractDungeon.actionManager.addToBottom(new AutoplayCardAction(this, AbstractDungeon.player.hand));
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Sloth();
    }
}
