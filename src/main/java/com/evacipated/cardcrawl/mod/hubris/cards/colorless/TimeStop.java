package com.evacipated.cardcrawl.mod.hubris.cards.colorless;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.powers.TimeStopPower;
import com.evacipated.cardcrawl.mod.hubris.relics.Pocketwatch2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TimeStop extends CustomCard
{
    public static final String ID = "hubris:TimeStop";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final int COST = 0;

    public TimeStop()
    {
        super(ID, NAME, null,  COST, DESCRIPTION, AbstractCard.CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);

        exhaust = true;
        setDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractRelic relic = p.getRelic(Pocketwatch2.ID);
        if (relic != null){
            relic.flash();
            relic.setCounter(relic.counter - 1);
            if (relic.counter <= 0) {
                purgeOnUse = true;
            }

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TimeStopPower(p, 1), 1));
        }
    }

    private void setDescription()
    {
        int useCount = 0;

        if (AbstractDungeon.player != null) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(Pocketwatch2.ID);
            if (relic != null) {
                useCount = relic.counter;
            }
        } else {
            // Fake so description includes Exhaust in card library
            useCount = 12;
        }

        if (useCount > 1) {
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

        setDescription();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        setDescription();
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
        return new TimeStop();
    }
}
