/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Informações do modal Rodoviário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Rodo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Registro Nacional de Transportadores Rodoviários de Carga
     */
    private java.lang.String _RNTRC;

    /**
     * Código Identificador da Operação de Transporte
     */
    private java.lang.String _CIOT;

    /**
     * Dados do Veículo com a Tração
     */
    private com.mercurio.lms.mdfe.model.v100.VeicPrincipal _veicPrincipal;
    
    /**
     * Dados do Veículo com a Tração
     */
    private com.mercurio.lms.mdfe.model.v100.VeicTracao _veicTracao;

    /**
     * Dados dos reboques
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.VeicReboque> _veicReboqueList;

    /**
     * Informações de Vale Pedágio
     */
    private com.mercurio.lms.mdfe.model.v100.ValePed _valePed;


      //----------------/
     //- Constructors -/
    //----------------/

    public Rodo() {
        super();
        this._veicReboqueList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.VeicReboque>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicReboque(
            final com.mercurio.lms.mdfe.model.v100.VeicReboque vVeicReboque)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._veicReboqueList.size() >= 3) {
            throw new IndexOutOfBoundsException("addVeicReboque has a maximum of 3");
        }

        this._veicReboqueList.add(vVeicReboque);
    }

    /**
     * 
     * 
     * @param index
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicReboque(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.VeicReboque vVeicReboque)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._veicReboqueList.size() >= 3) {
            throw new IndexOutOfBoundsException("addVeicReboque has a maximum of 3");
        }

        this._veicReboqueList.add(index, vVeicReboque);
    }

    /**
     * Method enumerateVeicReboque.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.VeicReboque> enumerateVeicReboque(
    ) {
        return java.util.Collections.enumeration(this._veicReboqueList);
    }

    /**
     * Returns the value of field 'CIOT'. The field 'CIOT' has the
     * following description: Código Identificador da Operação
     * de Transporte
     * 
     * @return the value of field 'CIOT'.
     */
    public java.lang.String getCIOT(
    ) {
        return this._CIOT;
    }

    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional de
     * Transportadores Rodoviários de Carga
     * 
     * @return the value of field 'RNTRC'.
     */
    public java.lang.String getRNTRC(
    ) {
        return this._RNTRC;
    }

    /**
     * Returns the value of field 'valePed'. The field 'valePed'
     * has the following description: Informações de Vale
     * Pedágio
     * 
     * @return the value of field 'ValePed'.
     */
    public com.mercurio.lms.mdfe.model.v100.ValePed getValePed(
    ) {
        return this._valePed;
    }

    /**
     * Returns the value of field 'veicPrincipal'. The field
     * 'veicPrincipal' has the following description: Dados do
     * Veículo com a Tração
     * 
     * @return the value of field 'VeicPrincipal'.
     */
    public com.mercurio.lms.mdfe.model.v100.VeicPrincipal getVeicPrincipal(
    ) {
        return this._veicPrincipal;
    }

    /**
     * Returns the value of field 'veicPrincipal'. The field
     * 'veicPrincipal' has the following description: Dados do
     * Veículo com a Tração
     * 
     * @return the value of field 'VeicPrincipal'.
     */
    public com.mercurio.lms.mdfe.model.v100.VeicTracao getVeicTracao(
    ) {
        return this._veicTracao;
    }
    
    /**
     * Method getVeicReboque.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100.VeicReboque at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100.VeicReboque getVeicReboque(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._veicReboqueList.size()) {
            throw new IndexOutOfBoundsException("getVeicReboque: Index value '" + index + "' not in range [0.." + (this._veicReboqueList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.VeicReboque) _veicReboqueList.get(index);
    }

    /**
     * Method getVeicReboque.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.VeicReboque[] getVeicReboque(
    ) {
        com.mercurio.lms.mdfe.model.v100.VeicReboque[] array = new com.mercurio.lms.mdfe.model.v100.VeicReboque[0];
        return (com.mercurio.lms.mdfe.model.v100.VeicReboque[]) this._veicReboqueList.toArray(array);
    }

    /**
     * Method getVeicReboqueCount.
     * 
     * @return the size of this collection
     */
    public int getVeicReboqueCount(
    ) {
        return this._veicReboqueList.size();
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
     * Method iterateVeicReboque.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.VeicReboque> iterateVeicReboque(
    ) {
        return this._veicReboqueList.iterator();
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
    public void removeAllVeicReboque(
    ) {
        this._veicReboqueList.clear();
    }

    /**
     * Method removeVeicReboque.
     * 
     * @param vVeicReboque
     * @return true if the object was removed from the collection.
     */
    public boolean removeVeicReboque(
            final com.mercurio.lms.mdfe.model.v100.VeicReboque vVeicReboque) {
        boolean removed = _veicReboqueList.remove(vVeicReboque);
        return removed;
    }

    /**
     * Method removeVeicReboqueAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.VeicReboque removeVeicReboqueAt(
            final int index) {
        java.lang.Object obj = this._veicReboqueList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.VeicReboque) obj;
    }

    /**
     * Sets the value of field 'CIOT'. The field 'CIOT' has the
     * following description: Código Identificador da Operação
     * de Transporte
     * 
     * @param CIOT the value of field 'CIOT'.
     */
    public void setCIOT(
            final java.lang.String CIOT) {
        this._CIOT = CIOT;
    }

    /**
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional de Transportadores
     * Rodoviários de Carga
     * 
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(
            final java.lang.String RNTRC) {
        this._RNTRC = RNTRC;
    }

    /**
     * Sets the value of field 'valePed'. The field 'valePed' has
     * the following description: Informações de Vale Pedágio
     * 
     * @param valePed the value of field 'valePed'.
     */
    public void setValePed(
            final com.mercurio.lms.mdfe.model.v100.ValePed valePed) {
        this._valePed = valePed;
    }

    /**
     * Sets the value of field 'veicPrincipal'. The field
     * 'veicPrincipal' has the following description: Dados do
     * Veículo com a Tração
     * 
     * @param veicPrincipal the value of field 'veicPrincipal'.
     */
    public void setVeicPrincipal(
            final com.mercurio.lms.mdfe.model.v100.VeicPrincipal veicPrincipal) {
        this._veicPrincipal = veicPrincipal;
    }
    
    /**
     * Sets the value of field 'veicTracao'. The field
     * 'veicTracao' has the following description: Dados do
     * Veículo com a Tração
     * 
     * @param veicTracao the value of field 'veicTracao'.
     */
    public void setVeicTracao(
            final com.mercurio.lms.mdfe.model.v100.VeicTracao veicTracao) {
        this._veicTracao = veicTracao;
    }

    /**
     * 
     * 
     * @param index
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setVeicReboque(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.VeicReboque vVeicReboque)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._veicReboqueList.size()) {
            throw new IndexOutOfBoundsException("setVeicReboque: Index value '" + index + "' not in range [0.." + (this._veicReboqueList.size() - 1) + "]");
        }

        this._veicReboqueList.set(index, vVeicReboque);
    }

    /**
     * 
     * 
     * @param vVeicReboqueArray
     */
    public void setVeicReboque(
            final com.mercurio.lms.mdfe.model.v100.VeicReboque[] vVeicReboqueArray) {
        //-- copy array
        _veicReboqueList.clear();

        for (int i = 0; i < vVeicReboqueArray.length; i++) {
                this._veicReboqueList.add(vVeicReboqueArray[i]);
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
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v100.Rod
     */
    public static com.mercurio.lms.mdfe.model.v100.Rodo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.Rodo) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.Rodo.class, reader);
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
