/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300.types;

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
    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, TtipoUnidTransp> enumConstants = new java.util.HashMap<java.lang.String, TtipoUnidTransp>();


    static {
        for (TtipoUnidTransp c: TtipoUnidTransp.values()) {
            TtipoUnidTransp.enumConstants.put(c.value, c);
        }

    }

    private TtipoUnidTransp(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.mercurio.lms.mdfe.model.v300.types.TtipoUnidTransp fromValue(final java.lang.String value) {
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
    public void setValue(final java.lang.String value) {
    }

    /**
     * Method toString.
     * 
     * @return the value of this constant
     */
    public java.lang.String toString() {
        return this.value;
    }

    /**
     * Method value.
     * 
     * @return the value of this constant
     */
    public java.lang.String value() {
        return this.value;
    }

}
