/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Tomador é contribuinte do ICMS
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TomaICMS implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Chave de acesso da NF-e emitida pelo Tomador
     */
    private java.lang.String _refNFe;

    /**
     * Informação da NF ou CT emitido pelo Tomador
     */
    private com.mercurio.lms.cte.model.v104.RefNF _refNF;

    /**
     * Chave de acesso do CT-e emitido pelo Tomador
     */
    private java.lang.String _refCte;


      //----------------/
     //- Constructors -/
    //----------------/

    public TomaICMS() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'refCte'. The field 'refCte' has
     * the following description: Chave de acesso do CT-e emitido
     * pelo Tomador
     * 
     * @return the value of field 'RefCte'.
     */
    public java.lang.String getRefCte(
    ) {
        return this._refCte;
    }

    /**
     * Returns the value of field 'refNF'. The field 'refNF' has
     * the following description: Informação da NF ou CT emitido
     * pelo Tomador
     * 
     * @return the value of field 'RefNF'.
     */
    public com.mercurio.lms.cte.model.v104.RefNF getRefNF(
    ) {
        return this._refNF;
    }

    /**
     * Returns the value of field 'refNFe'. The field 'refNFe' has
     * the following description: Chave de acesso da NF-e emitida
     * pelo Tomador
     * 
     * @return the value of field 'RefNFe'.
     */
    public java.lang.String getRefNFe(
    ) {
        return this._refNFe;
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
     * Sets the value of field 'refCte'. The field 'refCte' has the
     * following description: Chave de acesso do CT-e emitido pelo
     * Tomador
     * 
     * @param refCte the value of field 'refCte'.
     */
    public void setRefCte(
            final java.lang.String refCte) {
        this._refCte = refCte;
        this._choiceValue = refCte;
    }

    /**
     * Sets the value of field 'refNF'. The field 'refNF' has the
     * following description: Informação da NF ou CT emitido pelo
     * Tomador
     * 
     * @param refNF the value of field 'refNF'.
     */
    public void setRefNF(
            final com.mercurio.lms.cte.model.v104.RefNF refNF) {
        this._refNF = refNF;
        this._choiceValue = refNF;
    }

    /**
     * Sets the value of field 'refNFe'. The field 'refNFe' has the
     * following description: Chave de acesso da NF-e emitida pelo
     * Tomador
     * 
     * @param refNFe the value of field 'refNFe'.
     */
    public void setRefNFe(
            final java.lang.String refNFe) {
        this._refNFe = refNFe;
        this._choiceValue = refNFe;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.TomaICMS
     */
    public static com.mercurio.lms.cte.model.v104.TomaICMS unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.TomaICMS) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.TomaICMS.class, reader);
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
