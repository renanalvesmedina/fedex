/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Tipo procEvento
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TProcEvento implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private java.lang.String _versao;

    /**
     * Field _eventoMDFe.
     */
    private com.mercurio.lms.mdfe.model.v100.EventoMDFe _eventoMDFe;

    /**
     * Field _retEventoMDFe.
     */
    private com.mercurio.lms.mdfe.model.v100.RetEventoMDFe _retEventoMDFe;


      //----------------/
     //- Constructors -/
    //----------------/

    public TProcEvento() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'eventoMDFe'.
     * 
     * @return the value of field 'EventoMDFe'.
     */
    public com.mercurio.lms.mdfe.model.v100.EventoMDFe getEventoMDFe(
    ) {
        return this._eventoMDFe;
    }

    /**
     * Returns the value of field 'retEventoMDFe'.
     * 
     * @return the value of field 'RetEventoMDFe'.
     */
    public com.mercurio.lms.mdfe.model.v100.RetEventoMDFe getRetEventoMDFe(
    ) {
        return this._retEventoMDFe;
    }

    /**
     * Returns the value of field 'versao'.
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao(
    ) {
        return this._versao;
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
     * Sets the value of field 'eventoMDFe'.
     * 
     * @param eventoMDFe the value of field 'eventoMDFe'.
     */
    public void setEventoMDFe(
            final com.mercurio.lms.mdfe.model.v100.EventoMDFe eventoMDFe) {
        this._eventoMDFe = eventoMDFe;
    }

    /**
     * Sets the value of field 'retEventoMDFe'.
     * 
     * @param retEventoMDFe the value of field 'retEventoMDFe'.
     */
    public void setRetEventoMDFe(
            final com.mercurio.lms.mdfe.model.v100.RetEventoMDFe retEventoMDFe) {
        this._retEventoMDFe = retEventoMDFe;
    }

    /**
     * Sets the value of field 'versao'.
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final java.lang.String versao) {
        this._versao = versao;
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
     * com.mercurio.lms.mdfe.model.v100.TProcEvento
     */
    public static com.mercurio.lms.mdfe.model.v100.TProcEvento unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.TProcEvento) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.TProcEvento.class, reader);
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
