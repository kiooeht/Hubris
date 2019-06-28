package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.hubris.relics.OldNail;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.function.Predicate;

public class PureNailPatch
{
    private static float doubleDamage(AbstractCard card, float damage)
    {
        AbstractRelic nail = AbstractDungeon.player.getRelic(OldNail.ID);
        if (nail != null && nail.counter == -42 && card.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
            damage *= 2;
            if (card.baseDamage != damage) {
                card.isDamageModified = true;
            }
        }
        return damage;
    }

    private static float doubleDamage(DamageInfo info, float damage)
    {
        if (info.owner == AbstractDungeon.player) {
            AbstractRelic nail = AbstractDungeon.player.getRelic(OldNail.ID);
            if (nail != null && nail.counter == -42 && info.type == DamageInfo.DamageType.NORMAL) {
                damage *= 2;
                if (info.base != damage) {
                    info.isModified = true;
                }
            }
        }
        return damage;
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="applyPowers"
    )
    public static class ApplyPowersSingle
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"tmp"}
        )
        public static void Insert(AbstractCard __instance, @ByRef float[] tmp)
        {
            tmp[0] = doubleDamage(__instance, tmp[0]);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="applyPowers"
    )
    public static class ApplyPowersMulti
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"tmp", "i"}
        )
        public static void Insert(AbstractCard __instance, float[] tmp, int i)
        {
            tmp[i] = doubleDamage(__instance, tmp[i]);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class CalculateCardSingle
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"tmp"}
        )
        public static void Insert(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp)
        {
            tmp[0] = doubleDamage(__instance, tmp[0]);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class CalculateCardMulti
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"tmp", "i"}
        )
        public static void Insert(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i)
        {
            tmp[i] = doubleDamage(__instance, tmp[i]);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                matchers.add(finalMatcher);
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=DamageInfo.class,
            method="applyPowers"
    )
    public static class DamageInfoApplyPowers
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"tmp"}
        )
        public static void Insert(DamageInfo __instance, AbstractCreature owner, AbstractCreature target, @ByRef float[] tmp)
        {
            tmp[0] = doubleDamage(__instance, tmp[0]);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
