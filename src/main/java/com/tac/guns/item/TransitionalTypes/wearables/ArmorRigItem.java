package com.tac.guns.item.TransitionalTypes.wearables;

import com.tac.guns.Reference;
import com.tac.guns.client.InputHandler;
import com.tac.guns.common.NetworkRigManager;
import com.tac.guns.common.Rig;
import com.tac.guns.inventory.gear.armor.ArmorRigCapabilityProvider;
import com.tac.guns.inventory.gear.armor.ArmorRigContainerProvider;
import com.tac.guns.inventory.gear.armor.ArmorRigInventoryCapability;
import com.tac.guns.inventory.gear.armor.RigSlotsHandler;
import com.tac.guns.util.RigEnchantmentHelper;
import com.tac.guns.util.WearableHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ArmorRigItem extends Item implements IArmoredRigItem {
    public ArmorRigItem(Properties properties) {
        super(properties);
        numOfRows = 1;
    }

    private final int numOfRows;
    public int getNumOfRows() {
        return this.numOfRows;
    }

    public ArmorRigItem(/*String model, */int rows, Properties properties)
    {
        super(properties);
        //this.armorModelName = model;
        this.numOfRows = rows
        ;
    }
    private ArmorRigContainerProvider containerProvider;
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(world.isRemote) return super.onItemRightClick(world, player, hand);
        if(hand != Hand.MAIN_HAND) return ActionResult.resultPass(player.getHeldItem(hand));
        containerProvider = new ArmorRigContainerProvider(player.getHeldItem(hand));
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider);
        super.onItemRightClick(world, player, hand);
        return ActionResult.resultPass(player.getHeldItem(hand));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ArmorRigInventoryCapability();
    }

    private WeakHashMap<CompoundNBT, Rig> modifiedRigCache = new WeakHashMap<>();

    private Rig rig = new Rig();

    public void setRig(NetworkRigManager.Supplier supplier)
    {
        this.rig = supplier.getRig();
    }

    public Rig getRig()
    {
        return this.rig;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
       super.addInformation(stack, worldIn, tooltip, flag);

       tooltip.add(new TranslationTextComponent("info.tac.current_armor_amount").append(new TranslationTextComponent(ItemStack.DECIMALFORMAT.format(WearableHelper.GetCurrentDurability(stack))+"")).mergeStyle(TextFormatting.BLUE));
       int scancode = GLFW.glfwGetKeyScancode(InputHandler.ARMOR_REPAIRING.getKeyCode());
       if(GLFW.glfwGetKeyName(InputHandler.ARMOR_REPAIRING.getKeyCode(),scancode) != null)
           tooltip.add((new TranslationTextComponent("info.tac.tac_armor_repair1").append(new TranslationTextComponent(GLFW.glfwGetKeyName(InputHandler.ARMOR_REPAIRING.getKeyCode(), scancode)).mergeStyle(TextFormatting.AQUA)).append(new TranslationTextComponent("info.tac.tac_armor_repair2"))).mergeStyle(TextFormatting.YELLOW));
    }

    @Override
    public boolean shouldSyncTag() {return true;}

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack)
    {
        stack.getOrCreateTag();
        CompoundNBT nbt = super.getShareTag(stack);
        if (stack.getItem() instanceof ArmorRigItem) {
            RigSlotsHandler itemHandler = (RigSlotsHandler) stack.getCapability(ArmorRigCapabilityProvider.capability).resolve().get();
            nbt.put("storage", itemHandler.serializeNBT());
        }

        return nbt;
    }



    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return true;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> stacks)
    {
        if(this.isInGroup(group))
        {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag();
            WearableHelper.FillDefaults(stack, this.rig);
            stacks.add(stack);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        stack.getOrCreateTag();
        Rig modifiedRig = this.getModifiedRig(stack);
        return 1.0 - (WearableHelper.GetCurrentDurability(stack) / (double) RigEnchantmentHelper.getModifiedDurability(stack, modifiedRig));
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        return Objects.requireNonNull(TextFormatting.AQUA.getColor());
    }

    public Rig getModifiedRig(ItemStack stack)
    {
        CompoundNBT tagCompound = stack.getTag();
        if(tagCompound != null && tagCompound.contains("Rig", Constants.NBT.TAG_COMPOUND))
        {
            return this.modifiedRigCache.computeIfAbsent(tagCompound, item ->
            {
                if(tagCompound.getBoolean("Custom"))
                {
                    return Rig.create(tagCompound.getCompound("Rig"));
                }
                else
                {
                    Rig gunCopy = this.rig.copy();
                    gunCopy.deserializeNBT(tagCompound.getCompound("Rig"));
                    return gunCopy;
                }
            });
        }
        return this.rig;
    }

    /*@Override
    public ArmorBase getArmorModelName() {
        return this.armorModelName == null ? new ModernArmor() : this.armorModelName;
    }*/
}
