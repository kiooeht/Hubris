package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RewardGlowEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class RewardsScrolling
{
    private static class ScrollListener implements ScrollBarListener
    {
        @Override
        public void scrolledUsingBar(float v)
        {
            Fields.scrollPosition = MathHelper.valueFromPercentBetween(Fields.scrollLowerBound, Fields.scrollUpperBound, v);
            Fields.scrollTarget = Fields.scrollPosition;
            AbstractDungeon.combatRewardScreen.positionRewards();
            ScrollUpdate.updateBarPosition();
        }
    }

    private static class Fields
    {
        private static ScrollBar scrollBar = new ScrollBar(new ScrollListener(),
                Settings.WIDTH / 2.0f + 270.0f * Settings.scale,
                Settings.HEIGHT - 614.0f * Settings.scale,
                500 * Settings.scale);
        private static float scrollLowerBound = 0.0f;
        private static float scrollUpperBound = 0.0f;
        private static float scrollPosition = 0.0f;
        private static float scrollTarget = 0.0f;

        private static boolean grabbedScreen = false;
        private static float grabStartY = 0.0f;
    }

    @SpirePatch(
            clz=CombatRewardScreen.class,
            method="setupItemReward"
    )
    public static class ResetScrollPosition
    {
        public static void Prefix(CombatRewardScreen __instance)
        {
            Fields.scrollPosition = 0.0f;
            Fields.scrollTarget = 0.0f;

            Fields.grabbedScreen = false;
            Fields.grabStartY = 0.0f;
        }
    }

    @SpirePatch(
            clz=CombatRewardScreen.class,
            method="renderItemReward"
    )
    public static class RenderScissor
    {
        private static OrthographicCamera camera = null;

        @SpireInsertPatch(
                rloc=20
        )
        public static void Insert(CombatRewardScreen __instance, SpriteBatch sb)
        {
            if (camera == null) {
                try {
                    Field f = CardCrawlGame.class.getDeclaredField("camera");
                    f.setAccessible(true);
                    camera = (OrthographicCamera) f.get(Gdx.app.getApplicationListener());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
            }

            sb.flush();
            Rectangle scissors = new Rectangle();
            Rectangle clipBounds = new Rectangle(Settings.WIDTH / 2.0f - 300 * Settings.scale, Settings.HEIGHT / 2.0f - 350 * Settings.scale,
                    600 * Settings.scale, 600 * Settings.scale);
            ScissorStack.calculateScissors(camera, sb.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);
        }

        public static void Postfix(CombatRewardScreen __instance, SpriteBatch sb)
        {
            if (camera != null) {
                sb.flush();
                ScissorStack.popScissors();
            }

            if (__instance.rewards.size() > 5) {
                Fields.scrollBar.render(sb);
            }
        }
    }

    @SpirePatch(
            clz=CombatRewardScreen.class,
            method="positionRewards"
    )
    public static class PositionRewards
    {
        public static SpireReturn<Void> Prefix(CombatRewardScreen __instance)
        {
            float baseY = Settings.HEIGHT - 410.0f * Settings.scale;
            float spacingY = 100.0f * Settings.scale;

            for (int i=0; i<__instance.rewards.size(); ++i) {
                __instance.rewards.get(i).move(baseY - i * spacingY + Fields.scrollPosition);
            }
            if (__instance.rewards.isEmpty()) {
                __instance.hasTakenAll = true;
            }

            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
            clz=CombatRewardScreen.class,
            method="rewardViewUpdate"
    )
    public static class ScrollUpdate
    {
        public static void Postfix(CombatRewardScreen __instance)
        {
            if (__instance.rewards.size() < 6) {
                Fields.scrollTarget = 0.0f;
                Fields.scrollPosition = 0.0f;
                __instance.positionRewards();
                return;
            }

            if (Fields.scrollBar.update()) {
                return;
            }

            int y = InputHelper.mY;
            if (!Fields.grabbedScreen) {
                if (InputHelper.scrolledDown) {
                    Fields.scrollTarget += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    Fields.scrollTarget -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft) {
                    Fields.grabbedScreen = true;
                    Fields.grabStartY = y - Fields.scrollTarget;
                }
            } else if (InputHelper.isMouseDown) {
                Fields.scrollTarget = y - Fields.grabStartY;
            } else {
                Fields.grabbedScreen = false;
            }

            float prev_scrollPosition = Fields.scrollTarget;
            Fields.scrollPosition = MathHelper.scrollSnapLerpSpeed(Fields.scrollPosition, Fields.scrollTarget);

            if (Fields.scrollTarget < 0) {
                Fields.scrollTarget = 0.0f;
            }

            Fields.scrollUpperBound = (__instance.rewards.size() - 5) * 100.0f * Settings.scale;
            if (Fields.scrollTarget > Fields.scrollUpperBound) {
                Fields.scrollTarget = Fields.scrollUpperBound;
            }

            if (Fields.scrollPosition != prev_scrollPosition) {
                __instance.positionRewards();
            }

            updateBarPosition();
        }

        private static void updateBarPosition()
        {
            float percent = MathHelper.percentFromValueBetween(Fields.scrollLowerBound, Fields.scrollUpperBound, Fields.scrollPosition);
            Fields.scrollBar.parentScrolledToPercent(percent);
        }

        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(RewardItem.class.getName()) && m.getMethodName().equals("update")) {
                        m.replace("if (" + ScrollUpdate.class.getName() + ".DoUpdate($0)) {" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }

        public static boolean DoUpdate(RewardItem reward)
        {
            boolean ret = false;
            if (reward.y < 800.0f * Settings.scale && reward.y > 200.0f * Settings.scale) {
                ret = true;
            }

            if (!ret) {
                // Reset all the hitbox stuff to avoid weird behavior
                reward.hb.hovered = false;
                reward.hb.justHovered = false;
                reward.hb.clicked = false;
                reward.hb.clickStarted = false;

                // Continue the flash timer to avoid flash desyncing them
                if (reward.flashTimer > 0.0f) {
                    reward.flashTimer -= Gdx.graphics.getDeltaTime();
                    if (reward.flashTimer < 0.0f) {
                        reward.flashTimer = 0.0f;
                    }
                }
            }

            try {
                Field f = RewardItem.class.getDeclaredField("effects");
                f.setAccessible(true);
                ArrayList<AbstractGameEffect> effects = (ArrayList<AbstractGameEffect>) f.get(reward);

                if (!ret) {
                    // Continue glow effects to avoid desyncing them
                    if (effects.size() == 0) {
                        effects.add(new RewardGlowEffect(reward.hb.cX, reward.hb.cY));
                    }
                    for (Iterator<AbstractGameEffect> it = effects.iterator(); it.hasNext(); ) {
                        AbstractGameEffect e = it.next();
                        e.update();
                        if (e.isDone) {
                            it.remove();
                        }
                    }
                }
                for (AbstractGameEffect e : effects) {
                    if (e instanceof RewardGlowEffect) {
                        moveRewardGlowEffect((RewardGlowEffect) e, reward.hb.cX, reward.hb.cY);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            return ret;
        }

        private static void moveRewardGlowEffect(RewardGlowEffect effect, float x, float y)
        {
            try {
                Field f = RewardGlowEffect.class.getDeclaredField("x");
                f.setAccessible(true);
                f.setFloat(effect, x);

                f = RewardGlowEffect.class.getDeclaredField("y");
                f.setAccessible(true);
                f.setFloat(effect, y);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
