/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações do modal
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfModal implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Versão do leiaute específico para o Modal
     */
    private String _versaoModal;

    /**
     * Field _anyObject.
     */
    private Object _anyObject;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfModal() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'anyObject'.
     *
     * @return the value of field 'AnyObject'.
     */
    public Object getAnyObject(
    ) {
        return this._anyObject;
    }

    /**
     * Returns the value of field 'versaoModal'. The field
     * 'versaoModal' has the following description: Versão do
     * leiaute específico para o Modal
     *
     * @return the value of field 'VersaoModal'.
     */
    public String getVersaoModal(
    ) {
        return this._versaoModal;
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
     * Sets the value of field 'anyObject'.
     *
     * @param anyObject the value of field 'anyObject'.
     */
    public void setAnyObject(
            final Object anyObject) {
        this._anyObject = anyObject;
    }

    /**
     * Sets the value of field 'versaoModal'. The field
     * 'versaoModal' has the following description: Versão do
     * leiaute específico para o Modal
     *
     * @param versaoModal the value of field 'versaoModal'.
     */
    public void setVersaoModal(
            final String versaoModal) {
        this._versaoModal = versaoModal;
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
     * com.mercurio.lms.cte.model.v400.InfModal
     */
    public static InfModal unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfModal) org.exolab.castor.xml.Unmarshaller.unmarshal(InfModal.class, reader);
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
