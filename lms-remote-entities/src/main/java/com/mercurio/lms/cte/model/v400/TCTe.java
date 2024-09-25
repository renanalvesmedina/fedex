/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;


/**
 * Tipo Conhecimento de Transporte Eletrônico
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações do CT-e
     */
    private InfCte _infCte;
    
    private InfCTeSupl _infCTeSupl;

    /**
     * Field _signature.
     */
    private Signature _signature;


      //----------------/
     //- Constructors -/
    //----------------/

    public TCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'infCte'. The field 'infCte' has
     * the following description: Informações do CT-e
     * 
     * @return the value of field 'InfCte'.
     */
    public InfCte getInfCte(
    ) {
        return this._infCte;
    }
    
    public InfCTeSupl getInfCTeSupl(
    ) {
        return this._infCTeSupl;
    }

    /**
     * Returns the value of field 'signature'.
     * 
     * @return the value of field 'Signature'.
     */
    public Signature getSignature(
    ) {
        return this._signature;
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
     * Sets the value of field 'infCte'. The field 'infCte' has the
     * following description: Informações do CT-e
     * 
     * @param infCte the value of field 'infCte'.
     */
    public void setInfCte(
            final InfCte infCte) {
        this._infCte = infCte;
    }
    
    public void setInfCTeSupl(
            final InfCTeSupl infCTeSupl) {
        this._infCTeSupl = infCTeSupl;
    }

    /**
     * Sets the value of field 'signature'.
     * 
     * @param signature the value of field 'signature'.
     */
    public void setSignature(
            final Signature signature) {
        this._signature = signature;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.TCTe
     */
    public static TCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(TCTe.class, reader);
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
