package poring.world.market.filter;

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

    public boolean filter(String value) {
        return false;
    }

}
