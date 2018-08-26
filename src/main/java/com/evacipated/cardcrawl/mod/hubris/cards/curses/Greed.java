package com.evacipated.cardcrawl.mod.hubris.cards.curses;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.CardNoUnlock;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

@CardNoUnlock
public class Greed extends CustomCard
{
    public static final String ID = "hubris:Greed";
    public static final String IMG = "images/cards/potOfGreed.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;

    public static int GOLD_AMOUNT = 5;

    public Greed()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.NONE);

        SoulboundField.soulbound.set(this, true);
        magicNumber = baseMagicNumber = GOLD_AMOUNT;
    }

    @Override
    public boolean canUse(AbstractPlayer p , AbstractMonster m)
    {
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.hasRelic(BlueCandle.ID)) {
            useBlueCandle(p);
        }

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(CardLibrary.getCopy(ID), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
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
        return new Greed();
    }

    public static int countCopiesInDeck()
    {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.cardID.equals(ID)) {
                ++count;
            }
        }
        return count;
    }
}
