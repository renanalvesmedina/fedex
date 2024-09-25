/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300.types;

/**
 * Enumeration DescEventoType.
 * 
 * @version $Revision$ $Date$
 */
public enum DescEventoType {


      //------------------/
     //- Enum Constants -/
    //------------------/

	CANCELAMENTO("Cancelamento"),
	
    /**
     * Constant ENCERRAMENTO
     */
    ENCERRAMENTO("Encerramento");
    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, DescEventoType> enumConstants = new java.util.HashMap<java.lang.String, DescEventoType>();


    static {
        for (DescEventoType c: DescEventoType.values()) {
            DescEventoType.enumConstants.put(c.value, c);
        }

    }

    private DescEventoType(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.mercurio.lms.mdfe.model.v300.types.DescEventoType fromValue(final java.lang.String value) {
        DescEventoType c = DescEventoType.enumConstants.get(value);
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
