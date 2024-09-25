/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

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
    private com.mercurio.lms.cte.model.v103.InfCTeNorm _infCTeNorm;

    /**
     * Detalhamento do CT-e complementado
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.InfCteComp> _infCteCompList;

    /**
     * Detalhamento do CT-e do tipo Anulação de Valores
     */
    private com.mercurio.lms.cte.model.v103.InfCteAnu _infCteAnu;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCteChoice() {
        super();
        this._infCteCompList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.InfCteComp>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInfCteComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCteComp(
            final com.mercurio.lms.cte.model.v103.InfCteComp vInfCteComp)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infCteCompList.size() >= 10) {
            throw new IndexOutOfBoundsException("addInfCteComp has a maximum of 10");
        }

        this._infCteCompList.add(vInfCteComp);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCteComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCteComp(
            final int index,
            final com.mercurio.lms.cte.model.v103.InfCteComp vInfCteComp)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infCteCompList.size() >= 10) {
            throw new IndexOutOfBoundsException("addInfCteComp has a maximum of 10");
        }

        this._infCteCompList.add(index, vInfCteComp);
    }

    /**
     * Method enumerateInfCteComp.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.InfCteComp> enumerateInfCteComp(
    ) {
        return java.util.Collections.enumeration(this._infCteCompList);
    }

    /**
     * Returns the value of field 'infCTeNorm'. The field
     * 'infCTeNorm' has the following description: Grupo de
     * informações do CT-e Normal e Substituto
     * 
     * @return the value of field 'InfCTeNorm'.
     */
    public com.mercurio.lms.cte.model.v103.InfCTeNorm getInfCTeNorm(
    ) {
        return this._infCTeNorm;
    }

    /**
     * Returns the value of field 'infCteAnu'. The field
     * 'infCteAnu' has the following description: Detalhamento do
     * CT-e do tipo Anulação de Valores
     * 
     * @return the value of field 'InfCteAnu'.
     */
    public com.mercurio.lms.cte.model.v103.InfCteAnu getInfCteAnu(
    ) {
        return this._infCteAnu;
    }

    /**
     * Method getInfCteComp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.InfCteComp at the given index
     */
    public com.mercurio.lms.cte.model.v103.InfCteComp getInfCteComp(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infCteCompList.size()) {
            throw new IndexOutOfBoundsException("getInfCteComp: Index value '" + index + "' not in range [0.." + (this._infCteCompList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.InfCteComp) _infCteCompList.get(index);
    }

    /**
     * Method getInfCteComp.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.InfCteComp[] getInfCteComp(
    ) {
        com.mercurio.lms.cte.model.v103.InfCteComp[] array = new com.mercurio.lms.cte.model.v103.InfCteComp[0];
        return (com.mercurio.lms.cte.model.v103.InfCteComp[]) this._infCteCompList.toArray(array);
    }

    /**
     * Method getInfCteCompCount.
     * 
     * @return the size of this collection
     */
    public int getInfCteCompCount(
    ) {
        return this._infCteCompList.size();
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
     * Method iterateInfCteComp.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.InfCteComp> iterateInfCteComp(
    ) {
        return this._infCteCompList.iterator();
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
     */
    public void removeAllInfCteComp(
    ) {
        this._infCteCompList.clear();
    }

    /**
     * Method removeInfCteComp.
     * 
     * @param vInfCteComp
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfCteComp(
            final com.mercurio.lms.cte.model.v103.InfCteComp vInfCteComp) {
        boolean removed = _infCteCompList.remove(vInfCteComp);
        return removed;
    }

    /**
     * Method removeInfCteCompAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.InfCteComp removeInfCteCompAt(
            final int index) {
        java.lang.Object obj = this._infCteCompList.remove(index);
        return (com.mercurio.lms.cte.model.v103.InfCteComp) obj;
    }

    /**
     * Sets the value of field 'infCTeNorm'. The field 'infCTeNorm'
     * has the following description: Grupo de informações do
     * CT-e Normal e Substituto
     * 
     * @param infCTeNorm the value of field 'infCTeNorm'.
     */
    public void setInfCTeNorm(
            final com.mercurio.lms.cte.model.v103.InfCTeNorm infCTeNorm) {
        this._infCTeNorm = infCTeNorm;
    }

    /**
     * Sets the value of field 'infCteAnu'. The field 'infCteAnu'
     * has the following description: Detalhamento do CT-e do tipo
     * Anulação de Valores
     * 
     * @param infCteAnu the value of field 'infCteAnu'.
     */
    public void setInfCteAnu(
            final com.mercurio.lms.cte.model.v103.InfCteAnu infCteAnu) {
        this._infCteAnu = infCteAnu;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCteComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfCteComp(
            final int index,
            final com.mercurio.lms.cte.model.v103.InfCteComp vInfCteComp)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infCteCompList.size()) {
            throw new IndexOutOfBoundsException("setInfCteComp: Index value '" + index + "' not in range [0.." + (this._infCteCompList.size() - 1) + "]");
        }

        this._infCteCompList.set(index, vInfCteComp);
    }

    /**
     * 
     * 
     * @param vInfCteCompArray
     */
    public void setInfCteComp(
            final com.mercurio.lms.cte.model.v103.InfCteComp[] vInfCteCompArray) {
        //-- copy array
        _infCteCompList.clear();

        for (int i = 0; i < vInfCteCompArray.length; i++) {
                this._infCteCompList.add(vInfCteCompArray[i]);
        }
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
     * com.mercurio.lms.cte.model.v103.InfCteChoice
     */
    public static com.mercurio.lms.cte.model.v103.InfCteChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.InfCteChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.InfCteChoice.class, reader);
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
