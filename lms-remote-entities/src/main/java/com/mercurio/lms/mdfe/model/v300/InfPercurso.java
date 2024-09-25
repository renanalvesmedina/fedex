/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações do Percurso do MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfPercurso implements java.io.Serializable {

    /**
     * Sigla das Unidades da Federação do percurso do veículo.
     */
    private com.mercurio.lms.mdfe.model.v300.types.TUf UFPer;
    
    public InfPercurso() {
        super();
    }

    /**
     * Returns the value of field 'UFPer'. The field 'UFPer' has
     * the following description: Sigla das Unidades da Federação
     * do percurso do veículo.
     * 
     * @return the value of field 'UFPer'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TUf getUFPer() {
        return this.UFPer;
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
     * Sets the value of field 'UFPer'. The field 'UFPer' has the
     * following description: Sigla das Unidades da Federação do
     * percurso do veículo.
     * 
     * @param UFPer the value of field 'UFPer'.
     */
    public void setUFPer(final com.mercurio.lms.mdfe.model.v300.types.TUf UFPer) {
        this.UFPer = UFPer;
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
     * com.mercurio.lms.mdfe.model.v300.InfPercurso
     */
    public static com.mercurio.lms.mdfe.model.v300.InfPercurso unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfPercurso) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfPercurso.class, reader);
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
