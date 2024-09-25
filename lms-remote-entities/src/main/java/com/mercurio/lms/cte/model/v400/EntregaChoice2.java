/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Class EntregaChoice2.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EntregaChoice2 implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Entrega sem hora definida
     */
    private SemHora _semHora;

    /**
     * Entrega com hora definida
     */
    private ComHora _comHora;

    /**
     * Entrega no intervalo de horário definido
     */
    private NoInter _noInter;


      //----------------/
     //- Constructors -/
    //----------------/

    public EntregaChoice2() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'comHora'. The field 'comHora'
     * has the following description: Entrega com hora definida
     * 
     * @return the value of field 'ComHora'.
     */
    public ComHora getComHora(
    ) {
        return this._comHora;
    }

    /**
     * Returns the value of field 'noInter'. The field 'noInter'
     * has the following description: Entrega no intervalo de
     * horário definido
     * 
     * @return the value of field 'NoInter'.
     */
    public NoInter getNoInter(
    ) {
        return this._noInter;
    }

    /**
     * Returns the value of field 'semHora'. The field 'semHora'
     * has the following description: Entrega sem hora definida
     * 
     * @return the value of field 'SemHora'.
     */
    public SemHora getSemHora(
    ) {
        return this._semHora;
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
     * Sets the value of field 'comHora'. The field 'comHora' has
     * the following description: Entrega com hora definida
     * 
     * @param comHora the value of field 'comHora'.
     */
    public void setComHora(
            final ComHora comHora) {
        this._comHora = comHora;
    }

    /**
     * Sets the value of field 'noInter'. The field 'noInter' has
     * the following description: Entrega no intervalo de horário
     * definido
     * 
     * @param noInter the value of field 'noInter'.
     */
    public void setNoInter(
            final NoInter noInter) {
        this._noInter = noInter;
    }

    /**
     * Sets the value of field 'semHora'. The field 'semHora' has
     * the following description: Entrega sem hora definida
     * 
     * @param semHora the value of field 'semHora'.
     */
    public void setSemHora(
            final SemHora semHora) {
        this._semHora = semHora;
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
     * com.mercurio.lms.cte.model.v400.EntregaChoice2
     */
    public static EntregaChoice2 unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (EntregaChoice2) org.exolab.castor.xml.Unmarshaller.unmarshal(EntregaChoice2.class, reader);
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
