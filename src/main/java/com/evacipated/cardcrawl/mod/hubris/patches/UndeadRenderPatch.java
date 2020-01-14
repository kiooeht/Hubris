package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.powers.UndeadPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.Arrays;

@SpirePatch(
        clz=AbstractMonster.class,
        method="render"
)
public class UndeadRenderPatch
{
    private static ShaderProgram shader = new ShaderProgram(
            Gdx.files.internal(HubrisMod.assetPath("shaders/undead/vertexShader.vs")),
            Gdx.files.internal(HubrisMod.assetPath("shaders/undead/fragShader.fs"))
    );

    @SpireInsertPatch(
            locator=LocatorImageStart.class,
            localvars={"atlas"}
    )
    public static void InsertImageStart(AbstractMonster __instance, SpriteBatch sb, TextureAtlas atlas)
    {
        if (atlas == null) {
            UndeadPower undead = (UndeadPower) __instance.getPower(UndeadPower.POWER_ID);
            if (undead != null) {
                shader.begin();
                shader.setUniformf("shadeTimer", undead.shaderTimer);
                sb.setShader(shader);
            }
        }
    }

    @SpireInsertPatch(
            locator=LocatorImageEnd.class,
            localvars={"atlas"}
    )
    public static void InsertImageEnd(AbstractMonster __instance, SpriteBatch sb, TextureAtlas atlas)
    {
        if (atlas == null && __instance.hasPower(UndeadPower.POWER_ID)) {
            sb.setShader(null);
            shader.end();
        }
    }

    @SpireInsertPatch(
            locator=LocatorSkeletonStart.class
    )
    public static void InsertSkeletonStart(AbstractMonster __instance, SpriteBatch sb)
    {
        UndeadPower undead = (UndeadPower) __instance.getPower(UndeadPower.POWER_ID);
        if (undead != null) {
            shader.begin();
            shader.setUniformf("shadeTimer", undead.shaderTimer);
            CardCrawlGame.psb.setShader(shader);
        }
    }

    @SpireInsertPatch(
            locator=LocatorSkeletonEnd.class
    )
    public static void InsertSkeletonEnd(AbstractMonster __instance, SpriteBatch sb)
    {
        if (__instance.hasPower(UndeadPower.POWER_ID)) {
            CardCrawlGame.psb.setShader(null);
            shader.end();
        }
    }

    private static class LocatorImageStart extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "atlas");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class LocatorImageEnd extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "isDying");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class LocatorSkeletonStart extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(SkeletonMeshRenderer.class, "draw");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class LocatorSkeletonEnd extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(PolygonSpriteBatch.class, "end");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
