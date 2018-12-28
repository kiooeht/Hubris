package com.evacipated.cardcrawl.mod.hubris.cards.status;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.monsters.NecromanticTotem;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EvolvePower;
import com.megacrit.cardcrawl.powers.NoDrawPower;

public class NegativeEnergy extends CustomCard
{
    public static final String ID = "hubris:NegativeEnergy";
    public static final String IMG = HubrisMod.assetPath("images/cards/acid.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int HEAL = 25;

    public NegativeEnergy()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        exhaust = true;
        AlwaysRetainField.alwaysRetain.set(this, true);
        baseMagicNumber = magicNumber = HEAL;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        if (cardPlayable(m) && hasEnoughEnergy()) {
            return true;
        }
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (mo.id.equals(NecromanticTotem.ID)) {
                AbstractDungeon.actionManager.addToBottom(new HealAction(mo, p, magicNumber));
            }
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (AbstractDungeon.player.hasPower(EvolvePower.POWER_ID) && !AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID)) {
            AbstractPower evolve = AbstractDungeon.player.getPower(EvolvePower.POWER_ID);
            evolve.flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, evolve.amount));
        }
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
        return new NegativeEnergy();
    }
}
