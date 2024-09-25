/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400.types;

/**
 * Enumeration TpImpType.
 * 
 * @version $Revision$ $Date$
 */
public enum TpImpType {


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
    VALUE_2("2");

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
    private static final java.util.Map<String, TpImpType> enumConstants = new java.util.HashMap<String, TpImpType>();


    static {
        for (TpImpType c: TpImpType.values()) {
            TpImpType.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TpImpType(final String value) {
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
    public static TpImpType fromValue(
            final String value) {
        TpImpType c = TpImpType.enumConstants.get(value);
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
