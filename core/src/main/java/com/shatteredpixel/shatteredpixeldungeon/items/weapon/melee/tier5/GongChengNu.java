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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier5;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier0.Bow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class GongChengNu extends Bow{
	
	{
		image = ItemSpriteSheet.GONG_CHENG_NU;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1f;
		tier = 5;
	}

	@Override
	public int max(int lvl) {
		return  25 + 5*lvl;
	}

	public String statsInfo(){
		if (isIdentified()){
			return Messages.get(this, "stats_desc",  5 +buffedLvl(),16 +4*buffedLvl() );
		} else {
			return Messages.get(this, "typical_stats_desc", 5,16);
		}
	}
	@Override
	public int dartMin() {
		if (Dungeon.hero.buff(ChargedShot.class) != null){
			return 10 +2*buffedLvl() ;
		}else {
			return 5 +buffedLvl() ;
		}
	}
	@Override
	public int dartMax() {
		if (Dungeon.hero.buff(ChargedShot.class) != null){
			return  21 +5*buffedLvl();
		}else {
			return 16 +4*buffedLvl() ;
		}
	}

	@Override
	public String abilityInfo() {
		Hero hero= Dungeon.hero;
		if (levelKnown){
			return Messages.get(this, "ability_desc", 5+buffedLvl()+hero.weaponMastery);
		} else {
			return Messages.get(this, "typical_ability_desc", 5);
		}
	}
}
