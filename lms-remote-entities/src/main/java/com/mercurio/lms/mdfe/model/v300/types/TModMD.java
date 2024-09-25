/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300.types;

/**
 * Tipo Modelo Manifesto de Documento Fiscal Eletrônico
 * 
 * @version $Revision$ $Date$
 */
public enum TModMD {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_58
     */
    VALUE_58("58");
    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, TModMD> enumConstants = new java.util.HashMap<java.lang.String, TModMD>();


    static {
        for (TModMD c: TModMD.values()) {
            TModMD.enumConstants.put(c.value, c);
        }

    }

    private TModMD(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.mercurio.lms.mdfe.model.v300.types.TModMD fromValue(final java.lang.String value) {
        TModMD c = TModMD.enumConstants.get(value);
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
