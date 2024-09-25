/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a.types;

/**
 * Enumeration TpPropType.
 * 
 * @version $Revision$ $Date$
 */
public enum TpPropType {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_0
     */
    VALUE_0("0"),
    /**
     * Constant VALUE_1
     */
    VALUE_1("1"),
    /**
     * Constant VALUE_2
     */
    VALUE_2("2");
    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, TpPropType> enumConstants = new java.util.HashMap<java.lang.String, TpPropType>();


    static {
        for (TpPropType c: TpPropType.values()) {
            TpPropType.enumConstants.put(c.value, c);
        }

    }

    private TpPropType(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.mercurio.lms.mdfe.model.v100a.types.TpPropType fromValue(final java.lang.String value) {
        TpPropType c = TpPropType.enumConstants.get(value);
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
