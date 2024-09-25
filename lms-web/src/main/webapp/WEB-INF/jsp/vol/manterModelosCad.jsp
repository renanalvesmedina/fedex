<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.manterModelosAction" >
	<adsm:form action="/vol/manterModelos" idProperty="idModeloeqp" onDataLoadCallBack="myDataLoad">
	
		<adsm:textbox property="dsNome" dataType="text" label="nome" maxLength="30" size="35" width="85%" required="true"/>

        <adsm:hidden   property="tpSituacao" value="A"  />
		<adsm:lookup 
			action="/vol/manterFabricantes" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idFabricante" 
			label="fabricante" 
			maxLength="30" 
			property="volFabricante" 
			service="lms.vol.manterModelosAction.findLookupFabricante" 
			size="20" 
			labelWidth="15%" 
			width="85%"
			required="true"
			onDataLoadCallBack="myLookupDataLoad"
			afterPopupSetValue="populaDados">
			
			<adsm:propertyMapping relatedProperty="nmPessoa" modelProperty="volFabricante.pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="tpSituacao"  formProperty="tpSituacao"/> 		
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="nmPessoa" 
				serializable="false"
				size="58"/>
		</adsm:lookup>
		
		
		<adsm:lookup 
		       action="/vol/manterTiposEquipamento"
		       criteriaProperty="dsNome"
		       dataType="text" 
		       exactMatch="false"
			   minLengthForAutoPopUpSearch="3"
		       idProperty="idTipoEqpto"
		       label="tipo"
		       maxLength="50"
		       property="volTiposEqpto" 
		       service="lms.vol.manterModelosAction.findLookupTiposEquipamento" 
		       size="60" 
		       labelWidth="15%"
		       width="85%" 
		       required="true" />	    
		<adsm:checkbox property="blHomologado" label="homologado" labelWidth="15%" width="85%" />
    	<adsm:textarea property="obHistorico" columns="50" rows="4" width="35%" maxLength="255" label="historico"/>
		
		<adsm:buttonBar>
			<adsm:button caption="equipamentos" action="/vol/manterEquipamentos" cmd="main">
				<adsm:linkProperty src="idModeloeqp" target="volModeloseqp.idModeloeqp"/>
				<adsm:linkProperty src="dsNome" target="volModeloseqp.dsNome"/>
			</adsm:button>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
    /**
     * Utilizado para setar o nrIdentificacaoFormatado quando vier do click da grid 
     *
    */ 
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		if (error != undefined){
		   alert(error);
        }
        setElementValue("volFabricante.pessoa.nrIdentificacao",getNestedBeanPropertyValue(data,"volFabricante.pessoa.nrIdentificacaoFormatado"));
        setElementValue("volFabricante.idFabricante",getNestedBeanPropertyValue(data,"volFabricante.idFabricante"));
        setElementValue("nmPessoa",getNestedBeanPropertyValue(data,"volFabricante.pessoa.nmPessoa")); 
    }
    
    function populaDados(data) {
	   setElementValue("volFabricante.pessoa.nrIdentificacao",data.nrIdentificacao);
	   setElementValue("nmPessoa",data.nmPessoa);
	   setElementValue("volFabricante.idFabricante",data.idFabricante);
	}
	function myLookupDataLoad_cb(data,error) {
       var r = volFabricante_pessoa_nrIdentificacao_exactMatch_cb(data);
	   if (r == true) {
	   	  setElementValue("volFabricante.pessoa.nrIdentificacao",data[0].nrIdentificacaoFormatado);
	      setElementValue("nmPessoa",data[0].nmPessoa);
	      setElementValue("volFabricante.idFabricante",data[0].idFabricante);
	   }
	}
</script>    