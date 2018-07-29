package com.evacipated.cardcrawl.mod.hubris;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.hubris.events.shrines.TheFatedDie;
import com.evacipated.cardcrawl.mod.hubris.relics.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

@SpireInitializer
public class HubrisMod implements
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        EditStringsSubscriber,
        PostDeathSubscriber
{
    public static final Logger logger = LogManager.getLogger(HubrisMod.class.getSimpleName());

    // Crossover checks
    public static final boolean hasReplayTheSpire;
    public static final boolean hasConstructMod;

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
    }

    public static void initialize()
    {
        BaseMod.subscribe(new HubrisMod());
    }

    @Override
    public void receivePostInitialize()
    {
        BaseMod.addEvent(TheFatedDie.ID, TheFatedDie.class, BaseMod.EventPool.ANY);
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

        if (hasConstructMod) {
            BaseMod.addRelicToCustomPool(new ClockworkCow(), constructmod.patches.AbstractCardEnum.CONSTRUCTMOD.toString());
        }
    }

    @Override
    public void receiveEditKeywords()
    {
        BaseMod.addKeyword(new String[] {"temporary"}, "Temporary HP disappears at the end of combat.");
        BaseMod.addKeyword(new String[] {"greed"}, "Greed is a curse that gets you more gold.");
        BaseMod.addKeyword(new String[] {"dissonance"}, "Dissonant creatures take #b50% more damage from Attacks.");
    }

    @Override
    public void receiveEditStrings()
    {
        BaseMod.loadCustomStrings(RelicStrings.class,
                Gdx.files.internal("localization/Hubris-RelicStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)));
        BaseMod.loadCustomStrings(CardStrings.class,
                Gdx.files.internal("localization/Hubris-CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)));
        BaseMod.loadCustomStrings(CardStrings.class,
                Gdx.files.internal("localization/Hubris-IcosahedronStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)));
        BaseMod.loadCustomStrings(OrbStrings.class,
                Gdx.files.internal("localization/Hubris-OrbStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)));
        BaseMod.loadCustomStrings(PowerStrings.class,
                Gdx.files.internal("localization/Hubris-PowerStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)));
        BaseMod.loadCustomStrings(EventStrings.class,
                Gdx.files.internal("localization/Hubris-EventStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)));
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
            if (!cls.isAnnotationPresent(CardNoUnlock.class)) {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }
}
