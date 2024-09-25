/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300.types;

/**
 * Tipo Transportador
 * 
 * @version $Revision$ $Date$
 */
public enum TpTransp {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_FISICA
     */
    VALUE_FISICA("2"),
    /**
     * Constant VALUE_JURIDICA
     */
    VALUE_JURIDICA("1");
    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, TpTransp> enumConstants = new java.util.HashMap<java.lang.String, TpTransp>();


    static {
        for (TpTransp c: TpTransp.values()) {
            TpTransp.enumConstants.put(c.value, c);
        }

    }

    private TpTransp(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.mercurio.lms.mdfe.model.v300.types.TpTransp fromValue(final java.lang.String value) {
        TpTransp c = TpTransp.enumConstants.get(value);
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
