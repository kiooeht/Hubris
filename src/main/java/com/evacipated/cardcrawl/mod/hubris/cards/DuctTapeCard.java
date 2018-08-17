package com.evacipated.cardcrawl.mod.hubris.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.evacipated.cardcrawl.mod.hubris.CardIgnore;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.DuctTapeUseNextAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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

    private List<AbstractCard> cards;
    private List<String> savedKeywords = new ArrayList<>();

    public DuctTapeCard(List<AbstractCard> pCards)
    {
        super(ID, "Duct Tape", null, -2, "", CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        assert pCards != null;
        assert pCards.size() > 0;

        cards = new ArrayList<>(pCards.size());
        for (AbstractCard c : pCards) {
            cards.add(c.makeStatEquivalentCopy());
        }

        // Construct the combined artwork
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 250, 188, false);
        TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());

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
            portrait1 = new TextureAtlas.AtlasRegion(portrait1);
            portrait1.setRegion(
                    portrait1.getRegionX() + portrait1.getRegionWidth() / 2,
                    portrait1.getRegionY(),
                    portrait1.getRegionWidth() / 2,
                    portrait1.getRegionHeight()
            );

            fbo.begin();
            SpriteBatch sb = new SpriteBatch();
            sb.begin();

            sb.draw(portrait0, 0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            sb.draw(portrait1, Gdx.graphics.getWidth() / 2.0f, 0.0f, Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight());

            sb.end();
            fbo.end();

            try {
                Field f = AbstractCard.class.getDeclaredField("portrait");
                f.setAccessible(true);

                TextureAtlas.AtlasRegion atlasRegion = new TextureAtlas.AtlasRegion(region.getTexture(), 0, 0, region.getTexture().getWidth(), region.getTexture().getHeight());
                atlasRegion.flip(false, true);
                f.set(this, atlasRegion);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        calculateCard();
    }

    private void calculateCard()
    {
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

        calculateKeywords();

        calculateDescription();
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
    }

    private void calculateDescription()
    {
        // Make description from card names
        rawDescription = cards.stream().map(c -> c.name).collect(Collectors.joining(" NL + NL "));
        if (exhaust) {
            rawDescription += " NL Exhaust.";
        }
        if (isInnate && isEthereal) {
            rawDescription = "Innate. Ethereal. NL " + rawDescription;
        } else if (isInnate) {
            rawDescription = "Innate. NL " + rawDescription;
        } else if (isEthereal) {
            rawDescription = "Ethereal. NL " + rawDescription;
        }
        initializeDescription();
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        for (DescriptionLine line : description) {
            if (!line.text.startsWith("*")) {
                line.text = String.join(" *" , line.text.split(" "));
                line.text = "*" + line.text;
            }
        }
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

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : cards) {
            if (!c.canUse(p, m)) {
                return false;
            }
        }
        return true;
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
        cards.get(0).use(p, m);
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
        if (!upgraded) {
            upgradeName();
            for (int i=0; i<cards.size(); ++i) {
                AbstractCard c = cards.get(i).makeStatEquivalentCopy();
                if (c.canUpgrade()) {
                    c.upgrade();
                }
                cards.set(i, c);
            }
            calculateCard();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new DuctTapeCard(cards);
    }
}
