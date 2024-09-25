/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 *  informações do Produto predominante da carga do MDF-e 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfLotacao implements java.io.Serializable {

	/**
     * Informações da localização do carregamento do MDF-e de carga lotação 
     */
    private com.mercurio.lms.mdfe.model.v300.InfLocalCarrega infLocalCarrega;
    
    /**
     * Informações da localização do descarregamento do MDF-e de carga lotação 
     */
    private com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega infLocalDescarrega;

    public InfLotacao() {
        super();
    }

    /**
     * Returns the value of field 'infLocalCarrega'. The field
     * 'infLocalCarrega' has the following description: Informações da localização do
	 *	carregamento do MDF-e de carga lotação 
     * 
     * @return the value of field 'InfLocalCarrega'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfLocalCarrega getInfLocalCarrega() {
        return this.infLocalCarrega;
    }
    
    /**
     * Returns the value of field 'infLocalDescarrega'. The field
     * 'infLocalDescarrega' has the following description: Informações da localização do
	 *	descarregamento do MDF-e de carga lotação 
     * 
     * @return the value of field 'InfLocalDescarrega'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega getInfLocalDescarrega() {
        return this.infLocalDescarrega;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'InfLocalCarrega'. The field 'InfLocalCarrega'
     * has the following description: Informações da localização do
	 *	carregamento do MDF-e de carga lotação
     * 
     * @param InfLocalCarrega the value of field 'infLocalCarrega'.
     */
    public void setInfLocalCarrega(final com.mercurio.lms.mdfe.model.v300.InfLocalCarrega infLocalCarrega) {
        this.infLocalCarrega = infLocalCarrega;
    }
    
    /**
     * Sets the value of field 'InfLocalDescarrega'. The field 'InfLocalDescarrega'
     * has the following description: Informações da localização do
	 *	descarregamento do MDF-e de carga lotação
     * 
     * @param InfLocalDescarrega the value of field 'infLocalDescarrega'.
     */
    public void setInfLocalDescarrega(final com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega infLocalDescarrega) {
        this.infLocalDescarrega = infLocalDescarrega;
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
     * com.mercurio.lms.mdfe.model.v300.InfLotacao
     */
    public static com.mercurio.lms.mdfe.model.v300.InfLotacao unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfLotacao) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfLotacao.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
