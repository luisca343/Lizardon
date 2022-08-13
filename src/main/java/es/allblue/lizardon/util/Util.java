package es.allblue.lizardon.util;


import com.google.common.base.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Util {
    public static String getNumeroConPuntitos(int num) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);
        df.setMaximumFractionDigits(2);
        return df.format(num);
    }

    public static List<String> getLore(String lore) {
        return new ArrayList<>();
    }

    @SideOnly(Side.CLIENT)
    public static RayTraceResult raycast(double dist) {
        Minecraft mc = Minecraft.getMinecraft();
        Vec3d start = mc.player.getPositionEyes(1.0f);
        Vec3d lookVec = mc.player.getLook(1.0f);
        Vec3d end = start.addVector(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist);

        return mc.world.rayTraceBlocks(start, end, true, true, false);
    }


    public static RayTraceResult rayTraceEntities(Entity shooter, Vec3d startVec, Vec3d endVec, AxisAlignedBB boundingBox, Predicate<? super Entity> filter, double dist) {
        World world = shooter.world;
        double distance = dist;
        Entity rayTracedEntity = null;
        Vec3d hitVec = null;

        for (Entity entity : world.getEntitiesInAABBexcluding(shooter, boundingBox, filter)) {
            AxisAlignedBB boxToCheck = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
            Optional<Vec3d> optional = rayTrace(boxToCheck, startVec, endVec);

            if (boxToCheck.contains(startVec)) {
                if (distance >= 0.0D) {
                    rayTracedEntity = entity;
                    hitVec = optional.orElse(startVec);
                    distance = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3d vector = optional.get();
                double sqDist = startVec.squareDistanceTo(vector);

                if (sqDist < distance || distance == 0.0D) {
                    if (entity.getLowestRidingEntity() == shooter.getLowestRidingEntity() && !entity.canRiderInteract()) {
                        if (distance == 0.0D) {
                            rayTracedEntity = entity;
                            hitVec = vector;
                        }
                    } else {
                        rayTracedEntity = entity;
                        hitVec = vector;
                        distance = sqDist;
                    }
                }
            }
        }

        return rayTracedEntity == null ? null : new RayTraceResult(rayTracedEntity, hitVec);
    }


    private static Optional<Vec3d> rayTrace(AxisAlignedBB aabb, Vec3d startVec, Vec3d endVec) {
        double[] adouble = {
                1.0D
        };
        double d0 = endVec.x - startVec.x;
        double d1 = endVec.y - startVec.y;
        double d2 = endVec.z - startVec.z;
        EnumFacing enumFacing = func_197741_a(aabb, startVec, adouble, null, d0, d1, d2);

        if (enumFacing == null)
            return Optional.empty();
        else {
            double d3 = adouble[0];
            return Optional.of(startVec.add(new Vec3d(d3 * d0, d3 * d1, d3 * d2)));
        }
    }

    //what
    private static EnumFacing func_197741_a(AxisAlignedBB aabb, Vec3d p_197741_1_, double[] p_197741_2_, @Nullable EnumFacing facing, double p_197741_4_, double p_197741_6_, double p_197741_8_) {
        if (p_197741_4_ > 1.0E-7D)
            facing = func_197740_a(p_197741_2_, facing, p_197741_4_, p_197741_6_, p_197741_8_, aabb.minX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, EnumFacing.WEST, p_197741_1_.x, p_197741_1_.y, p_197741_1_.z);
        else if (p_197741_4_ < -1.0E-7D)
            facing = func_197740_a(p_197741_2_, facing, p_197741_4_, p_197741_6_, p_197741_8_, aabb.maxX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, EnumFacing.EAST, p_197741_1_.x, p_197741_1_.y, p_197741_1_.z);

        if (p_197741_6_ > 1.0E-7D)
            facing = func_197740_a(p_197741_2_, facing, p_197741_6_, p_197741_8_, p_197741_4_, aabb.minY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, EnumFacing.DOWN, p_197741_1_.y, p_197741_1_.z, p_197741_1_.x);
        else if (p_197741_6_ < -1.0E-7D)
            facing = func_197740_a(p_197741_2_, facing, p_197741_6_, p_197741_8_, p_197741_4_, aabb.maxY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, EnumFacing.UP, p_197741_1_.y, p_197741_1_.z, p_197741_1_.x);

        if (p_197741_8_ > 1.0E-7D)
            facing = func_197740_a(p_197741_2_, facing, p_197741_8_, p_197741_4_, p_197741_6_, aabb.minZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, EnumFacing.NORTH, p_197741_1_.z, p_197741_1_.x, p_197741_1_.y);
        else if (p_197741_8_ < -1.0E-7D)
            facing = func_197740_a(p_197741_2_, facing, p_197741_8_, p_197741_4_, p_197741_6_, aabb.maxZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, EnumFacing.SOUTH, p_197741_1_.z, p_197741_1_.x, p_197741_1_.y);

        return facing;
    }

    private static EnumFacing func_197740_a(double[] p_197740_0_, @Nullable EnumFacing p_197740_1_, double p_197740_2_, double p_197740_4_, double p_197740_6_, double p_197740_8_, double p_197740_10_, double p_197740_12_, double p_197740_14_, double p_197740_16_, EnumFacing p_197740_18_, double p_197740_19_, double p_197740_21_, double p_197740_23_) {
        double d0 = (p_197740_8_ - p_197740_19_) / p_197740_2_;
        double d1 = p_197740_21_ + d0 * p_197740_4_;
        double d2 = p_197740_23_ + d0 * p_197740_6_;

        if (0.0D < d0 && d0 < p_197740_0_[0] && p_197740_10_ - 1.0E-7D < d1 && d1 < p_197740_12_ + 1.0E-7D && p_197740_14_ - 1.0E-7D < d2 && d2 < p_197740_16_ + 1.0E-7D) {
            p_197740_0_[0] = d0;
            return p_197740_18_;
        } else
            return p_197740_1_;
    }

}


