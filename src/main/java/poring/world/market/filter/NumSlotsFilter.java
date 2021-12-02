package poring.world.market.filter;

import org.json.simple.JSONObject;

public class NumSlotsFilter extends BaseFilter {

    @Override
    public String getName() {
        return "Num slots";
    }

    @Override
    public String validate(String value) {
        String validateNumeric = FilterUtils.validateNumeric(value);
        if (validateNumeric != null) {
            return validateNumeric;
        } else if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > 2) {
            return "only from 0 to 2 slots";
        } else {
            return null;
        }
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
