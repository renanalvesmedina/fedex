/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Emissor do documento anterior
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EmiDocAnt implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _emiDocAntChoice.
     */
    private com.mercurio.lms.cte.model.v103.EmiDocAntChoice _emiDocAntChoice;

    /**
     * Field _emiDocAntSequence.
     */
    private com.mercurio.lms.cte.model.v103.EmiDocAntSequence _emiDocAntSequence;

    /**
     * Razão Social ou Nome do expedidor
     */
    private java.lang.String _xNome;

    /**
     * Informações de identificação dos documentos de
     * Transporte Anterior
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.IdDocAnt> _idDocAntList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EmiDocAnt() {
        super();
        this._idDocAntList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.IdDocAnt>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vIdDocAnt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAnt(
            final com.mercurio.lms.cte.model.v103.IdDocAnt vIdDocAnt)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._idDocAntList.size() >= 2) {
            throw new IndexOutOfBoundsException("addIdDocAnt has a maximum of 2");
        }

        this._idDocAntList.add(vIdDocAnt);
    }

    /**
     * 
     * 
     * @param index
     * @param vIdDocAnt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAnt(
            final int index,
            final com.mercurio.lms.cte.model.v103.IdDocAnt vIdDocAnt)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._idDocAntList.size() >= 2) {
            throw new IndexOutOfBoundsException("addIdDocAnt has a maximum of 2");
        }

        this._idDocAntList.add(index, vIdDocAnt);
    }

    /**
     * Method enumerateIdDocAnt.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.IdDocAnt> enumerateIdDocAnt(
    ) {
        return java.util.Collections.enumeration(this._idDocAntList);
    }

    /**
     * Returns the value of field 'emiDocAntChoice'.
     * 
     * @return the value of field 'EmiDocAntChoice'.
     */
    public com.mercurio.lms.cte.model.v103.EmiDocAntChoice getEmiDocAntChoice(
    ) {
        return this._emiDocAntChoice;
    }

    /**
     * Returns the value of field 'emiDocAntSequence'.
     * 
     * @return the value of field 'EmiDocAntSequence'.
     */
    public com.mercurio.lms.cte.model.v103.EmiDocAntSequence getEmiDocAntSequence(
    ) {
        return this._emiDocAntSequence;
    }

    /**
     * Method getIdDocAnt.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.IdDocAnt at the given index
     */
    public com.mercurio.lms.cte.model.v103.IdDocAnt getIdDocAnt(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntList.size()) {
            throw new IndexOutOfBoundsException("getIdDocAnt: Index value '" + index + "' not in range [0.." + (this._idDocAntList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.IdDocAnt) _idDocAntList.get(index);
    }

    /**
     * Method getIdDocAnt.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.IdDocAnt[] getIdDocAnt(
    ) {
        com.mercurio.lms.cte.model.v103.IdDocAnt[] array = new com.mercurio.lms.cte.model.v103.IdDocAnt[0];
        return (com.mercurio.lms.cte.model.v103.IdDocAnt[]) this._idDocAntList.toArray(array);
    }

    /**
     * Method getIdDocAntCount.
     * 
     * @return the size of this collection
     */
    public int getIdDocAntCount(
    ) {
        return this._idDocAntList.size();
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome do
     * expedidor
     * 
     * @return the value of field 'XNome'.
     */
    public java.lang.String getXNome(
    ) {
        return this._xNome;
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
     * Method iterateIdDocAnt.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.IdDocAnt> iterateIdDocAnt(
    ) {
        return this._idDocAntList.iterator();
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
    public void removeAllIdDocAnt(
    ) {
        this._idDocAntList.clear();
    }

    /**
     * Method removeIdDocAnt.
     * 
     * @param vIdDocAnt
     * @return true if the object was removed from the collection.
     */
    public boolean removeIdDocAnt(
            final com.mercurio.lms.cte.model.v103.IdDocAnt vIdDocAnt) {
        boolean removed = _idDocAntList.remove(vIdDocAnt);
        return removed;
    }

    /**
     * Method removeIdDocAntAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.IdDocAnt removeIdDocAntAt(
            final int index) {
        java.lang.Object obj = this._idDocAntList.remove(index);
        return (com.mercurio.lms.cte.model.v103.IdDocAnt) obj;
    }

    /**
     * Sets the value of field 'emiDocAntChoice'.
     * 
     * @param emiDocAntChoice the value of field 'emiDocAntChoice'.
     */
    public void setEmiDocAntChoice(
            final com.mercurio.lms.cte.model.v103.EmiDocAntChoice emiDocAntChoice) {
        this._emiDocAntChoice = emiDocAntChoice;
    }

    /**
     * Sets the value of field 'emiDocAntSequence'.
     * 
     * @param emiDocAntSequence the value of field
     * 'emiDocAntSequence'.
     */
    public void setEmiDocAntSequence(
            final com.mercurio.lms.cte.model.v103.EmiDocAntSequence emiDocAntSequence) {
        this._emiDocAntSequence = emiDocAntSequence;
    }

    /**
     * 
     * 
     * @param index
     * @param vIdDocAnt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setIdDocAnt(
            final int index,
            final com.mercurio.lms.cte.model.v103.IdDocAnt vIdDocAnt)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntList.size()) {
            throw new IndexOutOfBoundsException("setIdDocAnt: Index value '" + index + "' not in range [0.." + (this._idDocAntList.size() - 1) + "]");
        }

        this._idDocAntList.set(index, vIdDocAnt);
    }

    /**
     * 
     * 
     * @param vIdDocAntArray
     */
    public void setIdDocAnt(
            final com.mercurio.lms.cte.model.v103.IdDocAnt[] vIdDocAntArray) {
        //-- copy array
        _idDocAntList.clear();

        for (int i = 0; i < vIdDocAntArray.length; i++) {
                this._idDocAntList.add(vIdDocAntArray[i]);
        }
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome do expedidor
     * 
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(
            final java.lang.String xNome) {
        this._xNome = xNome;
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
     * com.mercurio.lms.cte.model.v103.EmiDocAnt
     */
    public static com.mercurio.lms.cte.model.v103.EmiDocAnt unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.EmiDocAnt) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.EmiDocAnt.class, reader);
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
