package com.evacipated.cardcrawl.mod.hubris.cards.curses;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.CardNoSeen;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@CardNoSeen
public class Hubris extends CustomCard
{
    public static final String ID = "hubris:Hubris";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;

    private static String[] sins = new String[]{Envy.ID, Gluttony.ID, Greed.ID, Lust.ID, Sloth.ID, Wrath.ID};

    public Hubris()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.SELF);

        SoulboundField.soulbound.set(this, true);
        AutoplayField.autoplay.set(this, true);
        isInnate = true;
        purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        String sin = sins[AbstractDungeon.cardRandomRng.random(sins.length-1)];
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(CardLibrary.getCopy(sin), 1, true, true));
    }

    @Override
    public boolean canUse(AbstractPlayer p , AbstractMonster m)
    {
        return true;
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
