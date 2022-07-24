package com.tac.guns.block;

import com.google.gson.GsonBuilder;
import com.tac.guns.Config;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModTileEntities;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import com.tac.guns.tileentity.WorkbenchTileEntity;
import com.tac.guns.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.tac.guns.GunMod.LOGGER;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class UpgradeBenchBlock extends RotatedObjectBlock
{
    private final Map<BlockState, VoxelShape> SHAPES = new HashMap<>();

    public UpgradeBenchBlock(Properties properties)
    {
        super(properties);
    }

    private VoxelShape getShape(BlockState state)
    {
        if(SHAPES.containsKey(state))
        {
            return SHAPES.get(state);
        }
        Direction direction = state.get(HORIZONTAL_FACING);
        List<VoxelShape> shapes = new ArrayList<>();
        shapes.add(Block.makeCuboidShape(0.5, 0, 0.5, 15.5, 13, 15.5));
        shapes.add(Block.makeCuboidShape(0, 13, 0, 16, 15, 16));
        shapes.add(VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 15, 0, 16, 16, 2), Direction.SOUTH))[direction.getHorizontalIndex()]);
        VoxelShape shape = VoxelShapeHelper.combineAll(shapes);
        SHAPES.put(state, shape);
        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return this.getShape(state);
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return this.getShape(state);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result)
    {
        if(!world.isRemote())
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof INamedContainerProvider)
            {
                Chunk chunk = Minecraft.getInstance().world.getChunkAt(result.getPos());
                UpgradeBenchTileEntity blockTileEntity = ModTileEntities.UPGRADE_BENCH.get().getIfExists(Minecraft.getInstance().world.getBlockReader(chunk.getPos().x, chunk.getPos().z), result.getPos());
                if(playerEntity.getHeldItemMainhand().getItem() instanceof TimelessGunItem && blockTileEntity != null) {
                    if(blockTileEntity.getStackInSlot(0) == null || blockTileEntity.getStackInSlot(0) == ItemStack.EMPTY) {
                        blockTileEntity.getInventory().set(0, playerEntity.getHeldItemMainhand());
                        playerEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.AIR));
                    }
                    else
                    {
                        /*GsonBuilder gsonB = new GsonBuilder().setLenient().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting();//.setNumberToNumberStrategy(ToNumberPolicy.DOUBLE).setObjectToNumberStrategy(ToNumberPolicy.DOUBLE).serializeSpecialFloatingPointValues();;

                        String jsonString = blockTileEntity.getStackInSlot(0).getTag().toString();*//*.toString()*//*;//gson.toJson(ch.getCatGlobal(1).get(this.previousWeaponTag));
                        File dir = new File(Config.COMMON.development.TDevPath.get()+"\\tac_export\\");
                        dir.mkdir();
                        try {
                            FileWriter dataWriter = new FileWriter (dir.getAbsolutePath() +"\\"+ "WoahPaskgangaDov" + "_export.json");
                            dataWriter.write(jsonString);
                            dataWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        LOGGER.log(Level.INFO, "WEAPON EDITOR EXPORTED FILE ( "+"WoahPaskgangaDov" + "_export.txt ). BE PROUD!");
*/
                        if(!playerEntity.addItemStackToInventory(blockTileEntity.getStackInSlot(0))) {
                            InventoryHelper.spawnItemStack(Minecraft.getInstance().world, result.getPos().getX() + 0.5, result.getPos().getY() + 1.125, result.getPos().getZ() + 0.5, blockTileEntity.getStackInSlot(0));
                        }
                        blockTileEntity.getInventory().set(0, playerEntity.getHeldItemMainhand());
                        playerEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.AIR));
                    }
                }
                else if(playerEntity.isCrouching())
                {
                    NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) tileEntity, pos);
                }
                else {
                    if(blockTileEntity != null && !playerEntity.addItemStackToInventory(blockTileEntity.getStackInSlot(0)))
                    {
                        blockTileEntity.getInventory().set(0, ItemStack.EMPTY);
                        InventoryHelper.spawnItemStack(Minecraft.getInstance().world, result.getPos().getX() + 0.5, result.getPos().getY() + 1.125, result.getPos().getZ() + 0.5, blockTileEntity.getStackInSlot(0));
                    }

                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    /*@Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
    {
        super.tick(state, worldIn, pos, rand);
        ItemFrameRenderer
    }*/

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new UpgradeBenchTileEntity();
    }
}
