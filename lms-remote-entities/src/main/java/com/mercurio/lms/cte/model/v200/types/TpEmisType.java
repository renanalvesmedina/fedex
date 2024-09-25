/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200.types;

/**
 * Enumeration TpEmisType.
 * 
 * @version $Revision$ $Date$
 */
public enum TpEmisType {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_1
     */
    VALUE_1("1"),
    /**
     * Constant VALUE_4
     */
    VALUE_4("4"),
    /**
     * Constant VALUE_5
     */
    VALUE_5("5"),
    /**
     * Constant VALUE_7
     */
    VALUE_7("7"),
    /**
     * Constant VALUE_8
     */
    VALUE_8("8");

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
    private static final java.util.Map<java.lang.String, TpEmisType> enumConstants = new java.util.HashMap<java.lang.String, TpEmisType>();


    static {
        for (TpEmisType c: TpEmisType.values()) {
            TpEmisType.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TpEmisType(final java.lang.String value) {
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
    public static com.mercurio.lms.cte.model.v200.types.TpEmisType fromValue(
            final java.lang.String value) {
        TpEmisType c = TpEmisType.enumConstants.get(value);
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
