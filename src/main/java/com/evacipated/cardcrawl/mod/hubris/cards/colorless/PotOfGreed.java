package com.evacipated.cardcrawl.mod.hubris.cards.colorless;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.Greed;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class PotOfGreed extends CustomCard
{
    public static final String ID = "hubris:PotOfGreed";
    public static final String IMG = "images/cards/potOfGreed.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;

    public PotOfGreed()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 2));
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Greed(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
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
        return new PotOfGreed();
    }
}
