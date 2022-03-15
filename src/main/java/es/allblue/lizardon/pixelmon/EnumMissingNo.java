package es.allblue.lizardon.pixelmon;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.enums.forms.FormAttributes;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;

import java.util.Collections;
import java.util.Set;

public enum EnumMissingNo implements IEnumForm {
    NORMAL(0, "normal"),
    AGUMON(127, "agumon");

    private final byte form;

    private final String suffix;

    EnumMissingNo(int form, String suffix) {
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

    public Set<FormAttributes> getFormAttributes() {
        return Collections.emptySet();
    }

    public String getUnlocalizedName() {
        return "lizardon.missingno." + name().toLowerCase();
    }

    public String getName() {
        return name().toLowerCase();
    }
}
