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
import com.evacipated.cardcrawl.mod.hubris.crossover.InfiniteCrossover;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.TheFatedDie;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.UpdateBodyText;
import com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler;
import com.evacipated.cardcrawl.mod.hubris.events.thecity.Experiment;
import com.evacipated.cardcrawl.mod.hubris.monsters.NecromanticTotem;
import com.evacipated.cardcrawl.mod.hubris.monsters.GrandSnecko;
import com.evacipated.cardcrawl.mod.hubris.monsters.MusketHawk;
import com.evacipated.cardcrawl.mod.hubris.relics.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
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
        StartGameSubscriber,
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

    // Crossover checks
    public static final boolean hasReplayTheSpire;
    public static final boolean hasConstructMod;
    public static final boolean hasFruityMod;
    public static final boolean hasInfiniteSpire;

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

    public static void loadData()
    {
        logger.info("Loading Save Data");
        try {
            SpireConfig config = new SpireConfig("Hubris", "SaveData");

            BottledRain.load(config);
            DisguiseKit.load(config);
            MysteriousPyramids.load(config);
            Zylophone.load(config);
            EmptyBottle.load(config);
            DuctTape.load(config);

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
            SpireConfig config = new SpireConfig("Hubris", "SaveData");

            BottledRain.save(config);
            DisguiseKit.save(config);
            MysteriousPyramids.save(config);
            Zylophone.save(config);
            EmptyBottle.save(config);
            // Duct Tape saving is handled separately

            if (otherSaveData == null) {
                otherSaveData = new SpireConfig("Hubris", "OtherSaveData");
            }
            if (hasInfiniteSpire) {
                InfiniteBlow.save();
            }
            config.save();
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

            BottledRain.clear();
            DisguiseKit.clear();
            MysteriousPyramids.clear();
            Zylophone.clear();
            EmptyBottle.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveStartGame()
    {
        loadData();
    }

    @Override
    public void receivePostInitialize()
    {
        try {
            otherSaveData = new SpireConfig("Hubris", "OtherSaveData");
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        BaseMod.addMonster(GrandSnecko.ID, GrandSnecko::new);
        BaseMod.addMonster(MusketHawk.ID, MusketHawk::new);
        BaseMod.addMonster(NecromanticTotem.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Centurion(-436.0f, -4.0f),
                new Cultist(-170.0f, 6.0f),
                new NecromanticTotem()
        }));

        BaseMod.addBoss(TheBeyond.ID, GrandSnecko.ID, assetPath("images/ui/map/boss/grandSnecko.png"), assetPath("images/ui/map/bossOutline/grandSnecko.png"));
        BaseMod.addBoss(TheCity.ID, MusketHawk.ID, assetPath("images/ui/map/boss/musketHawk.png"), assetPath("images/ui/map/bossOutline/musketHawk.png"));

        powerAtlas = new TextureAtlas(Gdx.files.internal(assetPath("images/powers/powers.atlas")));
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
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | CannotCompileException e) {
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
        BaseMod.addRelic(new Stopwatch(), RelicType.SHARED);
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
        }
    }

    @Override
    public void receiveEditKeywords()
    {
    }

    @Override
    public void receiveEditStrings()
    {
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("localization/Hubris-RelicStrings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath("localization/Hubris-CardStrings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath("localization/Hubris-IcosahedronStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class, assetPath("localization/Hubris-OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath("localization/Hubris-PowerStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath("localization/Hubris-EventStrings.json"));
        BaseMod.loadCustomStringsFile(RunModStrings.class, assetPath("localization/Hubris-RunModStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath("localization/Hubris-UIStrings.json"));
    }

    @Override
    public void receiveCustomModeMods(List<CustomMod> list)
    {
        list.add(new CustomMod(Hubris.ID, "r", true));
    }

    private static void autoAddCards() throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, CannotCompileException
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
            System.out.println(classInfo.getClassName());
            AbstractCard card = (AbstractCard) Loader.getClassPool().toClass(cls).newInstance();
            BaseMod.addCard(card);
            if (cls.hasAnnotation(CardNoSeen.class)) {
                UnlockTracker.hardUnlockOverride(card.cardID);
            } else {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }

    @Override
    public int receiveMapHPChange(int amount)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BottledHeart.ID)) {
            BottledHeart relic = (BottledHeart) AbstractDungeon.player.getRelic(BottledHeart.ID);
            return relic.onMaxHPChange(amount);
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

        if (!TerracottaHorce.allowedInPool()) {
            if (AbstractDungeon.bossRelicPool.removeIf(r -> r.equals(TerracottaHorce.ID))) {
                logger.info(TerracottaHorce.ID + " removed.");
            }
        }
    }
}
