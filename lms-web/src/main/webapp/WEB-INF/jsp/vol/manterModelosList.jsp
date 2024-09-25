<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterModelosAction" >
	<adsm:form  action="/vol/manterModelos">
		
		<adsm:textbox property="dsNome" dataType="text" label="nome" maxLength="30" size="35" width="85%"/>
		
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
			onDataLoadCallBack="myLookupDataLoad"
			afterPopupSetValue="populaDados">
			
			<adsm:propertyMapping 
				relatedProperty="nmPessoa" 
				modelProperty="volFabricante.pessoa.nmPessoa"/>
				
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
		       width="85%"  />	    
		<adsm:combobox property="blHomologado" label="homologado"
					   width="85%" domain="DM_SIM_NAO" labelWidth="15%"
					   autoLoad="true" onlyActiveValues="false"/>	
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="modelos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
				
	</adsm:form>
	
	<adsm:grid property="modelos" idProperty="idModeloeqp" selectionMode="check" 
			   rows="11" gridHeight="200"
			   service="lms.vol.manterModelosAction.findPaginatedModelos"			   
			   rowCountService="lms.vol.manterModelosAction.getRowCountModelos" >
		
		<adsm:gridColumn property="dsNome" title="nome" width="200" />
		<adsm:gridColumn property="volFabricante.pessoa.nmPessoa" title="fabricante" width="200" />
		<adsm:gridColumn property="volTiposEqpto.dsNome" title="tipo" width="200" />
		<adsm:gridColumn property="blHomologado" title="homologado" renderMode="image-check" width="100" />                           
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script>
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