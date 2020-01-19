package com.evacipated.cardcrawl.mod.hubris.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Waffle;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.ArrayList;
import java.util.List;

public class BloodStoreRelic
{
    public static List<Class<? extends AbstractRelic>> bannedRelics;

    static
    {
        bannedRelics = new ArrayList<>();
        bannedRelics.add(Waffle.class);
    }

    static final int GOLD_HP_RATIO = 15;
    static final float RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
    static final float RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
    static final float RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
    static final float RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
    static final float GOLD_IMG_WIDTH = ImageMaster.UI_GOLD.getWidth() * Settings.scale;

    public AbstractRelic relic;
    public boolean isPurchased = false;
    private BloodShopScreen shopScreen;
    public int price;
    private int slot;
    private boolean special;

    public static boolean isBannedRelic(AbstractRelic relic)
    {
        if (relic == null) {
            return false;
        }
        for (Class<? extends AbstractRelic> cls : bannedRelics) {
            if (cls.isInstance(relic)) {
                return true;
            }
        }
        return false;
    }

    public BloodStoreRelic(AbstractRelic relic, int slot, BloodShopScreen screenRef)
    {
        this(relic, slot, screenRef, false);
    }

    public BloodStoreRelic(AbstractRelic relic, int slot, BloodShopScreen screenRef, boolean special)
    {
        this.relic = relic;
        if (relic.tier == null) {
            price = -1;
        } else {
            price = relic.getPrice() / GOLD_HP_RATIO;
        }
        if (special) {
            price *= 0.75f;
        }
        this.slot = slot;
        shopScreen = screenRef;
        this.special = special;
    }

    public void hide()
    {
        if (relic != null) {
            relic.currentY = Settings.HEIGHT + 200.0f * Settings.scale;
        }
    }

    public void update(float rugY)
    {
        if (relic != null) {
            if (special) {
                relic.currentX = (670.0f + 150.0f * (slot % 3)) * Settings.scale;
                relic.currentY = rugY + 350.0f - (150.0f * (slot / 3)) * Settings.scale;
            } else {
                relic.currentX = (Settings.WIDTH / 2.0f + 150.0f * (slot - 2) + 75.0f) * Settings.scale;
                relic.currentY = rugY + 850.0f * Settings.scale;
            }
            relic.hb.move(relic.currentX, relic.currentY);

            relic.hb.update();
            if (relic.hb.hovered) {
                shopScreen.moveHand(relic.currentX - 190.0f * Settings.scale, relic.currentY - 70.0f * Settings.scale);
                if (InputHelper.justClickedLeft) {
                    relic.hb.clickStarted = true;
                }
                relic.scale = Settings.scale * 1.25f;
            } else {
                relic.scale = MathHelper.scaleLerpSnap(relic.scale, Settings.scale);
            }

            if (relic.hb.hovered && InputHelper.justClickedRight) {
                CardCrawlGame.relicPopup.open(relic);
            }

            if (relic.hb.clicked) {
                relic.hb.clicked = false;
                if (AbstractDungeon.player.currentHealth > price) {
                    AbstractDungeon.player.currentHealth -= price;
                    AbstractDungeon.player.healthBarUpdatedEvent();
                    CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1f);
                    CardCrawlGame.metricData.addShopPurchaseData(relic.relicId);
                    AbstractDungeon.getCurrRoom().relics.add(relic);
                    relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
                    relic.flash();
                    // TODO eggs
                    shopScreen.playBuySfx();
                    shopScreen.createSpeech(ShopScreen.getBuyMsg());

                    if (special) {
                        isPurchased = true;
                    } else {
                        do {
                            relic = AbstractDungeon.returnRandomRelicEnd(BloodShopScreen.rollRelicTier());
                        } while (isBannedRelic(relic));
                        price = relic.getPrice() / GOLD_HP_RATIO;
                        //shopScreen.getNewPrice(this);
                    }
                } else {
                    shopScreen.playCantBuySfx();
                    shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
                }
            }
        }
    }

    public void render(SpriteBatch sb)
    {
        if (relic != null)
        {
            relic.renderWithoutAmount(sb, new Color(0, 0, 0, 0.25f));

            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TP_HP, relic.currentX + RELIC_GOLD_OFFSET_X, relic.currentY + RELIC_GOLD_OFFSET_Y, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);

            Color color = Color.WHITE;
            if (price >= AbstractDungeon.player.currentHealth) {
                color = Color.SALMON;
            }
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
                    Integer.toString(price), relic.currentX + RELIC_PRICE_OFFSET_X, relic.currentY + RELIC_PRICE_OFFSET_Y, color);
        }
    }
}
