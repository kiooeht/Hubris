package com.evacipated.cardcrawl.mod.hubris;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.hubris.relics.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
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
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        EditStringsSubscriber
{
    public static void initialize()
    {
        BaseMod.subscribe(new HubrisMod());
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
    }

    @Override
    public void receiveEditKeywords()
    {
        BaseMod.addKeyword(new String[] {"purge"}, "Disappears upon use.");
        BaseMod.addKeyword(new String[] {"retain"}, "Not discarded at the end of your turn.");
        BaseMod.addKeyword(new String[] {"temporary"}, "Temporary HP disappears at the end of combat.");
        BaseMod.addKeyword(new String[] {"autoplay"}, "This card automatically plays itself when drawn.");
        BaseMod.addKeyword(new String[] {"inescapable"}, "Cannot be removed from your deck.");
        BaseMod.addKeyword(new String[] {"greed"}, "Greed is a curse that gets you more gold.");
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
