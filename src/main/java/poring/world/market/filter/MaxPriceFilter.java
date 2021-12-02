package poring.world.market.filter;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;

public class MaxPriceFilter extends BaseFilter {

    @Override
    public String getName() {
        return "Max price";
    }

    @Override
    public String validate(String value) {
        return FilterUtils.validateNumeric(value);
    }

    @Override
    public String translate(String value) {
        return new DecimalFormat("###,###,###,###").format(Double.parseDouble(value));
    }

    @Override
    public boolean filter(JSONObject obj, String value) {
        return ((long) ((JSONObject) obj.get("lastRecord")).get("price")) > Long.parseLong(value);
    }
}
