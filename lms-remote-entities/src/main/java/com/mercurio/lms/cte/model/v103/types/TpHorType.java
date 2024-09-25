/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103.types;

/**
 * Enumeration TphorType.
 * 
 * @version $Revision$ $Date$
 */
public enum TpHorType {


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
	VALUE_2("2"),
	/**
	 * Constant VALUE_3
	 */
	VALUE_3("3"),
    /**
     * Constant VALUE_4
     */
    VALUE_4("4");

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
    private static final java.util.Map<java.lang.String, TpHorType> enumConstants = new java.util.HashMap<java.lang.String, TpHorType>();


    static {
        for (TpHorType c: TpHorType.values()) {
            TpHorType.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TpHorType(final java.lang.String value) {
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
    public static com.mercurio.lms.cte.model.v103.types.TpHorType fromValue(
            final java.lang.String value) {
        TpHorType c = TpHorType.enumConstants.get(value);
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
