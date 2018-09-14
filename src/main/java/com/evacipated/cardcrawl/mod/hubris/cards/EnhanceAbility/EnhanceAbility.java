package com.evacipated.cardcrawl.mod.hubris.cards.EnhanceAbility;

import basemod.abstracts.CustomCard;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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

    private ModalChoice modal;

    public EnhanceAbility()
    {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        magicNumber = baseMagicNumber = AMT;
        makeModal();
    }

    private void makeModal()
    {
        modal = new ModalChoiceBuilder()
                .addOption(new BullsStrength(AMT, UPGRADE_AMT))
                .addOption(new CatsGrace(AMT, UPGRADE_AMT))
                .addOption(new FoxsCunning(AMT, UPGRADE_AMT))
                .addOption(new BearsEndurance(AMT * 10, UPGRADE_AMT * 10))
                .create();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        modal.open();
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMT);
            for (int i=0; i<4; ++i) {
                modal.getCard(i).upgrade();
            }
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new EnhanceAbility();
    }
}
