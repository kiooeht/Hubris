package com.evacipated.cardcrawl.mod.hubris.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.BaseModCardTags;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.DuctTapeUseNextAction;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CardIgnore
public class DuctTapeCard extends CustomCard
{
    public static final String ID = "hubris:DuctTapeCard";
    private static final List<String> keywordBlacklist = Arrays.asList(
            "strike"
    );
    private static final Map<CardColor, Map<CardType, Texture>> cardBgMap;
    private static final Map<CardColor, Map<CardType, Texture>> cardLargeBgMap;
    private static final Map<CardRarity, Map<CardType, Texture>> cardFrameMap;
    private static final Map<CardRarity, Map<CardType, Texture>> cardLargeFrameMap;

    private List<AbstractCard> cards;
    private List<Texture> cardBgs = new ArrayList<>();
    private List<Texture> cardLargeBgs = new ArrayList<>();
    private List<Texture> cardFrames = new ArrayList<>();
    private List<Texture> cardLargeFrames = new ArrayList<>();
    private List<String> savedKeywords = new ArrayList<>();

    static
    {
        // Base Game card backgrounds
        cardBgMap = new HashMap<>();
        Map<CardType, Texture> red = new HashMap<>();
        cardBgMap.put(CardColor.RED, red);
        Map<CardType, Texture> green = new HashMap<>();
        cardBgMap.put(CardColor.GREEN, green);
        Map<CardType, Texture> blue = new HashMap<>();
        cardBgMap.put(CardColor.BLUE, blue);
        Map<CardType, Texture> colorless = new HashMap<>();
        cardBgMap.put(CardColor.COLORLESS, colorless);
        Map<CardType, Texture> curse = new HashMap<>();
        cardBgMap.put(CardColor.CURSE, curse);
        
        red.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_RED);
        red.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_RED);
        red.put(CardType.POWER, ImageMaster.CARD_POWER_BG_RED);

        green.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_GREEN);
        green.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_GREEN);
        green.put(CardType.POWER, ImageMaster.CARD_POWER_BG_GREEN);

        blue.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_BLUE);
        blue.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_BLUE);
        blue.put(CardType.POWER, ImageMaster.CARD_POWER_BG_BLUE);

        colorless.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_GRAY);
        colorless.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_GRAY);
        colorless.put(CardType.POWER, ImageMaster.CARD_POWER_BG_GRAY);

        curse.put(CardType.ATTACK, ImageMaster.CARD_SKILL_BG_BLACK);
        curse.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_BLACK);
        curse.put(CardType.POWER, ImageMaster.CARD_SKILL_BG_BLACK);
        
        cardLargeBgMap = new HashMap<>();
        red = new HashMap<>();
        cardLargeBgMap.put(CardColor.RED, red);
        green = new HashMap<>();
        cardLargeBgMap.put(CardColor.GREEN, green);
        blue = new HashMap<>();
        cardLargeBgMap.put(CardColor.BLUE, blue);
        colorless = new HashMap<>();
        cardLargeBgMap.put(CardColor.COLORLESS, colorless);
        curse = new HashMap<>();
        cardLargeBgMap.put(CardColor.CURSE, curse);

        red.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_RED_L);
        red.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_RED_L);
        red.put(CardType.POWER, ImageMaster.CARD_POWER_BG_RED_L);

        green.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_GREEN_L);
        green.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_GREEN_L);
        green.put(CardType.POWER, ImageMaster.CARD_POWER_BG_GREEN_L);

        blue.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_BLUE_L);
        blue.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_BLUE_L);
        blue.put(CardType.POWER, ImageMaster.CARD_POWER_BG_BLUE_L);

        colorless.put(CardType.ATTACK, ImageMaster.CARD_ATTACK_BG_GRAY_L);
        colorless.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_GRAY_L);
        colorless.put(CardType.POWER, ImageMaster.CARD_POWER_BG_GRAY_L);

        curse.put(CardType.ATTACK, ImageMaster.CARD_SKILL_BG_BLACK_L);
        curse.put(CardType.SKILL, ImageMaster.CARD_SKILL_BG_BLACK_L);
        curse.put(CardType.POWER, ImageMaster.CARD_SKILL_BG_BLACK_L);
        
        // Base game card frames
        cardFrameMap = new HashMap<>();
        Map<CardType, Texture> common = new HashMap<>();
        cardFrameMap.put(CardRarity.COMMON, common);
        cardFrameMap.put(CardRarity.BASIC, common);
        cardFrameMap.put(CardRarity.CURSE, common);
        Map<CardType, Texture> uncommon = new HashMap<>();
        cardFrameMap.put(CardRarity.UNCOMMON, uncommon);
        Map<CardType, Texture> rare = new HashMap<>();
        cardFrameMap.put(CardRarity.RARE, rare);

        common.put(CardType.ATTACK, ImageMaster.CARD_FRAME_ATTACK_COMMON);
        common.put(CardType.SKILL, ImageMaster.CARD_FRAME_SKILL_COMMON);
        common.put(CardType.POWER, ImageMaster.CARD_FRAME_POWER_COMMON);

        uncommon.put(CardType.ATTACK, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON);
        uncommon.put(CardType.SKILL, ImageMaster.CARD_FRAME_SKILL_UNCOMMON);
        uncommon.put(CardType.POWER, ImageMaster.CARD_FRAME_POWER_UNCOMMON);

        rare.put(CardType.ATTACK, ImageMaster.CARD_FRAME_ATTACK_RARE);
        rare.put(CardType.SKILL, ImageMaster.CARD_FRAME_SKILL_RARE);
        rare.put(CardType.POWER, ImageMaster.CARD_FRAME_POWER_RARE);

        cardLargeFrameMap = new HashMap<>();
        common = new HashMap<>();
        cardLargeFrameMap.put(CardRarity.COMMON, common);
        cardLargeFrameMap.put(CardRarity.BASIC, common);
        cardLargeFrameMap.put(CardRarity.CURSE, common);
        uncommon = new HashMap<>();
        cardLargeFrameMap.put(CardRarity.UNCOMMON, uncommon);
        rare = new HashMap<>();
        cardLargeFrameMap.put(CardRarity.RARE, rare);

        common.put(CardType.ATTACK, ImageMaster.CARD_FRAME_ATTACK_COMMON_L);
        common.put(CardType.SKILL, ImageMaster.CARD_FRAME_SKILL_COMMON_L);
        common.put(CardType.POWER, ImageMaster.CARD_FRAME_POWER_COMMON_L);

        uncommon.put(CardType.ATTACK, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON_L);
        uncommon.put(CardType.SKILL, ImageMaster.CARD_FRAME_SKILL_UNCOMMON_L);
        uncommon.put(CardType.POWER, ImageMaster.CARD_FRAME_POWER_UNCOMMON_L);

        rare.put(CardType.ATTACK, ImageMaster.CARD_FRAME_ATTACK_RARE_L);
        rare.put(CardType.SKILL, ImageMaster.CARD_FRAME_SKILL_RARE_L);
        rare.put(CardType.POWER, ImageMaster.CARD_FRAME_POWER_RARE_L);
    }

    public DuctTapeCard(List<AbstractCard> pCards)
    {
        super(ID, "Duct Tape", null, -2, "", CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        assert pCards != null;
        assert pCards.size() > 0;

        cards = new ArrayList<>(pCards.size());
        for (AbstractCard c : pCards) {
            cards.add(c.makeStatEquivalentCopy());
        }

        calculateCard();
    }

    private void calculateCard()
    {
        calculateCardTags();

        calculateBgs();

        calculateCost();
        calculateTarget();

        // Rarity
        for (AbstractCard c : cards) {
            if (c.rarity.ordinal() > rarity.ordinal()) {
                rarity = c.rarity;
            }
        }

        // Type
        for (AbstractCard c : cards) {
            switch (c.type) {
                case POWER:
                    if (type != CardType.CURSE) {
                        type = CardType.POWER;
                    }
                    break;
                case ATTACK:
                    if (type != CardType.CURSE && type != CardType.POWER) {
                        type = CardType.ATTACK;
                    }
                    break;
                case SKILL:
                    if (type != CardType.CURSE && type != CardType.POWER && type != CardType.ATTACK) {
                        type = CardType.SKILL;
                    }
                    break;
            }
        }

        // Upgrade name
        name = "Duct Tape";
        for (AbstractCard c : cards) {
            if (c.upgraded) {
                name += "+";
                if (c.timesUpgraded > 1) {
                    name += c.timesUpgraded;
                }
            }
        }
        initializeTitle();

        calculateFrames();

        calculateKeywords();

        calculateDescription();
    }

    private void calculateCardTags()
    {
        Set<CardTags> tags = new HashSet<>();
        for (AbstractCard c : cards) {
            tags.addAll(c.tags);
        }
        tags.remove(BaseModCardTags.BASIC_STRIKE);
        tags.remove(BaseModCardTags.BASIC_DEFEND);
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public Texture calculateLargePortrait()
    {
        try {
            FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 500, 380, false);
            TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());

            Texture portraitTexture;
            if (cards.get(0) instanceof CustomCard) {
                portraitTexture = CustomCard.getPortraitImage((CustomCard) cards.get(0));
            } else {
                portraitTexture = ImageMaster.loadImage("images/1024Portraits/" + cards.get(0).assetUrl + ".png");
            }
            TextureRegion portrait0 = new TextureRegion(portraitTexture);
            portrait0.setRegion(
                    portrait0.getRegionX(),
                    portrait0.getRegionY(),
                    portrait0.getRegionWidth() / 2,
                    portrait0.getRegionHeight()
            );
            portrait0.flip(false, true);

            if (cards.get(1) instanceof CustomCard) {
                portraitTexture = CustomCard.getPortraitImage((CustomCard) cards.get(1));
            } else {
                portraitTexture = ImageMaster.loadImage("images/1024Portraits/" + cards.get(1).assetUrl + ".png");
            }
            TextureRegion portrait1 = new TextureRegion(portraitTexture);
            portrait1.setRegion(
                    portrait1.getRegionX() + portrait1.getRegionWidth() / 2,
                    portrait1.getRegionY(),
                    portrait1.getRegionWidth() / 2,
                    portrait1.getRegionHeight()
            );
            portrait1.flip(false, true);

            fbo.begin();
            SpriteBatch sb = new SpriteBatch();
            sb.begin();

            sb.draw(portrait0, 0.0f, 0.0f, Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight());

            sb.draw(portrait1, Gdx.graphics.getWidth() / 2.0f, 0.0f, Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight());

            sb.end();
            fbo.end();

            region.flip(false, true);
            return region.getTexture();
        } catch (Exception e) {
            HubrisMod.logger.error(e);
            return null;
        }
    }

    private void calculateBgs()
    {
        cardBgs.clear();
        cardLargeBgs.clear();
        for (AbstractCard c : cards) {
            if (cardBgMap.containsKey(c.color)) {
                Map<CardType, Texture> tmp = cardBgMap.get(c.color);
                if (tmp.containsKey(c.type)) {
                    cardBgs.add(tmp.get(c.type));
                    cardLargeBgs.add(cardLargeBgMap.get(c.color).get(c.type));
                } else {
                    cardBgs.add(ImageMaster.CARD_SKILL_BG_BLACK);
                    cardLargeBgs.add(ImageMaster.CARD_SKILL_BG_BLACK_L);
                }
            } else {
                Texture texture;
                Texture largeTexture;
                switch (c.type) {
                    case POWER:
                        if (BaseMod.getPowerBgTexture(c.color) == null) {
                            BaseMod.savePowerBgTexture(c.color, ImageMaster.loadImage(BaseMod.getPowerBg(c.color)));
                        }
                        texture = BaseMod.getPowerBgTexture(c.color);
                        if (BaseMod.getPowerBgPortraitTexture(c.color) == null) {
                            BaseMod.savePowerBgPortraitTexture(c.color, ImageMaster.loadImage(BaseMod.getPowerBgPortrait(c.color)));
                        }
                        largeTexture = BaseMod.getPowerBgPortraitTexture(c.color);
                        break;
                    case ATTACK:
                        if (BaseMod.getAttackBgTexture(c.color) == null) {
                            BaseMod.saveAttackBgTexture(c.color, ImageMaster.loadImage(BaseMod.getAttackBg(c.color)));
                        }
                        texture = BaseMod.getAttackBgTexture(c.color);
                        if (BaseMod.getAttackBgPortraitTexture(c.color) == null) {
                            BaseMod.saveAttackBgPortraitTexture(c.color, ImageMaster.loadImage(BaseMod.getAttackBgPortrait(c.color)));
                        }
                        largeTexture = BaseMod.getAttackBgPortraitTexture(c.color);
                        break;
                    case SKILL:
                        if (BaseMod.getSkillBgTexture(c.color) == null) {
                            BaseMod.saveSkillBgTexture(c.color, ImageMaster.loadImage(BaseMod.getSkillBg(c.color)));
                        }
                        texture = BaseMod.getSkillBgTexture(c.color);
                        if (BaseMod.getSkillBgPortraitTexture(c.color) == null) {
                            BaseMod.saveSkillBgPortraitTexture(c.color, ImageMaster.loadImage(BaseMod.getSkillBgPortrait(c.color)));
                        }
                        largeTexture = BaseMod.getSkillBgPortraitTexture(c.color);
                        break;
                    default:
                        texture = ImageMaster.CARD_SKILL_BG_BLACK;
                        largeTexture = ImageMaster.CARD_SKILL_BG_BLACK_L;
                        break;
                }
                cardBgs.add(texture);
                cardLargeBgs.add(largeTexture);
            }
        }
    }

    private void calculateFrames()
    {
        cardFrames.clear();
        cardLargeFrames.clear();
        for (AbstractCard c : cards) {
            if (cardFrameMap.containsKey(c.rarity)) {
                Map<CardType, Texture> tmp = cardFrameMap.get(c.rarity);
                if (tmp.containsKey(c.type)) {
                    cardFrames.add(tmp.get(c.type));
                    cardLargeFrames.add(cardLargeFrameMap.get(c.rarity).get(c.type));
                } else {
                    cardFrames.add(ImageMaster.CARD_FRAME_SKILL_COMMON);
                    cardLargeFrames.add(ImageMaster.CARD_FRAME_SKILL_COMMON_L);
                }
            } else {
                cardFrames.add(ImageMaster.CARD_FRAME_SKILL_COMMON);
                cardLargeFrames.add(ImageMaster.CARD_FRAME_SKILL_COMMON_L);
            }
        }
    }

    private void calculateCost()
    {
        // Add up card costs
        cost = 0;
        for (AbstractCard c : cards) {
            if (c.cost < 0) {
                cost = c.cost;
                break;
            }
            cost += c.cost;
        }
        costForTurn = cost;

        if (cost < 0) {
            for (AbstractCard c : cards) {
                c.costForTurn = c.cost = cost;
            }
        }
    }

    private void calculateTarget()
    {
        // Figure out card target type
        boolean self = false;
        boolean enemy = false;
        boolean all_enemy = false;
        for (AbstractCard c : cards) {
            switch (c.target) {
                case SELF:
                    self = true;
                    break;
                case ENEMY:
                    enemy = true;
                    break;
                case ALL_ENEMY:
                    all_enemy = true;
                    break;
                case SELF_AND_ENEMY:
                    self = true;
                    enemy = true;
                    break;
                case ALL:
                    self = true;
                    all_enemy = true;
                    break;
            }
        }
        if (self && enemy) {
            target = CardTarget.SELF_AND_ENEMY;
        } else if (self && all_enemy) {
            target = CardTarget.ALL;
        } else if (self) {
            target = CardTarget.SELF;
        } else if (enemy) {
            target = CardTarget.ENEMY;
        } else if (all_enemy) {
            target = CardTarget.ALL_ENEMY;
        }
    }

    private void calculateKeywords()
    {
        // Exhaust
        exhaust = false;
        for (AbstractCard c : cards) {
            if (c.exhaust) {
                exhaust = true;
                break;
            }
        }
        // Ethereal
        isEthereal = false;
        for (AbstractCard c : cards) {
            if (c.isEthereal) {
                isEthereal = true;
                break;
            }
        }
        // Innate
        isInnate = false;
        for (AbstractCard c : cards) {
            if (c.isInnate) {
                isInnate = true;
                break;
            }
        }
        // Retain
        AlwaysRetainField.alwaysRetain.set(this, false);
        for (AbstractCard c : cards) {
            if (AlwaysRetainField.alwaysRetain.get(c)) {
                AlwaysRetainField.alwaysRetain.set(this, true);
                break;
            }
        }
        // Autoplay
        AutoplayField.autoplay.set(this, false);
        for (AbstractCard c : cards) {
            if (AutoplayField.autoplay.get(c)) {
                AutoplayField.autoplay.set(this, true);
                break;
            }
        }
    }

    private void calculateDescription()
    {
        // Make description from card names
        rawDescription = cards.stream().map(c -> c.name).collect(Collectors.joining(" NL + NL "));
        if (exhaust) {
            rawDescription += " NL Exhaust.";
        }
        String prefix = "";
        if (isInnate) {
            prefix += " Innate.";
        }
        if (isEthereal) {
            prefix += " Ethereal.";
        }
        if (AlwaysRetainField.alwaysRetain.get(this)) {
            prefix += " Retain.";
        }
        rawDescription = prefix.trim() + " NL " + rawDescription;
        initializeDescription();
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        for (DescriptionLine line : description) {
            String[] words = line.text.split(" ");
            for (int i=0; i<words.length; ++i) {
                if (words[i].startsWith("*")) {
                    words[i] = words[i].substring(1);
                }
            }
            line.text = String.join(" *" , words);
            line.text = "*" + line.text;
        }
    }

    @SpireOverride
    protected void renderPortrait(SpriteBatch sb)
    {
        float drawX = current_x - 125.0f;
        float drawY = current_y - 95.0f;

        TextureAtlas.AtlasRegion portrait0 = null;
        TextureAtlas.AtlasRegion portrait1 = null;
        try {
            Field f = AbstractCard.class.getDeclaredField("portrait");
            f.setAccessible(true);

            portrait0 = (TextureAtlas.AtlasRegion) f.get(cards.get(0));
            portrait1 = (TextureAtlas.AtlasRegion) f.get(cards.get(1));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (portrait0 != null && portrait1 != null) {
            boolean sameCard = cards.get(0).cardID.equals(cards.get(1).cardID);

            portrait0 = new TextureAtlas.AtlasRegion(portrait0);
            if (!sameCard) {
                portrait0.setRegion(
                        portrait0.getRegionX(),
                        portrait0.getRegionY(),
                        portrait0.getRegionWidth() / 2,
                        portrait0.getRegionHeight()
                );
            }
            portrait1 = new TextureAtlas.AtlasRegion(portrait1);
            if (!sameCard) {
                portrait1.setRegion(
                        portrait1.getRegionX() + portrait1.getRegionWidth() / 2,
                        portrait1.getRegionY(),
                        portrait1.getRegionWidth() / 2,
                        portrait1.getRegionHeight()
                );
            }

            sb.draw(portrait0,
                    drawX, drawY + 72.0F,
                    portrait0.packedWidth / 2.0F, portrait0.packedHeight / 2.0F - 72.0F,
                    portrait0.packedWidth / 2.0f, portrait0.packedHeight,
                    drawScale * Settings.scale, drawScale * Settings.scale,
                    angle
            );
            sb.draw(portrait1,
                    drawX + (portrait0.packedWidth / 2.0f), drawY + 72.0F,
                    0, portrait0.packedHeight / 2.0F - 72.0F,
                    portrait0.packedWidth / 2.0f, portrait0.packedHeight,
                    drawScale * Settings.scale, drawScale * Settings.scale,
                    angle
            );
        }
    }

    @SpireOverride
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        sb.setColor(Color.WHITE);
        sb.draw(cardBgs.get(0),
                x, y,
                256.0f, 256.0f, 256.0f, 512.0f,
                drawScale * Settings.scale, drawScale * Settings.scale,
                angle, 0, 0, 256, 512, false, false
        );
        sb.draw(cardBgs.get(1),
                x + 256.0f, y,
                0.0f, 256.0f, 256.0f, 512.0f,
                drawScale * Settings.scale, drawScale * Settings.scale,
                angle, 256, 0, 256, 512, false, false
        );
    }

    @SuppressWarnings("unused")
    public void renderDuctTapeLargeCardBg(SpriteBatch sb)
    {
        sb.setColor(Color.WHITE);
        sb.draw(cardLargeBgs.get(0),
                Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f,
                512, 512, 512, 1024,
                Settings.scale, Settings.scale,
                0, 0, 0, 512, 1024, false, false
        );
        sb.draw(cardLargeBgs.get(1),
                Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f - 512.0f,
                0, 512, 512, 1024,
                Settings.scale, Settings.scale,
                0, 512, 0, 512, 1024, false, false
        );
    }

    @SpireOverride
    protected void renderPortraitFrame(SpriteBatch sb, float x, float y)
    {
        sb.setColor(Color.WHITE);
        sb.draw(cardFrames.get(0),
                x, y,
                256.0f, 256.0f, 256.0f, 512.0f,
                drawScale * Settings.scale, drawScale * Settings.scale,
                angle, 0, 0, 256, 512, false, false
        );
        sb.draw(cardFrames.get(1),
                x + 256.0f, y,
                0.0f, 256.0f, 256.0f, 512.0f,
                drawScale * Settings.scale, drawScale * Settings.scale,
                angle, 256, 0, 256, 512, false, false
        );
    }

    @SuppressWarnings("unused")
    public void renderDuctTapeLargeFrame(SpriteBatch sb)
    {
        sb.setColor(Color.WHITE);
        sb.draw(cardLargeFrames.get(0),
                Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f,
                512, 512, 512, 1024,
                Settings.scale, Settings.scale,
                0, 0, 0, 512, 1024, false, false
        );
        sb.draw(cardLargeFrames.get(1),
                Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f - 512.0f,
                0.0f, 512, 512, 1024,
                Settings.scale, Settings.scale,
                0, 512, 0, 512, 1024, false, false
        );
    }

    @Override
    public void renderCardTip(SpriteBatch sb)
    {
        keywords.removeIf(keywordBlacklist::contains);
        savedKeywords.addAll(keywords);
        keywords.clear();
        super.renderCardTip(sb);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips()
    {
        List<TooltipInfo> tooltips = new ArrayList<>();
        for (AbstractCard card : cards) {
            Scanner scanner;
            String description = "";
            boolean firstLine = true;
            for (DescriptionLine line : card.description) {
                if (!firstLine) {
                    description += " NL ";
                }
                firstLine = false;

                scanner = new Scanner(line.text);
                while (scanner.hasNext()) {
                    String tmp = scanner.next() + ' ';
                    if (tmp.charAt(0) == '*') {
                        tmp = FontHelper.colorString(tmp.substring(1), "y");
                    } else if (tmp.charAt(0) == '!') {
                        Pattern pattern = Pattern.compile("!(.+)!(.*) ");
                        Matcher matcher = pattern.matcher(tmp);
                        if (matcher.find()) {
                            tmp = matcher.group(1);
                        }

                        // Main body of method
                        StringBuilder stringBuilder = new StringBuilder();
                        String color = null;
                        int num = 0;
                        DynamicVariable dv = BaseMod.cardDynamicVariableMap.get(tmp);
                        if (dv != null) {
                            if (dv.isModified(card)) {
                                num = dv.value(card);
                                if (num >= dv.baseValue(card)) {
                                    color = "g";
                                } else {
                                    color = "r";
                                }
                            } else {
                                num = dv.baseValue(card);
                            }
                        }
                        tmp = Integer.toString(num);
                        if (color != null) {
                            tmp = FontHelper.colorString(tmp, color);
                        }
                    }
                    description += tmp + " ";
                }
            }
            tooltips.add(new TooltipInfo(card.name, description));
        }

        // Saved keywords
        for (String keyword : savedKeywords) {
            if (GameDictionary.keywords.containsKey(keyword)) {
                tooltips.add(new TooltipInfo(TipHelper.capitalize(keyword), GameDictionary.keywords.get(keyword)));
            }
        }

        return tooltips;
    }

    private boolean subCardCanUse(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (card.canUse(p, m)) {
            return true;
        }
        if (card.cardPlayable(m)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : cards) {
            if (!subCardCanUse(c, p, m)) {
                return false;
            }
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (costForTurn == -1) {
            if (energyOnUse < EnergyPanel.totalCount) {
                energyOnUse = EnergyPanel.totalCount;
            }
            for (AbstractCard c : cards) {
                c.energyOnUse = energyOnUse;
            }
        }
        cards.get(0).calculateCardDamage(m);
        if (cards.get(0).canUse(p, m)) {
            cards.get(0).use(p, m);
        }
        AbstractDungeon.actionManager.addToBottom(new DuctTapeUseNextAction(cards, 1, p, m));
    }

    @Override
    public void applyPowers()
    {
        for (AbstractCard c : cards) {
            c.applyPowers();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        for (AbstractCard c : cards) {
            c.triggerOnExhaust();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        for (AbstractCard c : cards) {
            c.triggerOnManualDiscard();
        }
    }
    
    @Override
    public void triggerOnOtherCardPlayed(AbstractCard cardPlayed)
    {
        for (AbstractCard c : cards) {
            c.triggerOnOtherCardPlayed(cardPlayed);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        for (AbstractCard c : cards) {
            if (c.canUpgrade()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void upgrade()
    {
        timesUpgraded = 0;
        upgraded = true;
        for (int i=0; i<cards.size(); ++i) {
            AbstractCard c = cards.get(i);//.makeStatEquivalentCopy();
            if (c.canUpgrade()) {
                c.upgrade();
            }
            timesUpgraded += c.timesUpgraded;
            cards.set(i, c);
        }
        calculateCard();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new DuctTapeCard(cards);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        DuctTapeCard card = (DuctTapeCard) super.makeStatEquivalentCopy();

        for (int i=0; i<cards.size(); ++i) {
            card.cards.set(i, cards.get(i).makeStatEquivalentCopy());
        }

        return card;
    }

    @Override
    public AbstractCard makeSameInstanceOf()
    {
        DuctTapeCard card = (DuctTapeCard) super.makeSameInstanceOf();

        for (int i=0; i<cards.size(); ++i) {
            card.cards.get(i).uuid = cards.get(i).uuid;
        }

        return card;
    }

    public List<CardSave> makeCardSaves()
    {
        ArrayList<CardSave> ret = new ArrayList<>();

        for (AbstractCard card : cards) {
            ret.add(new CardSave(card.cardID, card.timesUpgraded, card.misc));
        }

        return ret;
    }

    public void checkCardUUIDs(HashSet<AbstractCard> foundCards, UUID uuid)
    {
        for (AbstractCard card : cards) {
            if (card.uuid.equals(uuid)) {
                foundCards.add(card);
            }
        }
    }

    public boolean containsCard(String cardID)
    {
        for (AbstractCard card : cards) {
            if (card.cardID.equals(cardID)) {
                return true;
            }
        }
        return false;
    }
}
