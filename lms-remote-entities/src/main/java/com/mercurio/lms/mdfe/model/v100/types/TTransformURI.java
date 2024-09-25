/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100.types;

/**
 * Enumeration TTransformURI.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public enum TTransformURI implements java.io.Serializable {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant HTTP___WWW_W3_ORG_2000_09_XMLDSIG_ENVELOPED_SIGNATUR
     */
    HTTP___WWW_W3_ORG_2000_09_XMLDSIG_ENVELOPED_SIGNATURE("http://www.w3.org/2000/09/xmldsig#enveloped-signature"),
    /**
     * Constant HTTP___WWW_W3_ORG_TR_2001_REC_XML_C14N_20010315
     */
    HTTP___WWW_W3_ORG_TR_2001_REC_XML_C14N_20010315("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field value.
     */
    private final java.lang.String value;


      //----------------/
     //- Constructors -/
    //----------------/

    private TTransformURI(final java.lang.String value) {
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
    public static com.mercurio.lms.mdfe.model.v100.types.TTransformURI fromValue(
            final java.lang.String value) {
        for (TTransformURI c: TTransformURI.values()) {
            if (c.value.equals(value)) {
                return c;
            }
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
