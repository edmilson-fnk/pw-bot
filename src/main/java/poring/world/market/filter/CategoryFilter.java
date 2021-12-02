package poring.world.market.filter;

import static poring.world.constants.Constants.CATEGORY_MAP;

public class CategoryFilter extends BaseFilter {

    @Override
    public String getName() {
        return "Category";
    }

    @Override
    public String validate(String value) {
        if (CATEGORY_MAP.containsKey(value.toLowerCase())) {
            return null;
        } else {
            return "Invalid value \"" + value + "\". Try " + String.join(", ", CATEGORY_MAP.keySet());
        }
    }

}
