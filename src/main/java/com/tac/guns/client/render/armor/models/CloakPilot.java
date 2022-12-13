package com.tac.guns.client.render.armor.models;// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CloakPilot extends EntityModel<Entity> {
	private final ModelRenderer Head;
	private final ModelRenderer bone9;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer bone6;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;
	private final ModelRenderer cube_r17;
	private final ModelRenderer cube_r18;
	private final ModelRenderer cube_r19;
	private final ModelRenderer bone;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer bone4;
	private final ModelRenderer cube_r22;
	private final ModelRenderer cube_r23;
	private final ModelRenderer cube_r24;
	private final ModelRenderer cube_r25;
	private final ModelRenderer cube_r26;
	private final ModelRenderer cube_r27;
	private final ModelRenderer cube_r28;
	private final ModelRenderer bone2;
	private final ModelRenderer cube_r29;
	private final ModelRenderer cube_r30;
	private final ModelRenderer cube_r31;
	private final ModelRenderer cube_r32;
	private final ModelRenderer cube_r33;
	private final ModelRenderer cube_r34;
	private final ModelRenderer cube_r35;
	private final ModelRenderer cube_r36;
	private final ModelRenderer cube_r37;
	private final ModelRenderer cube_r38;
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
	private final ModelRenderer cube_r59;
	private final ModelRenderer cube_r60;
	private final ModelRenderer cube_r61;
	private final ModelRenderer cube_r62;
	private final ModelRenderer bone5;
	private final ModelRenderer cube_r63;
	private final ModelRenderer cube_r64;
	private final ModelRenderer cube_r65;
	private final ModelRenderer cube_r66;
	private final ModelRenderer cube_r67;
	private final ModelRenderer cube_r68;
	private final ModelRenderer cube_r69;
	private final ModelRenderer cube_r70;
	private final ModelRenderer cube_r71;
	private final ModelRenderer cube_r72;
	private final ModelRenderer cube_r73;
	private final ModelRenderer cube_r74;
	private final ModelRenderer cube_r75;
	private final ModelRenderer cube_r76;
	private final ModelRenderer cube_r77;
	private final ModelRenderer cube_r78;
	private final ModelRenderer cube_r79;
	private final ModelRenderer cube_r80;
	private final ModelRenderer cube_r81;
	private final ModelRenderer cube_r82;
	private final ModelRenderer cube_r83;
	private final ModelRenderer cube_r84;
	private final ModelRenderer cube_r85;
	private final ModelRenderer cube_r86;
	private final ModelRenderer bone3;
	private final ModelRenderer cube_r87;
	private final ModelRenderer cube_r88;
	private final ModelRenderer cube_r89;
	private final ModelRenderer cube_r90;
	private final ModelRenderer cube_r91;
	private final ModelRenderer cube_r92;
	private final ModelRenderer cube_r93;
	private final ModelRenderer cube_r94;
	private final ModelRenderer cube_r95;
	private final ModelRenderer cube_r96;
	private final ModelRenderer cube_r97;
	private final ModelRenderer cube_r98;
	private final ModelRenderer cube_r99;
	private final ModelRenderer cube_r100;
	private final ModelRenderer cube_r101;
	private final ModelRenderer cube_r102;
	private final ModelRenderer cube_r103;
	private final ModelRenderer cube_r104;
	private final ModelRenderer cube_r105;
	private final ModelRenderer bone7;
	private final ModelRenderer cube_r106;
	private final ModelRenderer cube_r107;
	private final ModelRenderer cube_r108;
	private final ModelRenderer cube_r109;
	private final ModelRenderer cube_r110;
	private final ModelRenderer cube_r111;
	private final ModelRenderer cube_r112;
	private final ModelRenderer cube_r113;
	private final ModelRenderer cube_r114;
	private final ModelRenderer cube_r115;
	private final ModelRenderer cube_r116;
	private final ModelRenderer bone43;
	private final ModelRenderer cube_r117;
	private final ModelRenderer cube_r118;
	private final ModelRenderer cube_r119;
	private final ModelRenderer cube_r120;
	private final ModelRenderer cube_r121;
	private final ModelRenderer cube_r122;
	private final ModelRenderer cube_r123;
	private final ModelRenderer cube_r124;
	private final ModelRenderer cube_r125;
	private final ModelRenderer cube_r126;
	private final ModelRenderer cube_r127;
	private final ModelRenderer cube_r128;
	private final ModelRenderer cube_r129;
	private final ModelRenderer cube_r130;
	private final ModelRenderer Body;
	private final ModelRenderer bone27;
	private final ModelRenderer cube_r131;
	private final ModelRenderer cube_r132;
	private final ModelRenderer cube_r133;
	private final ModelRenderer cube_r134;
	private final ModelRenderer cube_r135;
	private final ModelRenderer cube_r136;
	private final ModelRenderer cube_r137;
	private final ModelRenderer bone29;
	private final ModelRenderer bone8;
	private final ModelRenderer cube_r138;
	private final ModelRenderer cube_r139;
	private final ModelRenderer cube_r140;
	private final ModelRenderer cube_r141;
	private final ModelRenderer bone10;
	private final ModelRenderer cube_r142;
	private final ModelRenderer cube_r143;
	private final ModelRenderer cube_r144;
	private final ModelRenderer cube_r145;
	private final ModelRenderer cube_r146;
	private final ModelRenderer cube_r147;
	private final ModelRenderer bone28;
	private final ModelRenderer cube_r148;
	private final ModelRenderer cube_r149;
	private final ModelRenderer cube_r150;
	private final ModelRenderer cube_r151;
	private final ModelRenderer cube_r152;
	private final ModelRenderer cube_r153;
	private final ModelRenderer cube_r154;
	private final ModelRenderer cube_r155;
	private final ModelRenderer cube_r156;
	private final ModelRenderer cube_r157;
	private final ModelRenderer cube_r158;
	private final ModelRenderer cube_r159;
	private final ModelRenderer cube_r160;
	private final ModelRenderer cube_r161;
	private final ModelRenderer cube_r162;
	private final ModelRenderer cube_r163;
	private final ModelRenderer bone31;
	private final ModelRenderer cube_r164;
	private final ModelRenderer cube_r165;
	private final ModelRenderer cube_r166;
	private final ModelRenderer cube_r167;
	private final ModelRenderer cube_r168;
	private final ModelRenderer cube_r169;
	private final ModelRenderer cube_r170;
	private final ModelRenderer bone30;
	private final ModelRenderer cube_r171;
	private final ModelRenderer cube_r172;
	private final ModelRenderer cube_r173;
	private final ModelRenderer cube_r174;
	private final ModelRenderer bone32;
	private final ModelRenderer cube_r175;
	private final ModelRenderer cube_r176;
	private final ModelRenderer cube_r177;
	private final ModelRenderer cube_r178;
	private final ModelRenderer cube_r179;
	private final ModelRenderer cube_r180;
	private final ModelRenderer cube_r181;
	private final ModelRenderer cube_r182;
	private final ModelRenderer cube_r183;
	private final ModelRenderer cube_r184;
	private final ModelRenderer cube_r185;
	private final ModelRenderer bone34;
	private final ModelRenderer cube_r186;
	private final ModelRenderer cube_r187;
	private final ModelRenderer bone33;
	private final ModelRenderer cube_r188;
	private final ModelRenderer cube_r189;
	private final ModelRenderer bone35;
	private final ModelRenderer cube_r190;
	private final ModelRenderer cube_r191;
	private final ModelRenderer JS;
	private final ModelRenderer cube_r192;
	private final ModelRenderer cube_r193;
	private final ModelRenderer cube_r194;
	private final ModelRenderer cube_r195;
	private final ModelRenderer cube_r196;
	private final ModelRenderer cube_r197;
	private final ModelRenderer cube_r198;
	private final ModelRenderer cube_r199;
	private final ModelRenderer cube_r200;
	private final ModelRenderer cube_r201;
	private final ModelRenderer cube_r202;
	private final ModelRenderer cube_r203;
	private final ModelRenderer cube_r204;
	private final ModelRenderer cube_r205;
	private final ModelRenderer cube_r206;
	private final ModelRenderer cube_r207;
	private final ModelRenderer cube_r208;
	private final ModelRenderer cube_r209;
	private final ModelRenderer bone37;
	private final ModelRenderer cube_r210;
	private final ModelRenderer cube_r211;
	private final ModelRenderer cube_r212;
	private final ModelRenderer cube_r213;
	private final ModelRenderer cube_r214;
	private final ModelRenderer cube_r215;
	private final ModelRenderer cube_r216;
	private final ModelRenderer cube_r217;
	private final ModelRenderer cube_r218;
	private final ModelRenderer cube_r219;
	private final ModelRenderer cube_r220;
	private final ModelRenderer cube_r221;
	private final ModelRenderer cube_r222;
	private final ModelRenderer cube_r223;
	private final ModelRenderer cube_r224;
	private final ModelRenderer cube_r225;
	private final ModelRenderer cube_r226;
	private final ModelRenderer cube_r227;
	private final ModelRenderer bone38;
	private final ModelRenderer cube_r228;
	private final ModelRenderer cube_r229;
	private final ModelRenderer cube_r230;
	private final ModelRenderer cube_r231;
	private final ModelRenderer cube_r232;
	private final ModelRenderer cube_r233;
	private final ModelRenderer cube_r234;
	private final ModelRenderer cube_r235;
	private final ModelRenderer cube_r236;
	private final ModelRenderer cube_r237;
	private final ModelRenderer cube_r238;
	private final ModelRenderer cube_r239;
	private final ModelRenderer cube_r240;
	private final ModelRenderer cube_r241;
	private final ModelRenderer cube_r242;
	private final ModelRenderer cube_r243;
	private final ModelRenderer cube_r244;
	private final ModelRenderer cube_r245;
	private final ModelRenderer cube_r246;
	private final ModelRenderer cube_r247;
	private final ModelRenderer cube_r248;
	private final ModelRenderer cube_r249;
	private final ModelRenderer bone39;
	private final ModelRenderer cube_r250;
	private final ModelRenderer cube_r251;
	private final ModelRenderer cube_r252;
	private final ModelRenderer cube_r253;
	private final ModelRenderer cube_r254;
	private final ModelRenderer cube_r255;
	private final ModelRenderer cube_r256;
	private final ModelRenderer cube_r257;
	private final ModelRenderer cube_r258;
	private final ModelRenderer cube_r259;
	private final ModelRenderer cube_r260;
	private final ModelRenderer cube_r261;
	private final ModelRenderer cube_r262;
	private final ModelRenderer cube_r263;
	private final ModelRenderer cube_r264;
	private final ModelRenderer cube_r265;
	private final ModelRenderer cube_r266;
	private final ModelRenderer cube_r267;
	private final ModelRenderer bone40;
	private final ModelRenderer cube_r268;
	private final ModelRenderer cube_r269;
	private final ModelRenderer cube_r270;
	private final ModelRenderer cube_r271;
	private final ModelRenderer cube_r272;
	private final ModelRenderer cube_r273;
	private final ModelRenderer cube_r274;
	private final ModelRenderer cube_r275;
	private final ModelRenderer cube_r276;
	private final ModelRenderer cube_r277;
	private final ModelRenderer cube_r278;
	private final ModelRenderer cube_r279;
	private final ModelRenderer cube_r280;
	private final ModelRenderer cube_r281;
	private final ModelRenderer cube_r282;
	private final ModelRenderer cube_r283;
	private final ModelRenderer cube_r284;
	private final ModelRenderer cube_r285;
	private final ModelRenderer cube_r286;
	private final ModelRenderer cube_r287;
	private final ModelRenderer cube_r288;
	private final ModelRenderer cube_r289;
	private final ModelRenderer bone36;
	private final ModelRenderer cube_r290;
	private final ModelRenderer cube_r291;
	private final ModelRenderer cube_r292;
	private final ModelRenderer cube_r293;
	private final ModelRenderer cube_r294;
	private final ModelRenderer cube_r295;
	private final ModelRenderer cube_r296;
	private final ModelRenderer cube_r297;
	private final ModelRenderer cube_r298;
	private final ModelRenderer cube_r299;
	private final ModelRenderer cube_r300;
	private final ModelRenderer cube_r301;
	private final ModelRenderer cube_r302;
	private final ModelRenderer cube_r303;
	private final ModelRenderer cube_r304;
	private final ModelRenderer cube_r305;
	private final ModelRenderer cube_r306;
	private final ModelRenderer bone11;
	private final ModelRenderer cube_r307;
	private final ModelRenderer cube_r308;
	private final ModelRenderer cube_r309;
	private final ModelRenderer cube_r310;
	private final ModelRenderer cube_r311;
	private final ModelRenderer cube_r312;
	private final ModelRenderer cube_r313;
	private final ModelRenderer cube_r314;
	private final ModelRenderer cube_r315;
	private final ModelRenderer cube_r316;
	private final ModelRenderer cube_r317;
	private final ModelRenderer cube_r318;
	private final ModelRenderer cube_r319;
	private final ModelRenderer bone19;
	private final ModelRenderer cube_r320;
	private final ModelRenderer cube_r321;
	private final ModelRenderer cube_r322;
	private final ModelRenderer cube_r323;
	private final ModelRenderer cube_r324;
	private final ModelRenderer cube_r325;
	private final ModelRenderer cube_r326;
	private final ModelRenderer Left_Arm;
	private final ModelRenderer cube_r327;
	private final ModelRenderer bone12;
	private final ModelRenderer cube_r328;
	private final ModelRenderer cube_r329;
	private final ModelRenderer cube_r330;
	private final ModelRenderer cube_r331;
	private final ModelRenderer bone13;
	private final ModelRenderer cube_r332;
	private final ModelRenderer cube_r333;
	private final ModelRenderer cube_r334;
	private final ModelRenderer cube_r335;
	private final ModelRenderer cube_r336;
	private final ModelRenderer bone42;
	private final ModelRenderer cube_r337;
	private final ModelRenderer cube_r338;
	private final ModelRenderer Right_Arm;
	private final ModelRenderer bone14;
	private final ModelRenderer bone17;
	private final ModelRenderer cube_r339;
	private final ModelRenderer cube_r340;
	private final ModelRenderer cube_r341;
	private final ModelRenderer bone15;
	private final ModelRenderer cube_r342;
	private final ModelRenderer cube_r343;
	private final ModelRenderer cube_r344;
	private final ModelRenderer bone16;
	private final ModelRenderer bone18;
	private final ModelRenderer cube_r345;
	private final ModelRenderer cube_r346;
	private final ModelRenderer cube_r347;
	private final ModelRenderer cube_r348;
	private final ModelRenderer cube_r349;
	private final ModelRenderer bone41;
	private final ModelRenderer cube_r350;
	private final ModelRenderer cube_r351;
	private final ModelRenderer cube_r352;
	private final ModelRenderer Right_leg;
	private final ModelRenderer cube_r353;
	private final ModelRenderer cube_r354;
	private final ModelRenderer bone20;
	private final ModelRenderer cube_r355;
	private final ModelRenderer cube_r356;
	private final ModelRenderer cube_r357;
	private final ModelRenderer cube_r358;
	private final ModelRenderer bone21;
	private final ModelRenderer bone22;
	private final ModelRenderer cube_r359;
	private final ModelRenderer cube_r360;
	private final ModelRenderer cube_r361;
	private final ModelRenderer cube_r362;
	private final ModelRenderer bone23;
	private final ModelRenderer Left_Leg;
	private final ModelRenderer bone25;
	private final ModelRenderer bone26;
	private final ModelRenderer cube_r363;
	private final ModelRenderer cube_r364;
	private final ModelRenderer cube_r365;
	private final ModelRenderer cube_r366;
	private final ModelRenderer bone24;
	private final ModelRenderer cube_r367;
	private final ModelRenderer cube_r368;

	public CloakPilot() {
		textureWidth = 256;
		textureHeight = 256;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, -1.0345F, 4.5273F);
		Head.addChild(bone9);
		setRotationAngle(bone9, -0.0698F, 0.0F, 0.0F);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone9.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.3229F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(88, 79).addBox(-2.5F, -3.75F, 0.269F, 5.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-3.2177F, 0.1064F, 0.4698F);
		bone9.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.3498F, -0.3878F, -0.1371F);
		cube_r2.setTextureOffset(117, 35).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(-0.0463F, -0.1586F, 0.474F);
		bone9.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.8526F, -1.1358F, -0.804F);
		cube_r3.setTextureOffset(0, 44).addBox(-3.2807F, -4.75F, 3.0922F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(3.2177F, 0.1064F, 0.4698F);
		bone9.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.3498F, 0.3878F, 0.1371F);
		cube_r4.setTextureOffset(60, 117).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(0.0463F, -0.1586F, 0.474F);
		bone9.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.8526F, 1.1358F, 0.804F);
		cube_r5.setTextureOffset(48, 35).addBox(1.2807F, -4.75F, 3.0922F, 2.0F, 6.0F, 1.0F, 0.0F, false);

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(1.202F, -4.0884F, 4.0897F);
		Head.addChild(bone6);
		

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(-6.3323F, 1.8628F, -0.4331F);
		bone6.addChild(cube_r6);
		setRotationAngle(cube_r6, 1.5708F, 0.8029F, 1.5708F);
		cube_r6.setTextureOffset(129, 59).addBox(0.5F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(-6.3323F, 4.3188F, -7.0368F);
		bone6.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, 1.5708F, 0.0F);
		cube_r7.setTextureOffset(105, 14).addBox(-5.5F, -2.0F, -0.5F, 5.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(-1.702F, 1.8957F, 0.5929F);
		bone6.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.1396F, 0.0F, 0.0F);
		cube_r8.setTextureOffset(42, 124).addBox(-5.1302F, -1.0F, -1.9602F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(-1.702F, 1.8957F, 0.5929F);
		bone6.addChild(cube_r9);
		setRotationAngle(cube_r9, 0.1929F, -0.7586F, -0.1336F);
		cube_r9.setTextureOffset(121, 130).addBox(-3.6627F, -1.0F, 2.5924F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(-1.702F, 1.8957F, 0.5929F);
		bone6.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.1438F, -0.2419F, -0.0347F);
		cube_r10.setTextureOffset(70, 119).addBox(-4.1022F, -1.0F, 0.7797F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(3.9282F, 1.8628F, -0.4331F);
		bone6.addChild(cube_r11);
		setRotationAngle(cube_r11, 1.5708F, -0.8029F, -1.5708F);
		cube_r11.setTextureOffset(63, 129).addBox(-2.5F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(-5.0198F, 3.3188F, -8.3298F);
		bone6.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.0F, 0.9774F, 0.0F);
		cube_r12.setTextureOffset(107, 102).addBox(-2.5F, -1.0F, -0.5F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(-2.7696F, 3.3188F, -9.9247F);
		bone6.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.0F, 0.5585F, 0.0F);
		cube_r13.setTextureOffset(30, 117).addBox(-2.5F, -1.0F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(0.3655F, 3.3188F, -9.9247F);
		bone6.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.0F, -0.5585F, 0.0F);
		cube_r14.setTextureOffset(117, 66).addBox(0.5F, -1.0F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(2.6157F, 3.3188F, -8.3298F);
		bone6.addChild(cube_r15);
		setRotationAngle(cube_r15, 0.0F, -0.9774F, 0.0F);
		cube_r15.setTextureOffset(108, 53).addBox(-0.5F, -1.0F, -0.5F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r16 = new ModelRenderer(this);
		cube_r16.setRotationPoint(3.9282F, 4.3188F, -7.0368F);
		bone6.addChild(cube_r16);
		setRotationAngle(cube_r16, 0.0F, -1.5708F, 0.0F);
		cube_r16.setTextureOffset(105, 85).addBox(0.5F, -2.0F, -0.5F, 5.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r17 = new ModelRenderer(this);
		cube_r17.setRotationPoint(-0.702F, 1.8957F, 0.5929F);
		bone6.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.1396F, 0.0F, 0.0F);
		cube_r17.setTextureOffset(65, 125).addBox(4.1302F, -1.0F, -1.9602F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		cube_r17.setTextureOffset(109, 74).addBox(-2.5F, -1.0F, 0.4602F, 4.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r18 = new ModelRenderer(this);
		cube_r18.setRotationPoint(-0.702F, 1.8957F, 0.5929F);
		bone6.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.1929F, 0.7586F, 0.1336F);
		cube_r18.setTextureOffset(10, 131).addBox(2.6627F, -1.0F, 2.5924F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r19 = new ModelRenderer(this);
		cube_r19.setRotationPoint(-0.702F, 1.8957F, 0.5929F);
		bone6.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.1438F, 0.2419F, 0.0347F);
		cube_r19.setTextureOffset(119, 74).addBox(1.1022F, -1.0F, 0.7797F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, -1.375F, -0.3125F);
		Head.addChild(bone);
		bone.setTextureOffset(62, 112).addBox(-1.5F, -4.8125F, -6.375F, 3.0F, 4.0F, 1.0F, 0.0F, false);

		cube_r20 = new ModelRenderer(this);
		cube_r20.setRotationPoint(-2.4196F, -2.3125F, -5.0888F);
		bone.addChild(cube_r20);
		setRotationAngle(cube_r20, 0.0F, 0.6283F, 0.0F);
		cube_r20.setTextureOffset(40, 110).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 4.0F, 1.0F, 0.0F, false);

		cube_r21 = new ModelRenderer(this);
		cube_r21.setRotationPoint(2.4196F, -2.3125F, -5.0888F);
		bone.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.0F, -0.6283F, 0.0F);
		cube_r21.setTextureOffset(31, 112).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 4.0F, 1.0F, 0.0F, false);

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-0.0172F, 1.9174F, -3.6988F);
		bone.addChild(bone4);
		bone4.setTextureOffset(126, 10).addBox(-1.5F, -2.2299F, -3.1137F, 3.0F, 1.0F, 1.0F, 0.125F, false);
		bone4.setTextureOffset(120, 4).addBox(-1.5F, -2.346F, -2.2299F, 3.0F, 2.0F, 1.0F, 0.125F, false);

		cube_r22 = new ModelRenderer(this);
		cube_r22.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone4.addChild(cube_r22);
		setRotationAngle(cube_r22, -0.7854F, 0.0F, 0.0F);
		cube_r22.setTextureOffset(120, 97).addBox(-1.5F, -1.5F, -2.0625F, 3.0F, 2.0F, 1.0F, 0.125F, false);

		cube_r23 = new ModelRenderer(this);
		cube_r23.setRotationPoint(0.0F, -1.1049F, -2.3549F);
		bone4.addChild(cube_r23);
		setRotationAngle(cube_r23, -0.7854F, 0.0F, 0.0F);
		cube_r23.setTextureOffset(96, 125).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.125F, false);

		cube_r24 = new ModelRenderer(this);
		cube_r24.setRotationPoint(-2.6423F, -1.7299F, -1.6741F);
		bone4.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, -1.0996F, 0.0F);
		cube_r24.setTextureOffset(58, 0).addBox(-0.4219F, -1.0F, -0.4375F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r25 = new ModelRenderer(this);
		cube_r25.setRotationPoint(-1.7157F, -1.7299F, -1.7075F);
		bone4.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.0F, -0.4363F, 0.0F);
		cube_r25.setTextureOffset(28, 51).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r26 = new ModelRenderer(this);
		cube_r26.setRotationPoint(2.6767F, -1.7299F, -1.6741F);
		bone4.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.0F, 1.0996F, 0.0F);
		cube_r26.setTextureOffset(86, 125).addBox(-0.5781F, -1.0F, -0.4375F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r27 = new ModelRenderer(this);
		cube_r27.setRotationPoint(1.75F, -1.7299F, -1.7075F);
		bone4.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.0F, 0.4363F, 0.0F);
		cube_r27.setTextureOffset(92, 125).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r28 = new ModelRenderer(this);
		cube_r28.setRotationPoint(0.0F, -2.3549F, -2.3549F);
		bone4.addChild(cube_r28);
		setRotationAngle(cube_r28, -0.7854F, 0.0F, 0.0F);
		cube_r28.setTextureOffset(125, 114).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.125F, false);

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(2.7036F, -5.1679F, -2.8664F);
		bone.addChild(bone2);
		

		cube_r29 = new ModelRenderer(this);
		cube_r29.setRotationPoint(-6.8295F, 2.2822F, -2.1496F);
		bone2.addChild(cube_r29);
		setRotationAngle(cube_r29, 0.0F, 0.8029F, 0.0F);
		cube_r29.setTextureOffset(129, 2).addBox(-0.5F, 0.7071F, -0.4844F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r30 = new ModelRenderer(this);
		cube_r30.setRotationPoint(-6.7396F, 2.1054F, -2.0628F);
		bone2.addChild(cube_r30);
		setRotationAngle(cube_r30, -0.632F, 0.5336F, -0.9637F);
		cube_r30.setTextureOffset(42, 128).addBox(-0.8536F, 0.5607F, -0.4844F, 1.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r31 = new ModelRenderer(this);
		cube_r31.setRotationPoint(-6.7036F, 2.9804F, -2.1F);
		bone2.addChild(cube_r31);
		setRotationAngle(cube_r31, 0.0F, 0.8029F, 0.0F);
		cube_r31.setTextureOffset(8, 119).addBox(-0.7929F, -1.0F, -0.5F, 2.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r32 = new ModelRenderer(this);
		cube_r32.setRotationPoint(-6.7508F, 2.8554F, -2.0736F);
		bone2.addChild(cube_r32);
		setRotationAngle(cube_r32, -0.632F, 0.5336F, -0.9637F);
		cube_r32.setTextureOffset(17, 128).addBox(0.5607F, -0.8536F, -0.4687F, 1.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r33 = new ModelRenderer(this);
		cube_r33.setRotationPoint(-6.8407F, 2.6786F, -2.1605F);
		bone2.addChild(cube_r33);
		setRotationAngle(cube_r33, 0.0F, 0.8029F, 0.0F);
		cube_r33.setTextureOffset(42, 131).addBox(-0.5F, -1.7071F, -0.4688F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r34 = new ModelRenderer(this);
		cube_r34.setRotationPoint(-8.1291F, 4.7736F, 4.8605F);
		bone2.addChild(cube_r34);
		setRotationAngle(cube_r34, 1.5708F, 0.6458F, 1.5708F);
		cube_r34.setTextureOffset(117, 18).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 3.0F, 1.0F, -0.125F, false);

		cube_r35 = new ModelRenderer(this);
		cube_r35.setRotationPoint(-8.1291F, 5.5233F, 1.3569F);
		bone2.addChild(cube_r35);
		setRotationAngle(cube_r35, 0.0F, 1.5708F, 0.0F);
		cube_r35.setTextureOffset(101, 71).addBox(-3.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r36 = new ModelRenderer(this);
		cube_r36.setRotationPoint(-8.3166F, 4.8983F, 1.3569F);
		bone2.addChild(cube_r36);
		setRotationAngle(cube_r36, 0.0F, 1.5708F, 0.0F);
		cube_r36.setTextureOffset(94, 35).addBox(-3.5F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, -0.125F, false);

		cube_r37 = new ModelRenderer(this);
		cube_r37.setRotationPoint(-7.5685F, 4.8983F, -0.8955F);
		bone2.addChild(cube_r37);
		setRotationAngle(cube_r37, 0.0F, 0.9294F, 0.0F);
		cube_r37.setTextureOffset(96, 47).addBox(-1.5F, -2.3125F, -0.5F, 3.0F, 4.0F, 2.0F, -0.125F, false);

		cube_r38 = new ModelRenderer(this);
		cube_r38.setRotationPoint(-6.1988F, 1.1109F, 8.0101F);
		bone2.addChild(cube_r38);
		setRotationAngle(cube_r38, 2.8355F, 0.1856F, 3.0996F);
		cube_r38.setTextureOffset(109, 57).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r39 = new ModelRenderer(this);
		cube_r39.setRotationPoint(-6.0202F, 2.3937F, 7.3466F);
		bone2.addChild(cube_r39);
		setRotationAngle(cube_r39, -2.5052F, 0.1856F, 3.0996F);
		cube_r39.setTextureOffset(36, 93).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 2.0F, 0.0625F, false);

		cube_r40 = new ModelRenderer(this);
		cube_r40.setRotationPoint(-7.8193F, 1.1105F, 7.9264F);
		bone2.addChild(cube_r40);
		setRotationAngle(cube_r40, 2.377F, 1.1287F, 2.4436F);
		cube_r40.setTextureOffset(129, 27).addBox(0.0F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r41 = new ModelRenderer(this);
		cube_r41.setRotationPoint(-7.767F, 4.4671F, 6.0746F);
		bone2.addChild(cube_r41);
		setRotationAngle(cube_r41, 1.902F, 1.2653F, 1.4038F);
		cube_r41.setTextureOffset(0, 119).addBox(-1.0F, -1.5F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r42 = new ModelRenderer(this);
		cube_r42.setRotationPoint(-8.4118F, 1.9302F, 5.252F);
		bone2.addChild(cube_r42);
		setRotationAngle(cube_r42, 1.3959F, 1.2653F, 1.4038F);
		cube_r42.setTextureOffset(16, 117).addBox(-1.0F, -1.5F, -0.5F, 3.0F, 3.0F, 1.0F, 0.0625F, false);

		cube_r43 = new ModelRenderer(this);
		cube_r43.setRotationPoint(-8.1727F, 2.4715F, 0.6894F);
		bone2.addChild(cube_r43);
		setRotationAngle(cube_r43, 0.0F, 1.5184F, 0.0F);
		cube_r43.setTextureOffset(107, 0).addBox(-3.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0625F, false);

		cube_r44 = new ModelRenderer(this);
		cube_r44.setRotationPoint(-7.8317F, 2.4715F, -0.84F);
		bone2.addChild(cube_r44);
		setRotationAngle(cube_r44, 0.0F, 1.0297F, 0.0F);
		cube_r44.setTextureOffset(125, 0).addBox(-0.75F, -1.5F, -0.4375F, 2.0F, 3.0F, 1.0F, 0.0625F, false);

		cube_r45 = new ModelRenderer(this);
		cube_r45.setRotationPoint(1.411F, 2.2822F, -2.1388F);
		bone2.addChild(cube_r45);
		setRotationAngle(cube_r45, 0.0F, -0.8029F, 0.0F);
		cube_r45.setTextureOffset(129, 80).addBox(-0.5F, 0.7071F, -0.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r46 = new ModelRenderer(this);
		cube_r46.setRotationPoint(1.3323F, 2.1054F, -2.0628F);
		bone2.addChild(cube_r46);
		setRotationAngle(cube_r46, -0.632F, -0.5336F, 0.9637F);
		cube_r46.setTextureOffset(37, 129).addBox(-0.1464F, 0.5607F, -0.4844F, 1.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r47 = new ModelRenderer(this);
		cube_r47.setRotationPoint(1.2963F, 2.9804F, -2.1F);
		bone2.addChild(cube_r47);
		setRotationAngle(cube_r47, 0.0F, -0.8029F, 0.0F);
		cube_r47.setTextureOffset(120, 10).addBox(-1.2071F, -1.0F, -0.5F, 2.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r48 = new ModelRenderer(this);
		cube_r48.setRotationPoint(1.3323F, 2.8554F, -2.0628F);
		bone2.addChild(cube_r48);
		setRotationAngle(cube_r48, -0.632F, -0.5336F, 0.9637F);
		cube_r48.setTextureOffset(129, 77).addBox(-1.5607F, -0.8536F, -0.4844F, 1.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r49 = new ModelRenderer(this);
		cube_r49.setRotationPoint(1.411F, 2.6786F, -2.1388F);
		bone2.addChild(cube_r49);
		setRotationAngle(cube_r49, 0.0F, -0.8029F, 0.0F);
		cube_r49.setTextureOffset(131, 65).addBox(-0.5F, -1.7071F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r50 = new ModelRenderer(this);
		cube_r50.setRotationPoint(2.7219F, 4.7736F, 4.8605F);
		bone2.addChild(cube_r50);
		setRotationAngle(cube_r50, 1.5708F, -0.6458F, -1.5708F);
		cube_r50.setTextureOffset(80, 117).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 3.0F, 1.0F, -0.125F, false);

		cube_r51 = new ModelRenderer(this);
		cube_r51.setRotationPoint(2.7219F, 5.5233F, 1.3569F);
		bone2.addChild(cube_r51);
		setRotationAngle(cube_r51, 0.0F, -1.5708F, 0.0F);
		cube_r51.setTextureOffset(102, 62).addBox(-1.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r52 = new ModelRenderer(this);
		cube_r52.setRotationPoint(2.9094F, 4.8983F, 1.3569F);
		bone2.addChild(cube_r52);
		setRotationAngle(cube_r52, 0.0F, -1.5708F, 0.0F);
		cube_r52.setTextureOffset(95, 0).addBox(-1.5F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, -0.125F, false);

		cube_r53 = new ModelRenderer(this);
		cube_r53.setRotationPoint(2.1612F, 4.8983F, -0.8955F);
		bone2.addChild(cube_r53);
		setRotationAngle(cube_r53, 0.0F, -0.9294F, 0.0F);
		cube_r53.setTextureOffset(98, 53).addBox(-1.5F, -2.3125F, -0.5F, 3.0F, 4.0F, 2.0F, -0.125F, false);

		cube_r54 = new ModelRenderer(this);
		cube_r54.setRotationPoint(0.7916F, 1.1109F, 8.0101F);
		bone2.addChild(cube_r54);
		setRotationAngle(cube_r54, 2.8355F, -0.1856F, -3.0996F);
		cube_r54.setTextureOffset(111, 9).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r55 = new ModelRenderer(this);
		cube_r55.setRotationPoint(0.613F, 2.3937F, 7.3466F);
		bone2.addChild(cube_r55);
		setRotationAngle(cube_r55, -2.5052F, -0.1856F, -3.0996F);
		cube_r55.setTextureOffset(94, 30).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 2.0F, 0.0625F, false);

		cube_r56 = new ModelRenderer(this);
		cube_r56.setRotationPoint(2.412F, 1.1105F, 7.9264F);
		bone2.addChild(cube_r56);
		setRotationAngle(cube_r56, 2.377F, -1.1287F, -2.4436F);
		cube_r56.setTextureOffset(74, 129).addBox(-2.0F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r57 = new ModelRenderer(this);
		cube_r57.setRotationPoint(2.3597F, 4.4671F, 6.0746F);
		bone2.addChild(cube_r57);
		setRotationAngle(cube_r57, 1.902F, -1.2653F, -1.4038F);
		cube_r57.setTextureOffset(119, 62).addBox(-2.0F, -1.5F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r58 = new ModelRenderer(this);
		cube_r58.setRotationPoint(3.0045F, 1.9302F, 5.252F);
		bone2.addChild(cube_r58);
		setRotationAngle(cube_r58, 1.3959F, -1.2653F, -1.4038F);
		cube_r58.setTextureOffset(88, 117).addBox(-2.0F, -1.5F, -0.5F, 3.0F, 3.0F, 1.0F, 0.0625F, false);

		cube_r59 = new ModelRenderer(this);
		cube_r59.setRotationPoint(-8.4354F, 2.0965F, 0.9259F);
		bone2.addChild(cube_r59);
		setRotationAngle(cube_r59, 1.0317F, 1.3673F, 1.0317F);
		cube_r59.setTextureOffset(123, 90).addBox(-1.5F, -0.5F, -0.6875F, 3.0F, 1.0F, 1.0F, 0.0625F, false);

		cube_r60 = new ModelRenderer(this);
		cube_r60.setRotationPoint(3.0282F, 2.0965F, 0.9259F);
		bone2.addChild(cube_r60);
		setRotationAngle(cube_r60, 1.0317F, -1.3673F, -1.0317F);
		cube_r60.setTextureOffset(123, 112).addBox(-1.5F, -0.5F, -0.6875F, 3.0F, 1.0F, 1.0F, 0.0625F, false);

		cube_r61 = new ModelRenderer(this);
		cube_r61.setRotationPoint(2.7654F, 2.4715F, 0.6894F);
		bone2.addChild(cube_r61);
		setRotationAngle(cube_r61, 0.0F, -1.5184F, 0.0F);
		cube_r61.setTextureOffset(62, 108).addBox(-1.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 0.0625F, false);

		cube_r62 = new ModelRenderer(this);
		cube_r62.setRotationPoint(2.4245F, 2.4715F, -0.84F);
		bone2.addChild(cube_r62);
		setRotationAngle(cube_r62, 0.0F, -1.0297F, 0.0F);
		cube_r62.setTextureOffset(0, 126).addBox(-1.25F, -1.5F, -0.4375F, 2.0F, 3.0F, 1.0F, 0.0625F, false);

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(2.0F, 0.0F, -0.1875F);
		bone.addChild(bone5);
		bone5.setTextureOffset(119, 120).addBox(-3.5F, -5.6875F, -6.75F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r63 = new ModelRenderer(this);
		cube_r63.setRotationPoint(-2.0F, -3.75F, -5.9375F);
		bone5.addChild(cube_r63);
		setRotationAngle(cube_r63, 0.0524F, 0.0F, 0.0F);
		cube_r63.setTextureOffset(80, 129).addBox(-1.0F, -1.5625F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r64 = new ModelRenderer(this);
		cube_r64.setRotationPoint(-6.0636F, -4.6875F, -3.7156F);
		bone5.addChild(cube_r64);
		setRotationAngle(cube_r64, 0.0F, 1.2872F, 0.0F);
		cube_r64.setTextureOffset(119, 57).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r65 = new ModelRenderer(this);
		cube_r65.setRotationPoint(-4.5695F, -4.1875F, -5.5855F);
		bone5.addChild(cube_r65);
		setRotationAngle(cube_r65, 0.0F, 0.5061F, 0.0F);
		cube_r65.setTextureOffset(38, 119).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r66 = new ModelRenderer(this);
		cube_r66.setRotationPoint(-4.4585F, -6.0076F, -5.1756F);
		bone5.addChild(cube_r66);
		setRotationAngle(cube_r66, -0.5524F, 0.5253F, -0.2998F);
		cube_r66.setTextureOffset(119, 22).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r67 = new ModelRenderer(this);
		cube_r67.setRotationPoint(-5.6898F, -6.1515F, -3.3712F);
		bone5.addChild(cube_r67);
		setRotationAngle(cube_r67, -1.1033F, 0.5635F, -0.8136F);
		cube_r67.setTextureOffset(117, 130).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r68 = new ModelRenderer(this);
		cube_r68.setRotationPoint(-6.1194F, -5.097F, -3.3883F);
		bone5.addChild(cube_r68);
		setRotationAngle(cube_r68, -0.9068F, 0.9564F, -0.807F);
		cube_r68.setTextureOffset(52, 129).addBox(-0.5F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r69 = new ModelRenderer(this);
		cube_r69.setRotationPoint(-4.3335F, -7.1116F, -4.2058F);
		bone5.addChild(cube_r69);
		setRotationAngle(cube_r69, -0.9816F, 0.4329F, -0.5605F);
		cube_r69.setTextureOffset(119, 14).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r70 = new ModelRenderer(this);
		cube_r70.setRotationPoint(-6.4112F, -5.5293F, -2.5486F);
		bone5.addChild(cube_r70);
		setRotationAngle(cube_r70, -1.3868F, 0.3188F, -1.035F);
		cube_r70.setTextureOffset(97, 130).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r71 = new ModelRenderer(this);
		cube_r71.setRotationPoint(-4.7071F, -7.6405F, -3.359F);
		bone5.addChild(cube_r71);
		setRotationAngle(cube_r71, -1.3057F, 0.2562F, -0.7511F);
		cube_r71.setTextureOffset(124, 69).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r72 = new ModelRenderer(this);
		cube_r72.setRotationPoint(-6.5481F, -5.375F, -3.7724F);
		bone5.addChild(cube_r72);
		setRotationAngle(cube_r72, 0.0F, 0.9774F, 0.0F);
		cube_r72.setTextureOffset(124, 46).addBox(-1.5F, 0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r73 = new ModelRenderer(this);
		cube_r73.setRotationPoint(-6.2994F, -4.8518F, -3.6046F);
		bone5.addChild(cube_r73);
		setRotationAngle(cube_r73, -0.925F, 0.9774F, 0.0F);
		cube_r73.setTextureOffset(124, 17).addBox(-1.5F, -0.375F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r74 = new ModelRenderer(this);
		cube_r74.setRotationPoint(1.6898F, -6.1515F, -3.3712F);
		bone5.addChild(cube_r74);
		setRotationAngle(cube_r74, -1.1033F, -0.5635F, 0.8136F);
		cube_r74.setTextureOffset(14, 131).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r75 = new ModelRenderer(this);
		cube_r75.setRotationPoint(0.3335F, -7.1116F, -4.2058F);
		bone5.addChild(cube_r75);
		setRotationAngle(cube_r75, -0.9816F, -0.4329F, 0.5605F);
		cube_r75.setTextureOffset(15, 121).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r76 = new ModelRenderer(this);
		cube_r76.setRotationPoint(2.0636F, -4.6875F, -3.7156F);
		bone5.addChild(cube_r76);
		setRotationAngle(cube_r76, 0.0F, -1.2872F, 0.0F);
		cube_r76.setTextureOffset(119, 102).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r77 = new ModelRenderer(this);
		cube_r77.setRotationPoint(2.2994F, -4.8518F, -3.6046F);
		bone5.addChild(cube_r77);
		setRotationAngle(cube_r77, -0.925F, -0.9774F, 0.0F);
		cube_r77.setTextureOffset(125, 19).addBox(-1.5F, -0.375F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r78 = new ModelRenderer(this);
		cube_r78.setRotationPoint(2.5481F, -5.375F, -3.7724F);
		bone5.addChild(cube_r78);
		setRotationAngle(cube_r78, 0.0F, -0.9774F, 0.0F);
		cube_r78.setTextureOffset(125, 67).addBox(-1.5F, 0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r79 = new ModelRenderer(this);
		cube_r79.setRotationPoint(0.5695F, -4.1875F, -5.5855F);
		bone5.addChild(cube_r79);
		setRotationAngle(cube_r79, 0.0F, -0.5061F, 0.0F);
		cube_r79.setTextureOffset(111, 120).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r80 = new ModelRenderer(this);
		cube_r80.setRotationPoint(-1.5F, -6.3515F, -5.8505F);
		bone5.addChild(cube_r80);
		setRotationAngle(cube_r80, -0.4712F, 0.0F, 0.0F);
		cube_r80.setTextureOffset(119, 85).addBox(-2.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r81 = new ModelRenderer(this);
		cube_r81.setRotationPoint(2.1194F, -5.097F, -3.3883F);
		bone5.addChild(cube_r81);
		setRotationAngle(cube_r81, -0.9068F, -0.9564F, 0.807F);
		cube_r81.setTextureOffset(129, 74).addBox(-1.5F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r82 = new ModelRenderer(this);
		cube_r82.setRotationPoint(0.4585F, -6.0076F, -5.1756F);
		bone5.addChild(cube_r82);
		setRotationAngle(cube_r82, -0.5524F, -0.5253F, 0.2998F);
		cube_r82.setTextureOffset(30, 121).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r83 = new ModelRenderer(this);
		cube_r83.setRotationPoint(2.4112F, -5.5293F, -2.5486F);
		bone5.addChild(cube_r83);
		setRotationAngle(cube_r83, -1.3868F, -0.3188F, 1.035F);
		cube_r83.setTextureOffset(103, 130).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r84 = new ModelRenderer(this);
		cube_r84.setRotationPoint(0.7071F, -7.6405F, -3.359F);
		bone5.addChild(cube_r84);
		setRotationAngle(cube_r84, -1.3057F, -0.2562F, 0.7511F);
		cube_r84.setTextureOffset(125, 72).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r85 = new ModelRenderer(this);
		cube_r85.setRotationPoint(-1.5F, -8.8524F, -2.753F);
		bone5.addChild(cube_r85);
		setRotationAngle(cube_r85, -1.2043F, 0.0F, 0.0F);
		cube_r85.setTextureOffset(116, 30).addBox(-2.5F, 0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r86 = new ModelRenderer(this);
		cube_r86.setRotationPoint(-1.0F, -8.0762F, -4.3819F);
		bone5.addChild(cube_r86);
		setRotationAngle(cube_r86, -0.8552F, 0.0F, 0.0F);
		cube_r86.setTextureOffset(121, 25).addBox(-2.5F, -0.5F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.25F, 0.875F);
		Head.addChild(bone3);
		bone3.setTextureOffset(9, 37).addBox(-2.0F, -11.0625F, -4.5625F, 4.0F, 1.0F, 7.0F, 0.0F, false);

		cube_r87 = new ModelRenderer(this);
		cube_r87.setRotationPoint(-1.817F, -10.3795F, -1.75F);
		bone3.addChild(cube_r87);
		setRotationAngle(cube_r87, 0.0F, 0.0F, -1.0472F);
		cube_r87.setTextureOffset(48, 35).addBox(-1.5F, -0.5F, -2.8125F, 2.0F, 1.0F, 7.0F, 0.0F, false);

		cube_r88 = new ModelRenderer(this);
		cube_r88.setRotationPoint(1.817F, -10.3795F, -1.75F);
		bone3.addChild(cube_r88);
		setRotationAngle(cube_r88, 0.0F, 0.0F, 1.0472F);
		cube_r88.setTextureOffset(27, 49).addBox(-0.5F, -0.5F, -2.8125F, 2.0F, 1.0F, 7.0F, 0.0F, false);

		cube_r89 = new ModelRenderer(this);
		cube_r89.setRotationPoint(-0.5F, -6.5662F, 3.3988F);
		bone3.addChild(cube_r89);
		setRotationAngle(cube_r89, -0.1047F, 0.0F, 0.0F);
		cube_r89.setTextureOffset(123, 118).addBox(-1.0F, 1.5F, 0.9375F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r90 = new ModelRenderer(this);
		cube_r90.setRotationPoint(0.0F, -7.1351F, 3.7103F);
		bone3.addChild(cube_r90);
		setRotationAngle(cube_r90, 0.192F, 0.0F, 0.0F);
		cube_r90.setTextureOffset(90, 41).addBox(-2.0F, -1.5F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r91 = new ModelRenderer(this);
		cube_r91.setRotationPoint(-2.5313F, -7.1008F, 2.0749F);
		bone3.addChild(cube_r91);
		setRotationAngle(cube_r91, -0.4276F, 0.0F, 0.0F);
		cube_r91.setTextureOffset(80, 110).addBox(-2.0F, -1.1875F, -0.8906F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r91.setTextureOffset(111, 95).addBox(3.0625F, -1.1875F, -0.8906F, 4.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r92 = new ModelRenderer(this);
		cube_r92.setRotationPoint(-1.5313F, -7.7924F, 1.7877F);
		bone3.addChild(cube_r92);
		setRotationAngle(cube_r92, -1.0821F, 0.0F, 0.0F);
		cube_r92.setTextureOffset(74, 125).addBox(-3.0F, -0.9375F, 0.1094F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		cube_r92.setTextureOffset(80, 125).addBox(5.0625F, -0.9375F, 0.1094F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r93 = new ModelRenderer(this);
		cube_r93.setRotationPoint(-1.0625F, -8.7924F, 1.7877F);
		bone3.addChild(cube_r93);
		setRotationAngle(cube_r93, -1.0821F, 0.0F, 0.0F);
		cube_r93.setTextureOffset(48, 110).addBox(-3.0F, -0.9375F, 0.1094F, 1.0F, 2.0F, 3.0F, 0.0F, false);
		cube_r93.setTextureOffset(19, 112).addBox(4.125F, -0.9375F, 0.1094F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r94 = new ModelRenderer(this);
		cube_r94.setRotationPoint(-2.0625F, -8.1008F, 2.0749F);
		bone3.addChild(cube_r94);
		setRotationAngle(cube_r94, -0.4276F, 0.0F, 0.0F);
		cube_r94.setTextureOffset(110, 4).addBox(-2.0F, -1.1875F, -0.8906F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r94.setTextureOffset(112, 32).addBox(2.125F, -1.1875F, -0.8906F, 4.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r95 = new ModelRenderer(this);
		cube_r95.setRotationPoint(-1.25F, -8.6636F, 2.0761F);
		bone3.addChild(cube_r95);
		setRotationAngle(cube_r95, -0.4276F, 0.0F, 0.0F);
		cube_r95.setTextureOffset(109, 79).addBox(-2.0F, -1.1875F, -0.8906F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r95.setTextureOffset(112, 41).addBox(0.5F, -1.1875F, -0.8906F, 4.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r96 = new ModelRenderer(this);
		cube_r96.setRotationPoint(-1.25F, -8.3549F, 2.0377F);
		bone3.addChild(cube_r96);
		setRotationAngle(cube_r96, -1.0821F, 0.0F, 0.0F);
		cube_r96.setTextureOffset(76, 19).addBox(-2.0F, -1.1875F, -0.8906F, 4.0F, 2.0F, 3.0F, 0.0F, false);
		cube_r96.setTextureOffset(8, 81).addBox(0.5F, -1.1875F, -0.8906F, 4.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r97 = new ModelRenderer(this);
		cube_r97.setRotationPoint(0.0F, -9.2251F, 3.3723F);
		bone3.addChild(cube_r97);
		setRotationAngle(cube_r97, -0.8552F, 0.0F, 0.0F);
		cube_r97.setTextureOffset(50, 82).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r98 = new ModelRenderer(this);
		cube_r98.setRotationPoint(-4.9653F, -5.5168F, -1.1875F);
		bone3.addChild(cube_r98);
		setRotationAngle(cube_r98, 0.0F, 0.0F, 0.1222F);
		cube_r98.setTextureOffset(0, 61).addBox(-1.0F, -0.875F, -3.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

		cube_r99 = new ModelRenderer(this);
		cube_r99.setRotationPoint(-3.8534F, -5.7829F, -1.1875F);
		bone3.addChild(cube_r99);
		setRotationAngle(cube_r99, 0.0F, 0.0F, -0.733F);
		cube_r99.setTextureOffset(59, 24).addBox(-1.0F, -1.875F, -3.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

		cube_r100 = new ModelRenderer(this);
		cube_r100.setRotationPoint(-3.0518F, -8.2708F, -1.1875F);
		bone3.addChild(cube_r100);
		setRotationAngle(cube_r100, 0.0F, 0.0F, -1.1126F);
		cube_r100.setTextureOffset(42, 43).addBox(-3.0F, -1.5F, -3.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);

		cube_r101 = new ModelRenderer(this);
		cube_r101.setRotationPoint(-2.4121F, -8.4528F, -1.1875F);
		bone3.addChild(cube_r101);
		setRotationAngle(cube_r101, 0.0F, 0.0F, -0.4625F);
		cube_r101.setTextureOffset(30, 57).addBox(-0.7656F, -1.9219F, -3.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);

		cube_r102 = new ModelRenderer(this);
		cube_r102.setRotationPoint(3.8534F, -5.7829F, -1.1875F);
		bone3.addChild(cube_r102);
		setRotationAngle(cube_r102, 0.0F, 0.0F, 0.733F);
		cube_r102.setTextureOffset(62, 47).addBox(-1.0F, -1.875F, -3.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

		cube_r103 = new ModelRenderer(this);
		cube_r103.setRotationPoint(4.9653F, -5.5168F, -1.1875F);
		bone3.addChild(cube_r103);
		setRotationAngle(cube_r103, 0.0F, 0.0F, -0.1222F);
		cube_r103.setTextureOffset(56, 63).addBox(-1.0F, -0.875F, -3.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

		cube_r104 = new ModelRenderer(this);
		cube_r104.setRotationPoint(2.4121F, -8.4528F, -1.1875F);
		bone3.addChild(cube_r104);
		setRotationAngle(cube_r104, 0.0F, 0.0F, 0.4625F);
		cube_r104.setTextureOffset(46, 57).addBox(-1.2344F, -1.9219F, -3.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);

		cube_r105 = new ModelRenderer(this);
		cube_r105.setRotationPoint(3.0518F, -8.2708F, -1.1875F);
		bone3.addChild(cube_r105);
		setRotationAngle(cube_r105, 0.0F, 0.0F, 1.1126F);
		cube_r105.setTextureOffset(0, 45).addBox(-1.0F, -1.5F, -3.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.addChild(bone7);
		

		cube_r106 = new ModelRenderer(this);
		cube_r106.setRotationPoint(0.0F, -7.1223F, 6.0674F);
		bone7.addChild(cube_r106);
		setRotationAngle(cube_r106, -1.3832F, 0.0F, 0.0F);
		cube_r106.setTextureOffset(117, 113).addBox(-1.0F, 0.2473F, -1.1924F, 2.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r107 = new ModelRenderer(this);
		cube_r107.setRotationPoint(0.0F, -7.1223F, 6.0674F);
		bone7.addChild(cube_r107);
		setRotationAngle(cube_r107, -1.453F, 0.0F, 0.0F);
		cube_r107.setTextureOffset(109, 130).addBox(-0.5F, -1.974F, -0.6357F, 1.0F, 2.0F, 1.0F, 0.125F, false);
		cube_r107.setTextureOffset(31, 131).addBox(-0.5F, -0.1615F, -0.6357F, 1.0F, 1.0F, 1.0F, 0.25F, false);

		cube_r108 = new ModelRenderer(this);
		cube_r108.setRotationPoint(-5.375F, -9.4955F, -1.8726F);
		bone7.addChild(cube_r108);
		setRotationAngle(cube_r108, 0.0F, 0.0F, -0.7854F);
		cube_r108.setTextureOffset(90, 125).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r108.setTextureOffset(0, 130).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r109 = new ModelRenderer(this);
		cube_r109.setRotationPoint(-5.375F, -9.4955F, 1.1274F);
		bone7.addChild(cube_r109);
		setRotationAngle(cube_r109, -0.2637F, 0.0796F, -0.7931F);
		cube_r109.setTextureOffset(84, 125).addBox(-0.5F, 0.8125F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r109.setTextureOffset(49, 115).addBox(-0.5F, -0.6875F, -0.5F, 1.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r110 = new ModelRenderer(this);
		cube_r110.setRotationPoint(-4.625F, -9.4955F, 3.6899F);
		bone7.addChild(cube_r110);
		setRotationAngle(cube_r110, -0.6133F, 0.2227F, -0.8519F);
		cube_r110.setTextureOffset(78, 125).addBox(-0.75F, 0.625F, 0.0625F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r110.setTextureOffset(12, 104).addBox(-0.75F, -0.625F, 0.0625F, 1.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r111 = new ModelRenderer(this);
		cube_r111.setRotationPoint(4.625F, -9.4955F, 3.6899F);
		bone7.addChild(cube_r111);
		setRotationAngle(cube_r111, -0.6133F, -0.2227F, 0.8519F);
		cube_r111.setTextureOffset(131, 0).addBox(-0.25F, 0.625F, 0.0625F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r111.setTextureOffset(47, 130).addBox(-0.25F, -0.625F, 0.0625F, 1.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r112 = new ModelRenderer(this);
		cube_r112.setRotationPoint(5.375F, -9.4955F, 1.1274F);
		bone7.addChild(cube_r112);
		setRotationAngle(cube_r112, -0.2637F, -0.0796F, 0.7931F);
		cube_r112.setTextureOffset(18, 131).addBox(-0.5F, 0.8125F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r112.setTextureOffset(58, 130).addBox(-0.5F, -0.6875F, -0.5F, 1.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r113 = new ModelRenderer(this);
		cube_r113.setRotationPoint(5.375F, -9.4955F, -1.8726F);
		bone7.addChild(cube_r113);
		setRotationAngle(cube_r113, 0.0F, 0.0F, 0.7854F);
		cube_r113.setTextureOffset(27, 131).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r113.setTextureOffset(69, 130).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.125F, false);

		cube_r114 = new ModelRenderer(this);
		cube_r114.setRotationPoint(0.0F, -11.3348F, 3.4467F);
		bone7.addChild(cube_r114);
		setRotationAngle(cube_r114, -0.5934F, 0.0F, 0.0F);
		cube_r114.setTextureOffset(113, 130).addBox(-0.5F, -1.7368F, -0.6061F, 1.0F, 2.0F, 1.0F, 0.125F, false);

		cube_r115 = new ModelRenderer(this);
		cube_r115.setRotationPoint(0.0F, -11.3348F, 3.4467F);
		bone7.addChild(cube_r115);
		setRotationAngle(cube_r115, -0.4189F, 0.0F, 0.0F);
		cube_r115.setTextureOffset(131, 38).addBox(-0.5F, 0.1209F, -0.6537F, 1.0F, 1.0F, 1.0F, 0.25F, false);

		cube_r116 = new ModelRenderer(this);
		cube_r116.setRotationPoint(0.0F, -11.3348F, 3.4467F);
		bone7.addChild(cube_r116);
		setRotationAngle(cube_r116, -0.2443F, 0.0F, 0.0F);
		cube_r116.setTextureOffset(118, 70).addBox(-1.0F, 0.5223F, -1.2592F, 2.0F, 1.0F, 2.0F, 0.125F, false);

		bone43 = new ModelRenderer(this);
		bone43.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.addChild(bone43);
		

		cube_r117 = new ModelRenderer(this);
		cube_r117.setRotationPoint(0.9993F, -7.2836F, -7.0773F);
		bone43.addChild(cube_r117);
		setRotationAngle(cube_r117, -0.392F, 0.1574F, 0.7226F);
		cube_r117.setTextureOffset(78, 86).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r118 = new ModelRenderer(this);
		cube_r118.setRotationPoint(0.875F, -8.5F, -6.5625F);
		bone43.addChild(cube_r118);
		setRotationAngle(cube_r118, -0.4128F, -0.6134F, -0.6785F);
		cube_r118.setTextureOffset(61, 81).addBox(-0.6875F, -0.6875F, -0.5F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r119 = new ModelRenderer(this);
		cube_r119.setRotationPoint(1.9368F, -2.5336F, -6.2023F);
		bone43.addChild(cube_r119);
		setRotationAngle(cube_r119, 0.0F, -0.5367F, 0.0F);
		cube_r119.setTextureOffset(74, 6).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.1875F, false);

		cube_r120 = new ModelRenderer(this);
		cube_r120.setRotationPoint(5.7493F, -3.0336F, -2.9523F);
		bone43.addChild(cube_r120);
		setRotationAngle(cube_r120, 0.0F, -1.5184F, 0.0F);
		cube_r120.setTextureOffset(30, 61).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r121 = new ModelRenderer(this);
		cube_r121.setRotationPoint(7.1987F, -0.4571F, -2.3071F);
		bone43.addChild(cube_r121);
		setRotationAngle(cube_r121, 0.0F, -0.9294F, 0.0F);
		cube_r121.setTextureOffset(45, 29).addBox(-3.5625F, -1.5F, 0.3125F, 1.0F, 2.0F, 1.0F, -0.0625F, false);

		cube_r122 = new ModelRenderer(this);
		cube_r122.setRotationPoint(-7.1987F, -0.4571F, -2.3071F);
		bone43.addChild(cube_r122);
		setRotationAngle(cube_r122, 0.0F, 0.9294F, 0.0F);
		cube_r122.setTextureOffset(38, 49).addBox(2.5625F, -1.5F, 0.3125F, 1.0F, 2.0F, 1.0F, -0.0625F, false);

		cube_r123 = new ModelRenderer(this);
		cube_r123.setRotationPoint(6.0517F, -5.2211F, 1.7338F);
		bone43.addChild(cube_r123);
		setRotationAngle(cube_r123, 1.4112F, -1.2352F, -1.42F);
		cube_r123.setTextureOffset(44, 35).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r124 = new ModelRenderer(this);
		cube_r124.setRotationPoint(5.9763F, -5.7966F, 2.9346F);
		bone43.addChild(cube_r124);
		setRotationAngle(cube_r124, 2.2414F, -1.1428F, -2.3059F);
		cube_r124.setTextureOffset(36, 25).addBox(-0.5F, -0.375F, -0.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r125 = new ModelRenderer(this);
		cube_r125.setRotationPoint(-5.9763F, -5.7966F, 2.9346F);
		bone43.addChild(cube_r125);
		setRotationAngle(cube_r125, 2.2414F, 1.1428F, 2.3059F);
		cube_r125.setTextureOffset(29, 48).addBox(-0.5F, -0.375F, -0.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r126 = new ModelRenderer(this);
		cube_r126.setRotationPoint(-6.0517F, -5.2211F, 1.7338F);
		bone43.addChild(cube_r126);
		setRotationAngle(cube_r126, 1.4112F, 1.2352F, 1.42F);
		cube_r126.setTextureOffset(14, 56).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r127 = new ModelRenderer(this);
		cube_r127.setRotationPoint(-5.7493F, -3.0336F, -2.9523F);
		bone43.addChild(cube_r127);
		setRotationAngle(cube_r127, 0.0F, 1.5184F, 0.0F);
		cube_r127.setTextureOffset(80, 63).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r128 = new ModelRenderer(this);
		cube_r128.setRotationPoint(-1.9368F, -2.5336F, -6.2023F);
		bone43.addChild(cube_r128);
		setRotationAngle(cube_r128, 0.0F, 0.5367F, 0.0F);
		cube_r128.setTextureOffset(20, 74).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.1875F, false);

		cube_r129 = new ModelRenderer(this);
		cube_r129.setRotationPoint(-0.9993F, -7.2836F, -7.0773F);
		bone43.addChild(cube_r129);
		setRotationAngle(cube_r129, -0.392F, -0.1574F, -0.7226F);
		cube_r129.setTextureOffset(89, 30).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r130 = new ModelRenderer(this);
		cube_r130.setRotationPoint(-0.875F, -8.5F, -6.5625F);
		bone43.addChild(cube_r130);
		setRotationAngle(cube_r130, -0.4128F, 0.6134F, 0.6785F);
		cube_r130.setTextureOffset(93, 11).addBox(-1.3125F, -0.6875F, -0.5F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0625F, false);

		bone27 = new ModelRenderer(this);
		bone27.setRotationPoint(-2.0F, 2.9375F, 3.3125F);
		Body.addChild(bone27);
		bone27.setTextureOffset(0, 16).addBox(-2.0F, 3.0F, -5.3125F, 8.0F, 5.0F, 4.0F, 0.375F, false);
		bone27.setTextureOffset(36, 23).addBox(-2.0F, 1.25F, -5.3125F, 8.0F, 2.0F, 4.0F, 0.5F, false);
		bone27.setTextureOffset(11, 66).addBox(-4.25F, -0.5F, -5.8125F, 2.0F, 3.0F, 5.0F, 0.0625F, false);
		bone27.setTextureOffset(7, 104).addBox(6.5F, -0.5F, -5.1875F, 1.0F, 3.0F, 3.0F, 0.5F, false);
		bone27.setTextureOffset(40, 7).addBox(-1.5F, 6.0F, -4.1875F, 7.0F, 3.0F, 4.0F, 0.375F, false);
		bone27.setTextureOffset(66, 118).addBox(-0.5625F, -2.5F, -0.875F, 1.0F, 6.0F, 1.0F, 0.0625F, false);
		bone27.setTextureOffset(16, 29).addBox(3.5625F, -2.5F, -0.875F, 1.0F, 6.0F, 1.0F, 0.0625F, false);
		bone27.setTextureOffset(38, 103).addBox(0.5F, -2.5F, -1.5F, 3.0F, 6.0F, 1.0F, 0.375F, false);
		bone27.setTextureOffset(74, 54).addBox(-1.0F, -2.5F, -1.75F, 6.0F, 6.0F, 1.0F, 0.375F, false);
		bone27.setTextureOffset(59, 35).addBox(-1.5F, -2.5F, -2.375F, 7.0F, 6.0F, 1.0F, 0.375F, false);

		cube_r131 = new ModelRenderer(this);
		cube_r131.setRotationPoint(4.5625F, 1.0625F, -0.0625F);
		bone27.addChild(cube_r131);
		setRotationAngle(cube_r131, 0.0F, 0.0F, 0.7854F);
		cube_r131.setTextureOffset(128, 41).addBox(0.591F, 0.591F, -0.5F, 2.0F, 2.0F, 1.0F, 0.1875F, false);

		cube_r132 = new ModelRenderer(this);
		cube_r132.setRotationPoint(4.5625F, 1.5F, -0.0625F);
		bone27.addChild(cube_r132);
		setRotationAngle(cube_r132, 0.0F, 0.0F, 0.7854F);
		cube_r132.setTextureOffset(128, 33).addBox(-2.591F, -2.591F, -0.5F, 2.0F, 2.0F, 1.0F, 0.1875F, false);

		cube_r133 = new ModelRenderer(this);
		cube_r133.setRotationPoint(-0.5625F, 1.5F, -0.0625F);
		bone27.addChild(cube_r133);
		setRotationAngle(cube_r133, 0.0F, 0.0F, -0.7854F);
		cube_r133.setTextureOffset(5, 129).addBox(0.591F, -2.591F, -0.5F, 2.0F, 2.0F, 1.0F, 0.1875F, false);

		cube_r134 = new ModelRenderer(this);
		cube_r134.setRotationPoint(3.4375F, 2.8125F, 0.3125F);
		bone27.addChild(cube_r134);
		setRotationAngle(cube_r134, 0.0698F, 0.0F, 1.5708F);
		cube_r134.setTextureOffset(33, 128).addBox(-0.5F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.3125F, false);

		cube_r135 = new ModelRenderer(this);
		cube_r135.setRotationPoint(0.5625F, 2.8125F, 0.3125F);
		bone27.addChild(cube_r135);
		setRotationAngle(cube_r135, 0.0698F, 0.0F, -1.5708F);
		cube_r135.setTextureOffset(128, 126).addBox(-1.5F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.3125F, false);

		cube_r136 = new ModelRenderer(this);
		cube_r136.setRotationPoint(-0.5625F, 1.0625F, -0.0625F);
		bone27.addChild(cube_r136);
		setRotationAngle(cube_r136, 0.0F, 0.0F, -0.7854F);
		cube_r136.setTextureOffset(129, 24).addBox(-2.591F, 0.591F, -0.5F, 2.0F, 2.0F, 1.0F, 0.1875F, false);

		cube_r137 = new ModelRenderer(this);
		cube_r137.setRotationPoint(1.5F, 8.0951F, -6.0F);
		bone27.addChild(cube_r137);
		setRotationAngle(cube_r137, -0.1571F, 0.0F, 0.0F);
		cube_r137.setTextureOffset(74, 61).addBox(-1.5F, 0.0299F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r137.setTextureOffset(56, 47).addBox(-2.0F, -0.9701F, -0.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		bone29 = new ModelRenderer(this);
		bone29.setRotationPoint(-0.3605F, 2.828F, -4.2795F);
		Body.addChild(bone29);
		setRotationAngle(bone29, 0.0F, 0.0F, 1.4704F);
		bone29.setTextureOffset(105, 118).addBox(-1.0F, -2.9219F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		bone29.setTextureOffset(124, 52).addBox(-1.0F, 0.7656F, -0.5F, 2.0F, 3.0F, 1.0F, -0.125F, false);
		bone29.setTextureOffset(130, 88).addBox(-1.0F, -1.9219F, -0.5F, 2.0F, 1.0F, 1.0F, 0.125F, false);
		bone29.setTextureOffset(130, 53).addBox(-1.0F, -0.4219F, -0.5F, 2.0F, 1.0F, 1.0F, 0.125F, false);

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(0.0F, 0.375F, -1.0F);
		Body.addChild(bone8);
		bone8.setTextureOffset(40, 14).addBox(-3.0F, 1.1875F, -2.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
		bone8.setTextureOffset(67, 0).addBox(-3.5F, 2.1875F, -2.0F, 7.0F, 5.0F, 1.0F, 0.0F, false);
		bone8.setTextureOffset(78, 6).addBox(-3.0F, 2.1875F, -2.8125F, 6.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r138 = new ModelRenderer(this);
		cube_r138.setRotationPoint(-0.375F, 6.25F, -4.0313F);
		bone8.addChild(cube_r138);
		setRotationAngle(cube_r138, 0.0F, -0.1222F, 0.0F);
		cube_r138.setTextureOffset(89, 22).addBox(0.5F, -2.125F, -0.6563F, 3.0F, 6.0F, 2.0F, 0.0F, false);
		cube_r138.setTextureOffset(71, 104).addBox(0.5F, -2.125F, -0.7188F, 3.0F, 3.0F, 2.0F, 0.0625F, false);

		cube_r139 = new ModelRenderer(this);
		cube_r139.setRotationPoint(-3.5F, 6.25F, -4.0313F);
		bone8.addChild(cube_r139);
		setRotationAngle(cube_r139, 0.0F, 0.1047F, 0.0F);
		cube_r139.setTextureOffset(89, 67).addBox(0.5F, -2.25F, -0.6563F, 3.0F, 6.0F, 2.0F, 0.0F, false);
		cube_r139.setTextureOffset(46, 105).addBox(0.5F, -2.25F, -0.7188F, 3.0F, 3.0F, 2.0F, 0.0625F, false);

		cube_r140 = new ModelRenderer(this);
		cube_r140.setRotationPoint(3.4012F, 7.0625F, -2.3544F);
		bone8.addChild(cube_r140);
		setRotationAngle(cube_r140, 0.0F, -1.117F, 0.0F);
		cube_r140.setTextureOffset(88, 52).addBox(-1.5F, -2.25F, -0.9687F, 3.0F, 6.0F, 2.0F, 0.0F, false);
		cube_r140.setTextureOffset(104, 44).addBox(-1.5F, -2.25F, -1.0312F, 3.0F, 3.0F, 2.0F, 0.0625F, false);

		cube_r141 = new ModelRenderer(this);
		cube_r141.setRotationPoint(-3.3387F, 6.25F, -2.4169F);
		bone8.addChild(cube_r141);
		setRotationAngle(cube_r141, 0.0F, 1.117F, 0.0F);
		cube_r141.setTextureOffset(81, 105).addBox(-1.5F, -2.125F, -1.0312F, 3.0F, 3.0F, 2.0F, 0.0625F, false);
		cube_r141.setTextureOffset(89, 86).addBox(-1.5F, -2.125F, -0.9687F, 3.0F, 6.0F, 2.0F, 0.0F, false);

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(0.0F, 1.0F, 0.0F);
		Body.addChild(bone10);
		

		cube_r142 = new ModelRenderer(this);
		cube_r142.setRotationPoint(-5.5313F, 9.625F, -0.1875F);
		bone10.addChild(cube_r142);
		setRotationAngle(cube_r142, 3.1008F, 0.2168F, -2.8936F);
		cube_r142.setTextureOffset(61, 86).addBox(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 3.0F, -0.125F, false);

		cube_r143 = new ModelRenderer(this);
		cube_r143.setRotationPoint(-4.7656F, 9.875F, 1.375F);
		bone10.addChild(cube_r143);
		setRotationAngle(cube_r143, 2.9683F, -0.1417F, -3.0159F);
		cube_r143.setTextureOffset(28, 86).addBox(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 3.0F, -0.125F, false);

		cube_r144 = new ModelRenderer(this);
		cube_r144.setRotationPoint(-5.6875F, 8.9844F, -1.875F);
		bone10.addChild(cube_r144);
		setRotationAngle(cube_r144, -2.8788F, 0.2478F, -2.8427F);
		cube_r144.setTextureOffset(73, 85).addBox(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 3.0F, -0.125F, false);

		cube_r145 = new ModelRenderer(this);
		cube_r145.setRotationPoint(4.7656F, 9.75F, 1.625F);
		bone10.addChild(cube_r145);
		setRotationAngle(cube_r145, 0.2628F, -0.1808F, -0.1512F);
		cube_r145.setTextureOffset(81, 86).addBox(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 3.0F, -0.125F, false);

		cube_r146 = new ModelRenderer(this);
		cube_r146.setRotationPoint(5.5313F, 9.75F, 0.0625F);
		bone10.addChild(cube_r146);
		setRotationAngle(cube_r146, 0.1244F, 0.0469F, -0.2527F);
		cube_r146.setTextureOffset(49, 87).addBox(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 3.0F, -0.125F, false);

		cube_r147 = new ModelRenderer(this);
		cube_r147.setRotationPoint(5.6875F, 9.1094F, -1.5625F);
		bone10.addChild(cube_r147);
		setRotationAngle(cube_r147, -0.2634F, 0.2726F, -0.2766F);
		cube_r147.setTextureOffset(0, 89).addBox(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 3.0F, -0.125F, false);

		bone28 = new ModelRenderer(this);
		bone28.setRotationPoint(10.4531F, -6.0372F, -0.4419F);
		Body.addChild(bone28);
		setRotationAngle(bone28, 0.1745F, -0.003F, 0.7853F);
		bone28.setTextureOffset(83, 0).addBox(-1.5F, 5.959F, 0.3794F, 3.0F, 3.0F, 3.0F, -0.125F, false);
		bone28.setTextureOffset(0, 78).addBox(-1.0F, 0.6465F, 0.8794F, 2.0F, 9.0F, 2.0F, 0.0F, false);
		bone28.setTextureOffset(123, 80).addBox(-1.5F, 0.2715F, 1.3794F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		bone28.setTextureOffset(121, 83).addBox(-1.5F, -0.4785F, 1.3794F, 3.0F, 1.0F, 1.0F, 0.0625F, false);
		bone28.setTextureOffset(121, 100).addBox(-1.5F, -1.1427F, 1.3794F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		bone28.setTextureOffset(122, 44).addBox(-1.5F, -0.4356F, 2.0865F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		bone28.setTextureOffset(122, 8).addBox(-1.5F, -0.4356F, 0.6723F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		bone28.setTextureOffset(56, 24).addBox(-1.0F, 2.6465F, 0.8794F, 2.0F, 1.0F, 2.0F, 0.125F, false);

		cube_r148 = new ModelRenderer(this);
		cube_r148.setRotationPoint(0.5F, -1.9785F, -6.0581F);
		bone28.addChild(cube_r148);
		setRotationAngle(cube_r148, 0.7854F, 0.0F, 0.0F);
		cube_r148.setTextureOffset(104, 30).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r149 = new ModelRenderer(this);
		cube_r149.setRotationPoint(-0.5F, -0.4356F, 2.3794F);
		bone28.addChild(cube_r149);
		setRotationAngle(cube_r149, 0.7854F, 0.0F, 0.0F);
		cube_r149.setTextureOffset(121, 116).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r150 = new ModelRenderer(this);
		cube_r150.setRotationPoint(-0.5F, -0.4356F, 1.3794F);
		bone28.addChild(cube_r150);
		setRotationAngle(cube_r150, 0.7854F, 0.0F, 0.0F);
		cube_r150.setTextureOffset(121, 110).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r151 = new ModelRenderer(this);
		cube_r151.setRotationPoint(-0.5F, 0.5644F, 1.3794F);
		bone28.addChild(cube_r151);
		setRotationAngle(cube_r151, 0.7854F, 0.0F, 0.0F);
		cube_r151.setTextureOffset(122, 88).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r152 = new ModelRenderer(this);
		cube_r152.setRotationPoint(-0.5F, 0.5644F, 2.3794F);
		bone28.addChild(cube_r152);
		setRotationAngle(cube_r152, 0.7854F, 0.0F, 0.0F);
		cube_r152.setTextureOffset(123, 65).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r153 = new ModelRenderer(this);
		cube_r153.setRotationPoint(0.5F, -0.666F, 1.8794F);
		bone28.addChild(cube_r153);
		setRotationAngle(cube_r153, -1.7017F, 0.0F, 0.0F);
		cube_r153.setTextureOffset(10, 61).addBox(-1.0F, -2.25F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		cube_r153.setTextureOffset(72, 109).addBox(-1.5F, 2.0F, -1.0F, 2.0F, 3.0F, 2.0F, -0.125F, false);
		cube_r153.setTextureOffset(82, 61).addBox(-1.5F, 5.3125F, -5.0F, 2.0F, 2.0F, 4.0F, -0.1875F, false);
		cube_r153.setTextureOffset(23, 77).addBox(-1.5F, -3.5F, -3.0F, 2.0F, 10.0F, 2.0F, -0.1875F, false);
		cube_r153.setTextureOffset(24, 37).addBox(-1.5F, -3.5F, -1.0F, 2.0F, 5.0F, 2.0F, -0.1875F, false);

		cube_r154 = new ModelRenderer(this);
		cube_r154.setRotationPoint(-1.1875F, -2.2916F, -2.4399F);
		bone28.addChild(cube_r154);
		setRotationAngle(cube_r154, -0.5236F, 0.0F, 0.0F);
		cube_r154.setTextureOffset(99, 115).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, -0.1875F, false);
		cube_r154.setTextureOffset(115, 107).addBox(1.875F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, -0.1875F, false);

		cube_r155 = new ModelRenderer(this);
		cube_r155.setRotationPoint(-1.25F, -1.1228F, -1.5907F);
		bone28.addChild(cube_r155);
		setRotationAngle(cube_r155, -1.7017F, 0.0F, 0.0F);
		cube_r155.setTextureOffset(72, 95).addBox(-0.4375F, 0.75F, -1.875F, 1.0F, 5.0F, 3.0F, -0.1875F, false);
		cube_r155.setTextureOffset(82, 113).addBox(2.375F, 1.6875F, -2.3125F, 1.0F, 1.0F, 1.0F, 0.25F, false);
		cube_r155.setTextureOffset(95, 94).addBox(1.9375F, 0.75F, -1.875F, 1.0F, 5.0F, 3.0F, -0.1875F, false);

		cube_r156 = new ModelRenderer(this);
		cube_r156.setRotationPoint(-1.1875F, -3.0723F, -3.2142F);
		bone28.addChild(cube_r156);
		setRotationAngle(cube_r156, -0.1309F, 0.0F, 0.0F);
		cube_r156.setTextureOffset(85, 94).addBox(-0.5F, -1.5F, -3.0F, 1.0F, 2.0F, 4.0F, -0.1875F, false);
		cube_r156.setTextureOffset(95, 11).addBox(1.875F, -1.5F, -3.0F, 1.0F, 2.0F, 4.0F, -0.1875F, false);

		cube_r157 = new ModelRenderer(this);
		cube_r157.setRotationPoint(0.0F, -2.0352F, 5.8264F);
		bone28.addChild(cube_r157);
		setRotationAngle(cube_r157, 2.6005F, 0.0F, 0.0F);
		cube_r157.setTextureOffset(0, 115).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r158 = new ModelRenderer(this);
		cube_r158.setRotationPoint(0.0F, -1.9911F, 6.5548F);
		bone28.addChild(cube_r158);
		setRotationAngle(cube_r158, 2.1118F, 0.0F, 0.0F);
		cube_r158.setTextureOffset(91, 113).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r159 = new ModelRenderer(this);
		cube_r159.setRotationPoint(0.0F, 0.3626F, 7.1855F);
		bone28.addChild(cube_r159);
		setRotationAngle(cube_r159, 1.5533F, 0.0F, 0.0F);
		cube_r159.setTextureOffset(8, 115).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r160 = new ModelRenderer(this);
		cube_r160.setRotationPoint(0.0F, 3.0199F, 7.3946F);
		bone28.addChild(cube_r160);
		setRotationAngle(cube_r160, 1.7453F, 0.0F, 0.0F);
		cube_r160.setTextureOffset(36, 115).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r161 = new ModelRenderer(this);
		cube_r161.setRotationPoint(0.0F, 5.5007F, 7.2211F);
		bone28.addChild(cube_r161);
		setRotationAngle(cube_r161, 1.2566F, 0.0F, 0.0F);
		cube_r161.setTextureOffset(44, 115).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r162 = new ModelRenderer(this);
		cube_r162.setRotationPoint(0.0F, 7.5215F, 4.8794F);
		bone28.addChild(cube_r162);
		setRotationAngle(cube_r162, 0.4712F, 0.0F, 0.0F);
		cube_r162.setTextureOffset(72, 115).addBox(-0.5F, -0.375F, -0.5781F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r163 = new ModelRenderer(this);
		cube_r163.setRotationPoint(0.0F, 7.5215F, 4.8794F);
		bone28.addChild(cube_r163);
		setRotationAngle(cube_r163, 0.7156F, 0.0F, 0.0F);
		cube_r163.setTextureOffset(58, 114).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, 0.125F, false);

		bone31 = new ModelRenderer(this);
		bone31.setRotationPoint(1.5F, -0.9785F, -6.351F);
		bone28.addChild(bone31);
		bone31.setTextureOffset(88, 102).addBox(-2.0F, -1.7071F, -0.7071F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		bone31.setTextureOffset(66, 86).addBox(-3.5F, -1.5352F, -0.7071F, 4.0F, 1.0F, 1.0F, -0.125F, false);
		bone31.setTextureOffset(112, 44).addBox(-3.5F, -1.0049F, -0.1768F, 4.0F, 1.0F, 1.0F, -0.125F, false);
		bone31.setTextureOffset(111, 60).addBox(-3.5F, -0.4746F, -0.7071F, 4.0F, 1.0F, 1.0F, -0.125F, false);
		bone31.setTextureOffset(54, 104).addBox(-3.5F, -1.0049F, -1.2374F, 4.0F, 1.0F, 1.0F, -0.125F, false);
		bone31.setTextureOffset(112, 77).addBox(-3.5F, -1.0058F, -0.7071F, 4.0F, 1.0F, 1.0F, -0.0625F, false);
		bone31.setTextureOffset(101, 4).addBox(-2.0F, -1.0F, -1.4142F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		bone31.setTextureOffset(79, 104).addBox(-2.0F, -0.2929F, -0.7071F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		bone31.setTextureOffset(24, 105).addBox(-2.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r164 = new ModelRenderer(this);
		cube_r164.setRotationPoint(-1.0F, 0.0F, 0.2929F);
		bone31.addChild(cube_r164);
		setRotationAngle(cube_r164, 0.7854F, 0.0F, 0.0F);
		cube_r164.setTextureOffset(106, 36).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r165 = new ModelRenderer(this);
		cube_r165.setRotationPoint(-1.0F, 0.0F, -0.7071F);
		bone31.addChild(cube_r165);
		setRotationAngle(cube_r165, 0.7854F, 0.0F, 0.0F);
		cube_r165.setTextureOffset(103, 96).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r166 = new ModelRenderer(this);
		cube_r166.setRotationPoint(-2.5F, -0.1299F, -0.5821F);
		bone31.addChild(cube_r166);
		setRotationAngle(cube_r166, 0.7854F, 0.0F, 0.0F);
		cube_r166.setTextureOffset(111, 12).addBox(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r167 = new ModelRenderer(this);
		cube_r167.setRotationPoint(-2.5F, -0.1299F, 0.1679F);
		bone31.addChild(cube_r167);
		setRotationAngle(cube_r167, 0.7854F, 0.0F, 0.0F);
		cube_r167.setTextureOffset(111, 83).addBox(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r168 = new ModelRenderer(this);
		cube_r168.setRotationPoint(-2.5F, -0.8799F, 0.1679F);
		bone31.addChild(cube_r168);
		setRotationAngle(cube_r168, 0.7854F, 0.0F, 0.0F);
		cube_r168.setTextureOffset(54, 87).addBox(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r169 = new ModelRenderer(this);
		cube_r169.setRotationPoint(-2.5F, -0.8799F, -0.5821F);
		bone31.addChild(cube_r169);
		setRotationAngle(cube_r169, 0.7854F, 0.0F, 0.0F);
		cube_r169.setTextureOffset(77, 96).addBox(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r170 = new ModelRenderer(this);
		cube_r170.setRotationPoint(-1.0F, -1.0F, -0.7071F);
		bone31.addChild(cube_r170);
		setRotationAngle(cube_r170, 0.7854F, 0.0F, 0.0F);
		cube_r170.setTextureOffset(102, 44).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		bone30 = new ModelRenderer(this);
		bone30.setRotationPoint(0.5F, -30.0606F, 22.7525F);
		bone28.addChild(bone30);
		bone30.setTextureOffset(121, 78).addBox(-2.0F, 28.6982F, -18.4982F, 3.0F, 1.0F, 1.0F, 0.125F, false);
		bone30.setTextureOffset(32, 23).addBox(-2.0F, 30.466F, -18.4982F, 3.0F, 1.0F, 1.0F, 0.125F, false);
		bone30.setTextureOffset(121, 28).addBox(-2.0F, 29.5821F, -19.382F, 3.0F, 1.0F, 1.0F, 0.125F, false);
		bone30.setTextureOffset(113, 7).addBox(-2.5F, 29.5821F, -18.4982F, 4.0F, 1.0F, 1.0F, -0.1875F, false);
		bone30.setTextureOffset(22, 103).addBox(-2.0F, 29.5821F, -17.6143F, 3.0F, 1.0F, 1.0F, 0.125F, false);

		cube_r171 = new ModelRenderer(this);
		cube_r171.setRotationPoint(-1.0F, 30.7071F, -18.6232F);
		bone30.addChild(cube_r171);
		setRotationAngle(cube_r171, 0.7854F, 0.0F, 0.0F);
		cube_r171.setTextureOffset(64, 33).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.125F, false);

		cube_r172 = new ModelRenderer(this);
		cube_r172.setRotationPoint(-1.0F, 30.7071F, -17.3732F);
		bone30.addChild(cube_r172);
		setRotationAngle(cube_r172, 0.7854F, 0.0F, 0.0F);
		cube_r172.setTextureOffset(82, 67).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.125F, false);

		cube_r173 = new ModelRenderer(this);
		cube_r173.setRotationPoint(-1.0F, 29.4571F, -17.3732F);
		bone30.addChild(cube_r173);
		setRotationAngle(cube_r173, 0.7854F, 0.0F, 0.0F);
		cube_r173.setTextureOffset(97, 39).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.125F, false);

		cube_r174 = new ModelRenderer(this);
		cube_r174.setRotationPoint(-1.0F, 29.4571F, -18.6232F);
		bone30.addChild(cube_r174);
		setRotationAngle(cube_r174, 0.7854F, 0.0F, 0.0F);
		cube_r174.setTextureOffset(121, 60).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.125F, false);

		bone32 = new ModelRenderer(this);
		bone32.setRotationPoint(0.0492F, -1.573F, -6.5228F);
		bone28.addChild(bone32);
		setRotationAngle(bone32, 1.0036F, 0.0F, 0.0F);
		bone32.setTextureOffset(90, 60).addBox(-1.5492F, -0.9041F, -3.2912F, 3.0F, 2.0F, 3.0F, -0.5F, false);
		bone32.setTextureOffset(130, 8).addBox(-1.0492F, -0.4041F, -3.5412F, 2.0F, 1.0F, 1.0F, 0.125F, false);
		bone32.setTextureOffset(109, 65).addBox(-0.5492F, -1.9041F, -5.6662F, 1.0F, 2.0F, 3.0F, 0.0F, false);
		bone32.setTextureOffset(58, 0).addBox(-0.5492F, 0.2834F, -9.6662F, 1.0F, 1.0F, 7.0F, 0.0F, false);
		bone32.setTextureOffset(53, 93).addBox(-0.5492F, -0.9666F, -9.6662F, 1.0F, 2.0F, 4.0F, 0.0F, false);

		cube_r175 = new ModelRenderer(this);
		cube_r175.setRotationPoint(0.3286F, -0.3277F, -3.1662F);
		bone32.addChild(cube_r175);
		setRotationAngle(cube_r175, 0.0F, 0.0F, -0.2443F);
		cube_r175.setTextureOffset(56, 120).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r175.setTextureOffset(24, 118).addBox(-0.5F, -1.5F, -2.5F, 1.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r176 = new ModelRenderer(this);
		cube_r176.setRotationPoint(2.9374F, 0.9352F, 2.082F);
		bone32.addChild(cube_r176);
		setRotationAngle(cube_r176, 0.3744F, -0.8532F, 0.1149F);
		cube_r176.setTextureOffset(109, 112).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r177 = new ModelRenderer(this);
		cube_r177.setRotationPoint(2.5041F, 1.5412F, 2.3971F);
		bone32.addChild(cube_r177);
		setRotationAngle(cube_r177, -0.3063F, -0.8532F, 0.1149F);
		cube_r177.setTextureOffset(114, 62).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r178 = new ModelRenderer(this);
		cube_r178.setRotationPoint(3.73F, 1.5295F, 1.1292F);
		bone32.addChild(cube_r178);
		setRotationAngle(cube_r178, 0.2437F, -0.0832F, 0.3821F);
		cube_r178.setTextureOffset(127, 38).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, -0.125F, false);

		cube_r179 = new ModelRenderer(this);
		cube_r179.setRotationPoint(3.9821F, 1.4288F, -1.2251F);
		bone32.addChild(cube_r179);
		setRotationAngle(cube_r179, -0.402F, -0.0832F, 0.3821F);
		cube_r179.setTextureOffset(67, 114).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r180 = new ModelRenderer(this);
		cube_r180.setRotationPoint(3.5892F, 0.4206F, -3.1489F);
		bone32.addChild(cube_r180);
		setRotationAngle(cube_r180, -0.5236F, 0.6763F, 0.0F);
		cube_r180.setTextureOffset(114, 85).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, -0.125F, false);

		cube_r181 = new ModelRenderer(this);
		cube_r181.setRotationPoint(0.8258F, -0.2166F, -7.1662F);
		bone32.addChild(cube_r181);
		setRotationAngle(cube_r181, 0.0F, 0.6763F, 0.0F);
		cube_r181.setTextureOffset(114, 103).addBox(-0.8594F, -0.5F, 0.9844F, 1.0F, 1.0F, 3.0F, -0.125F, false);
		cube_r181.setTextureOffset(58, 127).addBox(-0.8594F, -0.5F, -0.7656F, 1.0F, 1.0F, 2.0F, 0.0625F, false);

		cube_r182 = new ModelRenderer(this);
		cube_r182.setRotationPoint(-0.4269F, 0.6098F, -5.9162F);
		bone32.addChild(cube_r182);
		setRotationAngle(cube_r182, 0.0F, 0.0F, 0.2443F);
		cube_r182.setTextureOffset(65, 92).addBox(-0.5F, -1.5F, -3.75F, 1.0F, 2.0F, 4.0F, 0.0F, false);

		cube_r183 = new ModelRenderer(this);
		cube_r183.setRotationPoint(0.3286F, 0.6098F, -5.9162F);
		bone32.addChild(cube_r183);
		setRotationAngle(cube_r183, 0.0F, 0.0F, -0.2443F);
		cube_r183.setTextureOffset(22, 92).addBox(-0.5F, -1.5F, -3.75F, 1.0F, 2.0F, 4.0F, 0.0F, false);

		cube_r184 = new ModelRenderer(this);
		cube_r184.setRotationPoint(-0.0492F, -0.5093F, -6.8083F);
		bone32.addChild(cube_r184);
		setRotationAngle(cube_r184, 0.2967F, 0.0F, 0.0F);
		cube_r184.setTextureOffset(21, 98).addBox(-0.5F, -1.0F, -2.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		cube_r185 = new ModelRenderer(this);
		cube_r185.setRotationPoint(-0.4269F, -0.3277F, -2.9162F);
		bone32.addChild(cube_r185);
		setRotationAngle(cube_r185, 0.0F, 0.0F, 0.2443F);
		cube_r185.setTextureOffset(50, 118).addBox(-0.5F, -1.5F, -2.75F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		cube_r185.setTextureOffset(109, 123).addBox(-0.5F, -1.5F, -0.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		bone34 = new ModelRenderer(this);
		bone34.setRotationPoint(-0.8624F, -0.0217F, -10.1238F);
		bone32.addChild(bone34);
		setRotationAngle(bone34, 0.0F, 0.0F, 0.2443F);
		

		cube_r186 = new ModelRenderer(this);
		cube_r186.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone34.addChild(cube_r186);
		setRotationAngle(cube_r186, 0.0F, -0.2967F, 0.0F);
		cube_r186.setTextureOffset(111, 118).addBox(-0.3419F, -0.4944F, -0.0048F, 1.0F, 1.0F, 1.0F, -0.1875F, false);

		cube_r187 = new ModelRenderer(this);
		cube_r187.setRotationPoint(0.8389F, 1.375F, 0.0F);
		bone34.addChild(cube_r187);
		setRotationAngle(cube_r187, 0.0F, -0.4363F, 0.0F);
		cube_r187.setTextureOffset(6, 126).addBox(-1.5037F, -1.8694F, -1.0791F, 1.0F, 1.0F, 2.0F, -0.125F, false);

		bone33 = new ModelRenderer(this);
		bone33.setRotationPoint(0.8578F, -0.0217F, -10.1238F);
		bone32.addChild(bone33);
		setRotationAngle(bone33, 0.0F, 0.0F, -0.2443F);
		

		cube_r188 = new ModelRenderer(this);
		cube_r188.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone33.addChild(cube_r188);
		setRotationAngle(cube_r188, 0.0F, 0.2967F, 0.0F);
		cube_r188.setTextureOffset(105, 88).addBox(-0.6581F, -0.4944F, -0.0048F, 1.0F, 1.0F, 1.0F, -0.1875F, false);

		cube_r189 = new ModelRenderer(this);
		cube_r189.setRotationPoint(-0.8389F, 1.375F, 0.0F);
		bone33.addChild(cube_r189);
		setRotationAngle(cube_r189, 0.0F, 0.4363F, 0.0F);
		cube_r189.setTextureOffset(126, 5).addBox(0.5037F, -1.8694F, -1.0791F, 1.0F, 1.0F, 2.0F, -0.125F, false);

		bone35 = new ModelRenderer(this);
		bone35.setRotationPoint(-0.0235F, 1.3533F, -10.1238F);
		bone32.addChild(bone35);
		setRotationAngle(bone35, 0.0F, 0.0F, -1.5708F);
		

		cube_r190 = new ModelRenderer(this);
		cube_r190.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone35.addChild(cube_r190);
		setRotationAngle(cube_r190, 0.0F, -0.4363F, 0.0F);
		cube_r190.setTextureOffset(69, 127).addBox(-0.7537F, -0.4944F, -1.4541F, 1.0F, 1.0F, 2.0F, -0.125F, false);

		cube_r191 = new ModelRenderer(this);
		cube_r191.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone35.addChild(cube_r191);
		setRotationAngle(cube_r191, 0.0F, -0.2967F, 0.0F);
		cube_r191.setTextureOffset(114, 66).addBox(-0.3419F, -0.4944F, -0.0048F, 1.0F, 1.0F, 1.0F, -0.1875F, false);

		JS = new ModelRenderer(this);
		JS.setRotationPoint(0.0F, -3.0625F, -6.75F);
		Body.addChild(JS);
		

		cube_r192 = new ModelRenderer(this);
		cube_r192.setRotationPoint(3.8921F, 12.0876F, 10.7278F);
		JS.addChild(cube_r192);
		setRotationAngle(cube_r192, -0.1585F, -0.0735F, -0.4305F);
		cube_r192.setTextureOffset(40, 57).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		cube_r193 = new ModelRenderer(this);
		cube_r193.setRotationPoint(3.741F, 12.5435F, 12.1846F);
		JS.addChild(cube_r193);
		setRotationAngle(cube_r193, -0.3779F, -0.1389F, -0.3356F);
		cube_r193.setTextureOffset(105, 112).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r194 = new ModelRenderer(this);
		cube_r194.setRotationPoint(-3.741F, 12.5435F, 12.1846F);
		JS.addChild(cube_r194);
		setRotationAngle(cube_r194, -0.3779F, 0.1389F, 0.3356F);
		cube_r194.setTextureOffset(54, 114).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r195 = new ModelRenderer(this);
		cube_r195.setRotationPoint(-0.5F, 10.9237F, 12.3289F);
		JS.addChild(cube_r195);
		setRotationAngle(cube_r195, -0.4014F, 0.0F, 0.0F);
		cube_r195.setTextureOffset(56, 57).addBox(-3.0F, -0.5F, -0.5F, 7.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r196 = new ModelRenderer(this);
		cube_r196.setRotationPoint(-0.5F, 11.4281F, 10.7836F);
		JS.addChild(cube_r196);
		setRotationAngle(cube_r196, 1.3526F, 0.0F, 0.0F);
		cube_r196.setTextureOffset(56, 43).addBox(-3.0F, -0.5F, -2.5F, 7.0F, 1.0F, 3.0F, 0.0F, false);

		cube_r197 = new ModelRenderer(this);
		cube_r197.setRotationPoint(-0.5F, 10.8009F, 11.1101F);
		JS.addChild(cube_r197);
		setRotationAngle(cube_r197, 0.829F, 0.0F, 0.0F);
		cube_r197.setTextureOffset(78, 12).addBox(-3.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r198 = new ModelRenderer(this);
		cube_r198.setRotationPoint(-3.8921F, 12.0876F, 10.7278F);
		JS.addChild(cube_r198);
		setRotationAngle(cube_r198, -0.1585F, 0.0735F, 0.4305F);
		cube_r198.setTextureOffset(0, 68).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		cube_r199 = new ModelRenderer(this);
		cube_r199.setRotationPoint(-0.5F, 10.5F, 10.5F);
		JS.addChild(cube_r199);
		setRotationAngle(cube_r199, -0.1745F, 0.0F, 0.0F);
		cube_r199.setTextureOffset(57, 70).addBox(-3.0F, -0.5F, 0.5F, 7.0F, 4.0F, 1.0F, 0.0F, false);

		cube_r200 = new ModelRenderer(this);
		cube_r200.setRotationPoint(-1.8975F, 13.7782F, 14.5757F);
		JS.addChild(cube_r200);
		setRotationAngle(cube_r200, -1.0821F, 0.0F, 0.0F);
		cube_r200.setTextureOffset(75, 35).addBox(-1.6025F, -1.2813F, -2.375F, 7.0F, 1.0F, 1.0F, 0.0625F, false);

		cube_r201 = new ModelRenderer(this);
		cube_r201.setRotationPoint(-3.1466F, 13.8371F, 14.465F);
		JS.addChild(cube_r201);
		setRotationAngle(cube_r201, -1.0347F, 0.4049F, 0.2299F);
		cube_r201.setTextureOffset(14, 45).addBox(-0.9652F, -1.0667F, -2.375F, 1.0F, 4.0F, 1.0F, 0.0625F, false);

		cube_r202 = new ModelRenderer(this);
		cube_r202.setRotationPoint(3.1466F, 13.8371F, 14.465F);
		JS.addChild(cube_r202);
		setRotationAngle(cube_r202, -1.0347F, -0.4049F, -0.2299F);
		cube_r202.setTextureOffset(47, 57).addBox(-0.0348F, -1.0667F, -2.375F, 1.0F, 4.0F, 1.0F, 0.0625F, false);

		cube_r203 = new ModelRenderer(this);
		cube_r203.setRotationPoint(3.1123F, 13.9173F, 15.1721F);
		JS.addChild(cube_r203);
		setRotationAngle(cube_r203, -0.6997F, -0.6427F, -0.4094F);
		cube_r203.setTextureOffset(31, 82).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r204 = new ModelRenderer(this);
		cube_r204.setRotationPoint(3.0313F, 13.7413F, 15.1778F);
		JS.addChild(cube_r204);
		setRotationAngle(cube_r204, -0.9441F, -0.6427F, -0.4094F);
		cube_r204.setTextureOffset(44, 120).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 2.0F, 2.0F, 0.0625F, false);

		cube_r205 = new ModelRenderer(this);
		cube_r205.setRotationPoint(-3.1123F, 13.9173F, 15.1721F);
		JS.addChild(cube_r205);
		setRotationAngle(cube_r205, -0.6997F, 0.6427F, 0.4094F);
		cube_r205.setTextureOffset(83, 29).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0625F, false);

		cube_r206 = new ModelRenderer(this);
		cube_r206.setRotationPoint(-3.0313F, 13.7413F, 15.1778F);
		JS.addChild(cube_r206);
		setRotationAngle(cube_r206, -0.9441F, 0.6427F, 0.4094F);
		cube_r206.setTextureOffset(122, 93).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 2.0F, 2.0F, 0.0625F, false);

		cube_r207 = new ModelRenderer(this);
		cube_r207.setRotationPoint(-1.8975F, 12.8953F, 14.1062F);
		JS.addChild(cube_r207);
		setRotationAngle(cube_r207, -1.0821F, 0.0F, 0.0F);
		cube_r207.setTextureOffset(0, 41).addBox(-1.1025F, -1.2813F, -0.25F, 6.0F, 1.0F, 2.0F, 0.0625F, false);

		cube_r208 = new ModelRenderer(this);
		cube_r208.setRotationPoint(0.5F, 10.7583F, 12.9742F);
		JS.addChild(cube_r208);
		setRotationAngle(cube_r208, -0.4887F, 0.0F, 0.0F);
		cube_r208.setTextureOffset(59, 31).addBox(-4.0F, -0.5F, 0.5F, 7.0F, 1.0F, 1.0F, 0.0625F, false);

		cube_r209 = new ModelRenderer(this);
		cube_r209.setRotationPoint(0.0F, 10.8527F, 12.9288F);
		JS.addChild(cube_r209);
		setRotationAngle(cube_r209, -0.2793F, 0.0F, 0.0F);
		cube_r209.setTextureOffset(87, 18).addBox(-2.5F, -0.5F, -1.5F, 5.0F, 1.0F, 2.0F, 0.0625F, false);

		bone37 = new ModelRenderer(this);
		bone37.setRotationPoint(-6.4411F, 14.3295F, 13.6131F);
		JS.addChild(bone37);
		setRotationAngle(bone37, -0.2705F, 0.0F, 0.0F);
		bone37.setTextureOffset(112, 70).addBox(-1.7159F, 0.2217F, -0.3663F, 1.0F, 1.0F, 1.0F, -0.0625F, false);
		bone37.setTextureOffset(101, 121).addBox(-1.6534F, 0.2842F, -2.3038F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		bone37.setTextureOffset(106, 88).addBox(-0.0277F, -1.6576F, -2.6131F, 2.0F, 1.0F, 3.0F, 0.0F, false);

		cube_r210 = new ModelRenderer(this);
		cube_r210.setRotationPoint(0.1523F, 0.1671F, 1.5966F);
		bone37.addChild(cube_r210);
		setRotationAngle(cube_r210, -0.847F, -0.9094F, -0.609F);
		cube_r210.setTextureOffset(86, 129).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r211 = new ModelRenderer(this);
		cube_r211.setRotationPoint(0.9723F, 0.5491F, 1.9679F);
		bone37.addChild(cube_r211);
		setRotationAngle(cube_r211, -1.1519F, 0.0F, 0.0F);
		cube_r211.setTextureOffset(129, 116).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r212 = new ModelRenderer(this);
		cube_r212.setRotationPoint(0.1012F, -0.4298F, 1.1727F);
		bone37.addChild(cube_r212);
		setRotationAngle(cube_r212, -0.6893F, 0.4121F, 0.4524F);
		cube_r212.setTextureOffset(99, 91).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r213 = new ModelRenderer(this);
		cube_r213.setRotationPoint(0.9723F, -0.2143F, 1.6644F);
		bone37.addChild(cube_r213);
		setRotationAngle(cube_r213, -0.7854F, 0.0F, 0.0F);
		cube_r213.setTextureOffset(127, 129).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r214 = new ModelRenderer(this);
		cube_r214.setRotationPoint(-0.4921F, -0.3034F, 0.3672F);
		bone37.addChild(cube_r214);
		setRotationAngle(cube_r214, -0.3743F, 0.1217F, 0.2998F);
		cube_r214.setTextureOffset(72, 79).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r215 = new ModelRenderer(this);
		cube_r215.setRotationPoint(0.2083F, -0.4566F, 0.4307F);
		bone37.addChild(cube_r215);
		setRotationAngle(cube_r215, -0.2537F, 0.3031F, 0.8555F);
		cube_r215.setTextureOffset(99, 88).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r216 = new ModelRenderer(this);
		cube_r216.setRotationPoint(0.9723F, -0.813F, 1.1195F);
		bone37.addChild(cube_r216);
		setRotationAngle(cube_r216, -0.3927F, 0.0F, 0.0F);
		cube_r216.setTextureOffset(130, 5).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r217 = new ModelRenderer(this);
		cube_r217.setRotationPoint(-0.5601F, -0.0587F, -2.1341F);
		bone37.addChild(cube_r217);
		setRotationAngle(cube_r217, 1.3378F, 0.5981F, -0.1328F);
		cube_r217.setTextureOffset(100, 79).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r218 = new ModelRenderer(this);
		cube_r218.setRotationPoint(0.9723F, -0.1509F, -2.6084F);
		bone37.addChild(cube_r218);
		setRotationAngle(cube_r218, 1.3788F, 0.0F, 0.0F);
		cube_r218.setTextureOffset(92, 129).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r219 = new ModelRenderer(this);
		cube_r219.setRotationPoint(0.9723F, -1.002F, -2.3482F);
		bone37.addChild(cube_r219);
		setRotationAngle(cube_r219, 1.1694F, 0.0F, 0.0F);
		cube_r219.setTextureOffset(129, 110).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r220 = new ModelRenderer(this);
		cube_r220.setRotationPoint(0.9512F, -1.8293F, 0.0032F);
		bone37.addChild(cube_r220);
		setRotationAngle(cube_r220, -0.192F, 0.0F, -0.1222F);
		cube_r220.setTextureOffset(106, 53).addBox(-0.5F, -0.4375F, -0.375F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r221 = new ModelRenderer(this);
		cube_r221.setRotationPoint(0.9723F, -1.6576F, -1.4881F);
		bone37.addChild(cube_r221);
		setRotationAngle(cube_r221, 0.1571F, 0.0F, -0.1222F);
		cube_r221.setTextureOffset(30, 107).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.1875F, false);

		cube_r222 = new ModelRenderer(this);
		cube_r222.setRotationPoint(2.2184F, 0.7217F, 2.368F);
		bone37.addChild(cube_r222);
		setRotationAngle(cube_r222, 0.0F, 1.5708F, 0.0F);
		cube_r222.setTextureOffset(38, 126).addBox(-0.5F, -0.5F, -2.125F, 1.0F, 1.0F, 2.0F, -0.0625F, false);

		cube_r223 = new ModelRenderer(this);
		cube_r223.setRotationPoint(0.2945F, 0.7217F, 1.487F);
		bone37.addChild(cube_r223);
		setRotationAngle(cube_r223, 0.0F, 0.6807F, 0.0F);
		cube_r223.setTextureOffset(114, 14).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 3.0F, -0.0625F, false);

		cube_r224 = new ModelRenderer(this);
		cube_r224.setRotationPoint(-0.6132F, 0.7842F, -2.7993F);
		bone37.addChild(cube_r224);
		setRotationAngle(cube_r224, 0.0F, -1.3788F, 0.0F);
		cube_r224.setTextureOffset(108, 27).addBox(-0.5F, -0.5F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r225 = new ModelRenderer(this);
		cube_r225.setRotationPoint(-0.9809F, 0.7842F, -2.5223F);
		bone37.addChild(cube_r225);
		setRotationAngle(cube_r225, 0.0F, -0.4712F, 0.0F);
		cube_r225.setTextureOffset(68, 103).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r226 = new ModelRenderer(this);
		cube_r226.setRotationPoint(-0.2927F, -1.1576F, -0.6131F);
		bone37.addChild(cube_r226);
		setRotationAngle(cube_r226, 0.0F, 0.0F, -1.2392F);
		cube_r226.setTextureOffset(106, 49).addBox(-2.25F, -0.5F, -2.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);

		cube_r227 = new ModelRenderer(this);
		cube_r227.setRotationPoint(-0.0481F, -1.033F, -0.6131F);
		bone37.addChild(cube_r227);
		setRotationAngle(cube_r227, 0.0F, 0.0F, -0.6109F);
		cube_r227.setTextureOffset(24, 114).addBox(-0.625F, -0.5F, -2.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);

		bone38 = new ModelRenderer(this);
		bone38.setRotationPoint(0.5113F, 2.9379F, 0.524F);
		bone37.addChild(bone38);
		setRotationAngle(bone38, 0.6192F, -0.1427F, 0.2794F);
		bone38.setTextureOffset(123, 36).addBox(-0.8515F, -3.1424F, 1.2692F, 2.0F, 3.0F, 1.0F, -0.0625F, false);
		bone38.setTextureOffset(122, 40).addBox(-0.8515F, -3.1424F, -2.3825F, 2.0F, 3.0F, 1.0F, -0.0625F, false);
		bone38.setTextureOffset(127, 62).addBox(-0.8515F, -0.3924F, 0.9879F, 2.0F, 2.0F, 1.0F, -0.25F, false);
		bone38.setTextureOffset(14, 45).addBox(-2.3515F, 0.4201F, -2.5121F, 5.0F, 1.0F, 5.0F, -0.5F, false);
		bone38.setTextureOffset(127, 107).addBox(-0.8515F, -0.3924F, -2.1334F, 2.0F, 2.0F, 1.0F, -0.25F, false);
		bone38.setTextureOffset(127, 120).addBox(-0.8515F, -0.7674F, 1.0817F, 2.0F, 2.0F, 1.0F, -0.1875F, false);
		bone38.setTextureOffset(122, 127).addBox(-0.8515F, -0.7674F, -2.2164F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r228 = new ModelRenderer(this);
		cube_r228.setRotationPoint(-1.0617F, 0.1389F, -1.2776F);
		bone38.addChild(cube_r228);
		setRotationAngle(cube_r228, 0.0F, -2.3562F, 0.0F);
		cube_r228.setTextureOffset(69, 88).addBox(-1.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
		cube_r228.setTextureOffset(107, 65).addBox(-0.5F, -0.6563F, -0.5625F, 1.0F, 1.0F, 1.0F, -0.1875F, false);
		cube_r228.setTextureOffset(84, 43).addBox(0.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r229 = new ModelRenderer(this);
		cube_r229.setRotationPoint(-1.0617F, 0.1389F, 1.1429F);
		bone38.addChild(cube_r229);
		setRotationAngle(cube_r229, 0.0F, -0.7854F, 0.0F);
		cube_r229.setTextureOffset(41, 98).addBox(-1.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
		cube_r229.setTextureOffset(12, 110).addBox(-0.5F, -0.6563F, -0.5625F, 1.0F, 1.0F, 1.0F, -0.1875F, false);
		cube_r229.setTextureOffset(57, 89).addBox(0.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r230 = new ModelRenderer(this);
		cube_r230.setRotationPoint(-1.1034F, 0.0451F, 1.5118F);
		bone38.addChild(cube_r230);
		setRotationAngle(cube_r230, 0.0F, -0.7854F, 0.0F);
		cube_r230.setTextureOffset(129, 51).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r231 = new ModelRenderer(this);
		cube_r231.setRotationPoint(-1.701F, 0.0451F, 0.8594F);
		bone38.addChild(cube_r231);
		setRotationAngle(cube_r231, 0.0F, 0.0F, 0.0F);
		cube_r231.setTextureOffset(129, 83).addBox(0.6386F, -0.5F, 0.5837F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r232 = new ModelRenderer(this);
		cube_r232.setRotationPoint(1.403F, -0.2049F, -0.2549F);
		bone38.addChild(cube_r232);
		setRotationAngle(cube_r232, 0.0F, 0.7854F, 0.0F);
		cube_r232.setTextureOffset(54, 99).addBox(-0.9571F, -0.5F, 0.4571F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r233 = new ModelRenderer(this);
		cube_r233.setRotationPoint(1.2704F, -0.2674F, -0.2107F);
		bone38.addChild(cube_r233);
		setRotationAngle(cube_r233, 0.0F, 0.7854F, 0.0F);
		cube_r233.setTextureOffset(20, 110).addBox(-1.3946F, -0.25F, 0.4571F, 1.0F, 1.0F, 1.0F, -0.1875F, false);

		cube_r234 = new ModelRenderer(this);
		cube_r234.setRotationPoint(1.3146F, -0.2049F, -0.1665F);
		bone38.addChild(cube_r234);
		setRotationAngle(cube_r234, 0.0F, 0.7854F, 0.0F);
		cube_r234.setTextureOffset(82, 99).addBox(-1.9571F, -0.5F, 0.4571F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r235 = new ModelRenderer(this);
		cube_r235.setRotationPoint(2.0779F, -0.2674F, -1.4942F);
		bone38.addChild(cube_r235);
		setRotationAngle(cube_r235, 0.0F, -1.5708F, 0.0F);
		cube_r235.setTextureOffset(27, 128).addBox(0.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r236 = new ModelRenderer(this);
		cube_r236.setRotationPoint(-1.2202F, -0.2674F, -1.4942F);
		bone38.addChild(cube_r236);
		setRotationAngle(cube_r236, 0.0F, -1.5708F, 0.0F);
		cube_r236.setTextureOffset(11, 128).addBox(0.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r237 = new ModelRenderer(this);
		cube_r237.setRotationPoint(1.3146F, 0.2326F, -1.2334F);
		bone38.addChild(cube_r237);
		setRotationAngle(cube_r237, 0.0F, -0.7854F, 0.0F);
		cube_r237.setTextureOffset(127, 48).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r238 = new ModelRenderer(this);
		cube_r238.setRotationPoint(-1.1318F, 0.1076F, -1.4995F);
		bone38.addChild(cube_r238);
		setRotationAngle(cube_r238, 0.0F, -1.5708F, 0.0F);
		cube_r238.setTextureOffset(116, 127).addBox(0.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r239 = new ModelRenderer(this);
		cube_r239.setRotationPoint(-1.2354F, 0.1076F, -0.2495F);
		bone38.addChild(cube_r239);
		setRotationAngle(cube_r239, -3.1416F, -0.7854F, 3.1416F);
		cube_r239.setTextureOffset(110, 127).addBox(-1.8536F, -0.5F, -0.0429F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r240 = new ModelRenderer(this);
		cube_r240.setRotationPoint(1.2521F, 0.1076F, 0.1772F);
		bone38.addChild(cube_r240);
		setRotationAngle(cube_r240, -3.1416F, 0.7854F, 3.1416F);
		cube_r240.setTextureOffset(104, 127).addBox(-0.0429F, -0.5F, 0.4571F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r241 = new ModelRenderer(this);
		cube_r241.setRotationPoint(1.9895F, 0.1076F, -1.4995F);
		bone38.addChild(cube_r241);
		setRotationAngle(cube_r241, 0.0F, -1.5708F, 0.0F);
		cube_r241.setTextureOffset(98, 127).addBox(0.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r242 = new ModelRenderer(this);
		cube_r242.setRotationPoint(1.2521F, 0.1076F, -0.3228F);
		bone38.addChild(cube_r242);
		setRotationAngle(cube_r242, 0.0F, 0.7854F, 0.0F);
		cube_r242.setTextureOffset(127, 85).addBox(-1.9571F, -0.5F, 0.4571F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r243 = new ModelRenderer(this);
		cube_r243.setRotationPoint(-1.3086F, 0.1076F, 0.6772F);
		bone38.addChild(cube_r243);
		setRotationAngle(cube_r243, 0.0F, -0.7854F, 0.0F);
		cube_r243.setTextureOffset(127, 56).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r244 = new ModelRenderer(this);
		cube_r244.setRotationPoint(1.4396F, -1.6424F, 0.0058F);
		bone38.addChild(cube_r244);
		setRotationAngle(cube_r244, -3.1416F, 0.7854F, 3.1416F);
		cube_r244.setTextureOffset(38, 122).addBox(-0.0429F, -1.5F, 0.4571F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r245 = new ModelRenderer(this);
		cube_r245.setRotationPoint(1.4396F, -1.6424F, -0.1192F);
		bone38.addChild(cube_r245);
		setRotationAngle(cube_r245, 0.0F, 0.7854F, 0.0F);
		cube_r245.setTextureOffset(122, 32).addBox(-1.9571F, -1.5F, 0.4571F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r246 = new ModelRenderer(this);
		cube_r246.setRotationPoint(-1.4229F, -1.6424F, -0.4209F);
		bone38.addChild(cube_r246);
		setRotationAngle(cube_r246, -3.1416F, -0.7854F, 3.1416F);
		cube_r246.setTextureOffset(70, 122).addBox(-1.8536F, -1.5F, -0.0429F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r247 = new ModelRenderer(this);
		cube_r247.setRotationPoint(2.2547F, -1.6424F, -1.4834F);
		bone38.addChild(cube_r247);
		setRotationAngle(cube_r247, 0.0F, -1.5708F, 0.0F);
		cube_r247.setTextureOffset(6, 122).addBox(0.4268F, -1.5F, -0.2197F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r248 = new ModelRenderer(this);
		cube_r248.setRotationPoint(-1.397F, -1.6424F, -1.4834F);
		bone38.addChild(cube_r248);
		setRotationAngle(cube_r248, 0.0F, -1.5708F, 0.0F);
		cube_r248.setTextureOffset(55, 122).addBox(0.4268F, -1.5F, -0.2197F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r249 = new ModelRenderer(this);
		cube_r249.setRotationPoint(-1.4961F, -1.6424F, 0.8808F);
		bone38.addChild(cube_r249);
		setRotationAngle(cube_r249, 0.0F, -0.7854F, 0.0F);
		cube_r249.setTextureOffset(22, 123).addBox(-0.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		bone39 = new ModelRenderer(this);
		bone39.setRotationPoint(6.4411F, 14.3295F, 13.6131F);
		JS.addChild(bone39);
		setRotationAngle(bone39, -0.2705F, 0.0F, 0.0F);
		bone39.setTextureOffset(95, 13).addBox(0.7159F, 0.2217F, -0.3663F, 1.0F, 1.0F, 1.0F, -0.0625F, false);
		bone39.setTextureOffset(45, 85).addBox(0.6534F, 0.2842F, -2.3038F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		bone39.setTextureOffset(26, 57).addBox(-1.9723F, -1.6576F, -2.6131F, 2.0F, 1.0F, 3.0F, 0.0F, false);

		cube_r250 = new ModelRenderer(this);
		cube_r250.setRotationPoint(-0.1523F, 0.1671F, 1.5966F);
		bone39.addChild(cube_r250);
		setRotationAngle(cube_r250, -0.847F, 0.9094F, 0.609F);
		cube_r250.setTextureOffset(88, 100).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r251 = new ModelRenderer(this);
		cube_r251.setRotationPoint(-0.9723F, 0.5491F, 1.9679F);
		bone39.addChild(cube_r251);
		setRotationAngle(cube_r251, -1.1519F, 0.0F, 0.0F);
		cube_r251.setTextureOffset(96, 103).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r252 = new ModelRenderer(this);
		cube_r252.setRotationPoint(-0.1012F, -0.4298F, 1.1727F);
		bone39.addChild(cube_r252);
		setRotationAngle(cube_r252, -0.6893F, -0.4121F, -0.4524F);
		cube_r252.setTextureOffset(53, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r253 = new ModelRenderer(this);
		cube_r253.setRotationPoint(-0.9723F, -0.2143F, 1.6644F);
		bone39.addChild(cube_r253);
		setRotationAngle(cube_r253, -0.7854F, 0.0F, 0.0F);
		cube_r253.setTextureOffset(99, 113).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r254 = new ModelRenderer(this);
		cube_r254.setRotationPoint(0.4921F, -0.3034F, 0.3672F);
		bone39.addChild(cube_r254);
		setRotationAngle(cube_r254, -0.3743F, -0.1217F, -0.2998F);
		cube_r254.setTextureOffset(48, 77).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r255 = new ModelRenderer(this);
		cube_r255.setRotationPoint(-0.2083F, -0.4566F, 0.4307F);
		bone39.addChild(cube_r255);
		setRotationAngle(cube_r255, -0.2537F, -0.3031F, -0.8555F);
		cube_r255.setTextureOffset(87, 41).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r256 = new ModelRenderer(this);
		cube_r256.setRotationPoint(-0.9723F, -0.813F, 1.1195F);
		bone39.addChild(cube_r256);
		setRotationAngle(cube_r256, -0.3927F, 0.0F, 0.0F);
		cube_r256.setTextureOffset(13, 115).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r257 = new ModelRenderer(this);
		cube_r257.setRotationPoint(0.5601F, -0.0587F, -2.1341F);
		bone39.addChild(cube_r257);
		setRotationAngle(cube_r257, 1.3378F, -0.5981F, 0.1328F);
		cube_r257.setTextureOffset(89, 32).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r258 = new ModelRenderer(this);
		cube_r258.setRotationPoint(-0.9723F, -0.1509F, -2.6084F);
		bone39.addChild(cube_r258);
		setRotationAngle(cube_r258, 1.3788F, 0.0F, 0.0F);
		cube_r258.setTextureOffset(127, 15).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r259 = new ModelRenderer(this);
		cube_r259.setRotationPoint(-0.9723F, -1.002F, -2.3482F);
		bone39.addChild(cube_r259);
		setRotationAngle(cube_r259, 1.1694F, 0.0F, 0.0F);
		cube_r259.setTextureOffset(127, 123).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r260 = new ModelRenderer(this);
		cube_r260.setRotationPoint(-0.9512F, -1.8293F, 0.0032F);
		bone39.addChild(cube_r260);
		setRotationAngle(cube_r260, -0.192F, 0.0F, 0.1222F);
		cube_r260.setTextureOffset(94, 47).addBox(-0.5F, -0.4375F, -0.375F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r261 = new ModelRenderer(this);
		cube_r261.setRotationPoint(-0.9723F, -1.6576F, -1.4881F);
		bone39.addChild(cube_r261);
		setRotationAngle(cube_r261, 0.1571F, 0.0F, 0.1222F);
		cube_r261.setTextureOffset(94, 65).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.1875F, false);

		cube_r262 = new ModelRenderer(this);
		cube_r262.setRotationPoint(-2.2184F, 0.7217F, 2.368F);
		bone39.addChild(cube_r262);
		setRotationAngle(cube_r262, 0.0F, -1.5708F, 0.0F);
		cube_r262.setTextureOffset(125, 75).addBox(-0.5F, -0.5F, -2.125F, 1.0F, 1.0F, 2.0F, -0.0625F, false);

		cube_r263 = new ModelRenderer(this);
		cube_r263.setRotationPoint(-0.2945F, 0.7217F, 1.487F);
		bone39.addChild(cube_r263);
		setRotationAngle(cube_r263, 0.0F, -0.6807F, 0.0F);
		cube_r263.setTextureOffset(113, 70).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 3.0F, -0.0625F, false);

		cube_r264 = new ModelRenderer(this);
		cube_r264.setRotationPoint(0.6132F, 0.7842F, -2.7993F);
		bone39.addChild(cube_r264);
		setRotationAngle(cube_r264, 0.0F, 1.3788F, 0.0F);
		cube_r264.setTextureOffset(32, 107).addBox(-0.5F, -0.5F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r265 = new ModelRenderer(this);
		cube_r265.setRotationPoint(0.9809F, 0.7842F, -2.5223F);
		bone39.addChild(cube_r265);
		setRotationAngle(cube_r265, 0.0F, 0.4712F, 0.0F);
		cube_r265.setTextureOffset(31, 79).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r266 = new ModelRenderer(this);
		cube_r266.setRotationPoint(0.2927F, -1.1576F, -0.6131F);
		bone39.addChild(cube_r266);
		setRotationAngle(cube_r266, 0.0F, 0.0F, 1.2392F);
		cube_r266.setTextureOffset(105, 32).addBox(0.25F, -0.5F, -2.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);

		cube_r267 = new ModelRenderer(this);
		cube_r267.setRotationPoint(0.0481F, -1.033F, -0.6131F);
		bone39.addChild(cube_r267);
		setRotationAngle(cube_r267, 0.0F, 0.0F, 0.6109F);
		cube_r267.setTextureOffset(77, 113).addBox(-0.375F, -0.5F, -2.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);

		bone40 = new ModelRenderer(this);
		bone40.setRotationPoint(-0.5113F, 2.9379F, 0.524F);
		bone39.addChild(bone40);
		setRotationAngle(bone40, 0.6192F, 0.1427F, -0.2794F);
		bone40.setTextureOffset(0, 122).addBox(-1.1485F, -3.1424F, 1.2692F, 2.0F, 3.0F, 1.0F, -0.0625F, false);
		bone40.setTextureOffset(121, 106).addBox(-1.1485F, -3.1424F, -2.3825F, 2.0F, 3.0F, 1.0F, -0.0625F, false);
		bone40.setTextureOffset(127, 30).addBox(-1.1485F, -0.3924F, 0.9879F, 2.0F, 2.0F, 1.0F, -0.25F, false);
		bone40.setTextureOffset(44, 29).addBox(-2.6485F, 0.4201F, -2.5121F, 5.0F, 1.0F, 5.0F, -0.5F, false);
		bone40.setTextureOffset(126, 104).addBox(-1.1485F, -0.3924F, -2.1334F, 2.0F, 2.0F, 1.0F, -0.25F, false);
		bone40.setTextureOffset(41, 115).addBox(-1.1485F, -0.7674F, 1.0817F, 2.0F, 2.0F, 1.0F, -0.1875F, false);
		bone40.setTextureOffset(5, 115).addBox(-1.1485F, -0.7674F, -2.2164F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r268 = new ModelRenderer(this);
		cube_r268.setRotationPoint(1.0617F, 0.1389F, -1.2776F);
		bone40.addChild(cube_r268);
		setRotationAngle(cube_r268, 0.0F, 2.3562F, 0.0F);
		cube_r268.setTextureOffset(0, 54).addBox(0.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
		cube_r268.setTextureOffset(85, 27).addBox(-0.5F, -0.6563F, -0.5625F, 1.0F, 1.0F, 1.0F, -0.1875F, false);
		cube_r268.setTextureOffset(25, 66).addBox(-1.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r269 = new ModelRenderer(this);
		cube_r269.setRotationPoint(1.0617F, 0.1389F, 1.1429F);
		bone40.addChild(cube_r269);
		setRotationAngle(cube_r269, 0.0F, 0.7854F, 0.0F);
		cube_r269.setTextureOffset(7, 71).addBox(0.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
		cube_r269.setTextureOffset(18, 86).addBox(-0.5F, -0.6563F, -0.5625F, 1.0F, 1.0F, 1.0F, -0.1875F, false);
		cube_r269.setTextureOffset(33, 72).addBox(-1.0625F, -0.8438F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r270 = new ModelRenderer(this);
		cube_r270.setRotationPoint(1.1034F, 0.0451F, 1.5118F);
		bone40.addChild(cube_r270);
		setRotationAngle(cube_r270, 0.0F, 0.7854F, 0.0F);
		cube_r270.setTextureOffset(96, 77).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r271 = new ModelRenderer(this);
		cube_r271.setRotationPoint(1.701F, 0.0451F, 0.8594F);
		bone40.addChild(cube_r271);
		setRotationAngle(cube_r271, 0.0F, 0.0F, 0.0F);
		cube_r271.setTextureOffset(97, 86).addBox(-2.6386F, -0.5F, 0.5837F, 2.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r272 = new ModelRenderer(this);
		cube_r272.setRotationPoint(-1.403F, -0.2049F, -0.2549F);
		bone40.addChild(cube_r272);
		setRotationAngle(cube_r272, 0.0F, -0.7854F, 0.0F);
		cube_r272.setTextureOffset(41, 72).addBox(-0.0429F, -0.5F, 0.4571F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r273 = new ModelRenderer(this);
		cube_r273.setRotationPoint(-1.2704F, -0.2674F, -0.2107F);
		bone40.addChild(cube_r273);
		setRotationAngle(cube_r273, 0.0F, -0.7854F, 0.0F);
		cube_r273.setTextureOffset(86, 86).addBox(0.3946F, -0.25F, 0.4571F, 1.0F, 1.0F, 1.0F, -0.1875F, false);

		cube_r274 = new ModelRenderer(this);
		cube_r274.setRotationPoint(-1.3146F, -0.2049F, -0.1665F);
		bone40.addChild(cube_r274);
		setRotationAngle(cube_r274, 0.0F, -0.7854F, 0.0F);
		cube_r274.setTextureOffset(72, 48).addBox(0.9571F, -0.5F, 0.4571F, 1.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r275 = new ModelRenderer(this);
		cube_r275.setRotationPoint(-2.0779F, -0.2674F, -1.4942F);
		bone40.addChild(cube_r275);
		setRotationAngle(cube_r275, 0.0F, 1.5708F, 0.0F);
		cube_r275.setTextureOffset(82, 24).addBox(-2.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r276 = new ModelRenderer(this);
		cube_r276.setRotationPoint(1.2202F, -0.2674F, -1.4942F);
		bone40.addChild(cube_r276);
		setRotationAngle(cube_r276, 0.0F, 1.5708F, 0.0F);
		cube_r276.setTextureOffset(22, 89).addBox(-2.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r277 = new ModelRenderer(this);
		cube_r277.setRotationPoint(-1.3146F, 0.2326F, -1.2334F);
		bone40.addChild(cube_r277);
		setRotationAngle(cube_r277, 0.0F, 0.7854F, 0.0F);
		cube_r277.setTextureOffset(91, 94).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		cube_r278 = new ModelRenderer(this);
		cube_r278.setRotationPoint(1.1318F, 0.1076F, -1.4995F);
		bone40.addChild(cube_r278);
		setRotationAngle(cube_r278, 0.0F, 1.5708F, 0.0F);
		cube_r278.setTextureOffset(54, 126).addBox(-2.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r279 = new ModelRenderer(this);
		cube_r279.setRotationPoint(1.2354F, 0.1076F, -0.2495F);
		bone40.addChild(cube_r279);
		setRotationAngle(cube_r279, -3.1416F, 0.7854F, -3.1416F);
		cube_r279.setTextureOffset(126, 92).addBox(-0.1464F, -0.5F, -0.0429F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r280 = new ModelRenderer(this);
		cube_r280.setRotationPoint(-1.2521F, 0.1076F, 0.1772F);
		bone40.addChild(cube_r280);
		setRotationAngle(cube_r280, -3.1416F, -0.7854F, -3.1416F);
		cube_r280.setTextureOffset(127, 12).addBox(-1.9571F, -0.5F, 0.4571F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r281 = new ModelRenderer(this);
		cube_r281.setRotationPoint(-1.9895F, 0.1076F, -1.4995F);
		bone40.addChild(cube_r281);
		setRotationAngle(cube_r281, 0.0F, 1.5708F, 0.0F);
		cube_r281.setTextureOffset(21, 127).addBox(-2.4268F, -0.5F, -0.2197F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r282 = new ModelRenderer(this);
		cube_r282.setRotationPoint(-1.2521F, 0.1076F, -0.3228F);
		bone40.addChild(cube_r282);
		setRotationAngle(cube_r282, 0.0F, -0.7854F, 0.0F);
		cube_r282.setTextureOffset(127, 21).addBox(-0.0429F, -0.5F, 0.4571F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r283 = new ModelRenderer(this);
		cube_r283.setRotationPoint(1.3086F, 0.1076F, 0.6772F);
		bone40.addChild(cube_r283);
		setRotationAngle(cube_r283, 0.0F, 0.7854F, 0.0F);
		cube_r283.setTextureOffset(47, 127).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);

		cube_r284 = new ModelRenderer(this);
		cube_r284.setRotationPoint(-1.4396F, -1.6424F, 0.0058F);
		bone40.addChild(cube_r284);
		setRotationAngle(cube_r284, -3.1416F, -0.7854F, -3.1416F);
		cube_r284.setTextureOffset(62, 49).addBox(-1.9571F, -1.5F, 0.4571F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r285 = new ModelRenderer(this);
		cube_r285.setRotationPoint(-1.4396F, -1.6424F, -0.1192F);
		bone40.addChild(cube_r285);
		setRotationAngle(cube_r285, 0.0F, -0.7854F, 0.0F);
		cube_r285.setTextureOffset(121, 48).addBox(-0.0429F, -1.5F, 0.4571F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r286 = new ModelRenderer(this);
		cube_r286.setRotationPoint(1.4229F, -1.6424F, -0.4209F);
		bone40.addChild(cube_r286);
		setRotationAngle(cube_r286, -3.1416F, 0.7854F, -3.1416F);
		cube_r286.setTextureOffset(77, 121).addBox(-0.1464F, -1.5F, -0.0429F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r287 = new ModelRenderer(this);
		cube_r287.setRotationPoint(-2.2547F, -1.6424F, -1.4834F);
		bone40.addChild(cube_r287);
		setRotationAngle(cube_r287, 0.0F, 1.5708F, 0.0F);
		cube_r287.setTextureOffset(83, 121).addBox(-2.4268F, -1.5F, -0.2197F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r288 = new ModelRenderer(this);
		cube_r288.setRotationPoint(1.397F, -1.6424F, -1.4834F);
		bone40.addChild(cube_r288);
		setRotationAngle(cube_r288, 0.0F, 1.5708F, 0.0F);
		cube_r288.setTextureOffset(89, 121).addBox(-2.4268F, -1.5F, -0.2197F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		cube_r289 = new ModelRenderer(this);
		cube_r289.setRotationPoint(1.4961F, -1.6424F, 0.8808F);
		bone40.addChild(cube_r289);
		setRotationAngle(cube_r289, 0.0F, 0.7854F, 0.0F);
		cube_r289.setTextureOffset(95, 121).addBox(-1.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, -0.0625F, false);

		bone36 = new ModelRenderer(this);
		bone36.setRotationPoint(0.0F, 0.0F, 0.0F);
		JS.addChild(bone36);
		

		cube_r290 = new ModelRenderer(this);
		cube_r290.setRotationPoint(-1.0F, 14.3693F, 14.4572F);
		bone36.addChild(cube_r290);
		setRotationAngle(cube_r290, -0.1222F, 0.0F, 0.0F);
		cube_r290.setTextureOffset(20, 16).addBox(-1.5F, -0.375F, -1.5F, 5.0F, 1.0F, 3.0F, -0.0625F, false);

		cube_r291 = new ModelRenderer(this);
		cube_r291.setRotationPoint(-1.5F, 15.0781F, 14.7188F);
		bone36.addChild(cube_r291);
		setRotationAngle(cube_r291, -0.2618F, 0.0F, 0.0F);
		cube_r291.setTextureOffset(45, 52).addBox(-2.0F, -0.9063F, -2.5625F, 7.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r292 = new ModelRenderer(this);
		cube_r292.setRotationPoint(-1.0F, 16.3424F, 11.919F);
		bone36.addChild(cube_r292);
		setRotationAngle(cube_r292, -1.501F, 0.0F, 0.0F);
		cube_r292.setTextureOffset(40, 0).addBox(-2.5F, -1.5F, -2.5F, 7.0F, 3.0F, 4.0F, 0.0F, false);

		cube_r293 = new ModelRenderer(this);
		cube_r293.setRotationPoint(-1.0F, 16.3589F, 12.7398F);
		bone36.addChild(cube_r293);
		setRotationAngle(cube_r293, -0.733F, 0.0F, 0.0F);
		cube_r293.setTextureOffset(52, 14).addBox(-2.5F, -1.5F, -1.5F, 7.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r294 = new ModelRenderer(this);
		cube_r294.setRotationPoint(0.0F, 16.7018F, 12.0753F);
		bone36.addChild(cube_r294);
		setRotationAngle(cube_r294, -1.501F, 0.0F, 0.0F);
		cube_r294.setTextureOffset(0, 54).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 4.0F, 0.0F, false);

		cube_r295 = new ModelRenderer(this);
		cube_r295.setRotationPoint(-0.5F, 16.7182F, 12.8961F);
		bone36.addChild(cube_r295);
		setRotationAngle(cube_r295, -0.733F, 0.0F, 0.0F);
		cube_r295.setTextureOffset(82, 75).addBox(-1.5F, -1.0F, -1.25F, 4.0F, 1.0F, 3.0F, 0.1875F, false);
		cube_r295.setTextureOffset(69, 24).addBox(-2.0F, -1.5F, -1.5F, 5.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r296 = new ModelRenderer(this);
		cube_r296.setRotationPoint(0.5F, 15.4375F, 14.875F);
		bone36.addChild(cube_r296);
		setRotationAngle(cube_r296, -0.2618F, 0.0F, 0.0F);
		cube_r296.setTextureOffset(12, 25).addBox(0.625F, -0.2813F, -2.4375F, 1.0F, 1.0F, 3.0F, 0.125F, false);
		cube_r296.setTextureOffset(20, 66).addBox(-2.625F, -0.2813F, -2.4375F, 1.0F, 1.0F, 3.0F, 0.125F, false);
		cube_r296.setTextureOffset(62, 8).addBox(-3.0F, -1.9063F, -2.5625F, 5.0F, 3.0F, 3.0F, 0.0F, false);

		cube_r297 = new ModelRenderer(this);
		cube_r297.setRotationPoint(1.625F, 15.762F, 15.241F);
		bone36.addChild(cube_r297);
		setRotationAngle(cube_r297, -0.2778F, 0.3367F, -0.0939F);
		cube_r297.setTextureOffset(20, 0).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);

		cube_r298 = new ModelRenderer(this);
		cube_r298.setRotationPoint(3.0111F, 15.3896F, 16.9537F);
		bone36.addChild(cube_r298);
		setRotationAngle(cube_r298, 0.0264F, 0.9755F, -0.4765F);
		cube_r298.setTextureOffset(59, 27).addBox(-0.7937F, -0.5F, -1.6299F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r299 = new ModelRenderer(this);
		cube_r299.setRotationPoint(3.0111F, 15.3896F, 16.9537F);
		bone36.addChild(cube_r299);
		setRotationAngle(cube_r299, 3.0875F, 1.2929F, 2.5912F);
		cube_r299.setTextureOffset(58, 8).addBox(-0.7937F, -0.5F, -0.3701F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r300 = new ModelRenderer(this);
		cube_r300.setRotationPoint(4.2937F, 9.3397F, 9.8348F);
		bone36.addChild(cube_r300);
		setRotationAngle(cube_r300, -0.4973F, 0.721F, -0.3441F);
		cube_r300.setTextureOffset(31, 80).addBox(-1.0625F, 0.0625F, -4.25F, 1.0F, 1.0F, 5.0F, 0.0F, false);

		cube_r301 = new ModelRenderer(this);
		cube_r301.setRotationPoint(4.8424F, 11.0687F, 14.3389F);
		bone36.addChild(cube_r301);
		setRotationAngle(cube_r301, -0.3665F, 0.0F, 0.0F);
		cube_r301.setTextureOffset(40, 57).addBox(-1.0625F, 0.0625F, -4.25F, 1.0F, 1.0F, 5.0F, 0.0F, false);
		cube_r301.setTextureOffset(82, 29).addBox(-9.6222F, 0.0625F, -4.25F, 1.0F, 1.0F, 5.0F, 0.0F, false);

		cube_r302 = new ModelRenderer(this);
		cube_r302.setRotationPoint(4.8424F, 13.8076F, 17.196F);
		bone36.addChild(cube_r302);
		setRotationAngle(cube_r302, -0.8552F, 0.0F, 0.0F);
		cube_r302.setTextureOffset(48, 77).addBox(-1.0625F, 0.0625F, -3.25F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		cube_r302.setTextureOffset(96, 81).addBox(-9.6222F, 0.0625F, -3.25F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		cube_r303 = new ModelRenderer(this);
		cube_r303.setRotationPoint(-4.2937F, 9.3397F, 9.8348F);
		bone36.addChild(cube_r303);
		setRotationAngle(cube_r303, -0.4973F, -0.721F, 0.3441F);
		cube_r303.setTextureOffset(81, 80).addBox(0.0625F, 0.0625F, -4.25F, 1.0F, 1.0F, 5.0F, 0.0F, false);

		cube_r304 = new ModelRenderer(this);
		cube_r304.setRotationPoint(-3.0111F, 15.3896F, 16.9537F);
		bone36.addChild(cube_r304);
		setRotationAngle(cube_r304, 3.0875F, -1.2929F, -2.5912F);
		cube_r304.setTextureOffset(69, 14).addBox(-0.2063F, -0.5F, -0.3701F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r305 = new ModelRenderer(this);
		cube_r305.setRotationPoint(-3.0111F, 15.3896F, 16.9537F);
		bone36.addChild(cube_r305);
		setRotationAngle(cube_r305, 0.0264F, -0.9755F, 0.4765F);
		cube_r305.setTextureOffset(73, 19).addBox(-0.2063F, -0.5F, -1.6299F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r306 = new ModelRenderer(this);
		cube_r306.setRotationPoint(-1.625F, 15.762F, 15.241F);
		bone36.addChild(cube_r306);
		setRotationAngle(cube_r306, -0.2778F, -0.3367F, 0.0939F);
		cube_r306.setTextureOffset(36, 0).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.addChild(bone11);
		

		cube_r307 = new ModelRenderer(this);
		cube_r307.setRotationPoint(-4.2246F, -0.4945F, 5.8213F);
		bone11.addChild(cube_r307);
		setRotationAngle(cube_r307, -0.134F, -0.3666F, 0.1114F);
		cube_r307.setTextureOffset(8, 86).addBox(-2.0F, -2.1875F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r308 = new ModelRenderer(this);
		cube_r308.setRotationPoint(4.8301F, 0.8114F, 4.9892F);
		bone11.addChild(cube_r308);
		setRotationAngle(cube_r308, -0.3879F, 1.0253F, 0.3992F);
		cube_r308.setTextureOffset(75, 69).addBox(-1.5F, -1.5F, -2.375F, 4.0F, 3.0F, 3.0F, 0.0F, false);

		cube_r309 = new ModelRenderer(this);
		cube_r309.setRotationPoint(-2.7131F, 2.2379F, 4.8132F);
		bone11.addChild(cube_r309);
		setRotationAngle(cube_r309, 0.2731F, -0.11F, 0.0076F);
		cube_r309.setTextureOffset(101, 9).addBox(-2.9375F, -1.8125F, -1.4375F, 3.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r310 = new ModelRenderer(this);
		cube_r310.setRotationPoint(0.0876F, 0.5068F, 3.9483F);
		bone11.addChild(cube_r310);
		setRotationAngle(cube_r310, 0.244F, 0.2039F, -0.0886F);
		cube_r310.setTextureOffset(32, 16).addBox(-4.25F, -1.3125F, -2.1875F, 8.0F, 3.0F, 4.0F, 0.0F, false);

		cube_r311 = new ModelRenderer(this);
		cube_r311.setRotationPoint(1.4722F, -2.75F, 5.9375F);
		bone11.addChild(cube_r311);
		setRotationAngle(cube_r311, 0.1552F, 0.1249F, -0.1423F);
		cube_r311.setTextureOffset(66, 63).addBox(-4.0F, -0.25F, -1.0F, 6.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r312 = new ModelRenderer(this);
		cube_r312.setRotationPoint(-5.8125F, -1.375F, 2.8125F);
		bone11.addChild(cube_r312);
		setRotationAngle(cube_r312, -0.0049F, 0.0679F, 0.1859F);
		cube_r312.setTextureOffset(64, 79).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 3.0F, 4.0F, 0.0F, false);

		cube_r313 = new ModelRenderer(this);
		cube_r313.setRotationPoint(-5.625F, 0.6875F, -3.0F);
		bone11.addChild(cube_r313);
		setRotationAngle(cube_r313, 0.027F, 0.7799F, 0.1576F);
		cube_r313.setTextureOffset(14, 110).addBox(-0.8125F, -1.0F, -0.125F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r314 = new ModelRenderer(this);
		cube_r314.setRotationPoint(-6.1875F, -1.3125F, -1.125F);
		bone11.addChild(cube_r314);
		setRotationAngle(cube_r314, 0.1625F, -0.3769F, 0.0847F);
		cube_r314.setTextureOffset(61, 103).addBox(-1.0F, -0.1875F, -2.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r315 = new ModelRenderer(this);
		cube_r315.setRotationPoint(-6.25F, -2.3125F, 1.0F);
		bone11.addChild(cube_r315);
		setRotationAngle(cube_r315, 0.1557F, 0.244F, 0.1829F);
		cube_r315.setTextureOffset(103, 4).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r316 = new ModelRenderer(this);
		cube_r316.setRotationPoint(6.0F, -1.4375F, -0.125F);
		bone11.addChild(cube_r316);
		setRotationAngle(cube_r316, 0.1516F, -0.0888F, -0.1585F);
		cube_r316.setTextureOffset(99, 103).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r317 = new ModelRenderer(this);
		cube_r317.setRotationPoint(4.9375F, 0.875F, -3.0F);
		bone11.addChild(cube_r317);
		setRotationAngle(cube_r317, 0.027F, -0.7799F, -0.1576F);
		cube_r317.setTextureOffset(115, 116).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r318 = new ModelRenderer(this);
		cube_r318.setRotationPoint(-3.625F, 1.25F, -3.4375F);
		bone11.addChild(cube_r318);
		setRotationAngle(cube_r318, -0.1047F, 0.0F, 0.2618F);
		cube_r318.setTextureOffset(107, 106).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r319 = new ModelRenderer(this);
		cube_r319.setRotationPoint(0.0F, 0.8125F, -3.625F);
		bone11.addChild(cube_r319);
		setRotationAngle(cube_r319, -0.2821F, 0.1328F, -0.1173F);
		cube_r319.setTextureOffset(56, 19).addBox(-4.0F, -0.875F, -1.6875F, 7.0F, 2.0F, 3.0F, 0.0F, false);

		bone19 = new ModelRenderer(this);
		bone19.setRotationPoint(-4.1756F, 3.6503F, 4.2635F);
		bone11.addChild(bone19);
		

		cube_r320 = new ModelRenderer(this);
		cube_r320.setRotationPoint(4.5245F, -2.2626F, -1.1927F);
		bone19.addChild(cube_r320);
		setRotationAngle(cube_r320, -0.4458F, 0.1119F, -0.2639F);
		cube_r320.setTextureOffset(61, 98).addBox(-1.9375F, -1.125F, 0.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r321 = new ModelRenderer(this);
		cube_r321.setRotationPoint(10.7739F, -3.181F, 0.5005F);
		bone19.addChild(cube_r321);
		setRotationAngle(cube_r321, 0.3095F, -0.2268F, -0.2187F);
		cube_r321.setTextureOffset(19, 81).addBox(-0.6875F, -0.875F, -1.0F, 1.0F, 2.0F, 1.0F, 0.1875F, false);

		cube_r322 = new ModelRenderer(this);
		cube_r322.setRotationPoint(8.3512F, -3.0558F, 3.3465F);
		bone19.addChild(cube_r322);
		setRotationAngle(cube_r322, 0.3072F, 0.3279F, 0.1187F);
		cube_r322.setTextureOffset(109, 18).addBox(-0.5625F, -1.5F, -2.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r323 = new ModelRenderer(this);
		cube_r323.setRotationPoint(-1.2094F, -4.9862F, 1.5424F);
		bone19.addChild(cube_r323);
		setRotationAngle(cube_r323, 0.2233F, -0.8642F, -0.0601F);
		cube_r323.setTextureOffset(99, 108).addBox(-3.0F, -1.75F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r324 = new ModelRenderer(this);
		cube_r324.setRotationPoint(1.1811F, -4.6513F, 2.9145F);
		bone19.addChild(cube_r324);
		setRotationAngle(cube_r324, 0.2958F, -0.1899F, -0.0406F);
		cube_r324.setTextureOffset(8, 93).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r325 = new ModelRenderer(this);
		cube_r325.setRotationPoint(5.0763F, -5.2653F, 2.3511F);
		bone19.addChild(cube_r325);
		setRotationAngle(cube_r325, 0.3008F, 0.2613F, 0.0968F);
		cube_r325.setTextureOffset(109, 36).addBox(-0.6875F, -1.625F, -0.5625F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r326 = new ModelRenderer(this);
		cube_r326.setRotationPoint(0.0F, 0.4375F, 0.0F);
		bone19.addChild(cube_r326);
		setRotationAngle(cube_r326, 0.0293F, -0.2147F, 0.0045F);
		cube_r326.setTextureOffset(6, 110).addBox(-1.0F, -1.5F, -1.125F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		Left_Arm = new ModelRenderer(this);
		Left_Arm.setRotationPoint(5.0F, 2.0F, 0.0F);
		Left_Arm.setTextureOffset(32, 33).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0625F, false);
		Left_Arm.setTextureOffset(72, 48).addBox(-1.0F, 5.75F, -2.0F, 4.0F, 1.0F, 4.0F, 0.1875F, false);
		Left_Arm.setTextureOffset(95, 71).addBox(2.25F, 7.5938F, -2.0F, 1.0F, 2.0F, 4.0F, 0.1875F, false);
		Left_Arm.setTextureOffset(111, 123).addBox(3.0F, 7.5F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		Left_Arm.setTextureOffset(105, 123).addBox(2.875F, 7.875F, -1.9375F, 1.0F, 2.0F, 2.0F, -0.1875F, false);
		Left_Arm.setTextureOffset(61, 123).addBox(2.875F, 7.875F, -0.0625F, 1.0F, 2.0F, 2.0F, -0.1875F, false);
		Left_Arm.setTextureOffset(72, 43).addBox(-1.0F, 1.75F, -2.0F, 4.0F, 1.0F, 4.0F, 0.1875F, false);

		cube_r327 = new ModelRenderer(this);
		cube_r327.setRotationPoint(0.4527F, 6.25F, -1.6736F);
		Left_Arm.addChild(cube_r327);
		setRotationAngle(cube_r327, 0.0F, -0.3316F, 0.0F);
		cube_r327.setTextureOffset(129, 36).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.1875F, false);

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(0.0F, -1.0F, 0.0F);
		Left_Arm.addChild(bone12);
		

		cube_r328 = new ModelRenderer(this);
		cube_r328.setRotationPoint(2.9375F, 5.0F, 1.1294F);
		bone12.addChild(cube_r328);
		setRotationAngle(cube_r328, 0.0F, -0.144F, 0.0F);
		cube_r328.setTextureOffset(56, 106).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 6.0F, 2.0F, 0.25F, false);

		cube_r329 = new ModelRenderer(this);
		cube_r329.setRotationPoint(2.9375F, 5.0F, -1.1294F);
		bone12.addChild(cube_r329);
		setRotationAngle(cube_r329, 0.0F, 0.144F, 0.0F);
		cube_r329.setTextureOffset(0, 107).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 6.0F, 2.0F, 0.25F, false);

		cube_r330 = new ModelRenderer(this);
		cube_r330.setRotationPoint(3.625F, 3.0F, 1.0938F);
		bone12.addChild(cube_r330);
		setRotationAngle(cube_r330, 0.0F, -0.2182F, 0.0F);
		cube_r330.setTextureOffset(10, 124).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, -0.125F, false);
		cube_r330.setTextureOffset(123, 123).addBox(-0.5F, 1.0625F, -1.0F, 1.0F, 2.0F, 2.0F, -0.125F, false);
		cube_r330.setTextureOffset(117, 123).addBox(-0.5F, 3.125F, -1.0F, 1.0F, 2.0F, 2.0F, -0.125F, false);

		cube_r331 = new ModelRenderer(this);
		cube_r331.setRotationPoint(3.625F, 7.125F, -1.0938F);
		bone12.addChild(cube_r331);
		setRotationAngle(cube_r331, 0.0F, 0.2182F, 0.0F);
		cube_r331.setTextureOffset(16, 124).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, -0.125F, false);
		cube_r331.setTextureOffset(28, 124).addBox(-0.5F, -3.0625F, -1.0F, 1.0F, 2.0F, 2.0F, -0.125F, false);
		cube_r331.setTextureOffset(34, 124).addBox(-0.5F, -5.125F, -1.0F, 1.0F, 2.0F, 2.0F, -0.125F, false);

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(3.125F, -1.3125F, -3.0F);
		Left_Arm.addChild(bone13);
		

		cube_r332 = new ModelRenderer(this);
		cube_r332.setRotationPoint(-1.4732F, 0.516F, 3.6747F);
		bone13.addChild(cube_r332);
		setRotationAngle(cube_r332, 0.1062F, -0.5171F, -0.1595F);
		cube_r332.setTextureOffset(36, 29).addBox(-2.0F, -2.5F, -2.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);

		cube_r333 = new ModelRenderer(this);
		cube_r333.setRotationPoint(0.5893F, 0.7035F, 3.0497F);
		bone13.addChild(cube_r333);
		setRotationAngle(cube_r333, -0.0337F, 0.0977F, -0.3416F);
		cube_r333.setTextureOffset(16, 103).addBox(-1.0F, -2.5F, -1.1875F, 2.0F, 5.0F, 2.0F, 0.0F, false);

		cube_r334 = new ModelRenderer(this);
		cube_r334.setRotationPoint(-2.5625F, -0.3125F, 4.4375F);
		bone13.addChild(cube_r334);
		setRotationAngle(cube_r334, 0.0971F, -0.0293F, -0.127F);
		cube_r334.setTextureOffset(93, 4).addBox(-1.6875F, -1.0F, -0.4375F, 3.0F, 5.0F, 2.0F, 0.0F, false);

		cube_r335 = new ModelRenderer(this);
		cube_r335.setRotationPoint(-0.75F, -0.4375F, 4.5625F);
		bone13.addChild(cube_r335);
		setRotationAngle(cube_r335, 0.123F, -0.6263F, -0.3017F);
		cube_r335.setTextureOffset(46, 97).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		cube_r336 = new ModelRenderer(this);
		cube_r336.setRotationPoint(-0.5625F, -0.0625F, 1.5625F);
		bone13.addChild(cube_r336);
		setRotationAngle(cube_r336, -0.1745F, 0.3142F, -0.2618F);
		cube_r336.setTextureOffset(91, 107).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		bone42 = new ModelRenderer(this);
		bone42.setRotationPoint(-0.75F, -0.4375F, 4.5625F);
		bone13.addChild(bone42);
		

		cube_r337 = new ModelRenderer(this);
		cube_r337.setRotationPoint(1.5138F, 0.8811F, -3.53F);
		bone42.addChild(cube_r337);
		setRotationAngle(cube_r337, -0.1521F, 0.4324F, -0.1832F);
		cube_r337.setTextureOffset(85, 113).addBox(-1.9375F, -2.0F, -1.5625F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r338 = new ModelRenderer(this);
		cube_r338.setRotationPoint(-0.25F, 0.0F, 0.0F);
		bone42.addChild(cube_r338);
		setRotationAngle(cube_r338, 0.1458F, -0.353F, -0.3787F);
		cube_r338.setTextureOffset(105, 96).addBox(0.0938F, -1.4375F, -2.125F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		Right_Arm = new ModelRenderer(this);
		Right_Arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		Right_Arm.setTextureOffset(0, 25).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0625F, false);
		Right_Arm.setTextureOffset(14, 57).addBox(-3.0F, 4.0F, -2.0F, 4.0F, 5.0F, 4.0F, 0.1875F, false);
		Right_Arm.setTextureOffset(41, 72).addBox(-3.0F, 4.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.25F, false);
		Right_Arm.setTextureOffset(76, 75).addBox(-3.0F, 3.0F, -2.0F, 1.0F, 6.0F, 4.0F, 0.3125F, false);

		bone14 = new ModelRenderer(this);
		bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
		Right_Arm.addChild(bone14);
		bone14.setTextureOffset(16, 88).addBox(-4.0F, -1.75F, -2.0F, 1.0F, 3.0F, 4.0F, 0.125F, false);
		bone14.setTextureOffset(39, 85).addBox(-3.5F, -2.25F, -2.0F, 1.0F, 4.0F, 4.0F, 0.125F, false);
		bone14.setTextureOffset(41, 77).addBox(-3.5F, -1.75F, -2.5F, 1.0F, 3.0F, 5.0F, 0.125F, false);

		bone17 = new ModelRenderer(this);
		bone17.setRotationPoint(-3.6875F, 8.3125F, -0.8958F);
		Right_Arm.addChild(bone17);
		setRotationAngle(bone17, -0.0521F, 0.0468F, -0.1073F);
		bone17.setTextureOffset(117, 80).addBox(-1.2529F, -1.0026F, -1.8282F, 2.0F, 1.0F, 2.0F, -0.125F, false);

		cube_r339 = new ModelRenderer(this);
		cube_r339.setRotationPoint(0.0003F, -0.5026F, 0.6719F);
		bone17.addChild(cube_r339);
		setRotationAngle(cube_r339, 0.0F, 0.0F, 1.5708F);
		cube_r339.setTextureOffset(102, 79).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, -0.3125F, false);

		cube_r340 = new ModelRenderer(this);
		cube_r340.setRotationPoint(0.0F, -0.4993F, -0.1405F);
		bone17.addChild(cube_r340);
		setRotationAngle(cube_r340, 0.0F, 0.0F, 1.5708F);
		cube_r340.setTextureOffset(89, 102).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, -0.125F, false);

		cube_r341 = new ModelRenderer(this);
		cube_r341.setRotationPoint(-0.0014F, -0.4863F, 2.1098F);
		bone17.addChild(cube_r341);
		setRotationAngle(cube_r341, 0.0F, 0.0F, 1.5708F);
		cube_r341.setTextureOffset(128, 101).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		bone15 = new ModelRenderer(this);
		bone15.setRotationPoint(-3.8125F, 6.375F, -1.0208F);
		Right_Arm.addChild(bone15);
		setRotationAngle(bone15, -0.0349F, 0.0F, 0.0F);
		bone15.setTextureOffset(38, 79).addBox(-1.25F, -1.0015F, -1.828F, 2.0F, 1.0F, 2.0F, -0.125F, false);

		cube_r342 = new ModelRenderer(this);
		cube_r342.setRotationPoint(0.0F, -0.5015F, 0.672F);
		bone15.addChild(cube_r342);
		setRotationAngle(cube_r342, 0.0F, 0.0F, 1.5708F);
		cube_r342.setTextureOffset(102, 39).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, -0.3125F, false);

		cube_r343 = new ModelRenderer(this);
		cube_r343.setRotationPoint(0.0F, -0.4993F, -0.1405F);
		bone15.addChild(cube_r343);
		setRotationAngle(cube_r343, 0.0F, 0.0F, 1.5708F);
		cube_r343.setTextureOffset(102, 74).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, -0.125F, false);

		cube_r344 = new ModelRenderer(this);
		cube_r344.setRotationPoint(0.0F, -0.4906F, 2.1097F);
		bone15.addChild(cube_r344);
		setRotationAngle(cube_r344, 0.0F, 0.0F, 1.5708F);
		cube_r344.setTextureOffset(128, 98).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1875F, false);

		bone16 = new ModelRenderer(this);
		bone16.setRotationPoint(-3.875F, 4.0804F, -0.4567F);
		Right_Arm.addChild(bone16);
		setRotationAngle(bone16, 0.0096F, 0.0336F, 1.8502F);
		bone16.setTextureOffset(128, 95).addBox(-0.9351F, -0.9916F, 1.1252F, 2.0F, 2.0F, 1.0F, -0.1875F, false);
		bone16.setTextureOffset(28, 102).addBox(-0.9375F, -1.0F, -2.125F, 2.0F, 2.0F, 3.0F, -0.125F, false);
		bone16.setTextureOffset(7, 68).addBox(-1.1875F, -0.5F, -2.3125F, 2.0F, 1.0F, 2.0F, -0.125F, false);
		bone16.setTextureOffset(0, 102).addBox(-0.9381F, -1.0021F, -1.3125F, 2.0F, 2.0F, 3.0F, -0.3125F, false);

		bone18 = new ModelRenderer(this);
		bone18.setRotationPoint(-0.5625F, -1.625F, 1.4375F);
		Right_Arm.addChild(bone18);
		

		cube_r345 = new ModelRenderer(this);
		cube_r345.setRotationPoint(-2.579F, 0.1073F, -3.5069F);
		bone18.addChild(cube_r345);
		setRotationAngle(cube_r345, -0.4997F, -1.1624F, 0.6201F);
		cube_r345.setTextureOffset(24, 107).addBox(-1.0F, -1.0F, -1.1875F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		cube_r346 = new ModelRenderer(this);
		cube_r346.setRotationPoint(-3.1518F, 1.016F, -1.3878F);
		bone18.addChild(cube_r346);
		setRotationAngle(cube_r346, 0.1015F, 0.0502F, 0.2695F);
		cube_r346.setTextureOffset(29, 96).addBox(-1.0F, -2.3281F, -2.25F, 2.0F, 3.0F, 3.0F, 0.0F, false);

		cube_r347 = new ModelRenderer(this);
		cube_r347.setRotationPoint(-1.0893F, 0.8285F, -0.7628F);
		bone18.addChild(cube_r347);
		setRotationAngle(cube_r347, 0.1062F, 0.5171F, 0.1595F);
		cube_r347.setTextureOffset(16, 51).addBox(-1.625F, -2.625F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

		cube_r348 = new ModelRenderer(this);
		cube_r348.setRotationPoint(-0.6856F, -0.1671F, 0.7062F);
		bone18.addChild(cube_r348);
		setRotationAngle(cube_r348, 2.1087F, 1.2813F, 2.3684F);
		cube_r348.setTextureOffset(32, 72).addBox(-1.0F, -1.0F, -2.5F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		cube_r349 = new ModelRenderer(this);
		cube_r349.setRotationPoint(-0.6455F, 0.4036F, -3.3001F);
		bone18.addChild(cube_r349);
		setRotationAngle(cube_r349, 2.743F, -1.3547F, -2.6806F);
		cube_r349.setTextureOffset(15, 96).addBox(-1.375F, -1.5F, -1.75F, 2.0F, 3.0F, 3.0F, 0.0F, false);

		bone41 = new ModelRenderer(this);
		bone41.setRotationPoint(-4.0F, 0.0F, 0.0F);
		bone18.addChild(bone41);
		

		cube_r350 = new ModelRenderer(this);
		cube_r350.setRotationPoint(2.1812F, 0.479F, -4.3202F);
		bone41.addChild(cube_r350);
		setRotationAngle(cube_r350, -0.1696F, 0.1988F, 0.437F);
		cube_r350.setTextureOffset(93, 113).addBox(-0.5625F, -1.0F, -1.4375F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r351 = new ModelRenderer(this);
		cube_r351.setRotationPoint(0.7603F, -0.461F, -3.2049F);
		bone41.addChild(cube_r351);
		setRotationAngle(cube_r351, -0.0293F, -0.0772F, 0.3514F);
		cube_r351.setTextureOffset(37, 66).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r352 = new ModelRenderer(this);
		cube_r352.setRotationPoint(0.8382F, -0.9825F, -0.1126F);
		bone41.addChild(cube_r352);
		setRotationAngle(cube_r352, 0.3727F, 0.3733F, 0.4412F);
		cube_r352.setTextureOffset(113, 98).addBox(-1.0F, -0.9375F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		Right_leg = new ModelRenderer(this);
		Right_leg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		Right_leg.setTextureOffset(24, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0625F, false);

		cube_r353 = new ModelRenderer(this);
		cube_r353.setRotationPoint(-3.25F, 1.875F, -0.5F);
		Right_leg.addChild(cube_r353);
		setRotationAngle(cube_r353, 0.0F, -0.1047F, 0.0F);
		cube_r353.setTextureOffset(0, 61).addBox(-0.5F, -2.0F, -1.625F, 1.0F, 4.0F, 2.0F, 0.0625F, false);

		cube_r354 = new ModelRenderer(this);
		cube_r354.setRotationPoint(-3.25F, 1.875F, 0.5F);
		Right_leg.addChild(cube_r354);
		setRotationAngle(cube_r354, 0.0F, 0.1047F, 0.0F);
		cube_r354.setTextureOffset(111, 112).addBox(-0.5F, -2.0F, -0.375F, 1.0F, 4.0F, 2.0F, 0.0625F, false);

		bone20 = new ModelRenderer(this);
		bone20.setRotationPoint(0.0F, -0.3125F, 0.0F);
		Right_leg.addChild(bone20);
		bone20.setTextureOffset(117, 0).addBox(-1.5F, 4.5625F, -2.6875F, 3.0F, 3.0F, 1.0F, 0.125F, false);
		bone20.setTextureOffset(116, 91).addBox(-1.5F, 4.5625F, -3.0625F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		bone20.setTextureOffset(99, 59).addBox(-2.0F, 6.4375F, 0.1875F, 4.0F, 1.0F, 2.0F, 0.125F, false);
		bone20.setTextureOffset(99, 68).addBox(-2.0F, 4.6875F, 0.1875F, 4.0F, 1.0F, 2.0F, 0.125F, false);
		bone20.setTextureOffset(54, 75).addBox(-2.0F, 4.5625F, -1.9375F, 4.0F, 3.0F, 3.0F, 0.1875F, false);

		cube_r355 = new ModelRenderer(this);
		cube_r355.setRotationPoint(-1.4437F, 6.0625F, -2.1186F);
		bone20.addChild(cube_r355);
		setRotationAngle(cube_r355, 0.0F, 0.8552F, 0.0F);
		cube_r355.setTextureOffset(121, 123).addBox(-0.3246F, -1.5F, -0.6617F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r355.setTextureOffset(115, 123).addBox(-0.3246F, 0.5F, -0.6617F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r356 = new ModelRenderer(this);
		cube_r356.setRotationPoint(-1.5368F, 8.0625F, -2.1463F);
		bone20.addChild(cube_r356);
		setRotationAngle(cube_r356, 0.0F, 0.8552F, 0.0F);
		cube_r356.setTextureOffset(0, 25).addBox(-0.6801F, -3.5F, -0.3786F, 1.0F, 3.0F, 1.0F, 0.125F, false);

		cube_r357 = new ModelRenderer(this);
		cube_r357.setRotationPoint(1.5368F, 8.0625F, -2.1463F);
		bone20.addChild(cube_r357);
		setRotationAngle(cube_r357, 0.0F, -0.8552F, 0.0F);
		cube_r357.setTextureOffset(40, 7).addBox(-0.3199F, -3.5F, -0.3786F, 1.0F, 3.0F, 1.0F, 0.125F, false);

		cube_r358 = new ModelRenderer(this);
		cube_r358.setRotationPoint(1.4437F, 8.0625F, -2.1186F);
		bone20.addChild(cube_r358);
		setRotationAngle(cube_r358, 0.0F, -0.8552F, 0.0F);
		cube_r358.setTextureOffset(14, 124).addBox(-0.6754F, -1.5F, -0.6617F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r358.setTextureOffset(32, 124).addBox(-0.6754F, -3.5F, -0.6617F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		bone21 = new ModelRenderer(this);
		bone21.setRotationPoint(0.0F, -0.125F, 0.0F);
		Right_leg.addChild(bone21);
		bone21.setTextureOffset(0, 69).addBox(-2.9375F, 0.0F, -2.5F, 1.0F, 4.0F, 5.0F, 0.0F, false);
		bone21.setTextureOffset(21, 72).addBox(-2.0F, 0.5625F, -2.0F, 4.0F, 1.0F, 4.0F, 0.25F, false);
		bone21.setTextureOffset(72, 14).addBox(-2.0F, 2.4375F, -2.0F, 4.0F, 1.0F, 4.0F, 0.25F, false);
		bone21.setTextureOffset(82, 100).addBox(-2.0625F, 0.5625F, -2.0F, 1.0F, 1.0F, 4.0F, 0.375F, false);
		bone21.setTextureOffset(100, 91).addBox(-2.0625F, 2.4375F, -2.0F, 1.0F, 1.0F, 4.0F, 0.375F, false);
		bone21.setTextureOffset(99, 86).addBox(0.8125F, 0.5625F, -2.0F, 1.0F, 1.0F, 4.0F, 0.3125F, false);
		bone21.setTextureOffset(99, 98).addBox(0.8125F, 2.4375F, -2.0F, 1.0F, 1.0F, 4.0F, 0.3125F, false);

		bone22 = new ModelRenderer(this);
		bone22.setRotationPoint(-0.5F, 1.125F, 0.5F);
		Right_leg.addChild(bone22);
		bone22.setTextureOffset(84, 14).addBox(-1.5F, 7.3125F, -2.5F, 4.0F, 1.0F, 3.0F, 0.1875F, false);
		bone22.setTextureOffset(114, 46).addBox(-1.5F, 8.3125F, -2.5F, 4.0F, 1.0F, 1.0F, 0.375F, false);
		bone22.setTextureOffset(97, 21).addBox(-1.5F, 8.3125F, -1.0625F, 4.0F, 1.0F, 2.0F, 0.3125F, false);
		bone22.setTextureOffset(41, 66).addBox(-1.5F, 8.875F, -2.5F, 4.0F, 2.0F, 4.0F, 0.125F, false);

		cube_r359 = new ModelRenderer(this);
		cube_r359.setRotationPoint(0.5F, 9.875F, 0.625F);
		bone22.addChild(cube_r359);
		setRotationAngle(cube_r359, 0.2094F, 0.0F, 0.0F);
		cube_r359.setTextureOffset(99, 27).addBox(-2.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.25F, false);

		cube_r360 = new ModelRenderer(this);
		cube_r360.setRotationPoint(4.5F, 9.0625F, 1.25F);
		bone22.addChild(cube_r360);
		setRotationAngle(cube_r360, -0.0698F, 0.0F, 0.0F);
		cube_r360.setTextureOffset(33, 86).addBox(-6.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.4375F, false);

		cube_r361 = new ModelRenderer(this);
		cube_r361.setRotationPoint(4.5F, 8.0625F, 1.2969F);
		bone22.addChild(cube_r361);
		setRotationAngle(cube_r361, -0.1222F, 0.0F, 0.0F);
		cube_r361.setTextureOffset(109, 23).addBox(-6.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.3125F, false);

		cube_r362 = new ModelRenderer(this);
		cube_r362.setRotationPoint(0.5F, 7.625F, -2.375F);
		bone22.addChild(cube_r362);
		setRotationAngle(cube_r362, 0.1745F, 0.0F, 0.0F);
		cube_r362.setTextureOffset(53, 66).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		bone23 = new ModelRenderer(this);
		bone23.setRotationPoint(-2.798F, 7.8905F, 0.2205F);
		Right_leg.addChild(bone23);
		setRotationAngle(bone23, -1.5708F, -0.7854F, -1.5708F);
		bone23.setTextureOffset(118, 52).addBox(-1.0F, -2.9219F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		bone23.setTextureOffset(49, 123).addBox(-1.0F, 0.7656F, -0.5F, 2.0F, 3.0F, 1.0F, -0.125F, false);
		bone23.setTextureOffset(130, 44).addBox(-1.0F, -1.9219F, -0.5F, 2.0F, 1.0F, 1.0F, 0.125F, false);
		bone23.setTextureOffset(22, 130).addBox(-1.0F, -0.4219F, -0.5F, 2.0F, 1.0F, 1.0F, 0.125F, false);

		Left_Leg = new ModelRenderer(this);
		Left_Leg.setRotationPoint(2.0F, 12.0F, 0.0F);
		Left_Leg.setTextureOffset(20, 21).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0625F, false);

		bone25 = new ModelRenderer(this);
		bone25.setRotationPoint(0.0F, -0.125F, 0.0F);
		Left_Leg.addChild(bone25);
		bone25.setTextureOffset(71, 38).addBox(-2.0F, 0.5625F, -2.0F, 4.0F, 1.0F, 4.0F, 0.25F, false);
		bone25.setTextureOffset(68, 70).addBox(1.9375F, 0.0F, -2.5F, 1.0F, 4.0F, 5.0F, 0.0F, false);
		bone25.setTextureOffset(84, 44).addBox(2.25F, 0.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		bone25.setTextureOffset(71, 29).addBox(-2.0F, 2.4375F, -2.0F, 4.0F, 1.0F, 4.0F, 0.25F, false);
		bone25.setTextureOffset(76, 99).addBox(1.0625F, 0.5625F, -2.0F, 1.0F, 1.0F, 4.0F, 0.375F, false);
		bone25.setTextureOffset(54, 99).addBox(1.0625F, 2.4375F, -2.0F, 1.0F, 1.0F, 4.0F, 0.375F, false);
		bone25.setTextureOffset(8, 99).addBox(-1.8125F, 0.5625F, -2.0F, 1.0F, 1.0F, 4.0F, 0.3125F, false);
		bone25.setTextureOffset(35, 98).addBox(-1.8125F, 2.4375F, -2.0F, 1.0F, 1.0F, 4.0F, 0.3125F, false);

		bone26 = new ModelRenderer(this);
		bone26.setRotationPoint(0.5F, 1.125F, 0.5F);
		Left_Leg.addChild(bone26);
		bone26.setTextureOffset(83, 37).addBox(-2.5F, 7.3125F, -2.5F, 4.0F, 1.0F, 3.0F, 0.1875F, false);
		bone26.setTextureOffset(113, 89).addBox(-2.5F, 8.3125F, -2.5F, 4.0F, 1.0F, 1.0F, 0.375F, false);
		bone26.setTextureOffset(97, 65).addBox(-2.5F, 8.3125F, -1.0625F, 4.0F, 1.0F, 2.0F, 0.3125F, false);
		bone26.setTextureOffset(25, 66).addBox(-2.5F, 8.875F, -2.5F, 4.0F, 2.0F, 4.0F, 0.125F, false);

		cube_r363 = new ModelRenderer(this);
		cube_r363.setRotationPoint(-0.5F, 9.875F, 0.625F);
		bone26.addChild(cube_r363);
		setRotationAngle(cube_r363, 0.2094F, 0.0F, 0.0F);
		cube_r363.setTextureOffset(99, 24).addBox(-2.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.25F, false);

		cube_r364 = new ModelRenderer(this);
		cube_r364.setRotationPoint(-4.5F, 9.0625F, 1.25F);
		bone26.addChild(cube_r364);
		setRotationAngle(cube_r364, -0.0698F, 0.0F, 0.0F);
		cube_r364.setTextureOffset(38, 52).addBox(2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.4375F, false);

		cube_r365 = new ModelRenderer(this);
		cube_r365.setRotationPoint(-4.5F, 8.0625F, 1.2969F);
		bone26.addChild(cube_r365);
		setRotationAngle(cube_r365, -0.1222F, 0.0F, 0.0F);
		cube_r365.setTextureOffset(106, 92).addBox(2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.3125F, false);

		cube_r366 = new ModelRenderer(this);
		cube_r366.setRotationPoint(-0.5F, 7.625F, -2.375F);
		bone26.addChild(cube_r366);
		setRotationAngle(cube_r366, 0.1745F, 0.0F, 0.0F);
		cube_r366.setTextureOffset(65, 54).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		bone24 = new ModelRenderer(this);
		bone24.setRotationPoint(0.0F, -0.3125F, 0.0F);
		Left_Leg.addChild(bone24);
		bone24.setTextureOffset(113, 48).addBox(-1.5F, 4.5625F, -2.6875F, 3.0F, 3.0F, 1.0F, 0.125F, false);
		bone24.setTextureOffset(113, 26).addBox(-1.5F, 4.5625F, -3.0625F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		bone24.setTextureOffset(99, 17).addBox(-2.0F, 6.4375F, 0.1875F, 4.0F, 1.0F, 2.0F, 0.125F, false);
		bone24.setTextureOffset(0, 99).addBox(-2.0F, 4.6875F, 0.1875F, 4.0F, 1.0F, 2.0F, 0.125F, false);
		bone24.setTextureOffset(9, 75).addBox(-2.0F, 4.5625F, -1.9375F, 4.0F, 3.0F, 3.0F, 0.1875F, false);

		cube_r367 = new ModelRenderer(this);
		cube_r367.setRotationPoint(1.5368F, 8.0625F, -2.1463F);
		bone24.addChild(cube_r367);
		setRotationAngle(cube_r367, 0.0F, -0.8552F, 0.0F);
		cube_r367.setTextureOffset(0, 0).addBox(-0.3199F, -3.5F, -0.3786F, 1.0F, 3.0F, 1.0F, 0.125F, false);

		cube_r368 = new ModelRenderer(this);
		cube_r368.setRotationPoint(-1.5368F, 8.0625F, -2.1463F);
		bone24.addChild(cube_r368);
		setRotationAngle(cube_r368, 0.0F, 0.8552F, 0.0F);
		cube_r368.setTextureOffset(0, 16).addBox(-0.6801F, -3.5F, -0.3786F, 1.0F, 3.0F, 1.0F, 0.125F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Left_Arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Right_Arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Right_leg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Left_Leg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}