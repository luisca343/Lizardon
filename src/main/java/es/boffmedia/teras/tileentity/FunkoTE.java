/*package es.boffmedia.teras.tileentity;

import com.mojang.authlib.GameProfile;
import es.boffmedia.teras.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FunkoTE extends TileEntity {
    private final ItemStackHandler itemHandler = createHandler();
        private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public FunkoTE() {
        super(TileEntityInit.FUNKO_TE.get());
    }
    public FunkoTE(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2){
            @Override
            protected void onContentsChanged(int slot){
                setChanged();
            }
          @Override
          public boolean isItemValid(int slot, @Nonnull ItemStack stack){

                return super.isItemValid(slot,stack);
          }
        };
    }



    @Override
    public void load(BlockState state, CompoundNBT nbt){
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.load(state,nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.save(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        }

        return super.getCapability(cap,side);
    }

    public GameProfile getOwnerProfile() {
        return null;
    }
}
*/