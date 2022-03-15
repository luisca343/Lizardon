package es.allblue.lizardon.pixelmon;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonSmdFactory;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumMega;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.forms.RegionalForms;
import es.allblue.lizardon.Lizardon;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomPokemon {
    ListMultimap<EnumSpecies, IEnumForm> lizardonFormList;
    Field formList = null;
    Method addModel = null;


    public void inicializarFormas() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        /* Streamers */
        crearForma(EnumSpecies.Voltorb, EnumFormasLizardon.HISUI, true);         //Hisui
        crearForma(EnumSpecies.Vulpix, EnumFormasLizardon.TERAS, false);         //Itami
        crearForma(EnumSpecies.Ninetales, EnumFormasLizardon.TERAS, false);      //Itami
        crearForma(EnumSpecies.Decidueye, EnumFormasLizardon.TERAS, true);       //RaRegius
        crearForma(EnumSpecies.Wooloo, EnumFormasLizardon.TERAS, false);         //Neofabi
        crearForma(EnumSpecies.Dubwool, EnumFormasLizardon.TERAS, false);        //Neofabi
        crearForma(EnumSpecies.Arcanine, EnumFormasLizardon.TERAS, false);       //Potimax
        crearForma(EnumSpecies.Charizard, EnumFormasLizardon.TERAS, true);       //Tsuki
        crearForma(EnumSpecies.Emboar, EnumFormasLizardon.TERAS, false);         //UrieBirra
        crearForma(EnumSpecies.Luxray, EnumFormasLizardon.TERAS, false);         //Tojen
        crearForma(EnumSpecies.Drifloon, EnumFormasLizardon.TERAS, false);       //Frankuslop
        crearForma(EnumSpecies.Drifblim, EnumFormasLizardon.TERAS, false);       //Frankuslop
        crearForma(EnumSpecies.Mismagius, EnumFormasLizardon.TERAS, false);      //Tokiru
        crearForma(EnumSpecies.Salamence, EnumFormasLizardon.TERAS, false);      //Aguirado
        crearForma(EnumSpecies.Wobbuffet, EnumFormasLizardon.TERAS, true);       //Fiiftn
        crearForma(EnumSpecies.Gardevoir, EnumFormasLizardon.TERAS, true);       //Lucasstars

        crearForma(EnumSpecies.Purugly, EnumFormasLizardon.TERAS, true);       //Leya
        crearForma(EnumSpecies.Furret, EnumFormasLizardon.TERAS, true);       //Lehe
        crearForma(EnumSpecies.Lucario, EnumFormasLizardon.TERAS, true);    //Yinarimi

        /* Tickets */
        crearForma(EnumSpecies.Dragonite, EnumFormasLizardon.TERAS, true);
        /* Teras */
        crearForma(EnumSpecies.Haxorus, EnumFormasLizardon.TERAS, false);        //Neouaca
        crearForma(EnumSpecies.Zoroark, EnumFormasLizardon.TERAS, false);        //Shu_r
        crearForma(EnumSpecies.Snorlax, EnumFormasLizardon.TERAS, false);        //Kamina

        crearForma(EnumSpecies.Unown, EnumFormasLizardon.TERAS, true);           //Luisca
        crearForma(EnumSpecies.Melmetal, EnumFormasLizardon.TERAS, true);        //Kamina
        crearForma(EnumSpecies.Inteleon, EnumFormasLizardon.TERAS, true);       //CPUTO DE LOE SHUVEOSV

        crearForma(EnumSpecies.Exeggutor, EnumFormasLizardon.TERAS, true);

        crearForma(EnumSpecies.Loudred, EnumFormasLizardon.TERAS, false);
        crearForma(EnumSpecies.Exploud, EnumFormasLizardon.TERAS, false);
        crearForma(EnumSpecies.Lopunny, EnumFormasLizardon.TERAS, false);
        crearForma(EnumSpecies.Riolu, EnumFormasLizardon.TERAS, false);
        crearForma(EnumSpecies.Krookodile, EnumFormasLizardon.TERAS, false);
        crearForma(EnumSpecies.Wobbuffet, EnumFormasLizardon.KAWAII, false);     //Fiiftn


        /* Extras */
        crearForma(EnumSpecies.Pikachu, EnumFormasLizardon.TERAS, true);         //Tengo miedo
        crearForma(EnumSpecies.MissingNo, EnumMissingNo.AGUMON, true);          //Luisca (?)
    }

    public void insertarPokemon() {
        Logger logger = Lizardon.getLogger();
        lizardonFormList = MultimapBuilder.enumKeys(EnumSpecies.class).arrayListValues(1).build();
        try {
            formList = EnumSpecies.class.getDeclaredField("formList");
            formList.setAccessible(true);
            cargarFormas(formList);

            addModel = PixelmonModelRegistry.class.getDeclaredMethod("addModel", new Class[]{EnumSpecies.class, IEnumForm.class, PixelmonSmdFactory.class});
            addModel.setAccessible(true);

            //Inicializar formas
            inicializarFormas();

            formList.set(null, this.lizardonFormList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarFormas(Field f) throws IllegalAccessException {
        ListMultimap<EnumSpecies, IEnumForm> lista = (ListMultimap<EnumSpecies, IEnumForm>) f.get(null);
        lista.forEach((enumSpecies, iEnumForm) -> insertar(enumSpecies, iEnumForm));
    }


    public void insertar(EnumSpecies enumSpecies, IEnumForm iEnumForm) {
        if (enumSpecies.getNationalPokedexInteger() != 0)
            this.lizardonFormList.put(enumSpecies, iEnumForm);

    }

    public void crearForma(EnumSpecies pokemon, IEnumForm forma, boolean customModel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Logger logger = Lizardon.getLogger();
        if (customModel) {
            crearForma(pokemon, forma, "models/pokemon/" + forma.getName() + "/" + pokemon.name + "/" + pokemon.name + ".pqc");
            //logger.info("Inicializando forma "+forma.getName()+" de "+pokemon.name+" + modelo externo");
        } else {
            crearForma(pokemon, forma, "models/pokemon/" + pokemon.name + "/" + pokemon.name + ".pqc");
            //logger.info("Inicializando forma "+forma.getName()+" de "+pokemon.name);
        }

    }


    public void crearForma(EnumSpecies pokemon, IEnumForm forma, String ruta) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //ruta = "models/pokemon/"+forma.getName()+"/"+pokemon.name+"/"+pokemon.name+".pqc";
        if (pokemon == EnumSpecies.MissingNo) {
            ruta = "models/pokemon/missingno/" + forma.getName() + "/" + forma.getName() + ".pqc";
        }
        if (!this.lizardonFormList.containsEntry(pokemon, RegionalForms.NORMAL)) {
            this.addModel.invoke(null, pokemon, RegionalForms.NORMAL, new PixelmonSmdFactory(new ResourceLocation("pixelmon", "models/pokemon/" + pokemon.name + "/" + pokemon.name + ".pqc")));
            this.lizardonFormList.put(pokemon, RegionalForms.NORMAL);
        }
        this.addModel.invoke(null, pokemon, forma, new PixelmonSmdFactory(new ResourceLocation("pixelmon", ruta)));
        this.lizardonFormList.put(pokemon, forma);
    }
}

