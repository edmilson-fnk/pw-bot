package poring.world.market.filter;

import org.json.simple.JSONObject;

public class RefineLTFilter extends BaseFilter {

    @Override
    public String getName() {
        return "Refine <=";
    }

    @Override
    public String validate(String value) {
        return FilterUtils.validateNumeric(value);
    }

    @Override
    public boolean filter(JSONObject obj, String value) {
        int refineFilter = Integer.parseInt(value);
        Integer refineItem = FilterUtils.getRefineValue(obj.get("name").toString());

        return refineItem > refineFilter;
    }

}
