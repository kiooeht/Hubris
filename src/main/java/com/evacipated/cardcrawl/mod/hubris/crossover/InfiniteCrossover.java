package com.evacipated.cardcrawl.mod.hubris.crossover;

import com.evacipated.cardcrawl.mod.hubris.cards.black.Fate;
import com.evacipated.cardcrawl.mod.hubris.cards.black.InfiniteBlow;
import infinitespire.helpers.CardHelper;

public class InfiniteCrossover
{
    public static void Cards()
    {
        CardHelper.addCard(new Fate());
        CardHelper.addCard(new InfiniteBlow());
    }
}
