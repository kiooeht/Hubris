package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.powers.TimeStopPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.monsters.MonsterGroup",
        method="applyEndOfTurnPowers"
)
public class TimeStopPatch
{
    private static ArrayList<AbstractMonster> empty = new ArrayList<>();
    private static ArrayList<AbstractMonster> savedMonsters;

    public static void Prefix(MonsterGroup __instance)
    {
        savedMonsters = __instance.monsters;
        if (AbstractDungeon.player.hasPower(TimeStopPower.POWER_ID)) {
            __instance.monsters = empty;
        }
    }

    public static void Postfix(MonsterGroup __instance)
    {
        __instance.monsters = savedMonsters;
    }
}
