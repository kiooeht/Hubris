package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.rooms.BloodShopRoom;
import com.evacipated.cardcrawl.mod.hubris.shop.BloodShopScreen;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class BloodShopPatch
{
    @SpirePatch(
            clz=Merchant.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez={
                    float.class,
                    float.class,
                    int.class
            }
    )
    public static class ScreenInit
    {
        public static void Postfix(Merchant __instance, float x, float y, int newShopScreen)
        {
            try {
                Field f = Merchant.class.getDeclaredField("idleMessages");
                f.setAccessible(true);

                ArrayList<String> idleMessages = (ArrayList<String>) f.get(__instance);
                idleMessages.add("Have you met my brother?");
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(ShopScreen.class.getName()) && m.getMethodName().equals("init")) {
                        m.replace(ScreenInit.class.getName() + ".init($$);");
                    }
                }
            };
        }

        @SuppressWarnings("unused")
        public static void init(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards)
        {
            if (AbstractDungeon.getCurrRoom() instanceof BloodShopRoom) {
                HubrisMod.bloodShopScreen.init();
            } else {
                HubrisMod.bloodShopScreen.init();
                //AbstractDungeon.shopScreen.init(coloredCards, colorlessCards);
            }
        }
    }
    @SpirePatch(
            clz=Merchant.class,
            method="update"
    )
    public static class ScreenOpen
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(ShopScreen.class.getName()) && m.getMethodName().equals("open")) {
                        m.replace(ScreenOpen.class.getName() + ".open();");
                    }
                }
            };
        }

        @SuppressWarnings("unused")
        public static void open()
        {
            if (AbstractDungeon.getCurrRoom() instanceof BloodShopRoom) {
                HubrisMod.bloodShopScreen.open();
            } else {
                HubrisMod.bloodShopScreen.open();
                //AbstractDungeon.shopScreen.open();
            }
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="generateRoom"
    )
    public static class GenerateRoom
    {
        public static AbstractRoom Postfix(AbstractRoom __result, AbstractDungeon __instance, EventHelper.RoomResult roomType)
        {
            if (__result instanceof ShopRoom) {
                if (AbstractDungeon.eventRng.random() < BloodShopRoom.BLOOD_SHOP_CHANCE) {
                    return new BloodShopRoom();
                }
            }
            return __result;
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

    @SpirePatch(
            clz=TopPanel.class,
            method="updateDeckViewButtonLogic"
    )
    public static class DeckViewButton
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"deckButtonDisabled"}
        )
        public static SpireReturn<Void> Insert(TopPanel __instance, @ByRef boolean[] deckButtonDisabled)
        {
            if (AbstractDungeon.screen == BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP) {
                deckButtonDisabled[0] = false;
                __instance.deckHb.update();

                if (((__instance.deckHb.hovered && InputHelper.justClickedLeft)
                            || InputActionSet.masterDeck.isJustPressed()
                            || CInputActionSet.pageLeftViewDeck.isJustPressed())
                        && !CardCrawlGame.isPopupOpen) {
                    AbstractDungeon.overlayMenu.cancelButton.hide();
                    AbstractDungeon.previousScreen = BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP;
                    AbstractDungeon.deckViewScreen.open();

                    InputHelper.justClickedLeft = false;
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=TopPanel.class,
            method="updateMapButtonLogic"
    )
    public static class MapButton
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"mapButtonDisabled"}
        )
        public static SpireReturn<Void> Insert(TopPanel __instance, @ByRef boolean[] deckButtonDisabled)
        {
            if (AbstractDungeon.screen == BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP) {
                deckButtonDisabled[0] = false;
                __instance.mapHb.update();

                if ((__instance.mapHb.hovered && InputHelper.justClickedLeft)
                        || InputActionSet.map.isJustPressed()
                        || CInputActionSet.map.isJustPressed()) {
                    AbstractDungeon.overlayMenu.cancelButton.hide();
                    AbstractDungeon.previousScreen = BloodShopScreen.Enum.HUBRIS_BLOOD_SHOP;
                    AbstractDungeon.dungeonMapScreen.open(false);

                    InputHelper.justClickedLeft = false;
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
