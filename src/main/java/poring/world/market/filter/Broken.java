package poring.world.market.filter;

import static poring.world.constants.Constants.*;

public class Broken extends BaseFilter {
    @Override
    public String getName() {
        return "Broken";
    }

    @Override
    public String validate(String value) {
        if (value.contains(QUERY_SPLIT_TOKEN)) {
            return "not a multiple values field, remove _&&_";
        }
        if (!value.equalsIgnoreCase(YES) && !value.equalsIgnoreCase(NO)) {
            return "use only _yes_ or _no_";
        }

        return null;
    }
}
