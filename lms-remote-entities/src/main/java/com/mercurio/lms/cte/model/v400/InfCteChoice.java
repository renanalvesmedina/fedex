/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Class InfCteChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCteChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Grupo de informações do CT-e Normal e Substituto
     */
    private InfCTeNorm _infCTeNorm;

    /**
     * Detalhamento do CT-e complementado
     */
    private InfCteComp _infCteComp;

    /**
     * Detalhamento do CT-e do tipo Anulação
     */
    private InfCteAnu _infCteAnu;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCteChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'infCTeNorm'. The field
     * 'infCTeNorm' has the following description: Grupo de
     * informações do CT-e Normal e Substituto
     * 
     * @return the value of field 'InfCTeNorm'.
     */
    public InfCTeNorm getInfCTeNorm(
    ) {
        return this._infCTeNorm;
    }

    /**
     * Returns the value of field 'infCteAnu'. The field
     * 'infCteAnu' has the following description: Detalhamento do
     * CT-e do tipo Anulação
     * 
     * @return the value of field 'InfCteAnu'.
     */
    public InfCteAnu getInfCteAnu(
    ) {
        return this._infCteAnu;
    }

    /**
     * Returns the value of field 'infCteComp'. The field
     * 'infCteComp' has the following description: Detalhamento do
     * CT-e complementado
     * 
     * @return the value of field 'InfCteComp'.
     */
    public InfCteComp getInfCteComp(
    ) {
        return this._infCteComp;
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
     * Sets the value of field 'infCTeNorm'. The field 'infCTeNorm'
     * has the following description: Grupo de informações do
     * CT-e Normal e Substituto
     * 
     * @param infCTeNorm the value of field 'infCTeNorm'.
     */
    public void setInfCTeNorm(
            final InfCTeNorm infCTeNorm) {
        this._infCTeNorm = infCTeNorm;
    }

    /**
     * Sets the value of field 'infCteAnu'. The field 'infCteAnu'
     * has the following description: Detalhamento do CT-e do tipo
     * Anulação
     * 
     * @param infCteAnu the value of field 'infCteAnu'.
     */
    public void setInfCteAnu(
            final InfCteAnu infCteAnu) {
        this._infCteAnu = infCteAnu;
    }

    /**
     * Sets the value of field 'infCteComp'. The field 'infCteComp'
     * has the following description: Detalhamento do CT-e
     * complementado
     * 
     * @param infCteComp the value of field 'infCteComp'.
     */
    public void setInfCteComp(
            final InfCteComp infCteComp) {
        this._infCteComp = infCteComp;
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
     * com.mercurio.lms.cte.model.v400.InfCteChoice
     */
    public static InfCteChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfCteChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(InfCteChoice.class, reader);
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
