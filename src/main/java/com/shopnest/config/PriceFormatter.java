package com.shopnest.config;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

/** Used in templates as ${@fmt.rupee(value)} to show prices like ₹12,499 */
@Component("fmt")
public class PriceFormatter {

    public String rupee(double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("en", "IN"));
        nf.setMaximumFractionDigits(0);
        return "₹" + nf.format(amount);
    }
}
