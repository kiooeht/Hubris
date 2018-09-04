package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.monsters.MerchantMonster;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.HitboxListener;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RugNotForSalePatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.shop.ShopScreen",
            method=SpirePatch.CLASS
    )
    public static class RugHitbox
    {
        public static SpireField<Hitbox> rugHB = new SpireField<>(() -> new Hitbox(100.0f * Settings.scale, 130.0f * Settings.scale));
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.shop.ShopScreen",
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
            cls="com.megacrit.cardcrawl.shop.ShopScreen",
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
            RugHitbox.rugHB.get(__instance).move(Settings.WIDTH - 85.0f * Settings.scale, rugY + 705.0f * Settings.scale);
            hitboxListener.instance = __instance;
            RugHitbox.rugHB.get(__instance).encapsulatedUpdate(hitboxListener);
        }

        private static void startCombat(ShopScreen __instance)
        {
            __instance.playCantBuySfx();

            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
            AbstractDungeon.lastCombatMetricKey = MerchantMonster.ID;
            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new MerchantMonster());
            AbstractDungeon.getCurrRoom().monsters.init();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                m.usePreBattleAction();
                m.useUniversalPreBattleAction();
            }
            ((ShopRoom)AbstractDungeon.getCurrRoom()).merchant = null;
            AbstractRoom.waitTimer = 0.1f;
            AbstractDungeon.player.preBattlePrep();
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.shop.ShopScreen",
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
}
