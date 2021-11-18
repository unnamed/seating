package team.unnamed.chairs.adapt;

import static team.unnamed.bukkit.ServerVersionUtils.SERVER_VERSION;

public class AdaptionModuleFactory {

    private static final String CLASS_NAME = AdaptionModuleFactory.class.getPackage().getName()
            + ".v" + SERVER_VERSION + ".AdaptionModule" + SERVER_VERSION;

    private AdaptionModuleFactory() {
        throw new UnsupportedOperationException();
    }

    public static AdaptionModule create() {
        try {
            Class<?> clazz = Class.forName(CLASS_NAME);
            Object module = clazz.newInstance();
            if (!(module instanceof AdaptionModule)) {
                throw new IllegalStateException("Invalid adaption module: '"
                        + CLASS_NAME + "'. It doesn't implement " + AdaptionModule.class);
            }
            return (AdaptionModule) module;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Adaption module not found: '" + CLASS_NAME + '.');
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate adaption module", e);
        }
    }

}
