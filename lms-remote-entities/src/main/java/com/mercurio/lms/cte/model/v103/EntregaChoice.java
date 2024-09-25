/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Class EntregaChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EntregaChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Entrega sem data definida
     */
    private com.mercurio.lms.cte.model.v103.SemData _semData;

    /**
     * Entrega com data definida
     */
    private com.mercurio.lms.cte.model.v103.ComData _comData;

    /**
     * Entrega no período definido
     */
    private com.mercurio.lms.cte.model.v103.NoPeriodo _noPeriodo;


      //----------------/
     //- Constructors -/
    //----------------/

    public EntregaChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'comData'. The field 'comData'
     * has the following description: Entrega com data definida
     * 
     * @return the value of field 'ComData'.
     */
    public com.mercurio.lms.cte.model.v103.ComData getComData(
    ) {
        return this._comData;
    }

    /**
     * Returns the value of field 'noPeriodo'. The field
     * 'noPeriodo' has the following description: Entrega no
     * período definido
     * 
     * @return the value of field 'NoPeriodo'.
     */
    public com.mercurio.lms.cte.model.v103.NoPeriodo getNoPeriodo(
    ) {
        return this._noPeriodo;
    }

    /**
     * Returns the value of field 'semData'. The field 'semData'
     * has the following description: Entrega sem data definida
     * 
     * @return the value of field 'SemData'.
     */
    public com.mercurio.lms.cte.model.v103.SemData getSemData(
    ) {
        return this._semData;
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
     * Sets the value of field 'comData'. The field 'comData' has
     * the following description: Entrega com data definida
     * 
     * @param comData the value of field 'comData'.
     */
    public void setComData(
            final com.mercurio.lms.cte.model.v103.ComData comData) {
        this._comData = comData;
    }

    /**
     * Sets the value of field 'noPeriodo'. The field 'noPeriodo'
     * has the following description: Entrega no período definido
     * 
     * @param noPeriodo the value of field 'noPeriodo'.
     */
    public void setNoPeriodo(
            final com.mercurio.lms.cte.model.v103.NoPeriodo noPeriodo) {
        this._noPeriodo = noPeriodo;
    }

    /**
     * Sets the value of field 'semData'. The field 'semData' has
     * the following description: Entrega sem data definida
     * 
     * @param semData the value of field 'semData'.
     */
    public void setSemData(
            final com.mercurio.lms.cte.model.v103.SemData semData) {
        this._semData = semData;
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
     * com.mercurio.lms.cte.model.v103.EntregaChoice
     */
    public static com.mercurio.lms.cte.model.v103.EntregaChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.EntregaChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.EntregaChoice.class, reader);
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
