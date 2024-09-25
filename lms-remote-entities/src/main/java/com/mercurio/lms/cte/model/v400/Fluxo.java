/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Previsão do fluxo da cargaPreenchimento obrigatório para o
 * modal aéreo.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Fluxo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Sigla ou código interno da Filial/Porto/Estação/
     * Aeroporto de Origem
     */
    private String _xOrig;

    /**
     * Field _passList.
     */
    private java.util.Vector<Pass> _passList;

    /**
     * Sigla ou código interno da Filial/Porto/Estação/Aeroporto
     * de Destino
     */
    private String _xDest;

    /**
     * Código da Rota de Entrega
     */
    private String _xRota;


      //----------------/
     //- Constructors -/
    //----------------/

    public Fluxo() {
        super();
        this._passList = new java.util.Vector<Pass>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vPass
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPass(
            final Pass vPass)
    throws IndexOutOfBoundsException {
        this._passList.addElement(vPass);
    }

    /**
     *
     *
     * @param index
     * @param vPass
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPass(
            final int index,
            final Pass vPass)
    throws IndexOutOfBoundsException {
        this._passList.add(index, vPass);
    }

    /**
     * Method enumeratePass.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.Pass elements
     */
    public java.util.Enumeration<? extends Pass> enumeratePass(
    ) {
        return this._passList.elements();
    }

    /**
     * Method getPass.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.Pass at the given index
     */
    public Pass getPass(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._passList.size()) {
            throw new IndexOutOfBoundsException("getPass: Index value '" + index + "' not in range [0.." + (this._passList.size() - 1) + "]");
        }

        return (Pass) _passList.get(index);
    }

    /**
     * Method getPass.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public Pass[] getPass(
    ) {
        Pass[] array = new Pass[0];
        return (Pass[]) this._passList.toArray(array);
    }

    /**
     * Method getPassCount.
     *
     * @return the size of this collection
     */
    public int getPassCount(
    ) {
        return this._passList.size();
    }

    /**
     * Returns the value of field 'xDest'. The field 'xDest' has
     * the following description: Sigla ou código interno da
     * Filial/Porto/Estação/Aeroporto de Destino
     *
     * @return the value of field 'XDest'.
     */
    public String getXDest(
    ) {
        return this._xDest;
    }

    /**
     * Returns the value of field 'xOrig'. The field 'xOrig' has
     * the following description: Sigla ou código interno da
     * Filial/Porto/Estação/ Aeroporto de Origem
     *
     * @return the value of field 'XOrig'.
     */
    public String getXOrig(
    ) {
        return this._xOrig;
    }

    /**
     * Returns the value of field 'xRota'. The field 'xRota' has
     * the following description: Código da Rota de Entrega
     *
     * @return the value of field 'XRota'.
     */
    public String getXRota(
    ) {
        return this._xRota;
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
     */
    public void removeAllPass(
    ) {
        this._passList.clear();
    }

    /**
     * Method removePass.
     *
     * @param vPass
     * @return true if the object was removed from the collection.
     */
    public boolean removePass(
            final Pass vPass) {
        boolean removed = _passList.remove(vPass);
        return removed;
    }

    /**
     * Method removePassAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public Pass removePassAt(
            final int index) {
        Object obj = this._passList.remove(index);
        return (Pass) obj;
    }

    /**
     *
     *
     * @param index
     * @param vPass
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setPass(
            final int index,
            final Pass vPass)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._passList.size()) {
            throw new IndexOutOfBoundsException("setPass: Index value '" + index + "' not in range [0.." + (this._passList.size() - 1) + "]");
        }

        this._passList.set(index, vPass);
    }

    /**
     *
     *
     * @param vPassArray
     */
    public void setPass(
            final Pass[] vPassArray) {
        //-- copy array
        _passList.clear();

        for (int i = 0; i < vPassArray.length; i++) {
                this._passList.add(vPassArray[i]);
        }
    }

    /**
     * Sets the value of field 'xDest'. The field 'xDest' has the
     * following description: Sigla ou código interno da
     * Filial/Porto/Estação/Aeroporto de Destino
     *
     * @param xDest the value of field 'xDest'.
     */
    public void setXDest(
            final String xDest) {
        this._xDest = xDest;
    }

    /**
     * Sets the value of field 'xOrig'. The field 'xOrig' has the
     * following description: Sigla ou código interno da
     * Filial/Porto/Estação/ Aeroporto de Origem
     *
     * @param xOrig the value of field 'xOrig'.
     */
    public void setXOrig(
            final String xOrig) {
        this._xOrig = xOrig;
    }

    /**
     * Sets the value of field 'xRota'. The field 'xRota' has the
     * following description: Código da Rota de Entrega
     *
     * @param xRota the value of field 'xRota'.
     */
    public void setXRota(
            final String xRota) {
        this._xRota = xRota;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.Fluxo
     */
    public static Fluxo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (Fluxo) org.exolab.castor.xml.Unmarshaller.unmarshal(Fluxo.class, reader);
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
