/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400.types;

/**
 * Tipo Modal transporte
 * 
 * @version $Revision$ $Date$
 */
public enum TModTransp {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_1
     */
    VALUE_1("01"),
    /**
     * Constant VALUE_2
     */
    VALUE_2("02"),
    /**
     * Constant VALUE_3
     */
    VALUE_3("03"),
    /**
     * Constant VALUE_4
     */
    VALUE_4("04"),
    /**
     * Constant VALUE_5
     */
    VALUE_5("05"),
    /**
     * Constant VALUE_6
     */
    VALUE_6("06");

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field value.
     */
    private final String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<String, TModTransp> enumConstants = new java.util.HashMap<String, TModTransp>();


    static {
        for (TModTransp c: TModTransp.values()) {
            TModTransp.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TModTransp(final String value) {
        this.value = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method fromValue.
     *
     * @param value
     * @return the constant for this value
     */
    public static TModTransp fromValue(
            final String value) {
        TModTransp c = TModTransp.enumConstants.get(value);
        if (c != null) {
            return c;
        }
        throw new IllegalArgumentException(value);
    }

    /**
     *
     *
     * @param value
     */
    public void setValue(
            final String value) {
    }

    /**
     * Method toString.
     *
     * @return the value of this constant
     */
    public String toString(
    ) {
        return this.value;
    }

    /**
     * Method value.
     *
     * @return the value of this constant
     */
    public String value(
    ) {
        return this.value;
    }

}
