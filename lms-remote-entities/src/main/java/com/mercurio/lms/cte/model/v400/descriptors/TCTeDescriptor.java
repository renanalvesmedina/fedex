/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400.descriptors;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import com.mercurio.lms.cte.model.v400.TCTe;

/**
 * Class TCTeDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class TCTeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private String _nsURI;

    /**
     * Field _xmlName.
     */
    private String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public TCTeDescriptor() {
        super();
        _nsURI = "http://www.portalfiscal.inf.br/cte";
        _xmlName = "TCTe";
        _elementDefinition = false;

        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors

        //-- initialize element descriptors

        //-- _infCte
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.mercurio.lms.cte.model.v400.InfCte.class, "_infCte", "infCte", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public Object getValue( Object object )
                throws IllegalStateException
            {
                TCTe target = (TCTe) object;
                return target.getInfCte();
            }
            @Override
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TCTe target = (TCTe) object;
                    target.setInfCte( (com.mercurio.lms.cte.model.v400.InfCte) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public Object newInstance(Object parent) {
                return new com.mercurio.lms.cte.model.v400.InfCte();
            }
        };
        desc.setSchemaType("com.mercurio.lms.cte.model.v400.InfCte");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _infCte
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);

      //-- _infCTeSupl
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.mercurio.lms.cte.model.v400.InfCTeSupl.class, "_infCTeSupl", "infCTeSupl", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public Object getValue( Object object )
                throws IllegalStateException
            {
                TCTe target = (TCTe) object;
                return target.getInfCTeSupl();
            }
            @Override
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TCTe target = (TCTe) object;
                    target.setInfCTeSupl((com.mercurio.lms.cte.model.v400.InfCTeSupl) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public Object newInstance(Object parent) {
                return new com.mercurio.lms.cte.model.v400.InfCTeSupl();
            }
        };
        desc.setSchemaType("com.mercurio.lms.cte.model.v400.InfCTeSupl");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setRequired(false);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _infCTeSupl
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);

        //-- _signature
        /*desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(Signature.class, "_signature", "Signature", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object )
                throws IllegalStateException
            {
                TCTe target = (TCTe) object;
                return target.getSignature();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TCTe target = (TCTe) object;
                    target.setSignature( (Signature) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new Signature();
            }
        };
        desc.setSchemaType("Signature");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.w3.org/2000/09/xmldsig#");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _signature
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);*/
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     *
     * @return the access mode specified for this class.
     */
    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode(
    ) {
        return null;
    }

    /**
     * Method getIdentity.
     *
     * @return the identity field, null if this class has no
     * identity.
     */
    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity(
    ) {
        return _identity;
    }

    /**
     * Method getJavaClass.
     *
     * @return the Java class represented by this descriptor.
     */
    @Override()
    public Class getJavaClass(
    ) {
        return TCTe.class;
    }

    /**
     * Method getNameSpacePrefix.
     *
     * @return the namespace prefix to use when marshaling as XML.
     */
    @Override()
    public String getNameSpacePrefix(
    ) {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     *
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    @Override()
    public String getNameSpaceURI(
    ) {
        return _nsURI;
    }

    /**
     * Method getValidator.
     *
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    @Override()
    public org.exolab.castor.xml.TypeValidator getValidator(
    ) {
        return this;
    }

    /**
     * Method getXMLName.
     *
     * @return the XML Name for the Class being described.
     */
    @Override()
    public String getXMLName(
    ) {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition(
    ) {
        return _elementDefinition;
    }

}
