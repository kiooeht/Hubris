package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.monsters.MusketHawk;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

public class GetEncounterPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.helpers.MonsterHelper",
            method="getEncounter"
    )
    public static class GetEncounter
    {
        private static final boolean testing = false;
        private static boolean doneOnce = false;

        public static void Prefix(@ByRef String[] key)
        {
            if (testing) {
                if (!doneOnce) {
                    doneOnce = true;
                    key[0] = MusketHawk.ID;
                }
            }
        }
    }
}
