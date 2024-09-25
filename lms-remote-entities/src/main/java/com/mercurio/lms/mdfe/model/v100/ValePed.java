/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Informações de Vale PedágioOutras informações sobre
 * Vale-Pedágio obrigatório que não tenham campos específicos
 * devem ser informadas no campo de observações gerais de uso
 * livre pelo contribuinte, visando atender as determinações
 * legais vigentes.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ValePed implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações dos dispositivos do Vale Pedágio
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.Disp> _dispList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ValePed() {
        super();
        this._dispList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.Disp>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDisp(
            final com.mercurio.lms.mdfe.model.v100.Disp vDisp)
    throws java.lang.IndexOutOfBoundsException {
        this._dispList.add(vDisp);
    }

    /**
     * 
     * 
     * @param index
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDisp(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.Disp vDisp)
    throws java.lang.IndexOutOfBoundsException {
        this._dispList.add(index, vDisp);
    }

    /**
     * Method enumerateDisp.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.Disp> enumerateDisp(
    ) {
        return java.util.Collections.enumeration(this._dispList);
    }

    /**
     * Method getDisp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100.Disp at the given index
     */
    public com.mercurio.lms.mdfe.model.v100.Disp getDisp(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dispList.size()) {
            throw new IndexOutOfBoundsException("getDisp: Index value '" + index + "' not in range [0.." + (this._dispList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.Disp) _dispList.get(index);
    }

    /**
     * Method getDisp.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.Disp[] getDisp(
    ) {
        com.mercurio.lms.mdfe.model.v100.Disp[] array = new com.mercurio.lms.mdfe.model.v100.Disp[0];
        return (com.mercurio.lms.mdfe.model.v100.Disp[]) this._dispList.toArray(array);
    }

    /**
     * Method getDispCount.
     * 
     * @return the size of this collection
     */
    public int getDispCount(
    ) {
        return this._dispList.size();
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
     * Method iterateDisp.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.Disp> iterateDisp(
    ) {
        return this._dispList.iterator();
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
    public void removeAllDisp(
    ) {
        this._dispList.clear();
    }

    /**
     * Method removeDisp.
     * 
     * @param vDisp
     * @return true if the object was removed from the collection.
     */
    public boolean removeDisp(
            final com.mercurio.lms.mdfe.model.v100.Disp vDisp) {
        boolean removed = _dispList.remove(vDisp);
        return removed;
    }

    /**
     * Method removeDispAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.Disp removeDispAt(
            final int index) {
        java.lang.Object obj = this._dispList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.Disp) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDisp(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.Disp vDisp)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dispList.size()) {
            throw new IndexOutOfBoundsException("setDisp: Index value '" + index + "' not in range [0.." + (this._dispList.size() - 1) + "]");
        }

        this._dispList.set(index, vDisp);
    }

    /**
     * 
     * 
     * @param vDispArray
     */
    public void setDisp(
            final com.mercurio.lms.mdfe.model.v100.Disp[] vDispArray) {
        //-- copy array
        _dispList.clear();

        for (int i = 0; i < vDispArray.length; i++) {
                this._dispList.add(vDispArray[i]);
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
     * com.mercurio.lms.mdfe.model.v100.ValePed
     */
    public static com.mercurio.lms.mdfe.model.v100.ValePed unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.ValePed) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.ValePed.class, reader);
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
