package poring.world.market.filter;

import java.text.DecimalFormat;

public class MaxPrice extends BaseFilter {

    @Override
    public String getName() {
        return "Max Price";
    }

    @Override
    public String validate(String value) {
        try {
            Integer.parseInt(value);
            return null;
        } catch (Exception e) {
            return "use only numbers";
        }
    }

    @Override
    public String translate(String value) {
        return new DecimalFormat("###,###,###,###").format(Double.parseDouble(value));
    }

}
