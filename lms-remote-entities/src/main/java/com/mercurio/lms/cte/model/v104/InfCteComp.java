/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Detalhamento do CT-e complementado
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCteComp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Chave do CT-e complementado
     */
    private java.lang.String _chave;

    /**
     * Valores da prestação de serviço
     */
    private com.mercurio.lms.cte.model.v104.VPresComp _vPresComp;

    /**
     * Iinformações relativas aos Impostos complementados
     */
    private com.mercurio.lms.cte.model.v104.ImpComp _impComp;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCteComp() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'chave'. The field 'chave' has
     * the following description: Chave do CT-e complementado
     * 
     * @return the value of field 'Chave'.
     */
    public java.lang.String getChave(
    ) {
        return this._chave;
    }

    /**
     * Returns the value of field 'impComp'. The field 'impComp'
     * has the following description: Iinformações relativas aos
     * Impostos complementados
     * 
     * @return the value of field 'ImpComp'.
     */
    public com.mercurio.lms.cte.model.v104.ImpComp getImpComp(
    ) {
        return this._impComp;
    }

    /**
     * Returns the value of field 'vPresComp'. The field
     * 'vPresComp' has the following description: Valores da
     * prestação de serviço
     * 
     * @return the value of field 'VPresComp'.
     */
    public com.mercurio.lms.cte.model.v104.VPresComp getVPresComp(
    ) {
        return this._vPresComp;
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
     * Sets the value of field 'chave'. The field 'chave' has the
     * following description: Chave do CT-e complementado
     * 
     * @param chave the value of field 'chave'.
     */
    public void setChave(
            final java.lang.String chave) {
        this._chave = chave;
    }

    /**
     * Sets the value of field 'impComp'. The field 'impComp' has
     * the following description: Iinformações relativas aos
     * Impostos complementados
     * 
     * @param impComp the value of field 'impComp'.
     */
    public void setImpComp(
            final com.mercurio.lms.cte.model.v104.ImpComp impComp) {
        this._impComp = impComp;
    }

    /**
     * Sets the value of field 'vPresComp'. The field 'vPresComp'
     * has the following description: Valores da prestação de
     * serviço
     * 
     * @param vPresComp the value of field 'vPresComp'.
     */
    public void setVPresComp(
            final com.mercurio.lms.cte.model.v104.VPresComp vPresComp) {
        this._vPresComp = vPresComp;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.InfCteComp
     */
    public static com.mercurio.lms.cte.model.v104.InfCteComp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.InfCteComp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.InfCteComp.class, reader);
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
