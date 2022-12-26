package com.tac.guns.client.render.armor.models;

// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Reference;
import com.tac.guns.client.render.armor.VestLayer.ArmorBase;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


@OnlyIn(Dist.CLIENT)
public class CardboardArmor extends ArmorBase {
	private final ModelRenderer Cardboard_armor;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer bone;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer bone2;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer bone4;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;
	private final ModelRenderer cube_r17;
	private final ModelRenderer cube_r18;
	private final ModelRenderer cube_r19;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer cube_r22;
	private final ModelRenderer bone3;
	private final ModelRenderer cube_r23;
	private final ModelRenderer cube_r24;
	private final ModelRenderer cube_r25;
	private final ModelRenderer cube_r26;
	private final ModelRenderer cube_r27;
	private final ModelRenderer cube_r28;
	private final ModelRenderer cube_r29;
	private final ModelRenderer cube_r30;
	private final ModelRenderer bone5;
	private final ModelRenderer cube_r31;
	private final ModelRenderer cube_r32;
	private final ModelRenderer cube_r33;
	private final ModelRenderer cube_r34;
	private final ModelRenderer cube_r35;
	private final ModelRenderer cube_r36;
	private final ModelRenderer cube_r37;
	private final ModelRenderer cube_r38;
	private final ModelRenderer bone6;
	private final ModelRenderer cube_r39;
	private final ModelRenderer cube_r40;
	private final ModelRenderer cube_r41;
	private final ModelRenderer cube_r42;
	private final ModelRenderer cube_r43;
	private final ModelRenderer cube_r44;
	private final ModelRenderer cube_r45;
	private final ModelRenderer cube_r46;
	private final ModelRenderer cube_r47;
	private final ModelRenderer cube_r48;
	private final ModelRenderer cube_r49;
	private final ModelRenderer cube_r50;
	private final ModelRenderer cube_r51;
	private final ModelRenderer cube_r52;
	private final ModelRenderer cube_r53;
	private final ModelRenderer cube_r54;
	private final ModelRenderer cube_r55;
	private final ModelRenderer cube_r56;
	private final ModelRenderer cube_r57;
	private final ModelRenderer cube_r58;

	public CardboardArmor() {
		textureWidth = 256;
		textureHeight = 256;

		Cardboard_armor = new ModelRenderer(this);
		Cardboard_armor.setRotationPoint(0.0F, 24.0F, 0.0F);
		Cardboard_armor.setTextureOffset(124, 174).addBox(-4.0F, -22.0F, -3.25F, 8.0F, 10.0F, 1.0F, -0.3125F, false);
		Cardboard_armor.setTextureOffset(105, 174).addBox(-4.0F, -24.0F, 2.4375F, 8.0F, 12.0F, 1.0F, -0.3125F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-4.7129F, -15.0F, 2.4973F);
		Cardboard_armor.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, -0.3927F, 0.0F);
		cube_r1.setTextureOffset(178, 178).addBox(-0.5F, -9.0F, -0.5F, 2.0F, 12.0F, 1.0F, -0.3125F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(4.7129F, -15.0F, 2.4973F);
		Cardboard_armor.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.3927F, 0.0F);
		cube_r2.setTextureOffset(171, 178).addBox(-1.5F, -9.0F, -0.5F, 2.0F, 12.0F, 1.0F, -0.3125F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(4.7129F, -15.0F, -2.3098F);
		Cardboard_armor.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, -0.3927F, 0.0F);
		cube_r3.setTextureOffset(152, 182).addBox(-1.5F, -6.0F, -0.5F, 2.0F, 9.0F, 1.0F, -0.3125F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(-4.7129F, -15.0F, -2.3098F);
		Cardboard_armor.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.3927F, 0.0F);
		cube_r4.setTextureOffset(185, 178).addBox(-0.5F, -6.0F, -0.5F, 2.0F, 9.0F, 1.0F, -0.3125F, false);

		bone = new ModelRenderer(this);
		bone.setRotationPoint(4.2928F, -23.7479F, -2.1132F);
		Cardboard_armor.addChild(bone);
		

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone.addChild(cube_r5);
		setRotationAngle(cube_r5, -0.0873F, 0.0F, 0.48F);
		cube_r5.setTextureOffset(110, 188).addBox(-1.5F, -1.2295F, -0.7156F, 3.0F, 5.0F, 1.0F, -0.3125F, false);

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone.addChild(cube_r6);
		setRotationAngle(cube_r6, -0.8727F, 0.0F, 0.48F);
		cube_r6.setTextureOffset(168, 192).addBox(-1.5F, -2.0509F, -1.2459F, 3.0F, 2.0F, 1.0F, -0.3125F, false);

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(-0.1755F, 0.3372F, 4.2767F);
		bone.addChild(cube_r7);
		setRotationAngle(cube_r7, -2.906F, 0.0F, 0.48F);
		cube_r7.setTextureOffset(78, 190).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 3.0F, 1.0F, -0.3125F, false);

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(0.711F, -1.3657F, 1.3814F);
		bone.addChild(cube_r8);
		setRotationAngle(cube_r8, -1.7279F, 0.0F, 0.48F);
		cube_r8.setTextureOffset(51, 190).addBox(-1.5F, -3.0F, -0.5F, 3.0F, 4.0F, 1.0F, -0.3125F, false);

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-4.2928F, -23.7479F, -2.1132F);
		Cardboard_armor.addChild(bone2);
		

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone2.addChild(cube_r9);
		setRotationAngle(cube_r9, -0.0873F, 0.0F, -0.48F);
		cube_r9.setTextureOffset(186, 160).addBox(-1.5F, -1.2295F, -0.7156F, 3.0F, 5.0F, 1.0F, -0.3125F, false);

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone2.addChild(cube_r10);
		setRotationAngle(cube_r10, -0.8727F, 0.0F, -0.48F);
		cube_r10.setTextureOffset(137, 192).addBox(-1.5F, -2.0509F, -1.2459F, 3.0F, 2.0F, 1.0F, -0.3125F, false);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(0.1755F, 0.3372F, 4.2767F);
		bone2.addChild(cube_r11);
		setRotationAngle(cube_r11, -2.906F, 0.0F, -0.48F);
		cube_r11.setTextureOffset(69, 190).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 3.0F, 1.0F, -0.3125F, false);

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(-0.711F, -1.3657F, 1.3814F);
		bone2.addChild(cube_r12);
		setRotationAngle(cube_r12, -1.7279F, 0.0F, -0.48F);
		cube_r12.setTextureOffset(42, 190).addBox(-1.5F, -3.0F, -0.5F, 3.0F, 4.0F, 1.0F, -0.3125F, false);

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(4.0172F, -13.375F, -2.7693F);
		Cardboard_armor.addChild(bone4);
		bone4.setTextureOffset(164, 174).addBox(-8.5172F, -1.0F, 6.0179F, 9.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(1.5451F, 0.0F, 0.9078F);
		bone4.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.0F, -1.2305F, 0.0F);
		cube_r13.setTextureOffset(192, 182).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(1.7811F, 0.0F, 3.7813F);
		bone4.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.0F, -1.5708F, 0.0F);
		cube_r14.setTextureOffset(9, 190).addBox(-2.5F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(0.5986F, 0.0F, 6.0671F);
		bone4.addChild(cube_r15);
		setRotationAngle(cube_r15, 0.0F, -2.3736F, 0.0F);
		cube_r15.setTextureOffset(87, 190).addBox(-2.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r16 = new ModelRenderer(this);
		cube_r16.setRotationPoint(-8.6331F, 0.0F, 6.0671F);
		bone4.addChild(cube_r16);
		setRotationAngle(cube_r16, 0.0F, 2.3736F, 0.0F);
		cube_r16.setTextureOffset(191, 167).addBox(-1.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r17 = new ModelRenderer(this);
		cube_r17.setRotationPoint(-9.8156F, 0.0F, 3.7813F);
		bone4.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.0F, 1.5708F, 0.0F);
		cube_r17.setTextureOffset(31, 190).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r18 = new ModelRenderer(this);
		cube_r18.setRotationPoint(-9.5796F, 0.0F, 0.9078F);
		bone4.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.0F, 1.2305F, 0.0F);
		cube_r18.setTextureOffset(129, 193).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r19 = new ModelRenderer(this);
		cube_r19.setRotationPoint(-0.5629F, 0.0F, -0.4032F);
		bone4.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.0F, -0.0873F, 0.0F);
		cube_r19.setTextureOffset(182, 138).addBox(-5.0F, -1.0F, -0.5F, 6.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r20 = new ModelRenderer(this);
		cube_r20.setRotationPoint(-7.4715F, 0.0F, -0.4032F);
		bone4.addChild(cube_r20);
		setRotationAngle(cube_r20, 0.0F, 0.0873F, 0.0F);
		cube_r20.setTextureOffset(20, 190).addBox(-1.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r21 = new ModelRenderer(this);
		cube_r21.setRotationPoint(0.7508F, 0.0F, -0.014F);
		bone4.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.0F, -0.4887F, 0.0F);
		cube_r21.setTextureOffset(192, 178).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r22 = new ModelRenderer(this);
		cube_r22.setRotationPoint(-8.7853F, 0.0F, -0.014F);
		bone4.addChild(cube_r22);
		setRotationAngle(cube_r22, 0.0F, 0.4887F, 0.0F);
		cube_r22.setTextureOffset(193, 142).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, 0.1875F);
		Cardboard_armor.addChild(bone3);
		bone3.setTextureOffset(160, 178).addBox(5.0266F, -17.625F, -2.0F, 1.0F, 6.0F, 4.0F, -0.3125F, false);
		bone3.setTextureOffset(143, 174).addBox(-4.5F, -17.625F, 2.8194F, 9.0F, 6.0F, 1.0F, -0.3125F, false);
		bone3.setTextureOffset(175, 149).addBox(-6.0266F, -17.625F, -2.0F, 1.0F, 6.0F, 4.0F, -0.3125F, false);

		cube_r23 = new ModelRenderer(this);
		cube_r23.setRotationPoint(-4.4858F, -14.625F, 2.8597F);
		bone3.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.0F, 0.6981F, 0.0F);
		cube_r23.setTextureOffset(143, 182).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 3.0F, -0.3125F, false);

		cube_r24 = new ModelRenderer(this);
		cube_r24.setRotationPoint(-4.8951F, -14.625F, -3.1748F);
		bone3.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, -0.3927F, 0.0F);
		cube_r24.setTextureOffset(186, 142).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 2.0F, -0.3125F, false);

		cube_r25 = new ModelRenderer(this);
		cube_r25.setRotationPoint(-4.526F, -14.625F, -3.2218F);
		bone3.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.0F, -0.925F, 0.0F);
		cube_r25.setTextureOffset(122, 186).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 6.0F, 2.0F, -0.3125F, false);

		cube_r26 = new ModelRenderer(this);
		cube_r26.setRotationPoint(-2.4605F, -14.625F, -3.3076F);
		bone3.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.0F, -1.7453F, 0.0F);
		cube_r26.setTextureOffset(175, 138).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, -0.3125F, false);

		cube_r27 = new ModelRenderer(this);
		cube_r27.setRotationPoint(2.4605F, -14.625F, -3.3076F);
		bone3.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.0F, 1.7453F, 0.0F);
		cube_r27.setTextureOffset(175, 160).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, -0.3125F, false);

		cube_r28 = new ModelRenderer(this);
		cube_r28.setRotationPoint(4.526F, -14.625F, -3.2218F);
		bone3.addChild(cube_r28);
		setRotationAngle(cube_r28, 0.0F, 0.925F, 0.0F);
		cube_r28.setTextureOffset(186, 151).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 6.0F, 2.0F, -0.3125F, false);

		cube_r29 = new ModelRenderer(this);
		cube_r29.setRotationPoint(4.4858F, -14.625F, 2.8597F);
		bone3.addChild(cube_r29);
		setRotationAngle(cube_r29, 0.0F, -0.6981F, 0.0F);
		cube_r29.setTextureOffset(185, 168).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 3.0F, -0.3125F, false);

		cube_r30 = new ModelRenderer(this);
		cube_r30.setRotationPoint(4.8951F, -14.625F, -3.1748F);
		bone3.addChild(cube_r30);
		setRotationAngle(cube_r30, 0.0F, 0.3927F, 0.0F);
		cube_r30.setTextureOffset(103, 188).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 2.0F, -0.3125F, false);

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
		Cardboard_armor.addChild(bone5);
		

		cube_r31 = new ModelRenderer(this);
		cube_r31.setRotationPoint(0.0793F, -21.4629F, -2.8786F);
		bone5.addChild(cube_r31);
		setRotationAngle(cube_r31, -0.1571F, -0.0027F, -0.0172F);
		cube_r31.setTextureOffset(185, 189).addBox(-2.0F, -0.8125F, -0.5F, 4.0F, 2.0F, 1.0F, -0.375F, false);

		cube_r32 = new ModelRenderer(this);
		cube_r32.setRotationPoint(3.9466F, -22.408F, 2.1129F);
		bone5.addChild(cube_r32);
		setRotationAngle(cube_r32, 0.1147F, 0.0399F, 0.2112F);
		cube_r32.setTextureOffset(159, 189).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, -0.375F, false);

		cube_r33 = new ModelRenderer(this);
		cube_r33.setRotationPoint(-3.3918F, -21.4629F, 2.2308F);
		bone5.addChild(cube_r33);
		setRotationAngle(cube_r33, 0.1182F, -0.0277F, -0.3153F);
		cube_r33.setTextureOffset(60, 190).addBox(-1.5F, -3.0F, -0.5F, 3.0F, 3.0F, 1.0F, -0.375F, false);

		cube_r34 = new ModelRenderer(this);
		cube_r34.setRotationPoint(2.9755F, -21.1954F, -2.9377F);
		bone5.addChild(cube_r34);
		setRotationAngle(cube_r34, -0.0715F, 0.0517F, 1.2193F);
		cube_r34.setTextureOffset(96, 190).addBox(-1.8125F, -1.4375F, -0.5F, 2.0F, 3.0F, 1.0F, -0.375F, false);

		cube_r35 = new ModelRenderer(this);
		cube_r35.setRotationPoint(-1.1332F, -20.544F, -2.9703F);
		bone5.addChild(cube_r35);
		setRotationAngle(cube_r35, -0.0767F, 0.0174F, 0.0749F);
		cube_r35.setTextureOffset(177, 192).addBox(-0.8281F, -1.4688F, -0.375F, 2.0F, 2.0F, 1.0F, -0.375F, false);

		cube_r36 = new ModelRenderer(this);
		cube_r36.setRotationPoint(-2.9655F, -21.3541F, -2.9016F);
		bone5.addChild(cube_r36);
		setRotationAngle(cube_r36, -0.0852F, -0.0227F, -0.8537F);
		cube_r36.setTextureOffset(146, 192).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, -0.375F, false);

		cube_r37 = new ModelRenderer(this);
		cube_r37.setRotationPoint(-3.5793F, -22.4629F, -2.8942F);
		bone5.addChild(cube_r37);
		setRotationAngle(cube_r37, -0.1055F, 0.0017F, -0.5579F);
		cube_r37.setTextureOffset(129, 186).addBox(-1.5F, -3.0F, -0.5F, 3.0F, 5.0F, 1.0F, -0.375F, false);

		cube_r38 = new ModelRenderer(this);
		cube_r38.setRotationPoint(3.7668F, -22.6504F, -2.9099F);
		bone5.addChild(cube_r38);
		setRotationAngle(cube_r38, -0.1055F, 0.0039F, 0.61F);
		cube_r38.setTextureOffset(0, 190).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, -0.375F, false);

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(0.4064F, 4.6354F, 1.5F);
		

		cube_r39 = new ModelRenderer(this);
		cube_r39.setRotationPoint(-9.167F, 4.4855F, -10.167F);
		bone6.addChild(cube_r39);
		setRotationAngle(cube_r39, 0.1173F, 0.6365F, 0.1958F);
		cube_r39.setTextureOffset(35, 172).addBox(-8.0F, -8.0F, -0.5F, 16.0F, 16.0F, 1.0F, -7.3438F, false);

		cube_r40 = new ModelRenderer(this);
		cube_r40.setRotationPoint(-7.3232F, -1.0672F, -10.609F);
		bone6.addChild(cube_r40);
		setRotationAngle(cube_r40, 0.3557F, 0.1701F, 1.1433F);
		cube_r40.setTextureOffset(169, 120).addBox(-5.9375F, -6.875F, -0.5156F, 16.0F, 16.0F, 1.0F, -7.4375F, false);

		cube_r41 = new ModelRenderer(this);
		cube_r41.setRotationPoint(6.48F, -3.7604F, -10.6738F);
		bone6.addChild(cube_r41);
		setRotationAngle(cube_r41, 0.0F, 2.7489F, 0.0F);
		cube_r41.setTextureOffset(140, 137).addBox(-8.1094F, -4.9688F, -14.2891F, 16.0F, 16.0F, 1.0F, -7.3125F, false);

		cube_r42 = new ModelRenderer(this);
		cube_r42.setRotationPoint(6.5352F, -0.6354F, -10.4972F);
		bone6.addChild(cube_r42);
		setRotationAngle(cube_r42, -0.0594F, -0.3884F, 0.1557F);
		cube_r42.setTextureOffset(35, 154).addBox(-7.7031F, -6.25F, -0.5F, 16.0F, 16.0F, 1.0F, -7.3125F, false);

		cube_r43 = new ModelRenderer(this);
		cube_r43.setRotationPoint(-7.3481F, -0.6354F, -10.4972F);
		bone6.addChild(cube_r43);
		setRotationAngle(cube_r43, 0.0842F, 0.384F, 0.2214F);
		cube_r43.setTextureOffset(0, 172).addBox(-7.9531F, -7.875F, -0.5F, 16.0F, 16.0F, 1.0F, -7.3125F, false);

		cube_r44 = new ModelRenderer(this);
		cube_r44.setRotationPoint(-0.3458F, 4.2547F, -11.2813F);
		bone6.addChild(cube_r44);
		setRotationAngle(cube_r44, 0.0F, 0.0F, -0.1396F);
		cube_r44.setTextureOffset(70, 154).addBox(-8.0F, -8.0F, -0.5F, 16.0F, 16.0F, 1.0F, -7.3125F, false);

		cube_r45 = new ModelRenderer(this);
		cube_r45.setRotationPoint(-0.4064F, 2.5521F, -11.2813F);
		bone6.addChild(cube_r45);
		setRotationAngle(cube_r45, 0.0F, 0.0F, 0.2443F);
		cube_r45.setTextureOffset(105, 156).addBox(-7.9375F, -8.0F, -0.5F, 16.0F, 16.0F, 1.0F, -7.3125F, false);

		cube_r46 = new ModelRenderer(this);
		cube_r46.setRotationPoint(8.8735F, 3.0214F, -9.9713F);
		bone6.addChild(cube_r46);
		setRotationAngle(cube_r46, 0.2048F, -0.6169F, -0.3447F);
		cube_r46.setTextureOffset(140, 156).addBox(-9.0F, -6.6875F, -0.5156F, 16.0F, 16.0F, 1.0F, -7.5F, false);

		cube_r47 = new ModelRenderer(this);
		cube_r47.setRotationPoint(8.3417F, 3.1771F, -10.1373F);
		bone6.addChild(cube_r47);
		setRotationAngle(cube_r47, -0.1554F, -0.6294F, 0.2601F);
		cube_r47.setTextureOffset(169, 102).addBox(-8.0F, -8.25F, -0.5F, 16.0F, 16.0F, 1.0F, -7.3125F, false);

		cube_r48 = new ModelRenderer(this);
		cube_r48.setRotationPoint(-9.1546F, 3.1771F, -10.1373F);
		bone6.addChild(cube_r48);
		setRotationAngle(cube_r48, -0.1554F, 0.6294F, -0.2601F);
		cube_r48.setTextureOffset(70, 172).addBox(-8.0F, -8.25F, -0.5F, 16.0F, 16.0F, 1.0F, -7.3125F, false);

		cube_r49 = new ModelRenderer(this);
		cube_r49.setRotationPoint(-1.5039F, 3.5165F, 15.71F);
		bone6.addChild(cube_r49);
		setRotationAngle(cube_r49, 0.0F, 0.0F, 0.0524F);
		cube_r49.setTextureOffset(0, 34).addBox(-16.0F, -15.6875F, -0.5F, 32.0F, 32.0F, 1.0F, -14.0F, false);

		cube_r50 = new ModelRenderer(this);
		cube_r50.setRotationPoint(0.5743F, 3.8867F, 15.71F);
		bone6.addChild(cube_r50);
		setRotationAngle(cube_r50, 0.0F, 0.0F, 0.1571F);
		cube_r50.setTextureOffset(67, 34).addBox(-16.0F, -16.0F, -0.5F, 32.0F, 32.0F, 1.0F, -14.0F, false);

		cube_r51 = new ModelRenderer(this);
		cube_r51.setRotationPoint(1.7277F, -2.5947F, 15.1719F);
		bone6.addChild(cube_r51);
		setRotationAngle(cube_r51, 0.0F, 0.0F, 0.1222F);
		cube_r51.setTextureOffset(0, 0).addBox(-15.9375F, -13.1875F, -0.5F, 32.0F, 32.0F, 1.0F, -14.0F, false);

		cube_r52 = new ModelRenderer(this);
		cube_r52.setRotationPoint(-2.5405F, -2.1259F, 15.1719F);
		bone6.addChild(cube_r52);
		setRotationAngle(cube_r52, 0.0F, 0.0F, -0.2793F);
		cube_r52.setTextureOffset(0, 68).addBox(-16.25F, -12.8125F, -0.5F, 32.0F, 32.0F, 1.0F, -14.0F, false);

		cube_r53 = new ModelRenderer(this);
		cube_r53.setRotationPoint(-2.8851F, -3.3275F, 15.1719F);
		bone6.addChild(cube_r53);
		setRotationAngle(cube_r53, 0.0F, 0.0F, -0.1222F);
		cube_r53.setTextureOffset(67, 68).addBox(-15.0625F, -15.3125F, -0.5F, 32.0F, 32.0F, 1.0F, -14.0F, false);

		cube_r54 = new ModelRenderer(this);
		cube_r54.setRotationPoint(1.7277F, -2.1259F, 15.1719F);
		bone6.addChild(cube_r54);
		setRotationAngle(cube_r54, 0.0F, 0.0F, 0.2793F);
		cube_r54.setTextureOffset(0, 102).addBox(-16.6875F, -17.25F, -0.5F, 32.0F, 32.0F, 1.0F, -14.0F, false);

		cube_r55 = new ModelRenderer(this);
		cube_r55.setRotationPoint(-0.9688F, 0.0F, 2.0F);
		bone6.addChild(cube_r55);
		setRotationAngle(cube_r55, 3.1416F, 0.0F, -3.002F);
		cube_r55.setTextureOffset(134, 68).addBox(-14.6563F, -15.0156F, 19.4531F, 32.0F, 32.0F, 1.0F, -14.0F, false);

		cube_r56 = new ModelRenderer(this);
		cube_r56.setRotationPoint(-0.3131F, 3.6449F, -18.5918F);
		bone6.addChild(cube_r56);
		setRotationAngle(cube_r56, 3.1247F, 0.1737F, 3.0441F);
		cube_r56.setTextureOffset(67, 102).addBox(-16.0F, -16.0F, -0.5F, 32.0F, 32.0F, 1.0F, -14.5F, false);

		cube_r57 = new ModelRenderer(this);
		cube_r57.setRotationPoint(-0.4998F, 3.6449F, -18.5918F);
		bone6.addChild(cube_r57);
		setRotationAngle(cube_r57, 3.1247F, -0.1737F, -3.0441F);
		cube_r57.setTextureOffset(134, 0).addBox(-16.0F, -16.0F, -0.5F, 32.0F, 32.0F, 1.0F, -14.5F, false);

		cube_r58 = new ModelRenderer(this);
		cube_r58.setRotationPoint(2.375F, 0.0F, 2.0F);
		bone6.addChild(cube_r58);
		setRotationAngle(cube_r58, 3.1416F, 0.0F, 2.8885F);
		cube_r58.setTextureOffset(134, 34).addBox(-14.6563F, -15.7656F, 19.4531F, 32.0F, 32.0F, 1.0F, -14.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		matrixStack.translate(0, 1.5, 0);
		Cardboard_armor.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected ModelRenderer getModel() {
		return Cardboard_armor;
	}

	// Updatable in order to allow for damaged textures as well to be applied
	private static ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/armor/cardboard_armor_1.png");

	@Override
	protected ResourceLocation getTexture() {
		return TEXTURE;
	}

	@Override
	protected void setTexture(String modId, String path) {
		TEXTURE = new ResourceLocation(modId, path);
	}
}