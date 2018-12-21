package com.evacipated.cardcrawl.mod.hubris.crossover;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.hubris.relics.FoxTail;
import com.evacipated.cardcrawl.mod.hubris.relics.MadmansSpellBook;

public class MysticCrossover
{
    public static void Relics()
    {
        BaseMod.addRelicToCustomPool(new FoxTail(), mysticmod.patches.AbstractCardEnum.MYSTIC_PURPLE);
        BaseMod.addRelicToCustomPool(new MadmansSpellBook(), mysticmod.patches.AbstractCardEnum.MYSTIC_PURPLE);
    }
}
