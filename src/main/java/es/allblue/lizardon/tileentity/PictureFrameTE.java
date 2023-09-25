package es.allblue.lizardon.tileentity;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.blocks.PictureFrame;
import es.allblue.lizardon.client.display.FrameDisplay;
import es.allblue.lizardon.client.texture.TextureCache;
import es.allblue.lizardon.init.TileEntityInit;
import es.allblue.lizardon.net.client.CreativePictureFramePacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.loading.FMLPaths;
import team.creative.creativecore.common.util.math.base.Axis;
import team.creative.creativecore.common.util.math.base.Facing;
import team.creative.creativecore.common.util.math.box.AlignedBox;
import team.creative.creativecore.common.util.math.vec.Vec2f;
import team.creative.creativecore.common.util.math.vec.Vec3d;

public class PictureFrameTE extends TileEntity {
    @OnlyIn(Dist.CLIENT)
    public static String replaceVariables(String url) {
        String result = url.replace("$(name)", Minecraft.getInstance().player.getDisplayName().getString()).replace("$(uuid)", Minecraft.getInstance().player.getStringUUID());
        if (result.startsWith("minecraft://"))
            result = result.replace("minecraft://", "file:///" + FMLPaths.GAMEDIR.get().toAbsolutePath().toString().replace("\\", "/") + "/");
        return result;
    }

    private String url = "";
    public Vec2f min = new Vec2f(0, 0);
    public Vec2f max = new Vec2f(1, 1);

    public float rotation = 0;
    public boolean flipX = false;
    public boolean flipY = false;

    public boolean visibleFrame = true;
    public boolean bothSides = false;

    public float brightness = 1;
    public float alpha = 1;

    public int renderDistance = 128;

    public float volume = 1;
    public float minDistance = 5;
    public float maxDistance = 20;

    public boolean loop = true;
    public int tick = 0;
    public boolean playing = true;

    @OnlyIn(Dist.CLIENT)
    public TextureCache cache;

    @OnlyIn(Dist.CLIENT)
    public FrameDisplay display;

    public PictureFrameTE() {
        super(TileEntityInit.FRAME.get());

    }

    /*
    public PictureFrameTE(BlockPos pos, BlockState state) {
        super(TileEntityInit.FRAME.get(), pos, state);
    }*/

    @OnlyIn(Dist.CLIENT)
    public boolean isURLEmpty() {
        return url.isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public String getURL() {
        return replaceVariables(url);
    }

    public String getRealURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public FrameDisplay requestDisplay() {
        String url = getURL();
        if (cache == null || !cache.url.equals(url)) {
            cache = TextureCache.get(url);
            if (display != null)
                display.release();
            display = null;
        }
        if (!cache.isVideo() && (!cache.ready() || cache.getError() != null))
            return null;
        if (display != null)
            return display;
        return display = cache.createDisplay(new Vec3d(worldPosition), url, volume, minDistance, maxDistance, loop);
    }

    public AlignedBox getBox() {
        Direction direction = getBlockState().getValue(PictureFrame.FACING);
        Facing facing = Facing.get(direction.ordinal());
        AlignedBox box = PictureFrame.box(direction);

        Axis one = facing.one();
        Axis two = facing.two();

        if (facing.axis != Axis.Z) {
            one = facing.two();
            two = facing.one();
        }

        box.setMin(one, min.x);
        box.setMax(one, max.x);

        box.setMin(two, min.y);
        box.setMax(two, max.y);
        return box;
    }

    public float getSizeX() {
        return max.x - min.x;
    }

    public float getSizeY() {
        return max.y - min.y;
    }


    /*
    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return getBox().getBB(getBlockPos());
    }*/

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        savePicture(nbt);
        return super.save(nbt);
    }


    public void play() {
        playing = true;

        Lizardon.NETWORK.sendToClient(new CreativePictureFramePacket(worldPosition, playing, tick), level.getChunkAt(worldPosition));
    }

    public void pause() {
        playing = false;
        Lizardon.NETWORK.sendToClient(new CreativePictureFramePacket(worldPosition, playing, tick), level.getChunkAt(worldPosition));
    }

    public void stop() {
        playing = false;
        tick = 0;
        Lizardon.NETWORK.sendToClient(new CreativePictureFramePacket(worldPosition, playing, tick), level.getChunkAt(worldPosition));
    }

    protected void savePicture(CompoundNBT nbt) {
        nbt.putString("url", url);
        nbt.putFloat("minx", min.x);
        nbt.putFloat("miny", min.y);
        nbt.putFloat("maxx", max.x);
        nbt.putFloat("maxy", max.y);
        nbt.putFloat("rotation", rotation);
        nbt.putInt("render", renderDistance);
        nbt.putBoolean("visibleFrame", visibleFrame);
        nbt.putBoolean("bothSides", bothSides);
        nbt.putBoolean("flipX", flipX);
        nbt.putBoolean("flipY", flipY);
        nbt.putFloat("alpha", alpha);
        nbt.putFloat("brightness", brightness);

        nbt.putFloat("volume", volume);
        nbt.putFloat("min", minDistance);
        nbt.putFloat("max", maxDistance);

        nbt.putBoolean("playing", playing);
        nbt.putInt("tick", tick);
        nbt.putBoolean("loop", loop);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT nbt) {
        super.load(p_230337_1_, nbt);
        loadPicture(nbt);
    }


    protected void loadPicture(CompoundNBT nbt) {
        url = nbt.getString("url");
        min.x = nbt.getFloat("minx");
        min.y = nbt.getFloat("miny");
        max.x = nbt.getFloat("maxx");
        max.y = nbt.getFloat("maxy");
        rotation = nbt.getFloat("rotation");
        renderDistance = nbt.getInt("render");
        visibleFrame = nbt.getBoolean("visibleFrame");
        bothSides = nbt.getBoolean("bothSides");
        flipX = nbt.getBoolean("flipX");
        flipY = nbt.getBoolean("flipY");
        if (nbt.contains("alpha"))
            alpha = nbt.getFloat("alpha");
        else
            alpha = 1;
        if (nbt.contains("brightness"))
            brightness = nbt.getFloat("brightness");
        else
            brightness = 1;

        volume = nbt.getFloat("volume");
        if (nbt.contains("min"))
            minDistance = nbt.getFloat("min");
        else
            minDistance = 5;
        if (nbt.contains("max"))
            maxDistance = nbt.getFloat("max");
        else
            maxDistance = 20;

        playing = nbt.getBoolean("playing");
        tick = nbt.getInt("tick");
        loop = nbt.getBoolean("loop");
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        loadPicture(tag);
        super.handleUpdateTag(state, tag);
    }

    /*
    ARREGLAR REVISAR
    @Override
    public void handleUpdate(CompoundNBT nbt, boolean chunkUpdate) {
        loadPicture(nbt);
        markDirty();
    }
*/
    public static void tick(World level, BlockPos pos, BlockState state, TileEntity blockEntity) {
        if (blockEntity instanceof PictureFrameTE) {
            PictureFrameTE be = (PictureFrameTE) blockEntity;
            if (level.isClientSide) {
                FrameDisplay display = be.requestDisplay();
                if (display != null)
                    display.tick(be.url, be.volume, be.minDistance, be.maxDistance, be.playing, be.loop, be.tick);
            }
            if (be.playing)
                be.tick++;
        }
    }

    @Override
    public void setRemoved() {
        if (isClient() && display != null)
            display.release();
    }

    @Override
    public void onChunkUnloaded() {
        if (isClient() && display != null)
            display.release();
    }



    public boolean isClient() {
        if (level != null)
            return level.isClientSide;
        return EffectiveSide.get().isClient();
    }
}
