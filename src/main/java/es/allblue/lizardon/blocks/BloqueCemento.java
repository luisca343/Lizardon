package es.allblue.lizardon.blocks;

import es.allblue.lizardon.items.templates.BlockBasic;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BloqueCemento extends BlockBasic {
    public BloqueCemento(String name) {
        super(name, Material.ROCK);
        setHardness(0.15f);
        setSoundType(SoundType.STONE);
    }
}
