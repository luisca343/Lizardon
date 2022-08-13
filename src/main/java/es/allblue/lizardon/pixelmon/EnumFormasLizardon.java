package es.allblue.lizardon.pixelmon;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.enums.forms.FormAttributes;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;

import java.util.Collections;
import java.util.Set;

public enum EnumFormasLizardon implements IEnumForm {
    NORMAL(0, "normal"),
    TERAS(127, "teras"),
    HISUI(126, "hisui"),
    KAWAII(125, "kawaii"),
    TERASNT(124, "terasnt");

    private final byte form;

    private final String suffix;

    EnumFormasLizardon(int form, String suffix) {
        this.form = (byte) form;
        this.suffix = suffix;
    }

    public String getFormSuffix() {
        return "-" + this.suffix;
    }

    public byte getForm() {
        return this.form;
    }

    public boolean isDefaultForm() {
        return (this == NORMAL);
    }

    @Override
    public boolean isRegionalForm() {
        return this != NORMAL;
    }

    public Set<FormAttributes> getFormAttributes() {
        return Collections.emptySet();
    }

    public String getUnlocalizedName() {
        return "lizardon.form." + name().toLowerCase();
    }

    public String getName() {
        return name().toLowerCase();
    }


}
