/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400.types;

/**
 * Tipo da Unidade de Transporte
 * 
 * @version $Revision$ $Date$
 */
public enum TtipoUnidTransp {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_1
     */
    VALUE_1("1"),
    /**
     * Constant VALUE_2
     */
    VALUE_2("2"),
    /**
     * Constant VALUE_3
     */
    VALUE_3("3"),
    /**
     * Constant VALUE_4
     */
    VALUE_4("4"),
    /**
     * Constant VALUE_5
     */
    VALUE_5("5"),
    /**
     * Constant VALUE_6
     */
    VALUE_6("6"),
    /**
     * Constant VALUE_7
     */
    VALUE_7("7");

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
    private static final java.util.Map<String, TtipoUnidTransp> enumConstants = new java.util.HashMap<String, TtipoUnidTransp>();


    static {
        for (TtipoUnidTransp c: TtipoUnidTransp.values()) {
            TtipoUnidTransp.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TtipoUnidTransp(final String value) {
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
    public static TtipoUnidTransp fromValue(
            final String value) {
        TtipoUnidTransp c = TtipoUnidTransp.enumConstants.get(value);
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
