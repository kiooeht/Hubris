package com.evacipated.cardcrawl.mod.hubris;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.TheFatedDie;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.UpdateBodyText;
import com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler;
import com.evacipated.cardcrawl.mod.hubris.events.thecity.Experiment;
import com.evacipated.cardcrawl.mod.hubris.monsters.GrandSnecko;
import com.evacipated.cardcrawl.mod.hubris.monsters.MusketHawk;
import com.evacipated.cardcrawl.mod.hubris.relics.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
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

@SpireInitializer
public class HubrisMod implements
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        EditStringsSubscriber,
        PostDeathSubscriber,
        StartGameSubscriber
{
    public static final Logger logger = LogManager.getLogger(HubrisMod.class.getSimpleName());

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
    }

    public static String assetPath(String path)
    {
        return "hubrisAssets/" + path;
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
        BaseMod.registerModBadge(ImageMaster.loadImage(assetPath("images/hubris/modBadge.png")), "Hubris", "kiooeht", "TODO", null);

        BaseMod.addEvent(TheFatedDie.ID, TheFatedDie.class);
        BaseMod.addEvent(Experiment.ID, Experiment.class, TheCity.ID);
        // Only appears if player has Bottle relic. See TheBottlerPatch
        BaseMod.addEvent(TheBottler.ID, TheBottler.class, TheBeyond.ID);
        BaseMod.addEvent(UpdateBodyText.ID, UpdateBodyText.class);

        BaseMod.addMonster(GrandSnecko.ID, GrandSnecko::new);
        BaseMod.addMonster(MusketHawk.ID, MusketHawk::new);

        BaseMod.addBoss(TheBeyond.ID, GrandSnecko.ID, null, null);
        BaseMod.addBoss(TheCity.ID, MusketHawk.ID, null, null);
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
        } catch (URISyntaxException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
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
        BaseMod.addRelic(new Pocketwatch(), RelicType.SHARED);
        BaseMod.addRelic(new ScarierMask(), RelicType.SHARED);
        BaseMod.addRelic(new DeadTorch(), RelicType.SHARED);
        BaseMod.addRelic(new BottledHeart(), RelicType.SHARED);
        BaseMod.addRelic(new DisguiseKit(), RelicType.SHARED);
        BaseMod.addRelic(new Teleporter(), RelicType.SHARED);
        BaseMod.addRelic(new MysteriousPyramids(), RelicType.SHARED);
        BaseMod.addRelic(new AstralHammer(), RelicType.SHARED);
        BaseMod.addRelic(new PrototypeTalaria(), RelicType.SHARED);
        BaseMod.addRelic(new Spice(), RelicType.SHARED);
        BaseMod.addRelic(new TinFlute(), RelicType.SHARED);
        BaseMod.addRelic(new GlazedTorus(), RelicType.SHARED);
        BaseMod.addRelic(new Backtick(), RelicType.SHARED);
        BaseMod.addRelic(new Test447(), RelicType.SHARED);
        BaseMod.addRelic(new BundleOfHerbs(), RelicType.SHARED);
        BaseMod.addRelic(new SphereOfDissonance(), RelicType.SHARED);
        BaseMod.addRelic(new Pocketwatch2(), RelicType.SHARED);
        BaseMod.addRelic(new HerbalPaste(), RelicType.SHARED);
        BaseMod.addRelic(new MedicalManual(), RelicType.SHARED);
        BaseMod.addRelic(new HollowSoul(), RelicType.SHARED);
        BaseMod.addRelic(new CrystalStatue(), RelicType.SHARED);
        BaseMod.addRelic(new BottledRain(), RelicType.SHARED);
        BaseMod.addRelic(new Zylophone(), RelicType.SHARED);
        BaseMod.addRelic(new EmptyBottle(), RelicType.SHARED);
        BaseMod.addRelic(new DuctTape(), RelicType.SHARED);
        //BaseMod.addRelic(new BloodyCrown(), RelicType.SHARED);

        BaseMod.addRelic(new RGBLights(), RelicType.BLUE);

        if (hasConstructMod) {
            BaseMod.addRelicToCustomPool(new ClockworkCow(), constructmod.patches.AbstractCardEnum.CONSTRUCTMOD);
        }
        if (hasFruityMod) {
            BaseMod.addRelicToCustomPool(new DustyCowl(), fruitymod.patches.AbstractCardEnum.SEEKER_PURPLE);
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
    }

    private static void autoAddCards() throws URISyntaxException, ClassNotFoundException, IllegalAccessException, InstantiationException
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
            Class<?> cls = HubrisMod.class.getClassLoader().loadClass(classInfo.getClassName());
            if (cls.isAnnotationPresent(CardIgnore.class)) {
                continue;
            }
            boolean isCard = false;
            Class<?> superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.equals(AbstractCard.class)) {
                    isCard = true;
                    break;
                }
            }
            if (!isCard) {
                continue;
            }
            System.out.println(classInfo.getClassName());
            AbstractCard card = (AbstractCard) cls.newInstance();
            BaseMod.addCard(card);
            if (cls.isAnnotationPresent(CardNoSeen.class)) {
                UnlockTracker.hardUnlockOverride(card.cardID);
            } else {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }
}
