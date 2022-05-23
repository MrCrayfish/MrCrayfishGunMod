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
 * Author: Forked from MrCrayfish, continued by Timeless devs
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
    SPRINGFIELD_1903_MOUNT("springfield_1903_mount"),
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
    SMLE_III_MOUNT("smle_iii_mount"),
    M870_CLASSIC("m870_classic"),
    M870_CLASSIC_SLIDE("m870_classic_slide"),
    M60_UNFOLDED_SIGHT("m60_unfolded_sight"),
    M60_FOLDED_SIGHT("m60_folded_sight"),
    M60_BOLT("m60_bolt"),
    AK47_BUTT_HEAVY("ak47_heavy_stock"),
    AK47_BUTT_LIGHTWEIGHT("ak47_light_stock"),
    AK47_BUTT_TACTICAL("ak47_tactical_stock"),
    AK47_SILENCER("ak47_silencer"),
    AK47_MAGAZINE("ak47_magazine"),
    AK74_BUTT_HEAVY("ak74_heavy_stock"),
    AK74_BUTT_TACTICAL("ak74_tactical_stock"),
    AR_15_CQB_IRONS("ar_15_cqb_irons"),
    AR_15_CQB_IRONS_2("ar_15_cqb_irons_2"),
    AR_15_CQB_DEFAULT_BARREL("ar_15_cqb_default_barrel"),
    AR_15_CQB_BRAKE("ar_15_cqb_brake"),
    AR_15_CQB_COMPENSATOR("ar_15_cqb_compensator"),
    AR_15_CQB_STANDARD_FLASHLIGHT("ar_15_standard_flashlight"),
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
    MAC_10_STANDARD_MAG("mac_10_standard_mag"),
    M4_COMPENSATOR("m4a1_compensator"),
    M4_DEFAULT_HANDGUARD("m4a1_default_handguard"),
    M4_DEFAULT_BARREL("m4a1_default_muzzle"),
    M4_EXTENDED_HANDGUARD("m4a1_extended_handguard"),
    M4_EXTENDED_MAG("m4a1_extended_mag"),
    M4_SUPPRESSOR("m4a1_silencer"),
    M4_STANDARD_MAG("m4a1_standard_mag"),
    M4_CARRY("m4a1_carry"),
    M4_BRAKE("m4a1_brake"),
    M1A1_SMG_BODY("m1a1_smg"),
    M1A1_SMG_BOLT("m1a1_smg_bolt"),
    MK14_BODY("mk14"),
    MK14_BOLT("mk14_bolt"),
    MK14_FLASHLIGHT("mk14_flashlight"),
    SPAS_12_BODY("spas_12"),
    SPAS_12_BOLT("spas_12_bolt"),
    SPAS_12_PUMP("spas_12_pump"),
    SPAS_12_STOCK("spas_12_stock"),
    DEAGLE_50_SILENCER("deagle_50_silencer"),
    DEAGLE_50_BRAKE("deagle_50_brake"),
    DEAGLE_50_COMPENSATOR("deagle_50_compensator"),
    HK_MP5A5_BODY("hk_mp5a5"),
    HK_MP5A5_BRAKE("hk_mp5a5_brake"),
    HK_MP5A5_COMPENSATOR("hk_mp5a5_compensator"),
    HK_MP5A5_BOLT("hk_mp5a5_bolt"),
    HK_MP5A5_SUPPRESSOR("hk_mp5a5_silencer"),
    HK_MP5A5_EXTENDED_MAG("hk_mp5a5_extended_mag"),
    HK_MP5A5_HEAVY_STOCK("hk_mp5a5_heavy_stock"),
    HK_MP5A5_STANDARD_MAG("hk_mp5a5_standard_mag"),
    HK_MP5A5_TACTICAL_GRIP("hk_mp5a5_tactical_grip"),
    HK_MP5A5_LIGHT_GRIP("hk_mp5a5_light_grip"),
    HK_MP5A5_LIGHT_STOCK("hk_mp5a5_light_stock"),
    HK_MP5A5_TACTICAL_STOCK("hk_mp5a5_tactical_stock"),
    HK_MP5A5_BOLT_HANDLE("hk_mp5a5_bolt_handle"),
    HK_MP5A5_STANDARD_HANDGUARD("hk_mp5a5_standard_handguard"),
    HK_MP5A5_EXTENDED_HANDGUARD("hk_mp5a5_extended_handguard"),
    HK_MP5A5_DEFAULT_STOCK("hk_mp5a5_default_stock"),
    HK_MP5A5_OPTICS_RAIL("hk_mp5a5_rail"),
    STEN_MK_II_BODY("sten_mk_ii"),
    STEN_MK_II_BOLT("sten_mk_ii_bolt"),
    STEN_MK_II_BOLT_SPRING("sten_mk_ii_bolt_spring"),
    STEN_MK_II_STANDARD_MAG("sten_mk_ii_standard_mag"),
    STEN_MK_II_STANDARD_MAG_EMPTY("sten_mk_ii_standard_mag_empty"),
    STEN_MK_II_EXTENDED_MAG("sten_mk_ii_extended_mag"),
    STEN_MK_II_EXTENDED_MAG_EMPTY("sten_mk_ii_extended_mag_empty"),
    GLOCK_18("glock_18"),
    GLOCK_18_SLIDE("glock_18_slide"),
    GLOCK_18_STANDARD_MAG("glock_18_standard_mag"),
    GLOCK_18_EXTENDED_MAG("glock_18_extended_mag"),
    M1873("m1873"),
    M1873_CYLINDER("m1873_cylinder"),
    CZ75("cz75"),
    CZ75_SLIDE("cz75_slide"),
    CZ75_STANDARD_MAG("cz75_standard_mag"),
    CZ75_EXTENDED_MAG("cz75_extended_mag"),
    CZ75_BRAKE("cz75_brake"),
    CZ75_COMPENSATOR("cz75_compensator"),
    CZ75_SUPPRESSOR("cz75_silencer"),
    CZ75_RAIL("cz75_rail"),
    CZ75_AUTO("cz75_auto"),
    CZ75_AUTO_SLIDE("cz75_auto_slide"),
    M1873_HAMMER("m1873_hammer"),
    VZ61("vz61"),
    VZ61_BOLT("vz61_bolt"),
    VZ61_STANDARD_MAG("vz61_standard_mag"),
    VZ61_EXTENDED_MAG("vz61_extended_mag"),
    QSZ92G1("qsz92g1"),
    QSZ92G1_SLIDE("qsz92g1_slide"),
    KAR98("kar98"),
    KAR98_BOLT("kar98_bolt_rotate"),
    KAR98_BOLT_EXTRA("kar98_bolt_fixed"),
    KAR98_BULLET("kar98_bullet"),
    HK416_A5_BODY("hk416_a5"),
    HK416_A5_BRAKE("hk416_a5_brake"),
    HK416_A5_COMPENSATOR("hk416_a5_compensator"),
    HK416_A5_BOLT("hk416_a5_bolt"),
    HK416_A5_SUPPRESSOR("hk416_a5_silencer"),
    HK416_A5_EXTENDED_MAG("hk416_a5_extended_mag"),
    HK416_A5_HEAVY_STOCK("hk416_a5_heavy_stock"),
    HK416_A5_STANDARD_MAG("hk416_a5_standard_mag"),
    HK416_A5_TACTICAL_GRIP("hk416_a5_tactical_grip"),
    HK416_A5_LIGHT_GRIP("hk416_a5_light_grip"),
    HK416_A5_LIGHT_STOCK("hk416_a5_light_stock"),
    HK416_A5_TACTICAL_STOCK("hk416_a5_tactical_stock"),
    HK416_A5_FOLDED("hk416_a5_folded_sights"),
    HK416_A5_UNFOLDED("hk416_a5_unfolded_sights"),
    HK416_A5_DEFAULT_MUZZLE("hk416_a5_default_muzzle"),
    GLOCK_17_SUPPRESSOR("glock_17_suppressor"),
    STI2011_SUPPRESSOR("sti2011_suppressor"),
    STI2011_EXTENDED_MAG("sti2011_extended_mag"),
    M92FS_SUPPRESSOR("m92fs_suppressor"),
    M1911_SUPPRESSOR("m1911_suppressor"),
    TYPE81_X("type81_x"),
    TYPE81_X_BOLT("type81_x_bolt"),
    TYPE81_X_MOUNT("type81_x_mount"),
    M16_A1_BOLT("m16a1_bolt"),
    M16_A1_DEFAULT_MUZZLE("m16a1_default_muzzle"),
    M16_A1_BRAKE("m16a1_brake"),
    M16_A1_COMPENSATOR("m16a1_compensator"),
    M16_A1_SUPPRESSOR("m16a1_silencer"),
    X95R_DEFAULT_MUZZLE("x95r_default_muzzle"),
    X95R_BRAKE("x95r_brake"),
    X95R_COMPENSATOR("x95r_compensator"),
    X95R_SUPPRESSOR("x95r_silencer"),
    X95R_TACTICAL_GRIP("x95r_tactical_grip"),
    X95R_LIGHT_GRIP("x95r_light_grip"),
    X95R_EXTENDED_MAG("x95r_extended_mag"),
    X95R_SIGHT_FOLDED("x95r_sight_fold"),
    PKP_PENCHENNBERG("pkp_penchenberg"),
    PKP_PENCHENNBERG_BOLT("pkp_penchenberg_bolt"),
    PKP_PENCHENNBERG_TACTICAL_GRIP("pkp_penchenberg_tactical_grip"),
    PKP_PENCHENNBERG_LIGHT_GRIP("pkp_penchenberg_light_grip"),
    PKP_PENCHENNBERG_MOUNT("pkp_penchenberg_mount"),
    MP7("mp7"),
    MP7_SIGHT("mp7_sight"),
    MP7_BRAKE("mp7_brake"),
    MP7_COMPENSATOR("mp7_compensator"),
    MP7_SUPPRESSOR("mp7_silencer"),
    MP7_BOLT("mp7_bolt"),
    MP7_EXTENDED_MAG("mp7_extended_mag"),
    MP7_STANDARD_MAG("mp7_standard_mag"),
    MP7_DEFAULT_MUZZLE("mp7_default_muzzle"),
    M82A2("m82a2"),
    M82A2_SIGHT("m82a2_sight"),
    M82A2_SIGHT_FOLDED("m82a2_sight_fold"),
    M82A2_BOLT("m82a2_bolt"),
    M82A2_BARREL("m82a2_barrel"),
    M82A2_STANDARD_MAG("m82a2_mag_short"),
    M82A2_EXTENDED_MAG("m82a2_extended_mag"),
    AI_AWP_BRAKE("ai_awp_brake"),
    AI_AWP_COMPENSATOR("ai_awp_compensator"),
    AI_AWP_SUPPRESSOR("ai_awp_silencer"),
    AI_AWP("ai_awp"),
    AI_AWP_BOLT("ai_awp_bolt"),
    AI_AWP_BOLT_EXTRA("ai_awp_bolt_extra"),
    TEC_9("tec_9"),
    TEC_9_BOLT("tec_9_bolt"),
    TEC_9_STANDARD_MAG("tec_9_standard_mag"),
    TEC_9_EXTENDED_MAG("tec_9_extended_mag"),
    RPK("rpk"),
    RPK_BOLT("rpk_bolt"),
    RPK_STANDARD_MAG("rpk_standard_mag"),
    RPK_EXTENDED_MAG("rpk_extended_mag"),
    RPK_BUTT_HEAVY("rpk_heavy_stock"),
    RPK_BUTT_LIGHTWEIGHT("rpk_light_stock"),
    RPK_BUTT_TACTICAL("rpk_tactical_stock"),
    RPK_MOUNT("rpk_mount"),
    RPG7("rpg7"),
    RPG7_ROCKET("rpg7_rocket"),
    FN_FAL("fn_fal"),
    FN_FAL_BOLT("fn_fal_bolt"),
    FN_FAL_STANDARD_MAG("fn_fal_standard_mag"),
    FN_FAL_EXTENDED_MAG("fn_fal_extended_mag"),
    FN_FAL_BUTT_HEAVY("fn_fal_heavy_stock"),
    FN_FAL_BUTT_LIGHTWEIGHT("fn_fal_light_stock"),
    FN_FAL_BUTT_TACTICAL("fn_fal_tactical_stock"),
    FN_FAL_MOUNT("fn_fal_mount"),
    FN_FAL_STANDARD_HANDGUARD("fn_fal_standard_handguard"),
    FN_FAL_EXTENDED_HANDGUARD("fn_fal_extended_handguard"),
    FN_FAL_DEFAULT_STOCK("fn_fal_default_stock"),
    FN_FAL_STANDARD_BARREL("fn_fal_standard_barrel"),
    FN_FAL_EXTENDED_BARREL("fn_fal_extended_barrel"),
    FN_FAL_TAC_GRIP("fn_fal_tactical_grip"),
    FN_FAL_LIGHTWEIGHT_GRIP("fn_fal_lightweight_grip"),
    AR15_HELLMOUTH_STANDARD_MAG("ar_15_hellmouth_standard_mag"),
    AR15_HELLMOUTH_EXTENDED_MAG("ar_15_hellmouth_extended_mag"),
    AR15_HELLMOUTH_DD_MAG("ar_15_hellmouth_dd_mag"),
    M4_EXTENDED_HANDGUARD_V2("m4a1_extended_handguard_v2"),
    VECTOR45_BRAKE("vector45_brake"),
    VECTOR45_COMP("vector45_compensator"),
    VECTOR45_LGRIP("vector45_light_grip"),
    QBZ_95_BRAKE("qbz_95_brake"),
    QBZ_95_COMP("qbz_95_compensator"),

    //Everything from this point on is all scope additions

    MINI_DOT_BASE("optics/mini_dot_base"),
    MICRO_HOLO_BASE("optics/micro_holo_base"),

    //Everything from this point on is all LOD renders

    M1911_LOD("lods/m1911_lod");

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
