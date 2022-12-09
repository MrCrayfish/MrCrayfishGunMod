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

public class MediumArmor extends ArmorBase {
	private final ModelRenderer Bulletproofvest_2;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer bone8;
	private final ModelRenderer bone3;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer bone4;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer bone;
	private final ModelRenderer bone5;
	private final ModelRenderer bone6;
	private final ModelRenderer bone2;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer cube_r13;
	private final ModelRenderer bone7;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;
	private final ModelRenderer cube_r17;
	private final ModelRenderer cube_r18;
	private final ModelRenderer bone9;
	private final ModelRenderer cube_r19;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer bone10;
	private final ModelRenderer cube_r22;
	private final ModelRenderer cube_r23;
	private final ModelRenderer cube_r24;

	public MediumArmor() {
		textureWidth = 64;
		textureHeight = 64;

		Bulletproofvest_2 = new ModelRenderer(this);
		Bulletproofvest_2.setRotationPoint(0.0F, 24.0F, 0.0F);
		Bulletproofvest_2.setTextureOffset(0, 14).addBox(-4.0F, -20.0F, -3.5F, 8.0F, 8.0F, 2.0F, 0.0F, false);
		Bulletproofvest_2.setTextureOffset(0, 0).addBox(-4.0F, -24.0F, 2.0F, 8.0F, 12.0F, 1.0F, 0.0F, false);
		Bulletproofvest_2.setTextureOffset(24, 11).addBox(4.0F, -18.0F, -2.5F, 1.0F, 6.0F, 5.0F, 0.0F, false);
		Bulletproofvest_2.setTextureOffset(16, 20).addBox(-5.0F, -18.0F, -2.5F, 1.0F, 6.0F, 5.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, -21.4033F, -2.7715F);
		Bulletproofvest_2.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.1571F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(29, 0).addBox(-3.0F, -1.5F, -0.5F, 6.0F, 3.0F, 1.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, -23.8125F, 3.6309F);
		Bulletproofvest_2.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.9055F, 0.9798F, 0.8137F);
		cube_r2.setTextureOffset(51, 11).addBox(-0.0799F, -1.0F, 1.0427F, 3.0F, 2.0F, 1.0F, -0.3125F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, -23.8125F, 3.6309F);
		Bulletproofvest_2.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.4538F, 0.0F, 0.0F);
		cube_r3.setTextureOffset(42, 23).addBox(-2.0F, -1.0F, -0.2403F, 4.0F, 2.0F, 1.0F, -0.3125F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, -23.8125F, 3.6309F);
		Bulletproofvest_2.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.9055F, -0.9798F, -0.8137F);
		cube_r4.setTextureOffset(18, 50).addBox(-2.9201F, -1.0F, 1.0427F, 3.0F, 2.0F, 1.0F, -0.3125F, false);

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
		Bulletproofvest_2.addChild(bone8);
		bone8.setTextureOffset(29, 23).addBox(0.75F, -18.875F, -5.4375F, 3.0F, 4.0F, 3.0F, -0.375F, false);
		bone8.setTextureOffset(26, 40).addBox(0.75F, -17.6875F, -4.875F, 3.0F, 5.0F, 1.0F, 0.0F, false);
		bone8.setTextureOffset(28, 31).addBox(0.75F, -18.6875F, -3.875F, 3.0F, 6.0F, 1.0F, 0.0F, false);

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(-2.75F, -15.0673F, -3.3711F);
		Bulletproofvest_2.addChild(bone3);
		setRotationAngle(bone3, 0.0F, 0.1047F, 0.0F);
		bone3.setTextureOffset(35, 38).addBox(-0.9477F, -1.6202F, -0.5636F, 2.0F, 4.0F, 2.0F, -0.1875F, false);
		bone3.setTextureOffset(33, 45).addBox(-0.9477F, -0.6202F, -1.4386F, 2.0F, 3.0F, 2.0F, -0.25F, false);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(0.875F, -1.4505F, -0.1565F);
		bone3.addChild(cube_r5);
		setRotationAngle(cube_r5, -0.1134F, 0.0F, 0.0F);
		cube_r5.setTextureOffset(36, 51).addBox(-1.8227F, -0.0062F, -1.4941F, 2.0F, 3.0F, 1.0F, -0.375F, false);

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(0.875F, -1.1202F, -0.1914F);
		bone3.addChild(cube_r6);
		setRotationAngle(cube_r6, -0.2443F, 0.0F, 0.0F);
		cube_r6.setTextureOffset(51, 25).addBox(-1.8227F, -0.4422F, -1.4825F, 2.0F, 1.0F, 2.0F, -0.375F, false);

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-1.0F, -15.0673F, -3.4961F);
		Bulletproofvest_2.addChild(bone4);
		setRotationAngle(bone4, 0.0F, 0.0349F, 0.0F);
		bone4.setTextureOffset(37, 31).addBox(-0.9826F, -1.6202F, -0.566F, 2.0F, 4.0F, 2.0F, -0.1875F, false);
		bone4.setTextureOffset(44, 27).addBox(-0.9826F, -0.6202F, -1.441F, 2.0F, 3.0F, 2.0F, -0.25F, false);

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(-0.875F, -1.4505F, -0.1565F);
		bone4.addChild(cube_r7);
		setRotationAngle(cube_r7, -0.1134F, 0.0F, 0.0F);
		cube_r7.setTextureOffset(51, 40).addBox(-0.1076F, -0.0059F, -1.4965F, 2.0F, 3.0F, 1.0F, -0.375F, false);

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(-0.875F, -1.1202F, -0.1914F);
		bone4.addChild(cube_r8);
		setRotationAngle(cube_r8, -0.2443F, 0.0F, 0.0F);
		cube_r8.setTextureOffset(27, 51).addBox(-0.1076F, -0.4416F, -1.4849F, 2.0F, 1.0F, 2.0F, -0.375F, false);

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 0.0F, 0.0F);
		Bulletproofvest_2.addChild(bone);
		bone.setTextureOffset(0, 25).addBox(4.1875F, -17.6875F, -2.0F, 3.0F, 4.0F, 4.0F, -0.1875F, false);
		bone.setTextureOffset(19, 0).addBox(4.875F, -17.375F, -2.5F, 2.0F, 5.0F, 5.0F, -0.125F, false);

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(-5.8125F, -14.5F, -1.5F);
		Bulletproofvest_2.addChild(bone5);
		setRotationAngle(bone5, 0.164F, -0.1496F, -0.1577F);
		bone5.setTextureOffset(44, 38).addBox(-0.875F, -3.5F, -2.375F, 2.0F, 6.0F, 1.0F, -0.1875F, false);
		bone5.setTextureOffset(44, 13).addBox(-0.875F, -3.5F, -0.125F, 2.0F, 6.0F, 1.0F, -0.1875F, false);
		bone5.setTextureOffset(49, 50).addBox(-0.875F, 1.875F, -1.75F, 2.0F, 1.0F, 2.0F, -0.1875F, false);
		bone5.setTextureOffset(37, 14).addBox(0.75F, -3.5F, -1.75F, 1.0F, 6.0F, 2.0F, -0.1875F, false);
		bone5.setTextureOffset(34, 5).addBox(-1.5F, -3.5F, -1.75F, 1.0F, 6.0F, 2.0F, -0.1875F, false);

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(-5.8125F, -14.5F, 1.8125F);
		Bulletproofvest_2.addChild(bone6);
		setRotationAngle(bone6, 0.0944F, -0.1391F, -0.2807F);
		bone6.setTextureOffset(9, 41).addBox(-0.875F, -3.5F, -2.375F, 2.0F, 6.0F, 1.0F, -0.1875F, false);
		bone6.setTextureOffset(41, 5).addBox(-0.875F, -3.5F, -0.125F, 2.0F, 6.0F, 1.0F, -0.1875F, false);
		bone6.setTextureOffset(49, 19).addBox(-0.875F, 1.875F, -1.75F, 2.0F, 1.0F, 2.0F, -0.1875F, false);
		bone6.setTextureOffset(20, 32).addBox(0.75F, -3.5F, -1.75F, 1.0F, 6.0F, 2.0F, -0.1875F, false);
		bone6.setTextureOffset(13, 32).addBox(-1.5F, -3.5F, -1.75F, 1.0F, 6.0F, 2.0F, -0.1875F, false);

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(3.6727F, -23.6804F, -0.1056F);
		Bulletproofvest_2.addChild(bone2);
		setRotationAngle(bone2, 0.0F, 0.0F, -0.0175F);
		

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(-0.1601F, 0.3865F, -1.8659F);
		bone2.addChild(cube_r9);
		setRotationAngle(cube_r9, 3.0369F, 0.0F, 0.3927F);
		cube_r9.setTextureOffset(0, 41).addBox(-1.5F, -3.5F, -0.5F, 3.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(0.3079F, -0.7434F, -1.5763F);
		bone2.addChild(cube_r10);
		setRotationAngle(cube_r10, 1.885F, 0.0F, 0.3927F);
		cube_r10.setTextureOffset(51, 36).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(0.2488F, -0.6006F, 1.4433F);
		bone2.addChild(cube_r11);
		setRotationAngle(cube_r11, 1.5533F, 0.0F, 0.3927F);
		cube_r11.setTextureOffset(48, 6).addBox(-1.5F, -2.875F, 0.0F, 3.0F, 3.0F, 1.0F, 0.125F, false);

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(0.2488F, -0.6006F, 1.4433F);
		bone2.addChild(cube_r12);
		setRotationAngle(cube_r12, 1.2566F, 0.0F, 0.3927F);
		cube_r12.setTextureOffset(9, 50).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(-0.4618F, 1.1149F, 2.1543F);
		bone2.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.0524F, 0.0F, 0.3927F);
		cube_r13.setTextureOffset(42, 46).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, 0.0F, false);

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(-3.6727F, -23.6804F, -0.1056F);
		Bulletproofvest_2.addChild(bone7);
		setRotationAngle(bone7, 0.0F, 0.0F, 0.0175F);
		

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(0.1601F, 0.3865F, -1.8659F);
		bone7.addChild(cube_r14);
		setRotationAngle(cube_r14, 3.0369F, 0.0F, -0.3927F);
		cube_r14.setTextureOffset(0, 34).addBox(-1.5F, -3.5F, -0.5F, 3.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(-0.3079F, -0.7434F, -1.5763F);
		bone7.addChild(cube_r15);
		setRotationAngle(cube_r15, 1.885F, 0.0F, -0.3927F);
		cube_r15.setTextureOffset(51, 15).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r16 = new ModelRenderer(this);
		cube_r16.setRotationPoint(-0.2488F, -0.6006F, 1.4433F);
		bone7.addChild(cube_r16);
		setRotationAngle(cube_r16, 1.5533F, 0.0F, -0.3927F);
		cube_r16.setTextureOffset(19, 11).addBox(-1.5F, -2.875F, 0.0F, 3.0F, 3.0F, 1.0F, 0.125F, false);

		cube_r17 = new ModelRenderer(this);
		cube_r17.setRotationPoint(-0.2488F, -0.6006F, 1.4433F);
		bone7.addChild(cube_r17);
		setRotationAngle(cube_r17, 1.2566F, 0.0F, -0.3927F);
		cube_r17.setTextureOffset(0, 48).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		cube_r18 = new ModelRenderer(this);
		cube_r18.setRotationPoint(0.4618F, 1.1149F, 2.1543F);
		bone7.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.0524F, 0.0F, -0.3927F);
		cube_r18.setTextureOffset(16, 41).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, 0.0F, false);

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(-6.5745F, -15.507F, -3.6935F);
		Bulletproofvest_2.addChild(bone9);
		setRotationAngle(bone9, 0.5342F, -0.1943F, 1.4105F);
		bone9.setTextureOffset(51, 45).addBox(-0.2579F, -0.4644F, -0.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r19 = new ModelRenderer(this);
		cube_r19.setRotationPoint(-1.0741F, -0.0553F, 0.0F);
		bone9.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.0F, 0.0F, -0.0349F);
		cube_r19.setTextureOffset(27, 47).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r20 = new ModelRenderer(this);
		cube_r20.setRotationPoint(0.4006F, 0.0779F, -0.0625F);
		bone9.addChild(cube_r20);
		setRotationAngle(cube_r20, 0.0F, 0.0F, 0.2967F);
		cube_r20.setTextureOffset(47, 3).addBox(-2.0F, -0.5F, -0.4375F, 4.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r21 = new ModelRenderer(this);
		cube_r21.setRotationPoint(1.523F, -0.0537F, 0.0F);
		bone9.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.0F, 0.0F, -0.2269F);
		cube_r21.setTextureOffset(16, 47).addBox(-3.0625F, -0.75F, -0.5F, 4.0F, 1.0F, 1.0F, -0.3125F, false);

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(-7.0745F, -15.882F, -0.3185F);
		Bulletproofvest_2.addChild(bone10);
		setRotationAngle(bone10, 0.812F, -0.138F, 1.4072F);
		bone10.setTextureOffset(11, 25).addBox(-0.2579F, -0.4644F, -0.5F, 1.0F, 1.0F, 1.0F, -0.125F, false);

		cube_r22 = new ModelRenderer(this);
		cube_r22.setRotationPoint(-1.0741F, -0.0553F, 0.0F);
		bone10.addChild(cube_r22);
		setRotationAngle(cube_r22, 0.0F, 0.0F, -0.0349F);
		cube_r22.setTextureOffset(19, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r23 = new ModelRenderer(this);
		cube_r23.setRotationPoint(0.4006F, 0.0779F, -0.0625F);
		bone10.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.0F, 0.0F, 0.2967F);
		cube_r23.setTextureOffset(44, 0).addBox(-2.0F, -0.5F, -0.4375F, 4.0F, 1.0F, 1.0F, -0.3125F, false);

		cube_r24 = new ModelRenderer(this);
		cube_r24.setRotationPoint(1.523F, -0.0537F, 0.0F);
		bone10.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, 0.0F, -0.2269F);
		cube_r24.setTextureOffset(46, 33).addBox(-3.0625F, -0.75F, -0.5F, 4.0F, 1.0F, 1.0F, -0.3125F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		matrixStack.translate(0, 1.5, 0);
		Bulletproofvest_2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected ModelRenderer getModel() {
		return Bulletproofvest_2;
	}

	// Updatable in order to allow for damaged textures as well to be applied
	private static ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/armor/bulletproof_vest_2.png");

	@Override
	protected ResourceLocation getTexture() {
		return TEXTURE;
	}

	@Override
	protected void setTexture(String modId, String path) {
		TEXTURE = new ResourceLocation(modId, path);
	}
}