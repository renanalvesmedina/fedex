/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Tipo Pedido de Consulta do Recibo do Lote de CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TConsReciCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private java.lang.String _versao;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.cte.model.v103.types.TAmb _tpAmb;

    /**
     * Número do Recibo do lote a ser consultado
     */
    private java.lang.String _nRec;


      //----------------/
     //- Constructors -/
    //----------------/

    public TConsReciCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo do lote a ser
     * consultado
     * 
     * @return the value of field 'NRec'.
     */
    public java.lang.String getNRec(
    ) {
        return this._nRec;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.cte.model.v103.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
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
     * Sets the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo do lote a ser
     * consultado
     * 
     * @param nRec the value of field 'nRec'.
     */
    public void setNRec(
            final java.lang.String nRec) {
        this._nRec = nRec;
    }

    /**
     * Sets the value of field 'tpAmb'. The field 'tpAmb' has the
     * following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @param tpAmb the value of field 'tpAmb'.
     */
    public void setTpAmb(
            final com.mercurio.lms.cte.model.v103.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
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
     * com.mercurio.lms.cte.model.v103.TConsReciCTe
     */
    public static com.mercurio.lms.cte.model.v103.TConsReciCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.TConsReciCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.TConsReciCTe.class, reader);
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
