package com.evacipated.cardcrawl.mod.hubris.cards.green;

import basemod.abstracts.CustomCard;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SlightOfHand extends CustomCard implements ModalChoice.Callback
{
    public static final String ID = "hubris:SlightOfHand";
    public static final String IMG = null;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private ModalChoice modal;

    public SlightOfHand()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.GREEN, CardRarity.COMMON, CardTarget.SELF);

        modal = new ModalChoiceBuilder()
                .setCallback(this)
                .setColor(CardColor.GREEN)
                .addOption("Draw", "Draw 1 card.", CardTarget.SELF)
                .addOption("Discard", "Discard 1 card.", CardTarget.SELF)
                .create();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        modal.open();
    }

    @Override
    public void optionSelected(AbstractPlayer p, AbstractMonster m, int i)
    {
        switch (i) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, 1, false));
                break;
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SlightOfHand();
    }
}
