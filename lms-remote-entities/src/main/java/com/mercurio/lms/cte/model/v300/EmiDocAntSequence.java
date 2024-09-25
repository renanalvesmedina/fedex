/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Class EmiDocAntSequence.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EmiDocAntSequence implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Inscrição Estadual
     */
    private java.lang.String _IE;

    /**
     * Sigla da UF
     */
    private com.mercurio.lms.cte.model.v300.types.TUf _UF;


      //----------------/
     //- Constructors -/
    //----------------/

    public EmiDocAntSequence() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     * 
     * @return the value of field 'IE'.
     */
    public java.lang.String getIE(
    ) {
        return this._IE;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF
     * 
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.cte.model.v300.types.TUf getUF(
    ) {
        return this._UF;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     * 
     * @param IE the value of field 'IE'.
     */
    public void setIE(
            final java.lang.String IE) {
        this._IE = IE;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF
     * 
     * @param UF the value of field 'UF'.
     */
    public void setUF(
            final com.mercurio.lms.cte.model.v300.types.TUf UF) {
        this._UF = UF;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * com.mercurio.lms.cte.model.v300.EmiDocAntSequence
     */
    public static com.mercurio.lms.cte.model.v300.EmiDocAntSequence unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.EmiDocAntSequence) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.EmiDocAntSequence.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
