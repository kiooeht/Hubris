package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.shop.BloodShopScreen;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class BloodShopPatch
{
    @SpirePatch(
            clz= Merchant.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez={
                    float.class,
                    float.class,
                    int.class
            }
    )
    public static class Open1
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(ShopScreen.class.getName()) && m.getMethodName().equals("init")) {
                        m.replace(HubrisMod.class.getName() + ".bloodShopScreen.init();");
                    }
                }
            };
        }
    }
    @SpirePatch(
            clz= Merchant.class,
            method="update"
    )
    public static class Open2
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(ShopScreen.class.getName()) && m.getMethodName().equals("open")) {
                        m.replace(HubrisMod.class.getName() + ".bloodShopScreen.open();");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="update"
    )
    public static class Update
    {
        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(AbstractDungeon __instance)
        {
            if (AbstractDungeon.screen == BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP) {
                HubrisMod.bloodShopScreen.update();
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="render"
    )
    public static class Render
    {
        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SpriteBatch sb)
        {
            if (AbstractDungeon.screen == BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP) {
                HubrisMod.bloodShopScreen.render(sb);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="openPreviousScreen"
    )
    public static class OpenPreviousScreen
    {
        public static void Postfix(AbstractDungeon.CurrentScreen s)
        {
            if (s == BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP) {
                HubrisMod.bloodShopScreen.open();
            }
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="closeCurrentScreen"
    )
    public static class CloseCurrentScreen
    {
        public static void Prefix()
        {
            if (AbstractDungeon.screen == BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP) {
                CardCrawlGame.sound.play("SHOP_CLOSE");
                //AbstractDungeon.genericScreenOverlayReset();
                if (AbstractDungeon.previousScreen == null) {
                    if (AbstractDungeon.player.isDead) {
                        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
                    } else {
                        AbstractDungeon.isScreenUp = false;
                        AbstractDungeon.overlayMenu.hideBlackScreen();
                    }
                }
                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead) {
                    AbstractDungeon.overlayMenu.showCombatPanels();
                }
                AbstractDungeon.overlayMenu.cancelButton.hide();
            }
        }
    }
}
