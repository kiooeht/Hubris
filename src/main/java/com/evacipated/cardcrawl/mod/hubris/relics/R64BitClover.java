package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class R64BitClover extends HubrisRelic
{
    public static final String ID = "hubris:64BitClover";
    private static final int FIGHTS_PER_RELIC = 7;
    private static Method returnRandomRelicTier = null;

    private Texture noBorder;

    static
    {
        try {
            returnRandomRelicTier = MonsterRoom.class.getDeclaredMethod("returnRandomRelicTier");
            returnRandomRelicTier.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public R64BitClover()
    {
        super(ID, "64BitClover.png", RelicTier.RARE, LandingSound.MAGICAL);

        // Disable anti-aliasing for pixel art
        img.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        outlineImg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

         noBorder = ImageMaster.loadImage(HubrisMod.assetPath("images/relics/64BitClover_noBorder.png"));
         noBorder.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (Settings.usesOrdinal) {
            return DESCRIPTIONS[0] + FIGHTS_PER_RELIC + TopPanel.getOrdinalNaming(FIGHTS_PER_RELIC) + DESCRIPTIONS[1];
        } else {
            // Mod only has English text, so always have ordinal naming
            return DESCRIPTIONS[0] + FIGHTS_PER_RELIC + TopPanel.getOrdinalNaming(FIGHTS_PER_RELIC) + DESCRIPTIONS[1];
        }
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        if (this.counter == FIGHTS_PER_RELIC - 1) {
            beginLongPulse();
        }
    }

    @Override
    public void onEquip()
    {
        setCounter(0);
    }

    @Override
    public void onTrigger()
    {
        ++counter;
        if (counter == FIGHTS_PER_RELIC) {
            flash();
            stopPulse();
            counter = 0;
            try {
                RelicTier tier = (RelicTier) returnRandomRelicTier.invoke(AbstractDungeon.getCurrRoom());
                flash();
                AbstractDungeon.getCurrRoom().addRelicToRewards(tier);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb, boolean renderAmount, Color outlineColor)
    {
        Texture saveImg = img;
        if (!isSeen) {
            img = noBorder;
        }

        super.render(sb, renderAmount, outlineColor);

        img = saveImg;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new R64BitClover();
    }

    @SpirePatch(
            clz=SingleRelicViewPopup.class,
            method="renderRelicImage"
    )
    public static class RenderOutlineNotSeenSingle
    {
        private static Texture saveImg;
        private static R64BitClover clover;

        @SpireInsertPatch(
                rloc=0,
                localvars={"relic"}
        )
        public static void Insert(SingleRelicViewPopup __instance, SpriteBatch sb, AbstractRelic relic)
        {
            if (relic instanceof R64BitClover) {
                clover = (R64BitClover) relic;
                saveImg = clover.img;
                if (!clover.isSeen) {
                    clover.img = clover.noBorder;
                }
            }
        }

        public static void Postfix(SingleRelicViewPopup __instance, SpriteBatch sb)
        {
            clover.img = saveImg;
            clover = null;
        }
    }
}
