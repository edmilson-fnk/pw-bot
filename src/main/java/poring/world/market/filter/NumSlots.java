package poring.world.market.filter;

import org.json.simple.JSONObject;

public class NumSlots extends BaseFilter {

    @Override
    public String getName() {
        return "Num slots";
    }

    @Override
    public String validate(String value) {
        return FilterUtils.validateNumeric(value);
    }

    @Override
    public boolean filter(JSONObject obj, String value) {
        Integer numSlots = FilterUtils.getNumSlots(obj.get("name").toString().toLowerCase());
        int expectedSlots = Integer.parseInt(value);
        if (expectedSlots == 0) {
            return numSlots != null;
        } else {
            return numSlots == null || numSlots != expectedSlots;
        }
    }

}
