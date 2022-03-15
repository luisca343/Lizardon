package es.allblue.lizardon.objects.mineria;

import es.allblue.lizardon.Lizardon;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class RecompensaMina {
    Item item;
    String name;
    String modId;
    int ancho;
    int alto;
    ResourceLocation imagen;

    public RecompensaMina(String name, int ancho, int alto, String modId, String resourceModId, String imagen) {
        this.name = name;
        this.ancho = ancho;
        this.alto = alto;
        this.modId = modId;
        this.imagen = new ResourceLocation(resourceModId, imagen);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public ResourceLocation getImagen() {
        return imagen;
    }

    public ResourceLocation getObjeto() {
        return new ResourceLocation(this.modId, this.name);
    }

    public void setImagen(ResourceLocation imagen) {
        this.imagen = imagen;
    }
}
