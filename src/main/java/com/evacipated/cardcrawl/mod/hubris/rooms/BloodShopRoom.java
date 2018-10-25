package com.evacipated.cardcrawl.mod.hubris.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.Merchant;

public class BloodShopRoom extends AbstractRoom
{
    public static final float BLOOD_SHOP_CHANCE = 10f;
    private static ShaderProgram shader = new ShaderProgram(
            Gdx.files.internal(HubrisMod.assetPath("shaders/bloodmerchant/vertexShader.vs")),
            Gdx.files.internal(HubrisMod.assetPath("shaders/bloodmerchant/fragShader.fs"))
    );

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ShopRoom");
    public static final String[] TEXT = uiStrings.TEXT;
    public int shopRarityBonus = 6;
    public Merchant merchant;

    public BloodShopRoom()
    {
        phase = RoomPhase.COMPLETE;
        merchant = null;
        mapSymbol = "$";
        mapImg = ImageMaster.MAP_NODE_MERCHANT;
        mapImgOutline = ImageMaster.MAP_NODE_MERCHANT_OUTLINE;
    }

    public void setMerchant(Merchant merc)
    {
        merchant = merc;
    }

    @Override
    public void onPlayerEntry()
    {
        playBGM("SHOP");
        AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[0]);
        setMerchant(new Merchant());
    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll)
    {
        if (roll < 3 + shopRarityBonus) {
            return AbstractCard.CardRarity.RARE;
        }
        if (roll < 40 + shopRarityBonus) {
            return AbstractCard.CardRarity.UNCOMMON;
        }
        return AbstractCard.CardRarity.COMMON;
    }

    @Override
    public void update()
    {
        super.update();
        if (merchant != null) {
            merchant.update();
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (merchant != null) {
            CardCrawlGame.psb.setShader(shader);
            merchant.render(sb);
            CardCrawlGame.psb.setShader(null);
        }
        super.render(sb);
        renderTips(sb);
    }
}
