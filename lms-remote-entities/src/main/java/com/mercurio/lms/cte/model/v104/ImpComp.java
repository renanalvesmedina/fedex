/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Iinformações relativas aos Impostos complementados
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ImpComp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ICMSComp.
     */
    private com.mercurio.lms.cte.model.v104.ICMSComp _ICMSComp;

    /**
     * Informações adicionais de interesse do Fisco
     */
    private java.lang.String _infAdFisco;


      //----------------/
     //- Constructors -/
    //----------------/

    public ImpComp() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ICMSComp'.
     * 
     * @return the value of field 'ICMSComp'.
     */
    public com.mercurio.lms.cte.model.v104.ICMSComp getICMSComp(
    ) {
        return this._ICMSComp;
    }

    /**
     * Returns the value of field 'infAdFisco'. The field
     * 'infAdFisco' has the following description: Informações
     * adicionais de interesse do Fisco
     * 
     * @return the value of field 'InfAdFisco'.
     */
    public java.lang.String getInfAdFisco(
    ) {
        return this._infAdFisco;
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
     * Sets the value of field 'ICMSComp'.
     * 
     * @param ICMSComp the value of field 'ICMSComp'.
     */
    public void setICMSComp(
            final com.mercurio.lms.cte.model.v104.ICMSComp ICMSComp) {
        this._ICMSComp = ICMSComp;
    }

    /**
     * Sets the value of field 'infAdFisco'. The field 'infAdFisco'
     * has the following description: Informações adicionais de
     * interesse do Fisco
     * 
     * @param infAdFisco the value of field 'infAdFisco'.
     */
    public void setInfAdFisco(
            final java.lang.String infAdFisco) {
        this._infAdFisco = infAdFisco;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.ImpComp
     */
    public static com.mercurio.lms.cte.model.v104.ImpComp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.ImpComp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.ImpComp.class, reader);
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
