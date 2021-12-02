package poring.world.market.filter;

import org.json.simple.JSONObject;

public class EnchantFilter extends BaseFilter {

    @Override
    public String getName() {
        return "Enchantment";
    }

    @Override
    public String translate(String value) {
        return value.replaceAll("\\s*&&\\s*", " or ");
    }

    @Override
    public boolean filter(JSONObject obj, String value) {
        return !obj.get("name").toString().toLowerCase().matches(String.format(".*<.*%s.*>.*", value));
    }

}
