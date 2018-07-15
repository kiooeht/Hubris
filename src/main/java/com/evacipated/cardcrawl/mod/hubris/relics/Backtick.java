package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.DevConsole;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.vfx.ObtainRelicLater;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class Backtick extends AbstractRelic
{
    public static final String ID = "hubris:Backtick";
    private boolean relicSelected = true;
    private boolean cardSelected = true;

    public Backtick()
    {
        super(ID, "backtick.png", RelicTier.BOSS, LandingSound.CLINK);

        RelicStrings newRelicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
        flavorText = Input.Keys.toString(DevConsole.toggleKey) + newRelicStrings.FLAVOR + Test447.ID;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        openRelicSelect();

        /*
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen(0.5f);

        CardCrawlGame.mainMenuScreen.cardLibraryScreen.open();
        //*/
    }

    private void openRelicSelect()
    {
        relicSelected = false;

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractRelic r : RelicLibrary.starterList) {
            group.addToTop(new RelicCard(r));
        }
        for (AbstractRelic r : RelicLibrary.commonList) {
            group.addToTop(new RelicCard(r));
        }
        for (AbstractRelic r : RelicLibrary.uncommonList) {
            group.addToTop(new RelicCard(r));
        }
        for (AbstractRelic r : RelicLibrary.rareList) {
            group.addToTop(new RelicCard(r));
        }
        for (AbstractRelic r : RelicLibrary.bossList) {
            group.addToTop(new RelicCard(r));
        }
        for (AbstractRelic r : RelicLibrary.specialList) {
            group.addToTop(new RelicCard(r));
        }
        for (AbstractRelic r : RelicLibrary.shopList) {
            group.addToTop(new RelicCard(r));
        }
        group.group.removeIf(c -> AbstractDungeon.player.hasRelic(c.cardID));

        AbstractDungeon.gridSelectScreen.open(group,
                1, "",
                false, false, false, false);
    }

    private void openCardSelect()
    {
        cardSelected = false;

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.group = CardLibrary.getAllCards();
        group.sortAlphabetically(true);
        group.group.removeIf(c -> !c.isSeen);

        AbstractDungeon.gridSelectScreen.open(group,
                1, "",
                false, false, false, false);
    }

    @Override
    public void update()
    {
        super.update();

        if (!relicSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            relicSelected = true;

            AbstractCard relicCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractRelic relic = RelicLibrary.getRelic(relicCard.cardID).makeCopy();
            AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(relic));

            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            openCardSelect();
        } else if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;

            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            //CardCrawlGame.mainMenuScreen.cardLibraryScreen.update();
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb)
    {
        super.render(sb);

        if (!cardSelected) {
            //CardCrawlGame.mainMenuScreen.cardLibraryScreen.render(sb);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Backtick();
    }

    static class RelicCard extends CustomCard
    {
        RelicCard(AbstractRelic relic)
        {
            super(relic.relicId, relic.name, null, -2, relic.description, CardType.SKILL, getColor(relic), CardRarity.SPECIAL, CardTarget.NONE);
        }

        private static CardColor getColor(AbstractRelic relic)
        {
            if (RelicLibrary.redList.contains(relic)) {
                return CardColor.RED;
            } else if (RelicLibrary.greenList.contains(relic)) {
                return CardColor.GREEN;
            } else if (RelicLibrary.blueList.contains(relic)) {
                return CardColor.BLUE;
            }
            return CardColor.COLORLESS;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
        {

        }

        @Override
        public boolean canUpgrade()
        {
            return false;
        }

        @Override
        public void upgrade()
        {

        }

        @Override
        public AbstractCard makeCopy()
        {
            return null;
        }
    }
}
