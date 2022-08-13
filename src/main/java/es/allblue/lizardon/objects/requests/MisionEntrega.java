package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.init.ItemsInit;
import es.allblue.lizardon.items.rol.AlbaranEntrega;
import minecrafttransportsimulator.baseclasses.Point3d;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.entities.instances.PartEngine;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;
import minecrafttransportsimulator.mcinterface.WrapperWorld;
import minecrafttransportsimulator.systems.ConfigSystem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MisionEntrega {
    String nombre;
    String descripcion;
    String nombreCoche;
    String nbt;
    String nombreZona;
    String pos1;
    String pos2;

    String recompensa_id;
    String recompensa_cantidad;

    String posInicio;

    public MisionEntrega(String nombre, String descripcion, String nombreCoche, String nbt, String nombreZona, String pos1, String pos2, String posInicio, String recompensa_id, String recompensa_cantidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nombreCoche = nombreCoche;
        this.nbt = nbt;
        this.nombreZona = nombreZona;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.posInicio = posInicio;
        this.recompensa_id = recompensa_id;
        this.recompensa_cantidad = recompensa_cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCoche() {
        return nombreCoche;
    }

    public void setNombreCoche(String nombreCoche) {
        this.nombreCoche = nombreCoche;
    }

    public String getNbt() {
        return nbt;
    }

    public void setNbt(String nbt) {
        this.nbt = nbt;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getPos1() {
        return pos1;
    }

    public void setPos1(String pos1) {
        this.pos1 = pos1;
    }

    public String getPos2() {
        return pos2;
    }

    public void setPos2(String pos2) {
        this.pos2 = pos2;
    }

    public String getPosInicio() {
        return posInicio;
    }

    public void setPosInicio(String posInicio) {
        this.posInicio = posInicio;
    }

    public String getRecompensa_id() {
        return recompensa_id;
    }

    public void setRecompensa_id(String recompensa_id) {
        this.recompensa_id = recompensa_id;
    }

    public String getRecompensa_cantidad() {
        return recompensa_cantidad;
    }

    public void setRecompensa_cantidad(String recompensa_cantidad) {
        this.recompensa_cantidad = recompensa_cantidad;
    }

    public void ejecutar(EntityPlayerMP player) {
            ItemStack albaran = new ItemStack(ItemsInit.albaran,1);
            World world = player.world;
            albaran.setTagCompound(new NBTTagCompound());
            albaran.getTagCompound().setString("pos1",getPos1());
            albaran.getTagCompound().setString("pos2",getPos2());
            albaran.getTagCompound().setBoolean("sellado",true);

            albaran.getTagCompound().setString("item",getRecompensa_id());
            albaran.getTagCompound().setInteger("cantidad",Integer.parseInt(getRecompensa_cantidad()));

            ItemStack coche = new ItemStack(ItemsInit.coche,1);
            world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, albaran));

            WrapperPlayer jugador = WrapperPlayer.getWrapperFor(player);
            WrapperWorld mundo = WrapperWorld.getWrapperFor(world);

            if (!world.isRemote) {
                try {
                    NBTTagCompound data = JsonToNBT.getTagFromJson(getNbt());
                    boolean wasSaved = !data.getString("uniqueUUID").isEmpty();
                    EntityVehicleF_Physics vehicle = createEntity(mundo, jugador, data);


                    if (!wasSaved) {
                        for (JSONPartDefinition partDef : vehicle.definition.parts) {
                            vehicle.addDefaultPart(partDef, jugador, vehicle.definition, true, false);
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
            String[] coords = posInicio.split(",");

            Point3d point3d = new Point3d(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));

            Constructor<WrapperNBT> constructor = WrapperNBT.class.getDeclaredConstructor(NBTTagCompound.class);
            constructor.setAccessible(true);
            WrapperNBT data = constructor.newInstance(datos);
            data.setPoint3d("position", point3d);
            data.setPoint3d("motion", point3d);
            data.setPoint3d("angles", point3d);
            data.setPoint3d("rotation", point3d);
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
