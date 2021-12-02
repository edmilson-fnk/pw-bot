package poring.world.market.filter;

import org.json.simple.JSONObject;

public class Enchant extends BaseFilter {

    @Override
    public String getName() {
        return "Enchant";
    }

    @Override
    public String translate(String value) {
        return value.replaceAll("\\s*&&\\s*", " or ").toLowerCase();
    }

    @Override
    public boolean filter(JSONObject obj, String value) {
        return !obj.get("name").toString().toLowerCase().matches(String.format(".*<.*%s.*>.*", value.toLowerCase()));
    }

}
