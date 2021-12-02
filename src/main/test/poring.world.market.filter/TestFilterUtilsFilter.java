package poring.world.market.filter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static poring.world.constants.Constants.*;

public class TestFilterUtilsFilter {

    JSONParser parser = new JSONParser();

    public void base() throws ParseException {
        String minStr = "{\"lastRecord\": {\"price\": 10}}";
        System.out.println(minStr);
        JSONObject minObj = (JSONObject) parser.parse(minStr);
        System.out.println(minObj);
    }

    @Test
    public void filterMaxPriceNo() throws ParseException {
        String obj1Str = "{\"lastRecord\": {\"price\": 9}}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(MAX_PRICE, "10");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterMaxPriceYes() throws ParseException {
        String obj1Str = "{\"lastRecord\": {\"price\": 11}}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(MAX_PRICE, "10");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterBrokenYesObjBroken() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1> (broken)\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(BROKEN, "yes");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterBrokenYesObjNotBroken() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1>\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(BROKEN, "yes");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterBrokenNoObjBroken() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1> (broken)\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(BROKEN, "no");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterBrokenNoObjNotBroken() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1>\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(BROKEN, "no");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterEnchantMatch() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1>\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(ENCHANT, "Sharp Blade");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterEnchantNoMatch() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1>\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(ENCHANT, "Morale");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterEnchantRegex() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1>\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(ENCHANT, "Eye of Dullahan");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterExcept() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1>\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(EXCEPT, "sharp blade");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterRefineGTKeep() throws ParseException {
        String obj1Str = "{\"name\": \"+10 Eye of Dullahan\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(REFINE_GT, "5");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterRefineGTKeepEquals() throws ParseException {
        String obj1Str = "{\"name\": \"+5 Eye of Dullahan\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(REFINE_GT, "5");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterRefineGTRemove() throws ParseException {
        String obj1Str = "{\"name\": \"+7 Eye of Dullahan\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(REFINE_GT, "10");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterRefineLTKeep() throws ParseException {
        String obj1Str = "{\"name\": \"+10 Eye of Dullahan\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(REFINE_LT, "5");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterRefineLTKeepEquals() throws ParseException {
        String obj1Str = "{\"name\": \"+5 Eye of Dullahan\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(REFINE_LT, "5");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterRefineLTRemove() throws ParseException {
        String obj1Str = "{\"name\": \"+7 Eye of Dullahan\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(REFINE_LT, "10");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterSlotsNoSlot() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(NUM_SLOTS, "0");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterSlots0() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan [1]\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(NUM_SLOTS, "0");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

    @Test
    public void filterSlots1() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan [1]\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(NUM_SLOTS, "1");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterEnchantBrokenKeep() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1> (broken)\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(ENCHANT, "Sharp Blade");
            this.put(BROKEN, "yes");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertFalse(result);
    }

    @Test
    public void filterEnchantBrokenRemove() throws ParseException {
        String obj1Str = "{\"name\": \"Eye of Dullahan <Sharp Blade 1> (broken)\"}";
        JSONObject obj1 = (JSONObject) parser.parse(obj1Str);

        Map<String, String> filters = new HashMap<String, String>(){{
            this.put(ENCHANT, "Sharp Blade");
            this.put(BROKEN, "no");
        }};
        boolean result = FilterUtils.filter(obj1, filters);

        Assert.assertTrue(result);
    }

}
