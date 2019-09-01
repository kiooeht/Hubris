package com.evacipated.cardcrawl.mod.hubris;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.hubris.cards.black.InfiniteBlow;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.Hubris;
import com.evacipated.cardcrawl.mod.hubris.cards.red.ReadiedAction;
import com.evacipated.cardcrawl.mod.hubris.crossover.InfiniteCrossover;
import com.evacipated.cardcrawl.mod.hubris.crossover.MysticCrossover;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.TheFatedDie;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.YourTowel;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.UpdateBodyText;
import com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler;
import com.evacipated.cardcrawl.mod.hubris.events.thecity.Experiment;
import com.evacipated.cardcrawl.mod.hubris.monsters.MerchantMonster;
import com.evacipated.cardcrawl.mod.hubris.monsters.NecromanticTotem;
import com.evacipated.cardcrawl.mod.hubris.monsters.GrandSnecko;
import com.evacipated.cardcrawl.mod.hubris.monsters.MusketHawk;
import com.evacipated.cardcrawl.mod.hubris.relics.*;
import com.evacipated.cardcrawl.mod.hubris.shop.BloodShopScreen;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Vintage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import infinitespire.InfiniteSpire;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@SpireInitializer
public class HubrisMod implements
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        EditStringsSubscriber,
        PostDeathSubscriber,
        MaxHPChangeSubscriber,
        AddCustomModeModsSubscriber,
        PostDungeonInitializeSubscriber
{
    public static final Logger logger = LogManager.getLogger(HubrisMod.class.getSimpleName());

    private static SpireConfig modConfig = null;
    public static SpireConfig otherSaveData = null;

    // Beta card asset paths
    public static final String BETA_ATTACK = HubrisMod.assetPath("images/cards/betaAttack.png");
    public static final String BETA_SKILL  = HubrisMod.assetPath("images/cards/betaSkill.png");
    public static final String BETA_POWER  = HubrisMod.assetPath("images/cards/betaPower.png");

    // Power asset texture atlas
    public static TextureAtlas powerAtlas;

    public static BloodShopScreen bloodShopScreen;

    // Crossover checks
    public static final boolean hasReplayTheSpire;
    public static final boolean hasConstructMod;
    public static final boolean hasFruityMod;
    public static final boolean hasInfiniteSpire;
    public static final boolean hasMimicMod;
    public static final boolean hasMysticMod;
    public static final boolean hasDisciple;
    public static final boolean hasBard;

    static
    {
        hasReplayTheSpire = Loader.isModLoaded("ReplayTheSpireMod");
        if (hasReplayTheSpire) {
            logger.info("Detected Replay The Spire");
        }
        hasConstructMod = Loader.isModLoaded("constructmod");
        if (hasConstructMod) {
            logger.info("Detected ConstructMod");
        }
        hasFruityMod = Loader.isModLoaded("fruitymod-sts");
        if (hasFruityMod) {
            logger.info("Detected FruityMod");
        }
        hasInfiniteSpire = Loader.isModLoaded("infinitespire");
        if (hasInfiniteSpire) {
            logger.info("Detected Infinite Spire");
        }
        hasMimicMod = Loader.isModLoaded("mimicmod");
        if (hasMimicMod) {
            logger.info("Detected Mimic Mod");
        }
        hasMysticMod = Loader.isModLoaded("MysticMod");
        if (hasMysticMod) {
            logger.info("Detected Mystic Mod");
        }
        hasDisciple = Loader.isModLoaded("chronomuncher");
        if (hasDisciple) {
            logger.info("Detected Disciple");
        }
        hasBard = Loader.isModLoaded("bard");
        if (hasBard) {
            logger.info("Detected Bard");
        }
    }

    public static void initialize()
    {
        BaseMod.subscribe(new HubrisMod());

        try {
            Properties defaults = new Properties();
            defaults.put("startingHubris", Boolean.toString(false));
            defaults.put("crackedHourglass", Boolean.toString(true));
            modConfig = new SpireConfig("Hubris", "Config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String assetPath(String path)
    {
        return "hubrisAssets/" + path;
    }

    public static boolean startingHubris()
    {
        if (AbstractPlayer.customMods != null && AbstractPlayer.customMods.contains(Hubris.ID)) {
            return true;
        }

        if (modConfig == null) {
            return false;
        }

        return modConfig.getBool("startingHubris");
    }

    public static boolean crackedHourglassEnabled()
    {
        if (modConfig == null) {
            return true;
        }
        return modConfig.getBool("crackedHourglass");
    }

    public static void loadOtherData()
    {
        logger.info("Loading Other Save Data");
        try {
            otherSaveData = new SpireConfig("Hubris", "OtherSaveData");
            if (hasInfiniteSpire) {
                InfiniteBlow.load();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData()
    {
        logger.info("Saving Data");
        try {
            if (otherSaveData == null) {
                otherSaveData = new SpireConfig("Hubris", "OtherSaveData");
            }
            if (hasInfiniteSpire) {
                InfiniteBlow.save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearData()
    {
        logger.info("Clearing Saved Data");
        try {
            SpireConfig config = new SpireConfig("Hubris", "SaveData");
            config.clear();
            config.save();

            MysteriousPyramids.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivePostInitialize()
    {
        loadOtherData();

        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton hubrisBtn = new ModLabeledToggleButton("Add Hubris curse to starting deck",
                350, 600, Settings.CREAM_COLOR, FontHelper.charDescFont,
                startingHubris(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("startingHubris", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(hubrisBtn);
        ModLabeledToggleButton hourglassBtn = new ModLabeledToggleButton("Enable Cracked Hourglass relic",
                350, 550, Settings.CREAM_COLOR, FontHelper.charDescFont,
                crackedHourglassEnabled(), settingsPanel, l -> {},
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("crackedHourglass", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(hourglassBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage(assetPath("images/hubris/modBadge.png")), "Hubris", "kiooeht", "TODO", settingsPanel);

        BaseMod.addEvent(TheFatedDie.ID, TheFatedDie.class);
        BaseMod.addEvent(Experiment.ID, Experiment.class, TheCity.ID);
        // Only appears if player has Bottle relic. See TheBottlerPatch
        BaseMod.addEvent(TheBottler.ID, TheBottler.class, TheBeyond.ID);
        BaseMod.addEvent(UpdateBodyText.ID, UpdateBodyText.class);
        BaseMod.addEvent(YourTowel.ID, YourTowel.class);

        BaseMod.addMonster(MerchantMonster.ID, (BaseMod.GetMonster) MerchantMonster::new);
        BaseMod.addMonster(GrandSnecko.ID, GrandSnecko::new);
        BaseMod.addMonster(MusketHawk.ID, MusketHawk::new);
        BaseMod.addMonster(NecromanticTotem.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Centurion(-436.0f, -4.0f),
                new Cultist(-170.0f, 6.0f),
                new NecromanticTotem()
        }));

        BaseMod.addBoss(TheBeyond.ID, GrandSnecko.ID, assetPath("images/ui/map/boss/grandSnecko.png"), assetPath("images/ui/map/bossOutline/grandSnecko.png"));
        BaseMod.addBoss(TheCity.ID, MusketHawk.ID, assetPath("images/ui/map/boss/musketHawk.png"), assetPath("images/ui/map/bossOutline/musketHawk.png"));
        //BaseMod.addBoss(TheCity.ID, NecromanticTotem.ID, assetPath("images/ui/map/boss/necromanticTotem.png"), assetPath("images/ui/map/bossOutline/necromanticTotem.png"));

        powerAtlas = new TextureAtlas(Gdx.files.internal(assetPath("images/powers/powers.atlas")));

        bloodShopScreen = new BloodShopScreen();
    }

    @Override
    public void receivePostDeath()
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(TinFlute.ID)) {
            TinFlute flute = (TinFlute) AbstractDungeon.player.getRelic(TinFlute.ID);
            flute.onDeath();
        }
    }

    @Override
    public void receiveEditCards()
    {
        try {
            autoAddCards();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (hasInfiniteSpire) {
            BaseMod.addColor(
                    infinitespire.patches.CardColorEnumPatch.CardColorPatch.INFINITE_BLACK,
                    InfiniteSpire.CARD_COLOR,
                    InfiniteSpire.CARD_COLOR,
                    InfiniteSpire.CARD_COLOR,
                    InfiniteSpire.CARD_COLOR,
                    InfiniteSpire.CARD_COLOR,
                    Color.BLACK.cpy(),
                    InfiniteSpire.CARD_COLOR,
                    "img/infinitespire/cards/ui/512/boss-attack.png",
                    "img/infinitespire/cards/ui/512/boss-skill.png",
                    "img/infinitespire/cards/ui/512/boss-power.png",
                    "img/infinitespire/cards/ui/512/boss-orb.png",
                    "img/infinitespire/cards/ui/1024/boss-attack.png",
                    "img/infinitespire/cards/ui/1024/boss-skill.png",
                    "img/infinitespire/cards/ui/1024/boss-power.png",
                    "img/infinitespire/cards/ui/1024/boss-orb.png"
            );
            InfiniteCrossover.Cards();
        }
    }

    @Override
    public void receiveEditRelics()
    {
        BaseMod.addRelic(new Icosahedron(), RelicType.SHARED);
        BaseMod.addRelic(new BlackHole(), RelicType.SHARED);
        BaseMod.addRelic(new VirtuousBlindfold(), RelicType.SHARED);
        BaseMod.addRelic(new PeanutButter(), RelicType.SHARED);
        BaseMod.addRelic(new CuriousFeather(), RelicType.SHARED);
        BaseMod.addRelic(new Metronome(), RelicType.SHARED);
        BaseMod.addRelic(new FunFungus(), RelicType.SHARED);
        BaseMod.addRelic(new CrackedHourglass(), RelicType.SHARED);
        BaseMod.addRelic(new ScarierMask(), RelicType.SHARED);
        BaseMod.addRelic(new DeadTorch(), RelicType.SHARED);
        BaseMod.addRelic(new BottledHeart(), RelicType.SHARED);
        BaseMod.addRelic(new DisguiseKit(), RelicType.SHARED);
        BaseMod.addRelic(new Teleporter(), RelicType.SHARED);
        BaseMod.addRelic(new MysteriousPyramids(), RelicType.SHARED);
        BaseMod.addRelic(new AstralHammer(), RelicType.SHARED);
        //BaseMod.addRelic(new PrototypeTalaria(), RelicType.SHARED);
        BaseMod.addRelic(new Spice(), RelicType.SHARED);
        BaseMod.addRelic(new TinFlute(), RelicType.SHARED);
        BaseMod.addRelic(new GlazedTorus(), RelicType.SHARED);
        BaseMod.addRelic(new Backtick(), RelicType.SHARED);
        BaseMod.addRelic(new Test447(), RelicType.SHARED);
        BaseMod.addRelic(new BundleOfHerbs(), RelicType.SHARED);
        BaseMod.addRelic(new SphereOfDissonance(), RelicType.SHARED);
        if (!hasDisciple) { // TODO: Currently there's a cross-mod bug with Disciple
            BaseMod.addRelic(new Stopwatch(), RelicType.SHARED);
        }
        BaseMod.addRelic(new HerbalPaste(), RelicType.SHARED);
        BaseMod.addRelic(new MedicalManual(), RelicType.SHARED);
        BaseMod.addRelic(new HollowSoul(), RelicType.SHARED);
        BaseMod.addRelic(new CrystalStatue(), RelicType.SHARED);
        BaseMod.addRelic(new BottledRain(), RelicType.SHARED);
        BaseMod.addRelic(new Zylophone(), RelicType.SHARED);
        BaseMod.addRelic(new EmptyBottle(), RelicType.SHARED);
        BaseMod.addRelic(new DuctTape(), RelicType.SHARED);
        //BaseMod.addRelic(new BloodyCrown(), RelicType.SHARED);
        BaseMod.addRelic(new OldNail(), RelicType.SHARED);
        BaseMod.addRelic(new NiceRug(), RelicType.SHARED);
        BaseMod.addRelic(new R64BitClover(), RelicType.SHARED);
        BaseMod.addRelic(new TerracottaHorce(), RelicType.SHARED);
        BaseMod.addRelic(new Reverence(), RelicType.SHARED);
        BaseMod.addRelic(new RunicObelisk(), RelicType.SHARED);
        BaseMod.addRelic(new SlimyHat(), RelicType.SHARED);
        BaseMod.addRelic(new ToyBattleship(), RelicType.SHARED);
        BaseMod.addRelic(new DeckOfHolding(), RelicType.SHARED);
        BaseMod.addRelic(new TenFootPole(), RelicType.SHARED);
        BaseMod.addRelic(new Towel(), RelicType.SHARED);
        BaseMod.addRelic(new Potato(), RelicType.SHARED);
        BaseMod.addRelic(new FruitBowl(), RelicType.SHARED);
        BaseMod.addRelic(new EvacipatedBox(), RelicType.SHARED);
        //BaseMod.addRelic(new Tomato(), RelicType.SHARED);
        BaseMod.addRelic(new Macrotransations(), RelicType.SHARED);
        BaseMod.addRelic(new GrandSneckoEye(), RelicType.SHARED);
        //BaseMod.addRelic(new Rogue(), RelicType.SHARED);

        // Ironclad only
        BaseMod.addRelic(new IronBody(), RelicType.RED);
        BaseMod.addRelic(new ChampionShield(), RelicType.RED);

        // Silent only
        BaseMod.addRelic(new AncientText(), RelicType.GREEN);

        // Defect only
        BaseMod.addRelic(new RGBLights(), RelicType.BLUE);
        BaseMod.addRelic(new BallOfYels(), RelicType.BLUE);
        BaseMod.addRelic(new SoftwareUpdate(), RelicType.BLUE);

        if (hasConstructMod) {
            BaseMod.addRelicToCustomPool(new ClockworkCow(), constructmod.patches.AbstractCardEnum.CONSTRUCTMOD);
        }
        if (hasFruityMod) {
            BaseMod.addRelicToCustomPool(new DustyCowl(), fruitymod.seeker.patches.AbstractCardEnum.SEEKER_PURPLE);
        }
        if (hasInfiniteSpire) {
            BaseMod.addRelic(new MobiusCoin(), RelicType.SHARED);
            BaseMod.addRelic(new KleinBottle(), RelicType.SHARED);
        }
        if (hasMysticMod) {
            MysticCrossover.Relics();
        }
    }

    private static String makeLocPath(String filename)
    {
        String toReturn = "localization/";
        switch (Settings.language)
        {
            case RUS:
                toReturn += "rus/";
                break;
            case ZHS:
                toReturn += "zhs/";
                break;
            default:
                toReturn += "eng/";
                break;
        }
        return (toReturn + filename + ".json");
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath(makeLocPath("Hubris-Keywords"))).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword("hubris", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditStrings()
    {
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath(makeLocPath("Hubris-RelicStrings")));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath(makeLocPath("Hubris-CardStrings")));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath(makeLocPath("Hubris-IcosahedronStrings")));
        BaseMod.loadCustomStringsFile(OrbStrings.class, assetPath(makeLocPath("Hubris-OrbStrings")));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath(makeLocPath("Hubris-PowerStrings")));
        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath(makeLocPath("Hubris-EventStrings")));
        BaseMod.loadCustomStringsFile(RunModStrings.class, assetPath(makeLocPath("Hubris-RunModStrings")));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath(makeLocPath("Hubris-UIStrings")));
    }

    @Override
    public void receiveCustomModeMods(List<CustomMod> list)
    {
        list.add(new CustomMod(Hubris.ID, "r", true));
        list.add(new CustomMod("hubris:Mercantile", "b", false));
    }

    private static void autoAddCards() throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, ClassNotFoundException
    {
        ClassFinder finder = new ClassFinder();
        URL url = HubrisMod.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new CardFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());
            if (cls.hasAnnotation(CardIgnore.class)) {
                continue;
            }
            boolean isCard = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractCard.class.getName())) {
                    isCard = true;
                    break;
                }
            }
            if (!isCard) {
                continue;
            }
            if (hasDisciple && classInfo.getClassName().equals(ReadiedAction.class.getName())) {
                // TODO: Currently there's a cross-mod bug with Disciple
                continue;
            }

            System.out.println(classInfo.getClassName());
            AbstractCard card = (AbstractCard) Loader.getClassPool().getClassLoader().loadClass(cls.getName()).newInstance();
            BaseMod.addCard(card);
            if (cls.hasAnnotation(CardNoSeen.class)) {
                UnlockTracker.hardUnlockOverride(card.cardID);
            } else {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }

    @Override
    public int receiveMaxHPChange(int amount)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Rogue.ID)) {
            Rogue relic = (Rogue) AbstractDungeon.player.getRelic(Rogue.ID);
            amount = relic.onMaxHPChange(amount);
        }
        if (amount == 0) {
            return amount;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(FruitBowl.ID)) {
            FruitBowl relic = (FruitBowl) AbstractDungeon.player.getRelic(FruitBowl.ID);
            amount = relic.onMaxHPChange(amount);
        }
        if (amount == 0) {
            return amount;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BottledHeart.ID)) {
            BottledHeart relic = (BottledHeart) AbstractDungeon.player.getRelic(BottledHeart.ID);
            amount = relic.onMaxHPChange(amount);
        }
        return amount;
    }

    @Override
    public void receivePostDungeonInitialize()
    {
        if (ModHelper.isModEnabled(Vintage.ID)) {
            if (AbstractDungeon.rareRelicPool.removeIf(r -> r.equals(R64BitClover.ID))) {
                logger.info(R64BitClover.ID + " removed.");
            }
        }

        if (!crackedHourglassEnabled()) {
            if (AbstractDungeon.bossRelicPool.removeIf(r -> r.equals(CrackedHourglass.ID))) {
                logger.info(CrackedHourglass.ID + " removed.");
            }
        }
    }
}
