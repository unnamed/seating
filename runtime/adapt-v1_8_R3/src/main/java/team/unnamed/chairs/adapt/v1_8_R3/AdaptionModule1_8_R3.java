package team.unnamed.chairs.adapt.v1_8_R3;

import team.unnamed.chairs.adapt.AdaptionModule;
import team.unnamed.chairs.adapt.entity.ChairEntityHandler;
import team.unnamed.chairs.adapt.v1_8_R3.chair.ChairEntityHandler1_8_R3;

public class AdaptionModule1_8_R3 implements AdaptionModule {

    @Override
    public ChairEntityHandler getChairEntityHandler() {
        return new ChairEntityHandler1_8_R3();
    }

}
