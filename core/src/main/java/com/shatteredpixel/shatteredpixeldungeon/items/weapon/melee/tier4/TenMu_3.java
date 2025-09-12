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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier4;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class TenMu_3 extends MeleeWeapon {

	{
		image = ItemSpriteSheet.TEN_MU_3;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.1f;

		tier = 4;
		RCH = 3;    //lots of extra reach
	}
	public  int[] counts=new int[27];
	private static final String COUNTS="counts";
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNTS, counts);
	}
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		counts = bundle.getIntArray( COUNTS );

	}
	@Override
	public int max(int lvl) {
		return 	20+4*lvl;
	}
	@Override
	public int min(int lvl){
		return Math.min(maxAttrib(),1+counts[Dungeon.depth]);
	}
	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Whip.lashAbility(hero,target,1,0,this);
	}

	@Override
	public String abilityInfo() {
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(minDamage(Dungeon.hero)), augment.damageFactor(maxDamage(Dungeon.hero)));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0), max(0));
		}
	}

	public String upgradeAbilityStat(int level){
		return augment.damageFactor(min(level)) + "-" + augment.damageFactor(max(level));
	}
}
