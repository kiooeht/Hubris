package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.BottleRainField;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PyramidsField;
import com.evacipated.cardcrawl.mod.hubris.relics.BottledRain;
import com.evacipated.cardcrawl.mod.hubris.relics.MysteriousPyramids;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method="renderCard"
)
public class RenderRelicOnCard
{
    private static class RelicInfo
    {
        private final SpireField<Boolean> field;
        private final AbstractRelic relic;

        private RelicInfo(SpireField<Boolean> field, AbstractRelic relic)
        {
            this.field = field;
            this.relic = relic;
        }
    }
    private static List<RelicInfo> relics = new ArrayList<>();

    private static Field relicRotation;

    static {
        relics.add(new RelicInfo(PyramidsField.inPyramids, new MysteriousPyramids()));
        relics.add(new RelicInfo(BottleRainField.inBottleRain, new BottledRain()));

        try {
            relicRotation = AbstractRelic.class.getDeclaredField("rotation");
            relicRotation.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void Postfix(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (!Settings.hideCards /*&& !__instance.isOnScreen()*/ && !__instance.isFlipped) {
            for (RelicInfo info : relics) {
                if (info.field.get(__instance)) {
                    AbstractRelic r = info.relic;
                    r.scale = __instance.drawScale * 0.8f;
                    try {
                        relicRotation.set(r, __instance.angle);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    Vector2 tmp = new Vector2(135.0f, 185.0f);
                    tmp.scl(__instance.drawScale * Settings.scale);
                    tmp.rotate(__instance.angle);
                    r.currentX = __instance.current_x + tmp.x;
                    r.currentY = __instance.current_y + tmp.y;
                    r.render(sb);
                }
            }
        }
    }
}
