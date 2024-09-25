/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400.types;

/**
 * Tipo Modelo Documento Fiscal
 * 
 * @version $Revision$ $Date$
 */
public enum TModCT {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_57
     */
    VALUE_57("57");

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
    private static final java.util.Map<String, TModCT> enumConstants = new java.util.HashMap<String, TModCT>();


    static {
        for (TModCT c: TModCT.values()) {
            TModCT.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TModCT(final String value) {
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
    public static TModCT fromValue(
            final String value) {
        TModCT c = TModCT.enumConstants.get(value);
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
