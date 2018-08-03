package com.evacipated.cardcrawl.mod.hubris.characters;

import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class FakePlayer extends AbstractPlayer
{
    public FakePlayer()
    {
        super("FAKE", PlayerClass.DEFECT);
    }

    @Override
    protected void initializeStarterDeck()
    {

    }

    @Override
    protected void initializeStarterRelics(PlayerClass chosenClass)
    {

    }
}
