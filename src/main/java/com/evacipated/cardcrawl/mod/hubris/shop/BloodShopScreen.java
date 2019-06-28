package com.evacipated.cardcrawl.mod.hubris.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.relics.KleinBottle;
import com.evacipated.cardcrawl.mod.hubris.relics.EvacipatedBox;
import com.evacipated.cardcrawl.mod.hubris.relics.FruitBowl;
import com.evacipated.cardcrawl.mod.hubris.relics.Macrotransations;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Hoarder;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.FloatyEffect;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class BloodShopScreen
{
    public static List<String> specialRelicIDs = new ArrayList<>();

    static
    {
        specialRelicIDs.add(EvacipatedBox.ID);
        specialRelicIDs.add(FruitBowl.ID);
        specialRelicIDs.add(Macrotransations.ID);
        if (HubrisMod.hasInfiniteSpire) {
            specialRelicIDs.add(KleinBottle.ID);
        }
    }

    private static final Logger logger = LogManager.getLogger(BloodShopScreen.class.getName());
    private static final float BOTTOM_ROW_Y = 307.0F * Settings.scale;
    private static final float GOLD_IMG_WIDTH = ImageMaster.UI_GOLD.getWidth() * Settings.scale;
    private static final float GOLD_IMG_OFFSET_X = -50.0F * Settings.scale;
    private static final float GOLD_IMG_OFFSET_Y = -220.0F * Settings.scale;
    private static final float PRICE_TEXT_OFFSET_X = 16.0F * Settings.scale;
    private static final float PRICE_TEXT_OFFSET_Y = -180.0F * Settings.scale;
    private static final float HAND_SPEED = 6.0F;
    private static float HAND_W;
    private static float HAND_H;

    private static final float SPEECH_DURATION = 4.0F;
    private static final float SPEECH_TEXT_R_X = 164.0F * Settings.scale;
    private static final float SPEECH_TEXT_L_X = -166.0F * Settings.scale;
    private static final float SPEECH_TEXT_Y = 126.0F * Settings.scale;

    public boolean isActive = true;
    private static Texture rugImg = null;
    private static Texture removeServiceImg = null;
    private static Texture soldOutImg = null;
    private static Texture handImg = null;
    private float rugY = Settings.HEIGHT;
    private static final float RUG_SPEED = 5.0F;

    private List<BloodStoreRelic> relics = new ArrayList<>();
    private List<BloodStorePotion> potions = new ArrayList<>();

    public boolean purgeAvailable = false;
    public static int purgeCost = 5;
    public static int actualPurgeCost = 5;
    private static final int PURGE_COST_RAMP = 2;
    private boolean purgeHovered = false;
    private float purgeCardX;
    private float purgeCardY;
    private float purgeCardScale = 1.0F;

    private FloatyEffect f_effect = new FloatyEffect(20.0F, 0.1F);
    private float handTimer = 1.0F;
    private float handX = Settings.WIDTH / 2.0F;
    private float handY = Settings.HEIGHT;
    private float handTargetX = 0.0F;
    private float handTargetY = Settings.HEIGHT;

    private boolean somethingHovered = false;
    private float notHoveredTimer = 0.0F;

    private ShopSpeechBubble speechBubble;
    private SpeechTextEffect dialogTextEffect;

    public static class Enum
    {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen HUBRIS_BLOOD_SHOP;
    }

    public void init()
    {
        if (rugImg == null)
        {
            switch (Settings.language)
            {
                case DEU:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/deu.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/deu.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/deu.png");
                    break;
                case FRA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/fra.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/fra.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/fra.png");
                    break;
                case ITA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ita.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ita.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ita.png");
                    break;
                case KOR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/kor.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/kor.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/kor.png");
                    break;
                case RUS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/rus.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/rus.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/rus.png");
                    break;
                case UKR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ukr.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ukr.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ukr.png");
                    break;
                case ZHS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/zhs.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/zhs.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/zhs.png");
                    break;
                default:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/eng.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/eng.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/eng.png");
            }
            handImg = ImageMaster.loadImage("images/npcs/merchantHand.png");
        }
        HAND_W = handImg.getWidth() * Settings.scale;
        HAND_H = handImg.getHeight() * Settings.scale;

        initRelics();
        initPotions();

        this.purgeAvailable = true;
        this.purgeCardY = -1000.0F;
        this.purgeCardX = (1400.0F * Settings.scale);
        this.purgeCardScale = 0.7F;
        actualPurgeCost = purgeCost;
    }

    private void initRelics()
    {
        relics.clear();
        for (int i=0; i<6; ++i) {
            AbstractRelic.RelicTier relicTier = AbstractRelic.RelicTier.SHOP;
            if (i < 4) {
                relicTier = rollRelicTier();
            }
            AbstractRelic tempRelic;
            do {
                tempRelic = AbstractDungeon.returnRandomRelicEnd(relicTier);
            } while (BloodStoreRelic.isBannedRelic(tempRelic));

            BloodStoreRelic relic = new BloodStoreRelic(tempRelic, i, this);
            if (!Settings.isDailyRun) {
                relic.price = MathUtils.round(relic.price * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
            }
            relics.add(relic);
        }

        for (int i=0; i<Math.min(specialRelicIDs.size(), 6); ++i) {
            if (!AbstractDungeon.player.hasRelic(specialRelicIDs.get(i))) {
                relics.add(new BloodStoreRelic(RelicLibrary.getRelic(specialRelicIDs.get(i)).makeCopy(), i, this, true));
            }
        }
    }

    private void initPotions()
    {
        potions.clear();
        for (int i=0; i<6; ++i) {
            AbstractPotion tempPotion;
            do {
                tempPotion = AbstractDungeon.returnRandomPotion();
            } while (BloodStorePotion.isBannedPotion(tempPotion));
            BloodStorePotion potion = new BloodStorePotion(tempPotion, i, this);
            if (!Settings.isDailyRun) {
                potion.price = MathUtils.round(potion.price * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
            }
            potions.add(potion);
        }
    }

    static AbstractRelic.RelicTier rollRelicTier()
    {
        int roll = AbstractDungeon.merchantRng.random(99);
        logger.info("ROLL " + roll);
        if (roll < 48) {
            return AbstractRelic.RelicTier.COMMON;
        }
        if (roll < 82) {
            return AbstractRelic.RelicTier.UNCOMMON;
        }
        return AbstractRelic.RelicTier.RARE;
    }

    public static void purgeCard()
    {
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, purgeCost, DamageInfo.DamageType.HP_LOSS));
        CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
        purgeCost += PURGE_COST_RAMP;
        actualPurgeCost = purgeCost;
        /*
        if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
            actualPurgeCost = 50;
        } else if ((AbstractDungeon.player.hasRelic("The Courier")) && (AbstractDungeon.player.hasRelic("Membership Card"))) {
            actualPurgeCost = MathUtils.round(purgeCost * 0.8F * 0.5F);
        } else if (AbstractDungeon.player.hasRelic("The Courier")) {
            actualPurgeCost = MathUtils.round(purgeCost * 0.8F);
        } else if (AbstractDungeon.player.hasRelic("Membership Card")) {
            actualPurgeCost = MathUtils.round(purgeCost * 0.5F);
        }
        */
    }

    public void updatePurge()
    {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            purgeCard();
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));

                AbstractDungeon.player.masterDeck.removeCard(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    public void open()
    {
        CardCrawlGame.sound.play("SHOP_OPEN");
        purgeCardY = -1000.0f;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = Enum.HUBRIS_BLOOD_SHOP;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.show(ShopScreen.NAMES[12]);
        for (BloodStoreRelic r : relics) {
            r.hide();
        }
        for (BloodStorePotion p : potions) {
            p.hide();
        }
        rugY = Settings.HEIGHT;
        handTargetX = handX = Settings.WIDTH / 2.0f;
        handTargetY = handY = Settings.HEIGHT;
        handTimer = 1.0f;
        speechBubble = null;
        dialogTextEffect = null;
        AbstractDungeon.overlayMenu.showBlackScreen();

        for (BloodStoreRelic r : relics) {
            if (r.relic != null) {
                UnlockTracker.markRelicAsSeen(r.relic.relicId);
            }
        }
        if (ModHelper.isModEnabled(Hoarder.ID)) {
            purgeAvailable = false;
        }
    }

    public void moveHand(float x, float y)
    {
        handTargetX = x - 50.0f * Settings.scale;
        handTargetY = y + 90.0f * Settings.scale;
    }

    public void update()
    {
        if (handTimer > 0) {
            handTimer -= Gdx.graphics.getDeltaTime();
        }
        f_effect.update();

        somethingHovered = false;
        updatePurgeCard();
        updatePurge();
        updateRelics();
        updatePotions();
        updateRug();
        updateSpeech();

        updateHand();

        if (!somethingHovered) {
            notHoveredTimer += Gdx.graphics.getDeltaTime();
            if (notHoveredTimer > 1.0f) {
                handTargetY = Settings.HEIGHT;
            }
        } else {
            notHoveredTimer = 0;
        }
    }

    private void updateRug()
    {
        if (rugY != 0) {
            rugY = MathUtils.lerp(rugY, 0, Gdx.graphics.getDeltaTime() * RUG_SPEED);
            if (Math.abs(rugY) < 0.5f) {
                rugY = 0;
            }
        }
    }

    private void updateHand()
    {
        if (handTimer <= 0) {
            if (handX != handTargetX) {
                handX = MathUtils.lerp(handX, handTargetX, Gdx.graphics.getDeltaTime() * HAND_SPEED);
            }
            if (handY != handTargetY) {
                if (handY > handTargetY) {
                    handY = MathUtils.lerp(handY, handTargetY, Gdx.graphics.getDeltaTime() * HAND_SPEED);
                } else {
                    handY = MathUtils.lerp(handY, handTargetY, Gdx.graphics.getDeltaTime() * HAND_SPEED / 4.0f);
                }
            }
        }
    }

    private void updatePurgeCard()
    {
        purgeCardX = 1200.0f * Settings.scale;
        purgeCardY = rugY + BOTTOM_ROW_Y;
        if (purgeAvailable) {
            float CARD_W = 110.0f * Settings.scale;
            float CARD_H = 150.0f * Settings.scale;
            if ((InputHelper.mX > this.purgeCardX - CARD_W) && (InputHelper.mX < this.purgeCardX + CARD_W) && (InputHelper.mY > this.purgeCardY - CARD_H) && (InputHelper.mY < this.purgeCardY + CARD_H)) {
                this.purgeHovered = true;
                moveHand(this.purgeCardX - AbstractCard.IMG_WIDTH / 2.0F, this.purgeCardY);
                somethingHovered = true;
                this.purgeCardScale = Settings.scale;
            } else {
                this.purgeHovered = false;
            }
            if (!this.purgeHovered) {
                this.purgeCardScale = MathHelper.cardScaleLerpSnap(this.purgeCardScale, 0.75F * Settings.scale);
            } else {
                if ((InputHelper.justClickedLeft) || (CInputActionSet.select.isJustPressed())) {
                    CInputActionSet.select.unpress();
                    this.purgeHovered = false;
                    if (AbstractDungeon.player.gold >= actualPurgeCost) {
                        AbstractDungeon.previousScreen = Enum.HUBRIS_BLOOD_SHOP;

                        AbstractDungeon.gridSelectScreen.open(
                                CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                        .getPurgeableCards()), 1, ShopScreen.NAMES[13], false, false, true, true);
                    } else {
                        playCantBuySfx();
                        //createSpeech(getCantBuyMsg());
                    }
                }
                TipHelper.renderGenericTip(
                        InputHelper.mX - 360.0F * Settings.scale, InputHelper.mY - 70.0F * Settings.scale,
                        ShopScreen.LABEL[0],
                        ShopScreen.MSG[0] + PURGE_COST_RAMP + ShopScreen.MSG[1]
                                + " NL " + FontHelper.colorString("Can be used any number of times.", "g")
                );
            }
        } else {
            purgeCardScale = MathHelper.cardScaleLerpSnap(purgeCardScale, 0.75f * Settings.scale);
        }
    }

    private void updateRelics()
    {
        for (Iterator<BloodStoreRelic> iter=relics.iterator(); iter.hasNext();) {
            BloodStoreRelic r = iter.next();
            r.update(rugY);
            if (r.isPurchased) {
                iter.remove();
                break;
            }
        }
    }

    private void updatePotions()
    {
        for (BloodStorePotion p : potions) {
            p.update(rugY);
        }
    }

    void createSpeech(String msg)
    {
        boolean isRight = MathUtils.randomBoolean();
        float x = MathUtils.random(66.0f, 1260.0f) * Settings.scale;
        float y = Settings.HEIGHT - 380.0f * Settings.scale;
        speechBubble = new ShopSpeechBubble(x, y, SPEECH_DURATION, msg, isRight);
        float offset_x = 0.0f;
        if (isRight) {
            offset_x = SPEECH_TEXT_R_X;
        } else {
            offset_x = SPEECH_TEXT_L_X;
        }
        dialogTextEffect = new SpeechTextEffect(x + offset_x, y + SPEECH_TEXT_Y, SPEECH_DURATION, msg, DialogWord.AppearEffect.BUMP_IN);
    }

    private void updateSpeech()
    {
        if (speechBubble != null) {
            speechBubble.update();
            if (speechBubble.hb.hovered && speechBubble.duration > 0.3f) {
                speechBubble.duration = 0.3f;
                dialogTextEffect.duration = 0.3f;
            }
            if (speechBubble.isDone) {
                speechBubble = null;
            }
        }
        if (dialogTextEffect != null) {
            dialogTextEffect.update();
            if (dialogTextEffect.isDone) {
                dialogTextEffect = null;
            }
        }
    }

    void playBuySfx()
    {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_KA");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_KB");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_KC");
        }
    }

    void playCantBuySfx()
    {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_2A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_2B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_2C");
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(Color.WHITE);
        sb.draw(rugImg, 0.0f, rugY, Settings.WIDTH, Settings.HEIGHT);

        renderRelics(sb);
        renderPotions(sb);
        renderPurge(sb);

        sb.setColor(Color.RED);
        sb.draw(handImg, handX + f_effect.x, handY + f_effect.y, HAND_W, HAND_H);
        sb.setColor(Color.WHITE);

        if (speechBubble != null) {
            speechBubble.render(sb);
        }
        if (dialogTextEffect != null) {
            dialogTextEffect.render(sb);
        }
    }

    private void renderRelics(SpriteBatch sb)
    {
        for (BloodStoreRelic r : relics) {
            r.render(sb);
        }
    }

    private void renderPotions(SpriteBatch sb)
    {
        for (BloodStorePotion p : potions) {
            p.render(sb);
        }
    }

    private void renderPurge(SpriteBatch sb)
    {
        sb.setColor(new Color(0, 0, 0, 0.25f));
        TextureAtlas.AtlasRegion img = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
        sb.draw(
                img,
                purgeCardX + 18.0f * Settings.scale + img.offsetX - img.originalWidth / 2f,
                purgeCardY - 14.0f * Settings.scale + img.offsetY - img.originalHeight / 2f,
                img.originalWidth / 2f - img.offsetX,
                img.originalHeight / 2f - img.offsetY,
                img.packedWidth,
                img.packedHeight,
                purgeCardScale, purgeCardScale,
                0.0f
        );

        sb.setColor(Color.WHITE);
        if (purgeAvailable) {
            sb.draw(
                    removeServiceImg,
                    purgeCardX - 256.0f, purgeCardY - 256.0f,
                    256.0f, 256.0f,
                    512.0f, 512.0f,
                    purgeCardScale, purgeCardScale,
                    0.0f,
                    0, 0,
                    512, 512,
                    false, false
            );
            sb.draw(
                    ImageMaster.TP_HP,
                    purgeCardX + GOLD_IMG_OFFSET_X, purgeCardY + GOLD_IMG_OFFSET_Y - (purgeCardScale / Settings.scale - 0.75f) * 200.0f * Settings.scale,
                    GOLD_IMG_WIDTH, GOLD_IMG_WIDTH
            );

            Color color = Color.WHITE;
            if (actualPurgeCost > AbstractDungeon.player.currentHealth) {
                color = Color.SALMON;
            }
            FontHelper.renderFontLeftTopAligned(
                    sb, FontHelper.tipHeaderFont,
                    Integer.toString(actualPurgeCost),
                    purgeCardX + PRICE_TEXT_OFFSET_X, purgeCardY + PRICE_TEXT_OFFSET_Y - (purgeCardScale / Settings.scale - 0.75f) * 200.0f * Settings.scale,
                    color
            );
        } else {
            sb.draw(
                    soldOutImg,
                    purgeCardX - 256.0f, purgeCardY - 256.0f,
                    256.0f, 256.0f,
                    512.0f, 512.0f,
                    purgeCardScale, purgeCardScale,
                    0.0f,
                    0, 0,
                    512, 512,
                    false, false
            );
        }
    }
}
