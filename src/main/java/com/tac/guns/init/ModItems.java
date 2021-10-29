package com.tac.guns.init;

import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.common.GunModifiers;
import com.tac.guns.item.*;
import com.tac.guns.item.TransitionalTypes.AutomaticTimelessGunItem;
import com.tac.guns.item.TransitionalTypes.TimelessAmmoItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.impl.Barrel;
import com.tac.guns.item.attachment.impl.Scope;
import com.tac.guns.item.attachment.impl.Stock;
import com.tac.guns.item.attachment.impl.UnderBarrel;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> M1911 = REGISTER.register("m1911", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    //public static final RegistryObject<TimelessGunItem> M1894 = REGISTER.register("m1894", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    //public static final RegistryObject<TimelessGunItem> M1851 = REGISTER.register("m1851", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    //public static final RegistryObject<TimelessGunItem> M1928 = REGISTER.register("m1928", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.SMG), Config.SERVER.gunHandlingCustomization.M1928_trigMax));
    public static final RegistryObject<Item> MOSIN = REGISTER.register("mosin", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<GunItem> AK47 = REGISTER.register("ak47", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.RIFLE), Config.SERVER.gunHandlingCustomization.AK47_trigMax));
    public static final RegistryObject<GunItem> M60 = REGISTER.register("m60", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), Config.SERVER.gunHandlingCustomization.M60_trigMax));
    //public static final RegistryObject<TimelessGunItem> M1917 = REGISTER.register("m1917", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> GLOCK_17 = REGISTER.register("glock_17", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> DP_28 = REGISTER.register("dp28", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), Config.SERVER.gunHandlingCustomization.DP28_trigMax));
    public static final RegistryObject<Item> M16A1 = REGISTER.register("m16a1", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.RIFLE), Config.SERVER.gunHandlingCustomization.M16A1_trigMax));
    //public static final RegistryObject<TimelessGunItem> MK18 = REGISTER.register("mk18", () -> new TimelessGunItem(properties -> properties.group(GunMod.GROUP)));
    public static final RegistryObject<Item> STI2011 = REGISTER.register("sti2011", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    //public static final RegistryObject<TimelessGunItem> AK74 = REGISTER.register("ak74", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.GROUP), Config.SERVER.gunHandlingCustomization.AK74_trigMax));
    public static final RegistryObject<Item> M92FS = REGISTER.register("m92fs", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<GunItem> AR15_HELLMOUTH = REGISTER.register("ar_15_hellmouth", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.RIFLE), Config.SERVER.gunHandlingCustomization.AR15HM_trigMax));
    //public static final RegistryObject<TimelessGunItem> AR15_P = REGISTER.register("ar_15_p", () -> new ar15_p_item(properties -> properties.group(GunMod.GROUP)));
    public static final RegistryObject<GunItem> VECTOR45 = REGISTER.register("vector45", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.SMG), Config.SERVER.gunHandlingCustomization.VECTOR45_trigMax));
    public static final RegistryObject<Item> MICRO_UZI = REGISTER.register("micro_uzi", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> M4 = REGISTER.register("m4", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.RIFLE), Config.SERVER.gunHandlingCustomization.AR15P_trigMax));
    //public static final RegistryObject<TimelessGunItem> M1911_NETHER = REGISTER.register("m1911_nether", () -> new TimelessGunItem(Item.Properties::isImmuneToFire) {
    //    public int getItemEnchantability() {
    //        return 12;
    //    }
    //});
    public static final RegistryObject<GunItem> MOSBERG590 = REGISTER.register("mosberg590", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> DB_SHORT = REGISTER.register("db_short", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> DB_LONG = REGISTER.register("db_long", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> WALTHER_PPK = REGISTER.register("walther_ppk", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<GunItem> M24 = REGISTER.register("m24", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> PPSH_41 = REGISTER.register("ppsh_41", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.SMG), Config.SERVER.gunHandlingCustomization.AR15P_trigMax));
    public static final RegistryObject<Item> QBZ_95 = REGISTER.register("qbz_95", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.RIFLE), Config.SERVER.gunHandlingCustomization.AR15P_trigMax));
    public static final RegistryObject<Item> SPRINGFIELD_1903 = REGISTER.register("springfield_1903", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    //public static final RegistryObject<TimelessGunItem> DEAGLE_50 = REGISTER.register("deagle_50", () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> AA_12 = REGISTER.register("aa_12", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.SHOTGUN), Config.SERVER.gunHandlingCustomization.AR15P_trigMax));
    public static final RegistryObject<Item> X95R = REGISTER.register("x95r", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.RIFLE), Config.SERVER.gunHandlingCustomization.AR15P_trigMax));
    public static final RegistryObject<Item> FR_F2 = REGISTER.register("fr_f2", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> SMLE_III = REGISTER.register("smle_iii", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> M870_CLASSIC = REGISTER.register("m870_classic", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> MG3 = REGISTER.register("mg3", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), Config.SERVER.gunHandlingCustomization.M60_trigMax));
    public static final RegistryObject<Item> MG42 = REGISTER.register("mg42", () -> new AutomaticTimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), Config.SERVER.gunHandlingCustomization.M60_trigMax));
    public static final RegistryObject<Item> AR_10 = REGISTER.register("ar_10", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));

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

    /* Scope Attachments */
    public static final RegistryObject<Item> COYOTE_SIGHT = REGISTER.register("coyote_sight", () -> new ScopeItem(Scope.create(0.00F, 2.275F, 0.70, GunModifiers.SLOW_ADS).viewFinderOffset(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MICRO_HOLO_SIGHT = REGISTER.register("micro_holo_sight", () -> new ScopeItem(Scope.create(0.00F, 1.645F,0.7, GunModifiers.SLOW_ADS).viewFinderOffset(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> AIMPOINT_T1_SIGHT = REGISTER.register("aimpoint_t1", () -> new ScopeItem(Scope.create(0.0F, 1.825F,0.7, GunModifiers.SLOW_ADS).viewFinderOffset(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    public static final RegistryObject<Item> VORTEX_LPVO_1_6 = REGISTER.register("lpvo_1_6", () -> new ScopeItem(Scope.create(0.2875F, 1.825F,0.32, GunModifiers.SLOW_ADS).viewFinderOffset(0.475), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    public static final RegistryObject<Item> ACOG_4 = REGISTER.register("acog_4x_scope", () -> new ScopeItem(Scope.create(0.1925F, 2.275F,0.26, GunModifiers.SLOW_ADS).viewFinderOffset(0.475), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    //public static final RegistryObject<ScopeItem> LONGRANGE_8x_SCOPE = REGISTER.register("8x_scope", () -> new ScopeItem(Scope.create(0.4F, 1.530F,0.75, GunModifiers.SLOW_ADS).viewFinderOffset(0.45), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    //public static final RegistryObject<Item> LONGRANGE_8x_SCOPE = REGISTER.register("8x_scope", () -> new ScopeItem(Scope.create(0.415F, 1.930F,0.225, GunModifiers.SLOW_ADS).viewFinderOffset(0.5), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Barrel Attachments */
    public static final RegistryObject<Item> SILENCER = REGISTER.register("silencer", () -> new BarrelItem(Barrel.create(8.0F, GunModifiers.TACTICAL_SILENCER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MUZZLE_BRAKE = REGISTER.register("muzzle_brake", () -> new BarrelItem(Barrel.create(2.0F, GunModifiers.MUZZLE_BRAKE_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MUZZLE_COMPENSATOR = REGISTER.register("muzzle_compensator", () -> new BarrelItem(Barrel.create(2.0F, GunModifiers.MUZZLE_COMPENSATOR_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Stock Attachments */
    public static final RegistryObject<Item> LIGHT_STOCK = REGISTER.register("light_stock", () -> new StockItem(Stock.create(GunModifiers.LIGHT_STOCK_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP), false));
    public static final RegistryObject<Item> TACTICAL_STOCK = REGISTER.register("tactical_stock", () -> new StockItem(Stock.create(GunModifiers.TACTICAL_STOCK_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP), false));
    public static final RegistryObject<Item> WEIGHTED_STOCK = REGISTER.register("weighted_stock", () -> new StockItem(Stock.create(GunModifiers.HEAVY_STOCK_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Under Barrel Attachments */
    public static final RegistryObject<Item> LIGHT_GRIP = REGISTER.register("light_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.TACTICAL_GRIP_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SPECIALISED_GRIP = REGISTER.register("specialised_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.HEAVY_GRIP_MODIFIER), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
}
