package com.evacipated.cardcrawl.mod.hubris.patches.monsters.MonsterGroup;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.monsters.GrandSnecko;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

@SpirePatch(
        cls="com.megacrit.cardcrawl.monsters.MonsterGroup",
        method="render"
)
public class ReverseRender
{
    @SpireInsertPatch(
            rloc=5
    )
    public static SpireReturn Insert(MonsterGroup __instance, SpriteBatch sb)
    {
        boolean containsGrandSnecko = false;
        for (AbstractMonster m : __instance.monsters) {
            if (m instanceof GrandSnecko) {
                containsGrandSnecko = true;
                break;
            }
        }

        if (containsGrandSnecko) {
            for (int i=__instance.monsters.size()-1; i>=0; --i) {
                __instance.monsters.get(i).render(sb);
            }
            return SpireReturn.Return(null);
        } else {
            return SpireReturn.Continue();
        }
    }
}
