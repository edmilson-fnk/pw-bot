package poring.world.market.filter;

import org.json.simple.JSONObject;

public class BaseFilter {

    public String getName() {
        return null;
    }

    public String validate(String value) {
        return null;
    }

    public String translate(String value) {
        return value.toLowerCase();
    }

    public boolean filter(JSONObject obj, String value) {
        return false;
    }

}
