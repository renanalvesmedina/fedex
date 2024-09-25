/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Documentos de transporte anterior em papel
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class IdDocAntPap implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo do Documento de Transporte Anterior
     */
    private com.mercurio.lms.cte.model.v300.types.TDocAssoc _tpDoc;

    /**
     * Série do Documento Fiscal
     */
    private java.lang.String _serie;

    /**
     * Série do Documento Fiscal
     */
    private java.lang.String _subser;

    /**
     * Número do Documento Fiscal
     */
    private java.lang.String _nDoc;

    /**
     * Data de emissão (AAAA-MM-DD)
     */
    private java.lang.String _dEmi;


      //----------------/
     //- Constructors -/
    //----------------/

    public IdDocAntPap() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão (AAAA-MM-DD)
     * 
     * @return the value of field 'DEmi'.
     */
    public java.lang.String getDEmi(
    ) {
        return this._dEmi;
    }

    /**
     * Returns the value of field 'nDoc'. The field 'nDoc' has the
     * following description: Número do Documento Fiscal
     * 
     * @return the value of field 'NDoc'.
     */
    public java.lang.String getNDoc(
    ) {
        return this._nDoc;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série do Documento Fiscal
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'subser'. The field 'subser' has
     * the following description: Série do Documento Fiscal
     * 
     * @return the value of field 'Subser'.
     */
    public java.lang.String getSubser(
    ) {
        return this._subser;
    }

    /**
     * Returns the value of field 'tpDoc'. The field 'tpDoc' has
     * the following description: Tipo do Documento de Transporte
     * Anterior
     * 
     * @return the value of field 'TpDoc'.
     */
    public com.mercurio.lms.cte.model.v300.types.TDocAssoc getTpDoc(
    ) {
        return this._tpDoc;
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
     * Sets the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão (AAAA-MM-DD)
     * 
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final java.lang.String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * Sets the value of field 'nDoc'. The field 'nDoc' has the
     * following description: Número do Documento Fiscal
     * 
     * @param nDoc the value of field 'nDoc'.
     */
    public void setNDoc(
            final java.lang.String nDoc) {
        this._nDoc = nDoc;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série do Documento Fiscal
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'subser'. The field 'subser' has the
     * following description: Série do Documento Fiscal
     * 
     * @param subser the value of field 'subser'.
     */
    public void setSubser(
            final java.lang.String subser) {
        this._subser = subser;
    }

    /**
     * Sets the value of field 'tpDoc'. The field 'tpDoc' has the
     * following description: Tipo do Documento de Transporte
     * Anterior
     * 
     * @param tpDoc the value of field 'tpDoc'.
     */
    public void setTpDoc(
            final com.mercurio.lms.cte.model.v300.types.TDocAssoc tpDoc) {
        this._tpDoc = tpDoc;
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
     * com.mercurio.lms.cte.model.v300.IdDocAntPap
     */
    public static com.mercurio.lms.cte.model.v300.IdDocAntPap unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.IdDocAntPap) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.IdDocAntPap.class, reader);
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
