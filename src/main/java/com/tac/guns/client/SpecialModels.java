package com.tac.guns.client;

import com.tac.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum SpecialModels
{
    FLAME("flame"),
    MINI_GUN_BASE("mini_gun_base"),
    MINI_GUN_BARRELS("mini_gun_barrels"),
    GRENADE_LAUNCHER_BASE("grenade_launcher_base"),
    GRENADE_LAUNCHER_CYLINDER("grenade_launcher_cylinder"),
    M1911("m1911"),
    M1911_SLIDE("m1911_slide"),
    M1911_STANDARD_MAG("m1911_standard_mag"),
    M1911_LONG_MAG("m1911_long_mag"),
    M1851("m1851"),
    M1851_CYLINDER("m1851_cylinder"),
    M1851_HAMMER("m1851_hammer"),
    M1928("m1928"),
    M1928_BOLT("m1928_bolt"),
    M1928_STICK_MAG("m1928_stick_mag"),
    M1928_DRUM_MAG("m1928_drum_mag"),
    MOSIN("mosin"),
    MOSIN_BOLT("mosin_bolt"),
    AK47("ak47"),
    AK47_BOLT("ak47_bolt"),
    M60("m60"),
    M60_sMAG("m60_standard_mag"),
    M60_eMAG("m60_extended_mag"),
    AK47_OPTIC_MOUNT("ak47_mount"),
    M1917("m1917"),
    M1917_CYLINDER("m1917_cylinder"),
    GLOCK_17("glock_17"),
    GLOCK_17_SLIDE("glock_17_slide"),
    GLOCK_17_STANDARD_MAG("glock_17_standard_mag"),
    GLOCK_17_EXTENDED_MAG("glock_17_extended_mag"),
    GLOCK_17_SUPPRESSOR_OVERIDE("glock_17_suppressor"),
    DP_28("dp28"),
    DP_28_MAG("dp28_mag"),
    DP_28_BOLT("dp28_bolt"),
    M16_A1("m16a1"),
    M16_A1_HANDLE("m16a1_handle"),
    M16_A1_STANDARD_MAG("m16a1_standard_mag"),
    M16_A1_EXTENDED_MAG("m16a1_extended_mag"),
    M16_A1_FRONT_SIGHT("m16a1_front_sight"),
    MK18_BODY("mk18_body"),
    MK18_BOLT("mk18_bolt"),
    MK18_SIGHTS("mk18_sights"),
    MK18_STOCK("mk18_stock"),
    MK18_STANDARD_MAG("mk18_standard_mag"),
    STI2011_BODY("sti2011_body"),
    STI2011_SLIDE("sti2011_slide"),
    STI2011_STANDARD_MAG("sti2011_standard_mag"),
    STI2011_RAIL("sti2011_rail"),
    AK74("ak74"),
    M92FS("m92fs"),
    M92FS_SLIDE("m92fs_slide"),
    M92FS_STANDARD_MAG("m92fs_standard_mag"),
    M92FS_EXTENDED_MAG("m92fs_extended_mag"),
    AR15_HELLMOUTH_BODY("ar_15_hellmouth_body"),
    AR15_HELLMOUTH_BUTT_HEAVY("ar_15_hellmouth_butt_heavy"),
    AR15_HELLMOUTH_BUTT_LIGHTWEIGHT("ar_15_hellmouth_butt_lightweight"),
    AR15_HELLMOUTH_SUPPRESSOR("ar_15_hellmouth_suppressor"),
    AR15_HELLMOUTH_MUZZLE("ar_15_hellmouth_muzzle"),
    AR15_HELLMOUTH_BUTT_TACTICAL("ar_15_hellmouth_butt_tactical"),
    AR15_HELLMOUTH_TACTICAL_GRIP("ar_15_hellmouth_tactical_grip"),
    AR15_HELLMOUTH_LIGHTWEIGHT_GRIP("ar_15_hellmouth_lightweight_grip"),
    AR15_P_BODY("ar_15_p_body"),
    AR15_P_IRONS("ar_15_p_irons"),
    AR15_P_MUZZLE("ar_15_p_muzzle"),
    AR15_P_SUPPRESSOR("ar_15_p_suppressor"),
    AR15_P_TACTICAL_GRIP("ar_15_p_tactical_grip"),
    AR15_BOLT("ar_15_bolt"),
    VECTOR45_BODY("vector45_body"),
    VECTOR45_BOLT("vector45_bolt"),
    VECTOR45_EXTENDED_MAG("vector45_extended_mag"),
    VECTOR45_GRIP("vector45_grip"),
    VECTOR45_HEAVY_STOCK("vector45_heavy_stock"),
    VECTOR45_LIGHT_STOCK("vector45_light_stock"),
    VECTOR45_SIGHT("vector45_sight"),
    VECTOR45_SILENCER("vector45_silencer"),
    VECTOR45_STANDARD_MAG("vector45_standard_mag"),
    VECTOR45_TACTICAL_STOCK("vector45_tactical_stock"),
    MICRO_UZI_BODY("micro_uzi_body"),
    MICRO_UZI_BOLT("micro_uzi_bolt"),
    MICRO_UZI_SIGHT("micro_uzi_sight"),
    MICRO_UZI_SILENCER("micro_uzi_silencer"),
    M1911_NETHER("m1911_nether"),
    M1911_SLIDE_NETHER("m1911_slide_nether"),
    M1911_STANDARD_MAG_NETHER("m1911_standard_mag_nether"),
    M1911_LONG_MAG_NETHER("m1911_long_mag_nether"),
    MOSBERG590_BODY("mosberg590_body"),
    MOSBERG590_BULLETS("mosberg590_bullets"),
    MOSBERG590_GRIP("mosberg590_grip"),
    MOSBERG590_SIGHTS("mosberg590_sights"),
    MOSBERG590_SLIDE("mosberg590_slide"),
    WALTHER_PPK_BODY("walther_ppk_body"),
    WALTHER_PPK_EXTENDED_MAG("walther_ppk_extended_mag"),
    WALTHER_PPK_SLIDE("walther_ppk_slide"),
    WALTHER_PPK_STANDARD_MAG("walther_ppk_standard_mag"),
    M4_BODY("m4_body"),
    M4_BOLT("m4_bolt"),
    M4_BOLT_HANDLE("m4_bolt_handle"),
    M4_SIGHT("m4_sight"),
    M4_TACTICAL_STOCK("m4_tactical_stock"),
    M24_BODY("m24_body"),
    M24_BOLT("m24_bolt"),
    M24_RAIL("m24_rail"),
    COYOTE_SIGHT("coyote_sight"),
    MIDRANGE_DOT_SCOPE("midrange_dot_scope"),
    PPSH_41("ppsh_41"),
    PPSH_41_BOLT("ppsh_41_bolt"),
    PPSH_41_STANDARD_MAG("ppsh_41_standard_mag"),
    PPSH_41_EXTENDED_MAG("ppsh_41_extended_mag"),
    QBZ_95_BODY("qbz_95"),
    QBZ_95_BOLT("qbz_95_bolt"),
    QBZ_95_SUPPRESSOR("qbz_95_suppressor"),
    QBZ_95_MUZZLE("qbz_95_muzzle_device"),
    SPRINGFIELD_1903("springfield_1903"),
    SPRINGFIELD_1903_BOLT("springfield_1903_bolt"),
    DEAGLE_50("deagle_50_body"),
    DEAGLE_50_EXTENDED_MAG("deagle_50_extended_mag"),
    DEAGLE_50_SLIDE("deagle_50_slide"),
    DEAGLE_50_STANDARD_MAG("deagle_50_standard_mag"),
    AA_12_BODY("aa_12_body"),
    AA_12_BOLT("aa_12_bolt"),
    AA_12_DRUM_MAG("aa_12_drum_mag"),
    AA_12_GRIP("aa_12_forgrip"),
    AA_12_LIGHT_GRIP("aa_12_light_grip"),
    AA_12_SIGHT("aa_12_sight_rail"),
    AA_12_SILENCER("aa_12_suppressor"),
    AA_12_STANDARD_MAG("aa_12_standard_mag"),
    AA_12_MUZZLE("aa_12_muzzle"),
    AA_12_FRONT_RAIL("aa_12_front_rail"),
    X95R("x95r"),
    X95R_BOLT("x95r_bolt"),
    X95R_SIGHT("x95r_sight"),
    X95R_STANDARD_MAG("x95r_standard_mag"),
    FR_F2("fr_f2"),
    FR_F2_BOLT("fr_f2_bolt"),
    SMLE_III("smle_iii"),
    SMLE_III_BOLT("smle_iii_bolt"),
    SMLE_III_BOLT_EXTRA("smle_iii_bolt_extra"),
    M870_CLASSIC("m870_classic"),
    M870_CLASSIC_SLIDE("m870_classic_slide"),
    M60_UNFOLDED_SIGHT("m60_unfolded_sight"),
    M60_FOLDED_SIGHT("m60_folded_sight"),
    M60_BOLT("m60_bolt"),
    AK47_BUTT_HEAVY("ak47_heavy_stock"),
    AK47_BUTT_LIGHTWEIGHT("ak47_light_stock"),
    AK47_BUTT_TACTICAL("ak47_tactical_stock"),
    AK47_SILENCER("ak47_silencer"),
    AK74_BUTT_HEAVY("ak74_heavy_stock"),
    AK74_BUTT_TACTICAL("ak74_tactical_stock"),
    AR_15_CQB_IRONS("ar_15_cqb_irons"),
    AR_15_CQB_IRONS_2("ar_15_cqb_irons_2"),
    AR_15_CQB_DEFAULT_BARREL("ar_15_cqb_default_barrel"),
    AR_15_CQB_BRAKE("ar_15_cqb_brake"),
    AR_15_CQB_COMPENSATOR("ar_15_cqb_compensator"),
    AK47_BRAKE("ak47_brake"),
    AK47_COMPENSATOR("ak47_compensator"),
    AR_10_BODY("ar_10_body"),
    AR_10_DEFAULT_BARREL("ar_10_default_muzzle"),
    AR_10_BRAKE("ar_10_brake"),
    AR_10_COMPENSATOR("ar_10_compensator"),
    AR_10_BOLT("ar_10_bolt"),
    AR_10_SUPPRESSOR("ar_10_suppressor"),
    AR_10_EXTENDED_MAG("ar_10_extended_mag"),
    AR_10_HEAVY_STOCK("ar_10_heavy_stock"),
    AR_10_STANDARD_MAG("ar_10_standard_mag"),
    AR_10_TACTICAL_GRIP("ar_10_tactical_grip"),
    MAC_10_EXTENDED_MAG("mac_10_extended_mag"),
    MAC_10_STANDARD_MAG("mac_10_standard_mag");

    /**
     * The location of an item model in the [MOD_ID]/models/special/[NAME] folder
     */
    private ResourceLocation modelLocation;

    /**
     * Determines if the model should be loaded as a special model.
     */
    private boolean specialModel;

    /**
     * Cached model
     */
    @OnlyIn(Dist.CLIENT)
    private IBakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModels(String modelName)
    {
        this(new ResourceLocation(Reference.MOD_ID, "special/" + modelName), true);
    }

    /**
     * Sets the model's location
     *
     * @param resource name of the model file
     * @param specialModel if the model is a special model
     */
    SpecialModels(ResourceLocation resource, boolean specialModel)
    {
        this.modelLocation = resource;
        this.specialModel = specialModel;
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    @OnlyIn(Dist.CLIENT)
    public IBakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
            if(model == Minecraft.getInstance().getModelManager().getMissingModel())
            {
                return model;
            }
            this.cachedModel = model;
        }
        return this.cachedModel;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void register(ModelRegistryEvent event)
    {
        for(SpecialModels model : values())
        {
            if(model.specialModel)
            {
                ModelLoader.addSpecialModel(model.modelLocation);
            }
        }
    }
}
