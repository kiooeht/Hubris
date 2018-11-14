package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PiercingTag;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PiercingPatch
{
    @SpireEnum
    public static DamageInfo.DamageType PIERCING;

    @SpirePatch(
            clz=DamageInfo.class,
            method=SpirePatch.CLASS
    )
    public static class PiercingField
    {
        public static SpireField<Boolean> isPiercing = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="resetAttributes"
    )
    public static class DamageTypeReset
    {
        public static void Postfix(AbstractCard __instance)
        {
            if (__instance.hasTag(PiercingTag.HUBRIS_PIERCING)) {
                __instance.damageTypeForTurn = PIERCING;
            }
        }
    }

    @SpirePatch(
            clz=DamageInfo.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez={
                    AbstractCreature.class,
                    int.class,
                    DamageInfo.DamageType.class
            }
    )
    public static class MakeDamageInfoPiercing
    {
        public static void Prefix(DamageInfo __instance, AbstractCreature damageSource, int base, @ByRef DamageInfo.DamageType[] type)
        {
            if (type[0] == PIERCING) {
                type[0] = DamageInfo.DamageType.NORMAL;
                PiercingField.isPiercing.set(__instance, true);
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="applyPowers"
    )
    public static class ApplyPowersFakeNormalDamage
    {
        // Fake damage type as NORMAL for powers to apply normally
        public static void Prefix(AbstractCard __instance)
        {
            if (__instance.hasTag(PiercingTag.HUBRIS_PIERCING)) {
                __instance.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
            }
        }

        public static void Postfix(AbstractCard __instance) {
            if (__instance.hasTag(PiercingTag.HUBRIS_PIERCING)) {
                __instance.damageTypeForTurn = PIERCING;
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class CalculateCardDamageFakeNormalDamage
    {
        // Fake damage type as NORMAL for powers to apply normally
        public static void Prefix(AbstractCard __instance, AbstractMonster mo)
        {
            if (__instance.hasTag(PiercingTag.HUBRIS_PIERCING)) {
                __instance.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
            }
        }

        public static void Postfix(AbstractCard __instance, AbstractMonster mo) {
            if (__instance.hasTag(PiercingTag.HUBRIS_PIERCING)) {
                __instance.damageTypeForTurn = PIERCING;
            }
        }
    }

    @SpirePatch(
            clz=AbstractCreature.class,
            method="decrementBlock"
    )
    public static class PassThroughBlock
    {
        public static SpireReturn<Integer> Prefix(AbstractCreature __instance, DamageInfo info, int damageAmount)
        {
            if (PiercingField.isPiercing.get(info)) {
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue();
        }
    }
}
