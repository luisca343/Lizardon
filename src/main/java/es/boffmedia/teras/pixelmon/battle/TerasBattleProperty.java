package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.battles.api.rules.PropertyValue;
import java.util.Optional;

import com.pixelmonmod.pixelmon.battles.api.rules.property.type.AbstractBooleanProperty;
import com.pixelmonmod.pixelmon.battles.api.rules.value.BooleanValue;


public class TerasBattleProperty extends AbstractBooleanProperty {
    public TerasBattleProperty() {
    }

    public String getId() {
        return "TerasBattle";
    }

    public boolean requiredByClient() {
        return true;
    }

    public Optional<PropertyValue<Boolean>> getDefault() {
        return Optional.of(new BooleanValue(false));
    }
}
