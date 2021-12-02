package poring.world.market.filter;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static poring.world.constants.Constants.*;

public class TestFilterUtilsTranslate {

    @Test
    public void testEmpty() {
        Map<String, String> filters = new HashMap<>();

        String translation = FilterUtils.translate(filters);
        System.out.println(translation);

        assert translation.equals("");
    }

    @Test
    public void testMaxPrice() {
        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(MAX_PRICE, "10000");
        }};

        String translation = FilterUtils.translate(filters);
        System.out.println(translation);

        assert translation.equals("_Max price_: 10,000; ");
    }

    @Test
    public void testBroken() {
        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(BROKEN, "YES");
        }};

        String translation = FilterUtils.translate(filters);
        System.out.println(translation);

        assert translation.equals("_Broken_: yes; ");
    }

    @Test
    public void testEnchant() {
        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(ENCHANT, "sharp blade&&morale");
        }};

        String translation = FilterUtils.translate(filters);
        System.out.println(translation);

        assert translation.equals("_Enchantment_: sharp blade or morale; ");
    }

    @Test
    public void testExcept() {
        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(EXCEPT, "sharp blade&&morale");
        }};

        String translation = FilterUtils.translate(filters);
        System.out.println(translation);

        assert translation.equals("_Except_: sharp blade and morale; ");
    }

    @Test
    public void testCategory() {
        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(CATEGORY, "offhand");
        }};

        String translation = FilterUtils.translate(filters);
        System.out.println(translation);

        assert translation.equals("_Category_: offhand; ");
    }

}
