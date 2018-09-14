package com.evacipated.cardcrawl.mod.hubris.cards.curses;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;

import java.lang.reflect.Field;

public class Gluttony extends CustomCard
{
    public static final String ID = "hubris:Gluttony";
    public static final String IMG = HubrisMod.BETA_SKILL;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = -2;

    public Gluttony()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.NONE);

        SoulboundField.soulbound.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.hasRelic(BlueCandle.ID)) {
            useBlueCandle(p);
        } else {
            AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        Field targetCard;
        try {
            targetCard = UseCardAction.class.getDeclaredField("targetCard");
            targetCard.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action instanceof UseCardAction) {
                try {
                    if (targetCard.get(action) == c) {
                        ((UseCardAction) action).exhaustCard = true;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
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
        return new Gluttony();
    }
}
