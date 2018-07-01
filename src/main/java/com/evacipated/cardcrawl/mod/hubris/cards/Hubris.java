package com.evacipated.cardcrawl.mod.hubris.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.actions.common.AutoplayCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Hubris extends CustomCard
{
    public static final String ID = "hubris:Hubris";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;

    // TODO: Make Inescapable
    public Hubris()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.SELF);

        isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        // TODO: Do something
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
        return new Hubris();
    }
}
