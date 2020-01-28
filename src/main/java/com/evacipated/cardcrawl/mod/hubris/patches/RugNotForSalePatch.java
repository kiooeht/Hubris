package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.MerchantFight;
import com.evacipated.cardcrawl.mod.hubris.fakes.FakeCauldron;
import com.evacipated.cardcrawl.mod.hubris.fakes.FakeMerchant;
import com.evacipated.cardcrawl.mod.hubris.monsters.MerchantMonster;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.HitboxListener;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.Cauldron;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RugNotForSalePatch
{
    @SpirePatch(
            clz=ShopScreen.class,
            method=SpirePatch.CLASS
    )
    public static class RugHitbox
    {
        public static SpireField<Hitbox> rugHB = new SpireField<>(() -> new Hitbox(100.0f * Settings.scale, 130.0f * Settings.scale));
    }

    @SpirePatch(
            clz=ShopScreen.class,
            method="init"
    )
    public static class Init
    {
        public static void Postfix(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards)
        {
            RugHitbox.rugHB.get(__instance).move(Settings.WIDTH - 85.0f * Settings.scale, Settings.HEIGHT - 380.0f * Settings.scale);
        }
    }

    @SpirePatch(
            clz=ShopScreen.class,
            method="update"
    )
    public static class Update
    {
        private static class Listener implements HitboxListener
        {
            ShopScreen instance = null;

            @Override
            public void hoverStarted(Hitbox hitbox) {}

            @Override
            public void startClicking(Hitbox hitbox) {}

            @Override
            public void clicked(Hitbox hitbox)
            {
                startCombat(instance);
            }
        }

        private static Listener hitboxListener = new Listener();

        public static void Postfix(ShopScreen __instance)
        {
            float rugY = Settings.HEIGHT;
            try {
                Field f = ShopScreen.class.getDeclaredField("rugY");
                f.setAccessible(true);
                rugY = f.getFloat(__instance);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            RugHitbox.rugHB.get(__instance).move(Settings.WIDTH - 85.0f * Settings.scale, rugY + (Settings.isFourByThree ? 915f : 705.0f) * Settings.scale);
            hitboxListener.instance = __instance;
            RugHitbox.rugHB.get(__instance).encapsulatedUpdate(hitboxListener);
        }

        private static void startCombat(ShopScreen __instance)
        {
            __instance.playCantBuySfx();

            Merchant merchant = ((ShopRoom)AbstractDungeon.getCurrRoom()).merchant;

            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
            AbstractDungeon.lastCombatMetricKey = MerchantMonster.ID;
            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new MerchantMonster());
            AbstractDungeon.getCurrRoom().event = new MerchantFight();

            AbstractDungeon.getCurrRoom().rewards.clear();
            ArrayList<StoreRelic> shopRelics = new ArrayList<>();
            ArrayList<AbstractCard> coloredCards = new ArrayList<>();
            ArrayList<AbstractCard> colorlessCards = new ArrayList<>();
            ArrayList<StorePotion> potions = new ArrayList<>();
            try {
                Field f = ShopScreen.class.getDeclaredField("relics");
                f.setAccessible(true);
                shopRelics = (ArrayList<StoreRelic>) f.get(AbstractDungeon.shopScreen);

                f = ShopScreen.class.getDeclaredField("coloredCards");
                f.setAccessible(true);
                coloredCards = (ArrayList<AbstractCard>) f.get(AbstractDungeon.shopScreen);

                f = ShopScreen.class.getDeclaredField("colorlessCards");
                f.setAccessible(true);
                colorlessCards = (ArrayList<AbstractCard>) f.get(AbstractDungeon.shopScreen);

                f = ShopScreen.class.getDeclaredField("potions");
                f.setAccessible(true);
                potions = (ArrayList<StorePotion>) f.get(AbstractDungeon.shopScreen);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            for (StoreRelic relic : shopRelics) {
                if (Cauldron.ID.equals(relic.relic.relicId)) {
                    AbstractDungeon.getCurrRoom().addRelicToRewards(new FakeCauldron());
                } else {
                    AbstractDungeon.getCurrRoom().addRelicToRewards(relic.relic);
                }
            }
            for (AbstractCard card : coloredCards) {
                RewardItem rewardItem = new RewardItem();
                rewardItem.cards.clear();
                rewardItem.cards.add(card);
                AbstractDungeon.getCurrRoom().addCardReward(rewardItem);
            }
            for (AbstractCard card : colorlessCards) {
                RewardItem rewardItem = new RewardItem();
                rewardItem.cards.clear();
                rewardItem.cards.add(card);
                AbstractDungeon.getCurrRoom().addCardReward(rewardItem);
            }
            for (StorePotion potion : potions) {
                AbstractDungeon.getCurrRoom().addPotionToRewards(potion.potion);
            }
            AbstractDungeon.getCurrRoom().addGoldToRewards(300);

            AbstractDungeon.getCurrRoom().monsters.init();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                m.usePreBattleAction();
                m.useUniversalPreBattleAction();
            }
            ((ShopRoom)AbstractDungeon.getCurrRoom()).merchant = new FakeMerchant(merchant);
            AbstractRoom.waitTimer = 0.1f;
            AbstractDungeon.player.preBattlePrep();
        }
    }

    @SpirePatch(
            clz=ShopScreen.class,
            method="render"
    )
    public static class Render
    {
        public static void Postfix(ShopScreen __instance, SpriteBatch sb)
        {
            RugHitbox.rugHB.get(__instance).render(sb);
            if (RugHitbox.rugHB.get(__instance).hovered) {
                // Render tip
                TipHelper.renderGenericTip(InputHelper.mX - 360.0f * Settings.scale, InputHelper.mY - 70.0f * Settings.scale,
                        "Steal Rug", "It's not for sale, but perhaps you can steal it?");
            }
        }
    }

    @SpirePatch(
            cls="shopmod.patches.ShopScreenPatch$Update",
            method="Postfix",
            optional=true
    )
    public static class ShopModCompatUpdate
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"shopmod.relics.MerchantsRug.rugHb", "currentY"}
        )
        public static void Insert(ShopScreen self, Hitbox rugHb, float currentY)
        {
            rugHb.y -= 280.0f * Settings.scale;
            rugHb.cY -= 280.0f * Settings.scale;
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "update");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            cls="shopmod.patches.ShopScreenPatch$RenderRelics",
            method="Postfix",
            optional=true
    )
    public static class ShopModCompatRender
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"rugY"}
        )
        public static void Insert(ShopScreen self, SpriteBatch sb, @ByRef float[] rugY)
        {
            rugY[0] -= 280.0f * Settings.scale;
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
