package es.allblue.lizardon.commands;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.init.ItemsInit;
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
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Test extends CommandBase {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.test.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayer)) {
            return;
        }
        WrapperPlayer player = WrapperPlayer.getWrapperFor((EntityPlayer) sender);
        WrapperWorld world = player.getWorld();
        if (!world.isClient()) {
            try {
                NBTTagCompound data = JsonToNBT.getTagFromJson("{variables1:\"doorleft\",motionz:0.0d,variables2:\"doorright\",variables0:\"doorhood\",motiony:0.0d,motionx:0.0d,Invulnerable:0b,entityid:\"EntityVehicleF_Physics\",savedRiderLocationscount:0,radio:{volume:0,randomOrder:0b,currentURL:\"\",currentSource:0,savedRadio:1b,preset:0,uniqueUUID:\"7e3b6f2f-1c6e-4e9d-ad9e-a187fbe78fbe\"},FallDistance:0.0f,systemName:\"1967fordmustang\",doorleft:1.0d,id:\"mts:mts_entity\",locked:0b,doorright:1.0d,fuelTank:{fluidLevel:0.0d,fluidDispensed:0.0d,currentFluid:\"\",uniqueUUID:\"54c96754-fdc7-42ca-ac98-7ca5671fad77\"},Air:300s,positionx:43.91544194529761d,positiony:73.625d,positionz:244.18681701554286d,rotationx:0.0d,rotationy:0.0d,rotationz:0.0d,Pos:[43.91544194529761d,73.625d,244.18681701554286d],anglesz:0.0d,anglesy:131.32229614257812d,anglesx:0.0d,towingConnectionCount:0,part_7:{packID:\"craftspeed\",variablescount:0,offsetx:0.0d,offsetz:3.4375d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:0.0d,systemName:\"1967fordmustangfrontbumperstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"0fc21ca3-91ab-4b26-98e6-4d745de90bd8\"},part_6:{isCreative:0b,hours:0.0d,temp:1.1185021332843097d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",oilLeak:0b,pressure:0.0d,savedRiderLocationscount:0,rpm:0.0d,packID:\"craftspeed\",variablescount:0,running:0b,offsetx:0.0d,offsetz:2.0625d,offsety:-0.1875d,systemName:\"enginefordsmallblock3024v\",subName:\"\",fuelLeak:0b,locked:0b,towingConnectionCount:0,uniqueUUID:\"d8eb7c65-43be-41ec-81b4-9c151b865d73\",brokenStarter:0b},part_5:{packID:\"craftspeed\",variablescount:0,offsetx:0.375d,offsetz:1.0625d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:-0.3125d,systemName:\"1967fordmustangseat\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"5b5d2cba-1fa9-4a46-ba74-f308f21b9b13\"},ForgeData:{},part_4:{packID:\"craftspeed\",variablescount:0,offsetx:-0.375d,offsetz:1.0625d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:-0.3125d,systemName:\"1967fordmustangseat\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"f9414703-435d-414f-9f74-15cd0fade3e4\"},part_9:{packID:\"craftspeed\",variablescount:0,offsetx:0.0d,offsetz:-0.875d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:0.25d,systemName:\"1967fordmustangrearpanelstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"8b14982e-d942-4af5-8825-5774bfceff33\"},part_8:{packID:\"craftspeed\",variablescount:0,offsetx:0.0d,offsetz:-1.03125d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:-0.1875d,systemName:\"1967fordmustangrearbumperstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"5516533b-b192-4f74-8f15-de7fac915aef\"},serverDeltaRx:0.0d,serverDeltaP:0.0d,PortalCooldown:0,part_3:{isFlat:0b,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",savedRiderLocationscount:0,packID:\"craftspeed\",variablescount:0,offsetx:-0.75d,offsetz:2.75d,offsety:-0.28125d,systemName:\"1967fordmustangwheel\",subName:\"\",locked:0b,towingConnectionCount:0,uniqueUUID:\"1de431e1-7549-4d6b-8eee-e067f633d232\"},part_15:{packID:\"craftspeed\",variablescount:0,offsetx:-0.84375d,offsetz:1.9375d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:-0.1875d,systemName:\"1967fordmustangdoorstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"8fb23d2b-05f7-437f-b18b-f35cba400692\"},part_2:{isFlat:0b,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",savedRiderLocationscount:0,packID:\"craftspeed\",variablescount:0,offsetx:0.75d,offsetz:2.75d,offsety:-0.28125d,systemName:\"1967fordmustangwheel\",subName:\"\",locked:0b,towingConnectionCount:0,uniqueUUID:\"e198b26b-07bf-493c-9776-9ed1fb9125c0\"},part_16:{packID:\"craftspeed\",variablescount:0,offsetx:0.0d,offsetz:2.125d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:0.28125d,systemName:\"1967fordmustanghoodstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"88bbcdd3-8f0b-45be-8bdf-9b452df7b957\"},part_1:{isFlat:0b,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",savedRiderLocationscount:0,packID:\"craftspeed\",variablescount:0,offsetx:-0.75d,offsetz:0.0d,offsety:-0.28125d,systemName:\"1967fordmustangwheel\",subName:\"\",locked:0b,towingConnectionCount:0,uniqueUUID:\"4204f62e-a5c1-4ea0-9207-6ded6be1ea48\"},part_17:{packID:\"craftspeed\",variablescount:0,offsetx:0.375d,offsetz:1.5d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:0.14063d,systemName:\"1967fordmustangsteeringwheel\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"c297778a-8431-4a9c-acef-f86e29916967\"},serverDeltaRy:0.0d,part_0:{isFlat:0b,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",savedRiderLocationscount:0,packID:\"craftspeed\",variablescount:0,offsetx:0.75d,offsetz:0.0d,offsety:-0.28125d,systemName:\"1967fordmustangwheel\",subName:\"\",locked:0b,towingConnectionCount:0,uniqueUUID:\"08312919-7fdc-463b-aa03-4c373978d962\"},serverDeltaRz:0.0d,packID:\"craftspeed\",part_11:{packID:\"craftspeed\",variablescount:0,offsetx:-0.70313d,offsetz:3.29688d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:0.0625d,systemName:\"1967fordmustangfrontlightstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"0c9da5c0-011e-4850-8de2-9b778b9386b3\"},serverDeltaMz:0.0d,part_12:{packID:\"craftspeed\",variablescount:0,offsetx:0.0d,offsetz:3.4375d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:0.1875d,systemName:\"1967fordmustanggrillestock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"52f77fb2-5534-40e9-a61f-443c19ec00a2\"},part_13:{packID:\"craftspeed\",variablescount:0,offsetx:0.0d,offsetz:0.25d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:-0.3125d,systemName:\"1967fordmustangbackseat\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"d7da2f52-0512-4869-ac86-9d6fb90db569\"},serverDeltaMx:0.0d,part_14:{packID:\"craftspeed\",variablescount:0,offsetx:0.84375d,offsetz:1.9375d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:-0.1875d,systemName:\"1967fordmustangdoorstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"ae0aea7e-c2c8-4661-8dee-ea2d0c21f518\"},serverDeltaMy:-0.16250000000012366d,flapCurrentAngle:0.0d,part_10:{packID:\"craftspeed\",variablescount:0,offsetx:0.70313d,offsetz:3.29688d,ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",offsety:0.0625d,systemName:\"1967fordmustangfrontlightstock\",subName:\"\",savedRiderLocationscount:0,locked:0b,towingConnectionCount:0,uniqueUUID:\"188caf97-2146-43e3-a9dd-db1152643f26\"},uniqueUUID:\"68a9cd5f-80e1-4f7e-9dd1-882bf8f595cc\",doorhood:1.0d,Motion:[0.0d,0.0d,0.0d],ownerUUID:\"67d9b543-5ac9-41e1-a8a5-20d7689e24a4\",totalParts:18,UUIDLeast:-4713209003389416354L,display:{Name:\"Coche de Misión\"},OnGround:0b,Dimension:0,Rotation:[-131.3223f,0.0f],UpdateBlocked:0b,electricPower:12.0d,variablescount:3,UUIDMost:6448144086203582976L,selectedBeaconName:\"\",subName:\"_white\",Fire:-1s}");
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
            } catch (NBTException e) {
                e.printStackTrace();
            }

        }
    }

    public EntityVehicleF_Physics createEntity(WrapperWorld world, WrapperPlayer placingPlayer, NBTTagCompound datos) {

        try {
            Constructor<WrapperNBT> constructor = WrapperNBT.class.getDeclaredConstructor(NBTTagCompound.class);
            constructor.setAccessible(true);
            WrapperNBT data = constructor.newInstance(datos);
            data.setPoint3d("position", placingPlayer.getPosition());
            data.setPoint3d("motion", placingPlayer.getPosition());
            data.setPoint3d("angles", placingPlayer.getPosition());
            data.setPoint3d("rotation", placingPlayer.getPosition());
            return new EntityVehicleF_Physics(world, placingPlayer, data);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}