/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104.types;

/**
 * Tipo Documento Associado
 * 
 * @version $Revision$ $Date$
 */
public enum TDocAssoc {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_0
     */
    VALUE_0("00"),
    /**
     * Constant VALUE_1
     */
    VALUE_1("01"),
    /**
     * Constant VALUE_2
     */
    VALUE_2("02"),
    /**
     * Constant VALUE_3
     */
    VALUE_3("03"),
    /**
     * Constant VALUE_4
     */
    VALUE_4("04"),
    /**
     * Constant VALUE_5
     */
    VALUE_5("05"),
    /**
     * Constant VALUE_6
     */
    VALUE_6("06"),
    /**
     * Constant VALUE_7
     */
    VALUE_7("07"),
    /**
     * Constant VALUE_8
     */
    VALUE_8("08"),
    /**
     * Constant VALUE_9
     */
    VALUE_9("09"),
    /**
     * Constant VALUE_10
     */
    VALUE_10("10"),
    /**
     * Constant VALUE_11
     */
    VALUE_11("11"),
    /**
     * Constant VALUE_12
     */
    VALUE_12("12"),
    /**
     * Constant VALUE_99
     */
    VALUE_99("99");

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
    private static final java.util.Map<java.lang.String, TDocAssoc> enumConstants = new java.util.HashMap<java.lang.String, TDocAssoc>();


    static {
        for (TDocAssoc c: TDocAssoc.values()) {
            TDocAssoc.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TDocAssoc(final java.lang.String value) {
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
    public static com.mercurio.lms.cte.model.v104.types.TDocAssoc fromValue(
            final java.lang.String value) {
        TDocAssoc c = TDocAssoc.enumConstants.get(value);
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
