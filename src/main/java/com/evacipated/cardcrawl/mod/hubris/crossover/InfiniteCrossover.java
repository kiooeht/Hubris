package com.evacipated.cardcrawl.mod.hubris.crossover;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.cards.black.Fate;
import com.evacipated.cardcrawl.mod.hubris.cards.black.InfiniteBlow;
import com.evacipated.cardcrawl.mod.hubris.cards.black.Rewind;
import infinitespire.helpers.CardHelper;

public class InfiniteCrossover
{
    public static void Cards()
    {
        // Force other data load here so Infinite Blow has the saved upgrade count
        HubrisMod.loadOtherData();

        CardHelper.addCard(new Fate());
        CardHelper.addCard(new InfiniteBlow());
        CardHelper.addCard(new Rewind());
    }
}
