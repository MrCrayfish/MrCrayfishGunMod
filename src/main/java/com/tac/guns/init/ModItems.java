package com.tac.guns.init;

import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.common.GunModifiers;
import com.tac.guns.item.*;
import com.tac.guns.item.TransitionalTypes.TimelessAmmoItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.TransitionalTypes.TimelessOldRifleGunItem;
import com.tac.guns.item.TransitionalTypes.TimelessPistolGunItem;
import com.tac.guns.item.TransitionalTypes.grenades.BaseballGrenadeItem;
import com.tac.guns.item.TransitionalTypes.grenades.LightGrenadeItem;
import com.tac.guns.item.attachment.impl.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<GunItem> M1911 = REGISTER.register( "m1911", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    //public static final RegistryObject<TimelessGunItem> M1894 = REGISTER.register("m1894", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> M1928 = REGISTER.register("m1928", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> MOSIN = REGISTER.register("mosin", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<GunItem> AK47 = REGISTER.register("ak47", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<GunItem> M60 = REGISTER.register("m60", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    public static final RegistryObject<Item> GLOCK_17 = REGISTER.register("glock_17", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> DP_28 = REGISTER.register("dp28", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    //public static final RegistryObject<Item> M16A1 = REGISTER.register("m16a1", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<Item> STI2011 = REGISTER.register("sti2011", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> M92FS = REGISTER.register("m92fs", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<GunItem> AR15_HELLMOUTH = REGISTER.register("ar_15_hellmouth", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<GunItem> VECTOR45 = REGISTER.register("vector45", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> MICRO_UZI = REGISTER.register("micro_uzi", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> M4 = REGISTER.register("m4", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    //public static final RegistryObject<TimelessGunItem> M1911_NETHER = REGISTER.register("m1911_nether", () -> new TimelessGunItem(Item.Properties::isImmuneToFire) {
    //    public int getItemEnchantability() {
    //        return 12;
    //    }
    //});
    public static final RegistryObject<GunItem> MOSBERG590 = REGISTER.register("mosberg590", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> DB_SHORT = REGISTER.register("db_short", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> DB_LONG = REGISTER.register("db_long", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> WALTHER_PPK = REGISTER.register("walther_ppk", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<GunItem> M24 = REGISTER.register("m24", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> PPSH_41 = REGISTER.register("ppsh_41", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> QBZ_95 = REGISTER.register("qbz_95", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<Item> SPRINGFIELD_1903 = REGISTER.register("springfield_1903", () -> new TimelessOldRifleGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> AA_12 = REGISTER.register("aa_12", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> X95R = REGISTER.register("x95r", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<Item> FR_F2 = REGISTER.register("fr_f2", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> SMLE_III = REGISTER.register("smle_iii", () -> new TimelessOldRifleGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> M870_CLASSIC = REGISTER.register("m870_classic", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));//properties.group(GunMod.SHOTGUN)));
    //public static final RegistryObject<Item> MG3 = REGISTER.register("mg3", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    public static final RegistryObject<Item> MG42 = REGISTER.register("mg42", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    public static final RegistryObject<Item> AR_10 = REGISTER.register("ar_10", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<Item> M1A1_SMG = REGISTER.register("m1a1_smg", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> MK14 = REGISTER.register("mk14", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<Item> SPAS_12 = REGISTER.register("spas_12", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> DEAGLE_357 = REGISTER.register("deagle_357", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> HK_MP5A5 = REGISTER.register("hk_mp5a5", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> STEN_MK_II = REGISTER.register("sten_mk_ii", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> GLOCK_18 = REGISTER.register("glock_18", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> M1873 = REGISTER.register("m1873", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> CZ75 = REGISTER.register("cz75", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> CZ75_AUTO = REGISTER.register("cz75_auto", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> VZ61 = REGISTER.register("vz61", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> QSZ92G1 = REGISTER.register("qsz92g1", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> KAR98 = REGISTER.register("kar98", () -> new TimelessOldRifleGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> HK416_A5 = REGISTER.register("hk416_a5", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<Item> TYPE81_X = REGISTER.register("type81_x", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    public static final RegistryObject<Item> PKP_PENCHENBERG = REGISTER.register("pkp_penchenberg", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    public static final RegistryObject<Item> MP7 = REGISTER.register("mp7", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> M82A2 = REGISTER.register("m82a2", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> AI_AWP = REGISTER.register("ai_awp", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<GunItem> RPG7 = REGISTER.register("rpg7", () -> new TimelessGunItem(properties -> properties.group(GunMod.EXPLOSIVES)));
    //public static final RegistryObject<Item> TEC_9 = REGISTER.register("tec_9", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    //public static final RegistryObject<Item> RPK = REGISTER.register("rpk", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    public static final RegistryObject<Item> FN_FAL = REGISTER.register("fn_fal", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));

    /* Ammunition */
    public static final RegistryObject<Item> MAGNUM_BULLET = REGISTER.register("magnumround", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_45 = REGISTER.register("round45", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_30_WIN = REGISTER.register("win_30-30", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_308 = REGISTER.register("bullet_308", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_556 = REGISTER.register("nato_556_bullet", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_9 = REGISTER.register("9mm_round", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_10g = REGISTER.register("10_gauge_round", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_58x42 = REGISTER.register("58x42", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_762x25 = REGISTER.register("762x25", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_762x54 = REGISTER.register("762x54", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_762x39 = REGISTER.register("762x39", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_50_BMG = REGISTER.register("50bmg", TimelessAmmoItem::new);

    //public static final RegistryObject<Item> BULLET_762x39 = REGISTER.register("762x39", TimelessAmmoItem::new);

    /* Explosives */
    public static final RegistryObject<Item> RPG7_MISSILE = REGISTER.register("rpg7_missile", () -> new AmmoItem(new Item.Properties().group(GunMod.AMMO)));

    public static final RegistryObject<Item> LIGHT_GRENADE = REGISTER.register("light_grenade", () ->  new LightGrenadeItem(new Item.Properties().group(GunMod.EXPLOSIVES), 25 * 4, 0.95f, 1.35f));
    public static final RegistryObject<Item> BASEBALL_GRENADE = REGISTER.register("baseball_grenade", () ->  new BaseballGrenadeItem(new Item.Properties().group(GunMod.EXPLOSIVES), 20 * 7, 1.425f, 1.135f));

    /* Scope Attachments */
    public static final RegistryObject<Item> COYOTE_SIGHT = REGISTER.register("coyote_sight", () -> new ScopeItem(Scope.create(0.00F, 2.275F, 0.325, GunModifiers.COYOTE_SIGHT_ADS).viewFinderOffset(0.355).viewFinderOffsetSpecial(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> AIMPOINT_T1_SIGHT = REGISTER.register("aimpoint_t1", () -> new ScopeItem(Scope.create(0.0F, 1.875F,0.325, GunModifiers.AIMPOINT_T1_SIGHT_ADS).viewFinderOffset(0.355).viewFinderOffsetSpecial(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> EOTECH_N_SIGHT = REGISTER.register("eotech_n", () -> new ScopeItem(Scope.create(0.00F, 1.775F,0.325, GunModifiers.EOTECH_N_SIGHT_ADS).viewFinderOffset(0.385).viewFinderOffsetSpecial(0.385), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> VORTEX_UH_1 = REGISTER.register("vortex_uh_1", () -> new ScopeItem(Scope.create(0.00F, 2.3F,0.325, GunModifiers.VORTEX_UH_1_ADS).viewFinderOffset(0.3725).viewFinderOffsetSpecial(0.3725), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> EOTECH_SHORT_SIGHT = REGISTER.register("eotech_short", () -> new ScopeItem(Scope.create(0.00F, 2.2F,0.325, GunModifiers.EOTECH_SHORT_SIGHT_ADS).viewFinderOffset(0.420).viewFinderOffsetSpecial(0.420), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SRS_RED_DOT_SIGHT = REGISTER.register("srs_red_dot", () -> new ScopeItem(Scope.create(0.00F, 1.9325F, 0.325, GunModifiers.SRS_RED_DOT_SIGHT_ADS).viewFinderOffset(0.355).viewFinderOffsetSpecial(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    public static final RegistryObject<Item> ACOG_4 = REGISTER.register("acog_4x_scope", () -> new ScopeItem(Scope.create(0.2475F, 2.275F,0.21, GunModifiers.ACOG_4_ADS).viewFinderOffset(0.475).viewFinderOffsetDR(0.41).viewFinderOffsetSpecial(0.4).viewFinderOffsetSpecialDR(0.328), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> QMK152 = REGISTER.register("qmk152", () -> new ScopeItem(Scope.create(0.26F, 2.69F,0.19, GunModifiers.QMK152_ADS).viewFinderOffset(0.45).viewFinderOffsetDR(0.315).viewFinderOffsetSpecial(0.34).viewFinderOffsetSpecialDR(0.238), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    public static final RegistryObject<Item> VORTEX_LPVO_1_6 = REGISTER.register("lpvo_1_6", () -> new ScopeItem(Scope.create(0.3175F, 1.925F,0.1725, GunModifiers.VORTEX_LPVO_1_6_ADS).viewFinderOffset(0.475).viewFinderOffsetDR(0.325).viewFinderOffsetSpecial(0.445).viewFinderOffsetSpecialDR(0.301), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));//.viewFinderOffset(0.475), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    public static final RegistryObject<Item> LONGRANGE_8x_SCOPE = REGISTER.register("8x_scope", () -> new ScopeItem(Scope.create(0.435F, 1.930F,0.1725, GunModifiers.LONGRANGE_8x_SCOPE_ADS).viewFinderOffset(0.595).viewFinderOffsetDR(0.3925).viewFinderOffsetSpecial(0.465).viewFinderOffsetSpecialDR(0.304), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Old Scopes */
    public static final RegistryObject<Item> OLD_LONGRANGE_8x_SCOPE = REGISTER.register("old_8x_scope", () -> new OldScopeItem(Scope.create(0.375F, 1.930F,0.14, GunModifiers.OLD_LONGRANGE_8x_SCOPE_ADS).viewFinderOffset(0.535).viewFinderOffsetDR(0.3745).viewFinderOffsetSpecial(0.5).viewFinderOffsetSpecialDR(0.35), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> OLD_LONGRANGE_4x_SCOPE = REGISTER.register("old_4x_scope", () -> new OldScopeItem(Scope.create(0.195F, 1.930F,0.21, GunModifiers.OLD_LONGRANGE_4x_SCOPE_ADS).viewFinderOffset(0.5).viewFinderOffsetDR(0.35).viewFinderOffsetSpecial(0.4).viewFinderOffsetSpecialDR(0.28), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Pistol-Scopes */
    public static final RegistryObject<Item> MINI_DOT = REGISTER.register("mini_dot", () -> new PistolScopeItem(Scope.create(0.00F, 1.475F,0.325, GunModifiers.MINI_DOT_ADS).viewFinderOffset(0.685).viewFinderOffsetSpecial(0.685), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MICRO_HOLO_SIGHT = REGISTER.register("micro_holo_sight", () -> new PistolScopeItem(Scope.create(0.00F, 1.645F,0.325, GunModifiers.MICRO_HOLO_SIGHT_ADS).viewFinderOffset(0.685).viewFinderOffsetSpecial(0.685), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Barrel Attachments */
    public static final RegistryObject<Item> SILENCER = REGISTER.register("silencer", () -> new BarrelItem(Barrel.create(8.0F, GunModifiers.TACTICAL_SILENCER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MUZZLE_BRAKE = REGISTER.register("muzzle_brake", () -> new BarrelItem(Barrel.create(2.0F, GunModifiers.MUZZLE_BRAKE_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MUZZLE_COMPENSATOR = REGISTER.register("muzzle_compensator", () -> new BarrelItem(Barrel.create(2.0F, GunModifiers.MUZZLE_COMPENSATOR_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Pistol-Barrel Attachments */
    public static final RegistryObject<Item> PISTOL_SILENCER = REGISTER.register("pistol_silencer", () -> new PistolBarrelItem(Barrel.create(8.0F, GunModifiers.PISTOL_SILENCER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Stock Attachments */
    public static final RegistryObject<Item> LIGHT_STOCK = REGISTER.register("light_stock", () -> new StockItem(Stock.create(GunModifiers.LIGHT_STOCK_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP), false));
    public static final RegistryObject<Item> TACTICAL_STOCK = REGISTER.register("tactical_stock", () -> new StockItem(Stock.create(GunModifiers.TACTICAL_STOCK_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP), false));
    public static final RegistryObject<Item> WEIGHTED_STOCK = REGISTER.register("weighted_stock", () -> new StockItem(Stock.create(GunModifiers.HEAVY_STOCK_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Under Barrel Attachments */
    public static final RegistryObject<Item> LIGHT_GRIP = REGISTER.register("light_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.TACTICAL_GRIP_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SPECIALISED_GRIP = REGISTER.register("specialised_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.HEAVY_GRIP_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    /* Side rail Attachments */
    //public static final RegistryObject<Item> STANDARD_FLASHLIGHT = REGISTER.register("standard_flashlight", () -> new SideRailItem(SideRail.create(), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

   //public static final RegistryObject<Item> LIGHT_GRENADE = REGISTER.register("light_grenade", () ->  new GrenadeItem(new Item.Properties().group(GunMod.GROUP), 20 * 4, 1.1f));
}
