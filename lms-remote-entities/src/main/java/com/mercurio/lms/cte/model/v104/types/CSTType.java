/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104.types;

/**
 * Enumeration CSTType.
 * 
 * @version $Revision$ $Date$
 */
public enum CSTType {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_0
     */
    VALUE_0("00"),
    /**
     * Constant VALUE_20
     */
    VALUE_20("20"),
    /**
     * Constant VALUE_40
     */
    VALUE_40("40"),
    /**
     * Constant VALUE_41
     */
    VALUE_41("41"),
    /**
     * Constant VALUE_51
     */
    VALUE_51("51"),
    /**
     * Constant VALUE_60
     */
    VALUE_60("60"),
    /**
     * Constant VALUE_90
     */
    VALUE_90("90");

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, CSTType> enumConstants = new java.util.HashMap<java.lang.String, CSTType>();


    static {
        for (CSTType c: CSTType.values()) {
            CSTType.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private CSTType(final java.lang.String value) {
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
    public static com.mercurio.lms.cte.model.v104.types.CSTType fromValue(
            final java.lang.String value) {
        CSTType c = CSTType.enumConstants.get(value);
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
            final java.lang.String value) {
    }

    /**
     * Method toString.
     * 
     * @return the value of this constant
     */
    public java.lang.String toString(
    ) {
        return this.value;
    }

    /**
     * Method value.
     * 
     * @return the value of this constant
     */
    public java.lang.String value(
    ) {
        return this.value;
    }

}
