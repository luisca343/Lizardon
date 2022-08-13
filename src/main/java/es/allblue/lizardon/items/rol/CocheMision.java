package es.allblue.lizardon.items.rol;

import es.allblue.lizardon.blocks.BloqueCemento;
import es.allblue.lizardon.items.templates.ItemBasic;
import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.entities.instances.PartEngine;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperItemStack;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;
import minecrafttransportsimulator.mcinterface.WrapperWorld;
import minecrafttransportsimulator.systems.ConfigSystem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CocheMision extends ItemBasic {
    public CocheMision(String name) {
        super(name);
    }
    @Override
    public EnumActionResult onItemUse(EntityPlayer player1, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        WrapperWorld world = WrapperWorld.getWrapperFor(worldIn);
        WrapperPlayer player = WrapperPlayer.getWrapperFor(player1);

        IBlockState state = worldIn.getBlockState(pos);
        /*
        if (!(state.getBlock() instanceof BloqueCemento)) {
            player1.sendMessage(new TextComponentString("No puedes colocar el vehículo ahí."));
            return EnumActionResult.FAIL;
        }*/

        if (!world.isClient()) {
            WrapperItemStack heldStack = player.getHeldStack();
            WrapperNBT data = heldStack.getData();
            populateDefaultData(data);
            boolean wasSaved = !data.getString("uniqueUUID").isEmpty();

            EntityVehicleF_Physics vehicle = createEntity(world, player, data);

            if (!wasSaved) {
                for (JSONPartDefinition partDef : vehicle.definition.parts) {
                    vehicle.addDefaultPart(partDef, player, vehicle.definition, true, false);
                }
                if (vehicle.definition.motorized.defaultFuelQty > 0) {
                    for (APart part : vehicle.partsFromNBT) {
                        if (part instanceof PartEngine) {
                            String mostPotentFluid = "";
                            for (String fluidName : ConfigSystem.configObject.fuel.fuels.get(part.definition.engine.fuelType).keySet()) {
                                if (mostPotentFluid.isEmpty() || ConfigSystem.configObject.fuel.fuels.get(part.definition.engine.fuelType).get(mostPotentFluid) < ConfigSystem.configObject.fuel.fuels.get(part.definition.engine.fuelType).get(fluidName)) {
                                    mostPotentFluid = fluidName;
                                }
                            }
                            vehicle.fuelTank.manuallySet(mostPotentFluid, vehicle.definition.motorized.defaultFuelQty);
                            break;
                        }
                    }
                    if (vehicle.fuelTank.getFluid().isEmpty()) {
                        vehicle.remove();
                        throw new IllegalArgumentException("A defaultFuelQty was specified for: " + vehicle.definition.packID + ":" + vehicle.definition.systemName + ", but no engine was noted as a defaultPart, so we don't know what fuel to put in the vehicle.");
                    }
                }
            }

            vehicle.world.spawnEntity(vehicle);

            if (!player.isCreative()) {
                ItemStack objeto = player1.getHeldItem(hand);
                if (objeto.getTagCompound().hasKey("mision")) {
                    player.getInventory().removeFromSlot(player.getHotbarIndex(), 1);
                }
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("ownerUUID", objeto.getTagCompound().getString("ownerUUID"));
                objeto.setTagCompound(nbt);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public void populateDefaultData(WrapperNBT data) {
        data.setPoint3d("position", new Point3d());
        data.setPoint3d("motion", new Point3d());
        data.setPoint3d("angles", new Point3d());
        data.setPoint3d("rotation", new Point3d());
    }

    public EntityVehicleF_Physics createEntity(WrapperWorld world, WrapperPlayer placingPlayer, WrapperNBT data) {
        return new EntityVehicleF_Physics(world, placingPlayer, data);
    }


    public Class<EntityVehicleF_Physics> getEntityClass() {
        return EntityVehicleF_Physics.class;
    }
}
