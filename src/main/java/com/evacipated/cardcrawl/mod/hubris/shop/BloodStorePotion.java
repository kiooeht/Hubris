package com.evacipated.cardcrawl.mod.hubris.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.FruitJuice;
import com.megacrit.cardcrawl.potions.RegenPotion;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;

import java.util.ArrayList;
import java.util.List;

public class BloodStorePotion
{
    public static List<Class<? extends AbstractPotion>> bannedPotions;

    static
    {
        bannedPotions = new ArrayList<>();
        bannedPotions.add(BloodPotion.class);
        bannedPotions.add(RegenPotion.class);
        bannedPotions.add(FruitJuice.class);
    }

    public static boolean isBannedPotion(AbstractPotion relic)
    {
        if (relic == null) {
            return false;
        }
        for (Class<? extends AbstractPotion> cls : bannedPotions) {
            if (cls.isInstance(relic)) {
                return true;
            }
        }
        return false;
    }

    public AbstractPotion potion;
    private BloodShopScreen shopScreen;
    public int price;
    private int slot;

    public BloodStorePotion(AbstractPotion potion, int slot, BloodShopScreen screenRef)
    {
        this.potion = potion;
        price = potion.getPrice() / BloodStoreRelic.GOLD_HP_RATIO;
        this.slot = slot;
        shopScreen = screenRef;
    }

    public void hide()
    {
        if (potion != null) {
            potion.posY = Settings.HEIGHT + 200.0f * Settings.scale;
        }
    }

    public void update(float rugY)
    {
        if (potion != null) {
            potion.posX = (Settings.WIDTH / 2.0f + 150.0f * (slot - 2) + 75.0f) * Settings.scale;
            potion.posY = rugY + 650.0f * Settings.scale;
            potion.hb.move(potion.posX, potion.posY);

            potion.hb.update();
            if (potion.hb.hovered) {
                shopScreen.moveHand(potion.posX - 190.0f * Settings.scale, potion.posY - 70.0f * Settings.scale);
                if (InputHelper.justClickedLeft) {
                    potion.hb.clickStarted = true;
                }
            }
            if (potion.hb.clicked) {
                potion.hb.clicked = false;
                if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
                    AbstractDungeon.player.getRelic(Sozu.ID).flash();
                    return;
                }
                if (AbstractDungeon.player.currentHealth > price) {
                    if (AbstractDungeon.player.obtainPotion(potion)) {
                        AbstractDungeon.player.currentHealth -= price;
                        AbstractDungeon.player.healthBarUpdatedEvent();
                        CardCrawlGame.sound.play("SHOP_PURCHASE");
                        CardCrawlGame.metricData.addShopPurchaseData(potion.ID);
                        shopScreen.playBuySfx();
                        shopScreen.createSpeech(ShopScreen.getBuyMsg());

                        do {
                            potion = AbstractDungeon.returnRandomPotion();
                        } while (isBannedPotion(potion));
                        price = potion.getPrice() / BloodStoreRelic.GOLD_HP_RATIO;
                        //shopScreen.getNewPrice(this);
                    } else {
                        shopScreen.createSpeech(StorePotion.TEXT[0]);
                        AbstractDungeon.topPanel.flashRed();
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
        if (potion != null) {
            potion.shopRender(sb);

            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TP_HP,
                    potion.posX + BloodStoreRelic.RELIC_GOLD_OFFSET_X, potion.posY + BloodStoreRelic.RELIC_GOLD_OFFSET_Y,
                    BloodStoreRelic.GOLD_IMG_WIDTH, BloodStoreRelic.GOLD_IMG_WIDTH);

            Color color = Color.WHITE;
            if (price >= AbstractDungeon.player.currentHealth) {
                color = Color.SALMON;
            }
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
                    Integer.toString(price), potion.posX + BloodStoreRelic.RELIC_PRICE_OFFSET_X, potion.posY + BloodStoreRelic.RELIC_PRICE_OFFSET_Y, color);
        }
    }
}
