package com.evacipated.cardcrawl.mod.hubris.patches.falselife;

import com.evacipated.cardcrawl.mod.hubris.vfx.combat.TempDamageNumberEffect;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.reflect.Field;

@SpirePatch(
        cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
        method="damage"
)
public class PlayerDamage
{
    @SpireInsertPatch(
            rloc=14,
            localvars={"damageAmount"}
    )
    public static void Insert(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount)
    {
        if (damageAmount[0] <= 0) {
            return;
        }

        try {
            Field f = AbstractCreature.class.getDeclaredField("temporaryHealth");
            int temporaryHealth = (int)f.get(__instance);
            if (temporaryHealth > 0) {
                if (temporaryHealth >= damageAmount[0]) {
                    temporaryHealth -= damageAmount[0];
                    AbstractDungeon.effectsQueue.add(new TempDamageNumberEffect(__instance, __instance.hb.cX, __instance.hb.cY, damageAmount[0]));
                    //AbstractDungeon.effectList.add(new BlockedWordEffect(__instance, __instance.hb.cX, __instance.hb.cY, "Absorbed"));
                    damageAmount[0] = 0;
                } else {
                    damageAmount[0] -= temporaryHealth;
                    AbstractDungeon.effectsQueue.add(new TempDamageNumberEffect(__instance, __instance.hb.cX, __instance.hb.cY, temporaryHealth));
                    temporaryHealth = 0;
                }

                f.set(__instance, temporaryHealth);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("Final damage: " + damageAmount[0]);
    }
}
