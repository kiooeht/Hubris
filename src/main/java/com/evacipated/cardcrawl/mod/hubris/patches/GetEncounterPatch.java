package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.monsters.GrandSnecko;
import com.evacipated.cardcrawl.mod.hubris.monsters.MusketHawk;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

public class GetEncounterPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.dungeons.TheBeyond",
            method="initializeBoss"
    )
    public static class TheBeyondBoss
    {
        public static void Postfix(TheBeyond __instance)
        {
            AbstractDungeon.bossList.clear();
            AbstractDungeon.bossList.add(GrandSnecko.ID);
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.helpers.MonsterHelper",
            method="getEncounter"
    )
    public static class GetEncounter
    {
        private static final boolean testing = true;
        private static boolean done = false;

        public static MonsterGroup Postfix(MonsterGroup __result, String key)
        {
            if (testing) {
                if (!done) {
                    done = true;
                    return new MonsterGroup(new MusketHawk());
                }
                return __result;
            } else {
                if (key.equals(GrandSnecko.ID)) {
                    return new MonsterGroup(new GrandSnecko());
                } else if (key.equals(MusketHawk.ID)) {
                    return new MonsterGroup(new MusketHawk());
                }
                return __result;
            }
        }
    }
}
