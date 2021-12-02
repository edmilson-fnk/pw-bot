package poring.world.market.filter;

import org.json.simple.JSONObject;

public class ExceptFilter extends BaseFilter {

    @Override
    public String getName() {
        return "Except";
    }

    @Override
    public String translate(String value) {
        return value.replaceAll("\\s*&&\\s*", " and ").toLowerCase();
    }

    @Override
    public boolean filter(JSONObject obj, String value) {
        return obj.get("name").toString().toLowerCase().contains(value.toLowerCase());
    }

}
