/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUpgrade;
import com.watabou.noosa.audio.Sample;

public class ScrollOfAbility extends Scroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_UPGRADE;
		unique = true;
		talentFactor = 2f;
	}
	protected static boolean identifiedByUse = false;

	@Override
	public void doRead() {
		if (!isKnown()) {
			identify();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}
		abilityOptinons();
	}
	private void confirmCancelation() {
		GameScene.show( new WndOptions(new ItemSprite(this),
				Messages.titleCase(name()),
				Messages.get(InventoryScroll.class, "warning"),
				Messages.get(InventoryScroll.class, "yes"),
				Messages.get(InventoryScroll.class, "no") ) {
			@Override
			protected void onSelect( int index ) {
				switch (index) {
					case 0:
						curUser.spendAndNext( TIME_TO_READ );
						identifiedByUse = false;
						detach( curUser.belongings.backpack );
						break;
					case 1:
						abilityOptinons();
						break;
				}
			}
			public void onBackPressed() {}
		} );
	}
	private void abilityOptinons(){
		GameScene.show(new WndOptions(new ItemSprite(this),
				Messages.get(this, "name"),
				Messages.get(this, "please_choose"),
				Messages.get(this, "weapon"), Messages.get(this, "armor") ,
				Messages.get(this, "missile"), Messages.get(this, "mage")) {
			@Override
			protected void onSelect(int index) {
				switch (index) {
					case 0:
						curUser.weaponMastery++;
						break;
					case 1:
						curUser.armorMastery++;
						break;
					case 2:
						curUser.missileMastery++;
						break;
					case 3:
						curUser.mageMastery++;
						break;
				}

				detach( curUser.belongings.backpack );
				GLog.p(Messages.get(ScrollOfAbility.class, "strengthen"));
				curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
				Sample.INSTANCE.play(Assets.Sounds.READ);
				readAnimation();
			}

			@Override
			public void onBackPressed() {
				if(identifiedByUse && !((Scroll)curItem).anonymous){
					confirmCancelation();
				}
				super.onBackPressed();
			}
		});
	}



	@Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}

	@Override
	public int energyVal() {
		return isKnown() ? 10 * quantity : super.energyVal();
	}
}
