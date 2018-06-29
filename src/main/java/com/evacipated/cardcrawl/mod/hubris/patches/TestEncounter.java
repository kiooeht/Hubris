package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.monsters.GrandSnecko;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

@SpirePatch(
        cls="com.megacrit.cardcrawl.helpers.MonsterHelper",
        method="getEncounter"
)
public class TestEncounter
{
    public static Object Replace(String key)
    {
        return new MonsterGroup(new GrandSnecko());
    }
}
