package com.evacipated.cardcrawl.mod.hubris.cards;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@CardIgnore
public class DisguiseKitOption extends AbstractCard
{
    public static final String ID = "hubris:DisguiseKitOption";

    public AbstractPlayer.PlayerClass chosenClass;

    public DisguiseKitOption(AbstractPlayer.PlayerClass chosenClass)
    {
        super(ID, BaseMod.findCharacter(chosenClass).getLocalizedCharacterName(),
                null, null, -2, "", CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        this.chosenClass = chosenClass;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
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
        return new DisguiseKitOption(chosenClass);
    }
}
