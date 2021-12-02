package poring.world.market.filter;

import org.junit.Assert;
import org.junit.Test;
import poring.world.market.filter.FilterUtils;

import static poring.world.constants.Constants.*;

public class TestFilterUtilsValidate {

    @Test
    public void testValidateEmptyValue() {
        String result = FilterUtils.validate("", "");

        Assert.assertEquals("empty value", result);
    }

    @Test
    public void testBrokenMultiple() {
        String result = FilterUtils.validate(BROKEN, "yes && no");

        Assert.assertEquals("not a multiple values field, remove _&&_", result);
    }

    @Test
    public void testBrokenInvalidValue() {
        String result = FilterUtils.validate(BROKEN, "maybe");

        Assert.assertEquals("use only _yes_ or _no_", result);
    }

    @Test
    public void testBrokenYes() {
        String result = FilterUtils.validate(BROKEN, "yes");

        Assert.assertNull(result);
    }

    @Test
    public void testBrokenNo() {
        String result = FilterUtils.validate(BROKEN, "no");

        Assert.assertNull(result);
    }

    @Test
    public void testMaxPriceNumericValueInvalid() {
        String result = FilterUtils.validate(MAX_PRICE, "A");

        Assert.assertEquals("use only numbers", result);
    }

    @Test
    public void testMaxPriceNumericValueValid() {
        String result = FilterUtils.validate(MAX_PRICE, "10");

        Assert.assertNull(result);
    }

    @Test
    public void testMaxPriceRefineGTcValueInvalid() {
        String result = FilterUtils.validate(REFINE_GT, "A");

        Assert.assertEquals("use only numbers", result);
    }

    @Test
    public void testMaxPriceRefineGTValueValid() {
        String result = FilterUtils.validate(REFINE_GT, "10");

        Assert.assertNull(result);
    }

    @Test
    public void testMaxPriceRefineLTValueInvalid() {
        String result = FilterUtils.validate(REFINE_LT, "A");

        Assert.assertEquals("use only numbers", result);
    }

    @Test
    public void testMaxPriceRefineLTValueValid() {
        String result = FilterUtils.validate(REFINE_LT, "10");

        Assert.assertNull(result);
    }

    @Test
    public void testNumSlotsLowerLimit() {
        String result = FilterUtils.validate(NUM_SLOTS, "-1");

        Assert.assertEquals("only from 0 to 2 slots", result);
    }

    @Test
    public void testNumSlotsUpperLimit() {
        String result = FilterUtils.validate(NUM_SLOTS, "3");

        Assert.assertEquals("only from 0 to 2 slots", result);
    }

    @Test
    public void testNumSlotsInvalid() {
        String result = FilterUtils.validate(NUM_SLOTS, "A");

        Assert.assertEquals("use only numbers", result);
    }

    @Test
    public void testCategoryInvalid() {
        String value = "nova-categoria";
        String result = FilterUtils.validate(CATEGORY, value);

        assert result != null;
        assert result.startsWith("Invalid value \"" + value + "\". Try: ");
    }

}