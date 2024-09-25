/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300.types;

/**
 * Tipo Modal Manifesto
 * 
 * @version $Revision$ $Date$
 */
public enum TModalMD {


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
    VALUE_4("4");
    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, TModalMD> enumConstants = new java.util.HashMap<java.lang.String, TModalMD>();


    static {
        for (TModalMD c: TModalMD.values()) {
            TModalMD.enumConstants.put(c.value, c);
        }

    }

    private TModalMD(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.mercurio.lms.mdfe.model.v300.types.TModalMD fromValue(final java.lang.String value) {
        TModalMD c = TModalMD.enumConstants.get(value);
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
