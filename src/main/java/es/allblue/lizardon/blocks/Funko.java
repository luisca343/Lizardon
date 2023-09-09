package es.allblue.lizardon.blocks;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.init.TileEntityInit;
import es.allblue.lizardon.tileentity.FunkoTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.UUID;

public class Funko extends Block {
    public Funko(Properties props) {
        super(props);
        String id = "https://crafatar.com/skins/67d9b543-5ac9-41e1-a8a5-20d7689e24a4";

        

        try{
            String url = id;
            String[] partes = url.split("/");
            String carpeta = "Lizardon/skins/";
            File directorio = new File(carpeta);
            if(!directorio.exists()){
                Files.createDirectories(Lizardon.PROXY.getRuta(carpeta));
            }

            URLConnection connection = new URL(url).openConnection();

            connection.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream is = connection.getInputStream();

            OutputStream outstream = new FileOutputStream(new File(carpeta + partes[partes.length-1]+".png"));

            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, length);
            }
            System.out.println("png download completed.");
            outstream.close();
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
    }




    @Override
    public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
        super.onPlace(p_220082_1_, p_220082_2_, p_220082_3_, p_220082_4_, p_220082_5_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        player.sendMessage(new StringTextComponent("Click en Estatua"), UUID.randomUUID());

        return ActionResultType.SUCCESS;
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityInit.FUNKO_TE.get().create();

    }





}
