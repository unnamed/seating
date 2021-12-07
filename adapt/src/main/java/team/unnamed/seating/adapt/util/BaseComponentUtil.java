package team.unnamed.seating.adapt.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class BaseComponentUtil {

    private BaseComponentUtil() {
        throw new UnsupportedOperationException();
    }

    public static BaseComponent modernDismountComponent(String dismountMessage) {;
        String[] componentKeys = dismountMessage.split("%keybind%");
        BaseComponent baseComponent = new TextComponent();
        BaseComponent lastComponent = null;

        for (int i = 0; i < componentKeys.length; i++) {
            if (i != 0) {
                BaseComponent component = new KeybindComponent("key.sneak");
                component.copyFormatting(lastComponent);
                baseComponent.addExtra(component);
            }

            BaseComponent[] components = TextComponent.fromLegacyText(componentKeys[i]);
            lastComponent = components[components.length - 1];
            for (BaseComponent component : components) {
                baseComponent.addExtra(component);
            }
        }

        return baseComponent;
    }

}
