/*      */
package com.omg.framework.common.utils.money;
/*      */ 
/*      */

import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.util.Currency;

/*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Money
/*      */ implements Serializable, Comparable<Object>
/*      */ {
    /*      */   private static final long serialVersionUID = 6009335074727417445L;
    /*      */   public static final String DEFAULT_CURRENCY_CODE = "CNY";
    /*      */   public static final int DEFAULT_ROUNDING_MODE = 6;
    /*      */   public static final int DEFAULT_EXPAND_LENGTH = 6;
    /*      */   public static final int DEFAULT_DIVIDE_LENGTH = 6;
    /*      */   public static final int DEFAULT_EXPAND_VALUE = 1000000;
    /*   72 */   private static final int[] centFactors = {1, 10, 100, 1000};
    /*      */
/*   74 */   private static final Long max_amount_value = Long.valueOf(9223372036854L);
    /*      */
/*      */   private static final int max_amount_length = 13;
    /*      */
/*   78 */   private static final int max_long_length = String.valueOf(Long.MAX_VALUE).length();
    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private long calculate_amount;
    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private BigDecimal calculate_big_amount;
    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */   private long cent;
    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */   private Currency currency;

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money()
/*      */ {
/*  108 */
        this(0.0D);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(long yuan, int cent)
/*      */ {
/*  123 */
        this(yuan, cent, Currency.getInstance("CNY"));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(long yuan, int cent, Currency currency)
/*      */ {
/*  139 */
        this.currency = currency;
/*  140 */
        if ((yuan + "").length() >= 13)
/*      */ {
/*  142 */
            this.calculate_big_amount = new BigDecimal(yuan).add(BigDecimal.valueOf(cent, currency.getDefaultFractionDigits()));
/*      */
        }
/*      */     
/*  145 */
        this.cent = (yuan * getCentFactor() + cent);
/*  146 */
        this.calculate_amount = (this.cent * 1000000L / getCentFactor());
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(String amount)
/*      */ {
/*  159 */
        this(amount, Currency.getInstance("CNY"));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(String amount, Currency currency)
/*      */ {
/*  178 */
        this(new BigDecimal(amount), currency);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(String amount, Currency currency, int roundingMode)
/*      */ {
/*  196 */
        this(new BigDecimal(amount), currency, roundingMode);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(double amount)
/*      */ {
/*  213 */
        this(amount, Currency.getInstance("CNY"));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(double amount, Currency currency)
/*      */ {
/*  227 */
        this.currency = currency;
/*  228 */
        if (amount > max_amount_value.longValue()) {
/*  229 */
            this.calculate_big_amount = new BigDecimal(amount);
/*      */
        } else {
/*  231 */
            this.calculate_amount = Math.round(amount * 1000000.0D);
/*      */
        }
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(BigDecimal amount)
/*      */ {
/*  247 */
        this(amount, Currency.getInstance("CNY"));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(BigDecimal amount, int roundingMode)
/*      */ {
/*  264 */
        this(amount, Currency.getInstance("CNY"), roundingMode);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(BigDecimal amount, Currency currency)
/*      */ {
/*  280 */
        this(amount, currency, 6);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money(BigDecimal amount, Currency currency, int roundingMode)
/*      */ {
/*  298 */
        this.currency = currency;
/*  299 */
        assertOutOfBound(amount.toString());
/*  300 */
        if (this.calculate_big_amount == null) {
/*  301 */
            this.calculate_amount = rounding(amount.movePointRight(6), roundingMode);
/*      */
        }
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public BigDecimal getAmount()
/*      */ {
/*  312 */
        if (this.calculate_big_amount != null) {
/*  313 */
            return this.calculate_big_amount;
/*      */
        }
/*  315 */
        return BigDecimal.valueOf(this.calculate_amount, 6);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public void setAmount(BigDecimal amount)
/*      */ {
/*  325 */
        if (amount != null) {
/*  326 */
            assertOutOfBound(amount.toString());
/*  327 */
            if (this.calculate_big_amount == null) {
/*  328 */
                this.calculate_amount = rounding(amount.movePointRight(6), 6);
/*      */
            }
/*      */
        }
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public long getCent()
/*      */     throws RuntimeException
/*      */ {
/*  343 */
        if (this.calculate_big_amount != null) {
/*  344 */
            throw new RuntimeException("超出long金额范围,请调用方法getAmount!");
/*      */
        }
/*  346 */
        return this.calculate_amount * getCentFactor() / 1000000L;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Currency getCurrency()
/*      */ {
/*  355 */
        return this.currency;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public int getCentFactor()
/*      */ {
/*  364 */
        return centFactors[this.currency.getDefaultFractionDigits()];
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public boolean equals(Object other)
/*      */ {
/*  387 */
        if (other == null) {
/*  388 */
            return false;
/*      */
        }
/*  390 */
        if (!(other instanceof Money)) {
/*  391 */
            return false;
/*      */
        }
/*  393 */
        Money m = (Money) other;
/*  394 */
        if (m.calculate_big_amount != null) {
/*  395 */
            return (this.currency.equals(m.currency)) && (getAmount().compareTo(m.calculate_big_amount) == 0);
/*      */
        }
/*      */     
/*  398 */
        return (this.currency.equals(m.currency)) && (this.calculate_amount == m.calculate_amount);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public int hashCode()
/*      */ {
/*  432 */
        if (this.calculate_big_amount != null)
/*      */ {
/*  434 */
            return this.calculate_big_amount.hashCode() ^ this.calculate_big_amount.hashCode() >>> 32;
/*      */
        }
/*      */     
/*  437 */
        return (int) (this.calculate_amount ^ this.calculate_amount >>> 32);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public int compareTo(Object other)
/*      */ {
/*  465 */
        return compareTo((Money) other);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public int compareTo(Money other)
/*      */ {
/*  485 */
        assertSameCurrencyAs(other);
/*  486 */
        if ((other.calculate_big_amount != null) || (this.calculate_big_amount != null)) {
/*  487 */
            return getAmount().compareTo(other.calculate_big_amount);
/*      */
        }
/*  489 */
        if (this.calculate_amount < other.calculate_amount)
/*  490 */ return -1;
/*  491 */
        if (this.calculate_amount == other.calculate_amount) {
/*  492 */
            return 0;
/*      */
        }
/*  494 */
        return 1;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public boolean greaterThan(Money other)
/*      */ {
/*  514 */
        return compareTo(other) > 0;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money add(Money other)
/*      */ {
/*  535 */
        if ((other.calculate_big_amount != null) || (this.calculate_big_amount != null) || (Math.max((this.calculate_amount + "").length(), (other.calculate_amount + "").length()) + 1 >= max_long_length))
/*      */ {
/*      */ 
/*      */ 
/*  539 */
            return newMoneyWithSameCurrency(getAmount().add(other.getAmount()));
/*      */
        }
/*  541 */
        return newMoneyWithSameCurrency(this.calculate_amount + other.calculate_amount);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money addTo(Money other)
/*      */ {
/*  560 */
        assertSameCurrencyAs(other);
/*  561 */
        if ((other.calculate_big_amount != null) || (this.calculate_big_amount != null) || (Math.max((this.calculate_amount + "").length(), (other.calculate_amount + "").length()) + 1 >= max_long_length))
/*      */ {
/*      */ 
/*      */ 
/*  565 */
            this.calculate_big_amount = getAmount().add(other.getAmount());
/*  566 */
            return this;
/*      */
        }
/*  568 */
        this.calculate_amount += other.calculate_amount;
/*      */     
/*  570 */
        return this;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money subtract(Money other)
/*      */ {
/*  589 */
        assertSameCurrencyAs(other);
/*  590 */
        if ((other.calculate_big_amount != null) || (this.calculate_big_amount != null)) {
/*  591 */
            return newMoneyWithSameCurrency(getAmount().subtract(other.getAmount()));
/*      */
        }
/*  593 */
        return newMoneyWithSameCurrency(this.calculate_amount - other.calculate_amount);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money subtractFrom(Money other)
/*      */ {
/*  612 */
        assertSameCurrencyAs(other);
/*  613 */
        if ((other.calculate_big_amount != null) || (this.calculate_big_amount != null)) {
/*  614 */
            this.calculate_big_amount = getAmount().subtract(other.getAmount());
/*  615 */
            return this;
/*      */
        }
/*  617 */
        this.calculate_amount -= other.calculate_amount;
/*      */     
/*  619 */
        return this;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiply(long val)
/*      */ {
/*  634 */
        if ((this.calculate_big_amount != null) || (getAddLength(Long.valueOf(this.calculate_amount), Long.valueOf(val)) >= max_long_length))
/*      */ {
/*  636 */
            return newMoneyWithSameCurrency(getAmount().multiply(new BigDecimal(val)));
/*      */
        }
/*  638 */
        return newMoneyWithSameCurrency(this.calculate_amount * val);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiplyBy(long val)
/*      */ {
/*  653 */
        if ((this.calculate_big_amount != null) || (getAddLength(Long.valueOf(this.calculate_amount), Long.valueOf(val)) >= max_long_length))
/*      */ {
/*  655 */
            this.calculate_big_amount = getAmount().multiply(new BigDecimal(val));
/*  656 */
            return this;
/*      */
        }
/*  658 */
        this.calculate_amount *= val;
/*      */     
/*  660 */
        return this;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiply(double val)
/*      */ {
/*  675 */
        if ((this.calculate_big_amount != null) || (getAddLength(Long.valueOf(this.calculate_amount), Double.valueOf(val)) >= max_long_length))
/*      */ {
/*  677 */
            return newMoneyWithSameCurrency(getAmount().multiply(new BigDecimal(val)));
/*      */
        }
/*  679 */
        return newMoneyWithSameCurrency(Math.round(this.calculate_amount * val));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiplyBy(double val)
/*      */ {
/*  694 */
        if ((this.calculate_big_amount != null) || (getAddLength(Long.valueOf(this.calculate_amount), Double.valueOf(val)) >= max_long_length))
/*      */ {
/*  696 */
            this.calculate_big_amount = getAmount().multiply(new BigDecimal(val));
/*  697 */
            return this;
/*      */
        }
/*      */     
/*  700 */
        this.calculate_amount = Math.round(this.calculate_amount * val);
/*      */     
/*  702 */
        return this;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiply(BigDecimal val)
/*      */ {
/*  719 */
        return multiply(val, 6);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiplyBy(BigDecimal val)
/*      */ {
/*  735 */
        return multiplyBy(val, 6);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiply(BigDecimal val, int roundingMode)
/*      */ {
/*  753 */
        BigDecimal newcalculate_amount = getAmount().multiply(val);
/*  754 */
        if (isOutOfBound(newcalculate_amount.toString()).booleanValue()) {
/*  755 */
            return newMoneyWithSameCurrency(newcalculate_amount);
/*      */
        }
/*  757 */
        return newMoneyWithSameCurrency(rounding(newcalculate_amount.movePointRight(6), roundingMode));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money multiplyBy(BigDecimal val, int roundingMode)
/*      */ {
/*  776 */
        BigDecimal newcalculate_amount = getAmount().multiply(val);
/*  777 */
        assertOutOfBound(newcalculate_amount.toString());
/*  778 */
        if (this.calculate_big_amount == null) {
/*  779 */
            this.calculate_amount = rounding(newcalculate_amount.movePointRight(6), roundingMode);
/*      */
        }
/*      */     
/*      */ 
/*  783 */
        return this;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money divide(double val)
/*      */ {
/*  800 */
        if ((this.calculate_big_amount != null) || (this.calculate_amount / val >= 9.223372036854776E18D))
/*      */ {
/*  802 */
            return newMoneyWithSameCurrency(getAmount().divide(new BigDecimal(val), 6, 6));
/*      */
        }
/*      */     
/*  805 */
        return newMoneyWithSameCurrency(Math.round(this.calculate_amount / val));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money divideBy(double val)
/*      */ {
/*  820 */
        if ((this.calculate_big_amount != null) || (this.calculate_amount / val > max_amount_value.longValue())) {
/*  821 */
            this.calculate_big_amount = getAmount().divide(new BigDecimal(val), 6, 6);
/*      */       
/*  823 */
            return this;
/*      */
        }
/*  825 */
        this.calculate_amount = Math.round(this.calculate_amount / val);
/*      */     
/*  827 */
        return this;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money divide(BigDecimal val)
/*      */ {
/*  844 */
        return divide(val, 6);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money divide(BigDecimal val, int roundingMode)
/*      */ {
/*  862 */
        BigDecimal newcalculate_amount = getAmount().divide(val, 6, roundingMode);
/*      */     
/*      */ 
/*  865 */
        if (isOutOfBound(newcalculate_amount.toString()).booleanValue()) {
/*  866 */
            return newMoneyWithSameCurrency(newcalculate_amount);
/*      */
        }
/*  868 */
        return newMoneyWithSameCurrency(rounding(newcalculate_amount.movePointRight(6), roundingMode));
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money divideBy(BigDecimal val)
/*      */ {
/*  885 */
        return divideBy(val, 6);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money divideBy(BigDecimal val, int roundingMode)
/*      */ {
/*  901 */
        BigDecimal newcalculate_amount = getAmount().divide(val, 6, roundingMode);
/*      */     
/*  903 */
        if (isOutOfBound(newcalculate_amount.toString()).booleanValue()) {
/*  904 */
            this.calculate_big_amount = newcalculate_amount;
/*  905 */
            return this;
/*      */
        }
/*  907 */
        this.calculate_amount = rounding(newcalculate_amount.movePointRight(6), roundingMode);
/*      */     
/*      */ 
/*  910 */
        return this;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money[] allocate(int targets)
/*      */ {
/*  927 */
        Money[] results = new Money[targets];
/*  928 */
        if (this.calculate_big_amount != null) {
/*  929 */
            Money result = newMoneyWithSameCurrency(this.calculate_big_amount.divide(new BigDecimal(targets), 6, 6));
/*      */       
/*  931 */
            for (int i = 0; i < targets; i++) {
/*  932 */
                results[i] = result;
/*      */
            }
/*  934 */
            return results;
/*      */
        }
/*  936 */
        Money lowResult = newMoneyWithSameCurrency(this.calculate_amount / targets);
/*  937 */
        Money highResult = newMoneyWithSameCurrency(lowResult.calculate_amount + 1L);
/*      */     
/*  939 */
        int remainder = (int) this.calculate_amount % targets;
/*      */     
/*  941 */
        for (int i = 0; i < remainder; i++) {
/*  942 */
            results[i] = highResult;
/*      */
        }
/*      */     
/*  945 */
        for (int i = remainder; i < targets; i++) {
/*  946 */
            results[i] = lowResult;
/*      */
        }
/*      */     
/*  949 */
        return results;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public Money[] allocate(long[] ratios)
/*      */ {
/*  964 */
        Money[] results = new Money[ratios.length];
/*      */     
/*  966 */
        long total = 0L;
/*      */     
/*  968 */
        for (int i = 0; i < ratios.length; i++) {
/*  969 */
            total += ratios[i];
/*      */
        }
/*      */     
/*  972 */
        long remainder = this.calculate_amount;
/*      */     
/*  974 */
        for (int i = 0; i < results.length; i++) {
/*  975 */
            results[i] = newMoneyWithSameCurrency(this.calculate_amount * ratios[i] / total);
/*  976 */
            remainder -= results[i].calculate_amount;
/*      */
        }
/*      */     
/*  979 */
        for (int i = 0; i < remainder; i++) {
/*  980 */
            results[i].calculate_amount += 1L;
/*      */
        }
/*      */     
/*  983 */
        return results;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public String toString()
/*      */ {
/*  992 */
        return getAmount().toString();
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    protected void assertSameCurrencyAs(Money other)
/*      */ {
/* 1011 */
        if (!this.currency.equals(other.currency)) {
/* 1012 */
            throw new IllegalArgumentException("Money math currency mismatch.");
/*      */
        }
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    protected long rounding(BigDecimal val, int roundingMode)
/*      */ {
/* 1027 */
        return val.setScale(0, roundingMode).longValue();
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    private void assertOutOfBound(String value)
/*      */ {
/* 1036 */
        if (isOutOfBound(value).booleanValue()) {
/* 1037 */
            this.calculate_big_amount = new BigDecimal(value);
/*      */
        }
/*      */
    }

    /*      */
/*      */
    private Boolean isOutOfBound(String value) {
/* 1042 */
        int length = 0;
/* 1043 */
        if (value.contains(".")) {
/* 1044 */
            length = value.split("\\.")[0].length();
/*      */
        } else {
/* 1046 */
            length = value.length();
/*      */
        }
/* 1048 */
        if (length >= 13) {
/* 1049 */
            return Boolean.valueOf(true);
/*      */
        }
/* 1051 */
        return Boolean.valueOf(false);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    private int getAddLength(Object x1, Object x2)
/*      */ {
/* 1062 */
        int length = 0;
/* 1063 */
        String value = x2.toString();
/* 1064 */
        if (value.contains(".")) {
/* 1065 */
            length = value.split("\\.")[0].length();
/*      */
        } else {
/* 1067 */
            length = value.length();
/*      */
        }
/* 1069 */
        return x1.toString().length() + length;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    protected Money newMoneyWithSameCurrency(long calculate_amount)
/*      */ {
/* 1081 */
        Money money = new Money(0.0D, this.currency);
/*      */     
/* 1083 */
        money.calculate_amount = calculate_amount;
/*      */     
/* 1085 */
        return money;
/*      */
    }

    /*      */
/*      */
    protected Money newMoneyWithSameCurrency(BigDecimal calculate_big_amount) {
/* 1089 */
        Money money = new Money(0.0D, this.currency);
/*      */     
/* 1091 */
        money.calculate_big_amount = calculate_big_amount;
/*      */     
/* 1093 */
        return money;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public String dump()
/*      */ {
/* 1104 */
        String lineSeparator = System.getProperty("line.separator");
/*      */     
/* 1106 */
        StringBuffer sb = new StringBuffer();
/*      */     
/* 1108 */
        sb.append("amount = ").append(getAmount()).append(lineSeparator);
/* 1109 */
        sb.append("currency = ").append(this.currency);
/*      */     
/* 1111 */
        return sb.toString();
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public String getCurrencyCode()
/*      */ {
/* 1120 */
        return this.currency.getCurrencyCode();
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public void setCurrencyCode(String currencyCode)
/*      */ {
/* 1129 */
        this.currency = Currency.getInstance(currencyCode);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public void setYuan(BigDecimal yuan)
/*      */ {
/* 1138 */
        if (yuan != null) {
/* 1139 */
            assertOutOfBound(yuan.movePointLeft(6).toString());
/* 1140 */
            if (this.calculate_big_amount == null) {
/* 1141 */
                this.calculate_amount = rounding(yuan, 6);
/*      */
            }
/*      */
        }
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public BigDecimal getYuan()
/*      */ {
/* 1153 */
        if (this.calculate_big_amount != null) {
/* 1154 */
            return getAmount().movePointRight(6);
/*      */
        }
/* 1156 */
        return new BigDecimal(this.calculate_amount);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public long getLongYuan()
/*      */ {
/* 1166 */
        if (this.calculate_big_amount != null) {
/* 1167 */
            throw new RuntimeException("超出Long的最大值");
/*      */
        }
/* 1169 */
        return this.calculate_amount;
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public BigDecimal getDecimalYuan()
/*      */ {
/* 1178 */
        if (this.calculate_big_amount != null) {
/* 1179 */
            return this.calculate_big_amount.movePointRight(6).setScale(0, 6);
/*      */
        }
/* 1181 */
        return new BigDecimal(this.calculate_amount);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public BigDecimal getNegeteYuan()
/*      */ {
/* 1190 */
        if (this.calculate_big_amount != null) {
/* 1191 */
            return getAmount().movePointRight(6).negate();
/*      */
        }
/* 1193 */
        return new BigDecimal(this.calculate_amount).negate();
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public String getAmount2Figures()
/*      */ {
/* 1202 */
        String amount2Figures = getAmount().toString();
/* 1203 */
        if (!amount2Figures.contains(".")) {
/* 1204 */
            return amount2Figures + ".00";
/*      */
        }
/* 1206 */
        return amount2Figures.substring(0, amount2Figures.length() - 4);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public static boolean isIntegerValue(BigDecimal bd)
/*      */ {
/* 1215 */
        return (bd.signum() == 0) || (bd.scale() <= 0) || (bd.stripTrailingZeros().scale() <= 0);
/*      */
    }

    /*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */
    public boolean isIntegerValue()
/*      */ {
/* 1224 */
        return (this.calculate_big_amount.signum() == 0) || (this.calculate_big_amount.scale() <= 0) || (this.calculate_big_amount.stripTrailingZeros().scale() <= 0);
/*      */
    }
/*      */
}

