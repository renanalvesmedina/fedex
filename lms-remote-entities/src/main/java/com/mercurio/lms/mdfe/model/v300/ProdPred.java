/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 *  informações do Produto
 *  predominante da carga do MDF-e 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ProdPred implements java.io.Serializable {

    /**
     * Tipo da Carga
     */
    private com.mercurio.lms.mdfe.model.v300.types.TpCargaType tpCarga;

    /**
     * Descrição do produto predominante
     */
    private java.lang.String xProd;
    
    /**
     * Informação da Carga Lotação
     */
    private com.mercurio.lms.mdfe.model.v300.InfLotacao infLotacao;

    public ProdPred() {
        super();
    }

    /**
     * Returns the value of field 'tpCarga'. The field
     * 'tpCarga' has the following description: Tipo da Carga
     * 
     * @return the value of field 'TpCarga'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TpCargaType getTpCarga() {
        return this.tpCarga;
    }

    /**
     * Returns the value of field 'xProd'. The field 'xProd' has
     * the following description: Descrição do produto predominante
     * 
     * @return the value of field 'XProd'.
     */
    public java.lang.String getXProd() {
        return this.xProd;
    }
    
    /**
     * Returns the value of field 'infLotacao'. The field 'infLotacao' has
     * the following description: Informação da Carga Lotação
     * 
     * @return the value of field 'InfLotacao'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfLotacao getInfLotacao() {
    	return this.infLotacao;
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
     * Sets the value of field 'tpCarga'. The field 'tpCarga'
     * has the following description: Tipo da Carga
     * 
     * @param tpCarga the value of field 'tpCarga'.
     */
    public void setTpCarga(final com.mercurio.lms.mdfe.model.v300.types.TpCargaType tpCarga) {
        this.tpCarga = tpCarga;
    }

    /**
     * Sets the value of field 'xProd'. The field 'xProd' has the
     * following description: Descrição do produto predominante 
     * 
     * @param xProd the value of field 'xProd'.
     */
    public void setXProd(final java.lang.String xProd) {
        this.xProd = xProd;
    }
    
    /**
     * Sets the value of field 'infLotacao'. The field 'infLotacao' has the
     * following description: Informação da Carga Lotação 
     * 
     * @param infLotacao the value of field 'infLotacao'.
     */
    public void setInfLotacao(final com.mercurio.lms.mdfe.model.v300.InfLotacao infLotacao) {
    	this.infLotacao = infLotacao;
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
     * com.mercurio.lms.mdfe.model.v300.ProdPred
     */
    public static com.mercurio.lms.mdfe.model.v300.ProdPred unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.ProdPred) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.ProdPred.class, reader);
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
