/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Class InfCTeNormChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCTeNormChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações do modal Rodoviário
     */
    private com.mercurio.lms.cte.model.v103.Rodo _rodo;

    /**
     * Informações do modal Aéreo
     */
    private com.mercurio.lms.cte.model.v103.Aereo _aereo;

    /**
     * Informações do modal Aquaviário
     */
    private com.mercurio.lms.cte.model.v103.Aquav _aquav;

    /**
     * Informações do modal Ferroviário
     */
    private com.mercurio.lms.cte.model.v103.Ferrov _ferrov;

    /**
     * Informações do modal Dutoviário
     */
    private com.mercurio.lms.cte.model.v103.Duto _duto;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCTeNormChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'aereo'. The field 'aereo' has
     * the following description: Informações do modal Aéreo
     * 
     * @return the value of field 'Aereo'.
     */
    public com.mercurio.lms.cte.model.v103.Aereo getAereo(
    ) {
        return this._aereo;
    }

    /**
     * Returns the value of field 'aquav'. The field 'aquav' has
     * the following description: Informações do modal
     * Aquaviário
     * 
     * @return the value of field 'Aquav'.
     */
    public com.mercurio.lms.cte.model.v103.Aquav getAquav(
    ) {
        return this._aquav;
    }

    /**
     * Returns the value of field 'duto'. The field 'duto' has the
     * following description: Informações do modal Dutoviário
     * 
     * @return the value of field 'Duto'.
     */
    public com.mercurio.lms.cte.model.v103.Duto getDuto(
    ) {
        return this._duto;
    }

    /**
     * Returns the value of field 'ferrov'. The field 'ferrov' has
     * the following description: Informações do modal
     * Ferroviário
     * 
     * @return the value of field 'Ferrov'.
     */
    public com.mercurio.lms.cte.model.v103.Ferrov getFerrov(
    ) {
        return this._ferrov;
    }

    /**
     * Returns the value of field 'rodo'. The field 'rodo' has the
     * following description: Informações do modal Rodoviário
     * 
     * @return the value of field 'Rodo'.
     */
    public com.mercurio.lms.cte.model.v103.Rodo getRodo(
    ) {
        return this._rodo;
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
     * Sets the value of field 'aereo'. The field 'aereo' has the
     * following description: Informações do modal Aéreo
     * 
     * @param aereo the value of field 'aereo'.
     */
    public void setAereo(
            final com.mercurio.lms.cte.model.v103.Aereo aereo) {
        this._aereo = aereo;
    }

    /**
     * Sets the value of field 'aquav'. The field 'aquav' has the
     * following description: Informações do modal Aquaviário
     * 
     * @param aquav the value of field 'aquav'.
     */
    public void setAquav(
            final com.mercurio.lms.cte.model.v103.Aquav aquav) {
        this._aquav = aquav;
    }

    /**
     * Sets the value of field 'duto'. The field 'duto' has the
     * following description: Informações do modal Dutoviário
     * 
     * @param duto the value of field 'duto'.
     */
    public void setDuto(
            final com.mercurio.lms.cte.model.v103.Duto duto) {
        this._duto = duto;
    }

    /**
     * Sets the value of field 'ferrov'. The field 'ferrov' has the
     * following description: Informações do modal Ferroviário
     * 
     * @param ferrov the value of field 'ferrov'.
     */
    public void setFerrov(
            final com.mercurio.lms.cte.model.v103.Ferrov ferrov) {
        this._ferrov = ferrov;
    }

    /**
     * Sets the value of field 'rodo'. The field 'rodo' has the
     * following description: Informações do modal Rodoviário
     * 
     * @param rodo the value of field 'rodo'.
     */
    public void setRodo(
            final com.mercurio.lms.cte.model.v103.Rodo rodo) {
        this._rodo = rodo;
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
     * com.mercurio.lms.cte.model.v103.InfCTeNormChoice
     */
    public static com.mercurio.lms.cte.model.v103.InfCTeNormChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.InfCTeNormChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.InfCTeNormChoice.class, reader);
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
