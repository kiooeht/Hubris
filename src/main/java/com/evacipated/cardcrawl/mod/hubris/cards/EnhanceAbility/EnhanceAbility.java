package com.evacipated.cardcrawl.mod.hubris.cards.EnhanceAbility;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class EnhanceAbility extends CustomCard
{
    public static final String ID = "hubris:EnhanceAbility";
    public static final String IMG = HubrisMod.BETA_POWER;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int AMT = 1;
    private static final int UPGRADE_AMT = 1;

    public EnhanceAbility()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        if (HubrisMod.hasBard) {
            tags.add(WildCardNote.TAG);
        }

        magicNumber = baseMagicNumber = AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new BullsStrength(AMT, UPGRADE_AMT));
        choices.add(new CatsGrace(AMT, UPGRADE_AMT));
        choices.add(new FoxsCunning(AMT, UPGRADE_AMT));
        choices.add(new BearsEndurance(AMT * 10, UPGRADE_AMT * 10));

        if (upgraded) {
            choices.forEach(AbstractCard::upgrade);
        }

        addToBot(new ChooseOneAction(choices));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMT);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new EnhanceAbility();
    }
}
