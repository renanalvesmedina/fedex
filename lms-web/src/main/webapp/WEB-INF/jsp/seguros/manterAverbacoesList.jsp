<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterAverbacoesAction">

	<adsm:form action="/seguros/manterAverbacoes" height="110" id="formAverbacao">

		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente"
 					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.seguros.manterAverbacoesAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
 					 label="cliente" size="20" maxLength="20" serializable="true" labelWidth="15%" width="85%"> 
 			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
 			<adsm:propertyMapping relatedProperty="cliente.identificacao" modelProperty="pessoa.nrIdentificacao"/>
 			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/> 
 		</adsm:lookup> 
 		<adsm:hidden property="cliente.identificacao"/>

		<adsm:hidden property="tipoMeioTransporte.tpMeioTransporte" value="R" />
		<adsm:combobox
			property="tpModal" boxWidth="150" label="modal" 
			optionProperty="value" onchange="comboboxChange({e:this});"
			optionLabelProperty="description"
			onchange="setaDescricaoModal(this);" 
			service="lms.seguros.manterAverbacoesAction.findComboModal"  width="25%" labelWidth="15%"
			onlyActiveValues="true"
		>
		</adsm:combobox>	
		<adsm:hidden property="dsModal" serializable="true"/>
		
		<adsm:combobox
			property="tpFrete"
			label="tipoFrete"
			domain="DM_TIPO_FRETE"
			labelWidth="10%"
			onchange="setaDescricaoTpFrete(this);"
			width="20%"/>
			<adsm:hidden property="dsFrete" serializable="true"/>
		
		<adsm:combobox property="tipoSeguro.idTipoSeguro"
				optionProperty="idTipoSeguro" 
				optionLabelProperty="sgTipo"
				service="lms.seguros.manterAverbacoesAction.findTipoSeguroOrderBySgTipo"
				label="tipoSeguro"
				labelWidth="10%" 
				width="20%">
				<adsm:propertyMapping modelProperty="sgTipo" relatedProperty="tipoSeguro.sgTipo"/>		
		</adsm:combobox>
		<adsm:hidden property="tipoSeguro.sgTipo" serializable="true"/>
				
		<adsm:combobox property="reguladoraSeguro.idReguladora"
				   optionProperty="idReguladora" optionLabelProperty="pessoa.nmPessoa" 
				   service="lms.seguros.manterAverbacoesAction.findReguladoraOrderByNmPessoa" 
				   label="reguladora" width="40%">
				  <adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="reguladoraSeguro.pessoa.nmPessoa"/>
		</adsm:combobox>	
		<adsm:hidden property="reguladoraSeguro.pessoa.nmPessoa" />   
				   
		<adsm:combobox property="seguradora.idSeguradora" 
				optionProperty="seguradora.idSeguradora" 
				optionLabelProperty="seguradora.pessoa.nmPessoa" 
				service="lms.seguros.manterAverbacoesAction.findReguladoraSeguradoraOrderByNmPessoa" 
				label="seguradora" labelWidth="15%" width="30%">
				<adsm:propertyMapping modelProperty="seguradora.pessoa.nmPessoa" relatedProperty="seguradora.pessoa.nmPessoa"/>	
		</adsm:combobox>
		<adsm:hidden property="seguradora.pessoa.nmPessoa" serializable="true"/>

		<adsm:range label="periodoViagem" labelWidth="15%" width="40%">
			<adsm:textbox dataType="JTDate" property="dtInicioViagem"/>
			<adsm:textbox dataType="JTDate" property="dtFimViagem"/>
		</adsm:range>

		<adsm:lookup label="meioTransporte" labelWidth="15%" width="30%" maxLength="6" size="6"
		             dataType="text" 
		             picker="false"
		             property="meioTransporteRodoviario2" 
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
					 service="lms.seguros.manterAverbacoesAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onchange="meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler()"> 

			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteRodoviario.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  relatedProperty="meioTransporteRodoviario.nrIdentificador"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  criteriaProperty="meioTransporteRodoviario.nrIdentificador" disable="false"/> 
			
			<adsm:lookup dataType="text" 
			             property="meioTransporteRodoviario" 
			             idProperty="idMeioTransporte" 
			             criteriaProperty="nrIdentificador"
						 service="lms.seguros.manterAverbacoesAction.findLookupMeioTransporte"
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="23" picker="true">
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteRodoviario2.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteRodoviario2.nrFrota" disable="false"/> 
			</adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="meioTransporteRodoviario2.nrFrota" serializable="true"/>
		<adsm:hidden property="meioTransporteRodoviario.nrIdentificador" serializable="true"/>

		<adsm:lookup dataType="text"
					 property="filialOrigem"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.seguros.manterAverbacoesAction.findLookupFilial"
    				 label="filialOrigem" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="15%" 
    				 width="40%"  
    				 exactMatch="true" 
    				 action="/municipios/manterFiliais"
		             onDataLoadCallBack="filialDataLoad" 
		             onchange="return filialChange(this);">
         	
			<%--adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
         	<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />    
         	<adsm:propertyMapping relatedProperty="sgFilialOrigem" modelProperty="sgFilial" />     
         	<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="25" disabled="true" serializable="true" />         	
	    </adsm:lookup>
	    <adsm:hidden property="sgFilialOrigem" serializable="true"/>

	    <adsm:lookup dataType="text" 
	    			 property="filialDestino"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.seguros.manterAverbacoesAction.findLookupFilial"
    				 label="filialDestino" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="15%"
    				 width="30%" 
    				 exactMatch="true"
    				 action="/municipios/manterFiliais">
    				 
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilialDestino" modelProperty="sgFilial" />        	
         	<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="25" disabled="true" serializable="true" />
	    </adsm:lookup>
        <adsm:hidden property="sgFilialDestino" serializable="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="exportarExcel" caption="exportarExcel" onclick="emitirReport();"/>
			<adsm:button  id="btnConsultar" caption="consultar" onclick="return validaFiltros();"/>
			<adsm:button id="newButton" caption="limpar" onclick="resetForm(this.document);"/>
		</adsm:buttonBar>

	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
	</adsm:i18nLabels>

	</adsm:form>
	
	<adsm:grid property="averbacoes" idProperty="idAverbacao" 
			   service="lms.seguros.manterAverbacoesAction.findPaginatedAverbacoes" selectionMode="check" 
			   rowCountService="lms.seguros.manterAverbacoesAction.getRowCountAverbacoes"
			   unique="true" scrollBars="horizontal" rows="8" gridHeight="165" >
			   
		<adsm:gridColumn property="cliente" title="cliente" dataType="text"  width="230"/>
		<adsm:gridColumn property="tpModal" title="modal" width="70" />
		<adsm:gridColumn property="tpFrete" title="tipoFrete" width="70" />
		<adsm:gridColumn property="tpSeguro" title="tipoSeguro" width="70" />
		<adsm:gridColumn property="dtViagem" title="dataViagem" width="70" dataType="JTDate"/>
		<adsm:gridColumn property="vlEstimado" title="valorEstimado" width="70" dataType="currency"/>
		<adsm:gridColumn property="psTotal" title="pesoTotal" width="70"  dataType="decimal" align="right" mask="###,###,###,##0.000"/>
		<adsm:gridColumn property="filialOrigem" title="origem" width="60" dataType="text"/>
		<adsm:gridColumn property="filialDestino" title="destino" width="60" dataType="text"/>
		<adsm:gridColumn property="nmCorretora" title="reguladora" width="190" dataType="text"/>
		<adsm:gridColumn property="nmSeguradora" title="seguradora" width="190" dataType="text"/>

		<adsm:buttonBar>
			<adsm:button caption="excluir" buttonType="removeButton" onclick="removeAverbacoes();"/>
 		</adsm:buttonBar>
	</adsm:grid>
	
	
</adsm:window>

<script>

	function initWindow(eventObj){
 		setDisabled("btnConsultar", false);
		setDisabled("newButton", false);
		setDisabled("exportarExcel", false);
	}

	function filialChange(obj){
	
		var retorno = filialOrigem_sgFilialOnChangeHandler();

		if (obj.value == ''){
			setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", true);
			resetValue("doctoServico.idDoctoServico");
			setDisabled("doctoServico.idDoctoServico", true);
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			
			if (getElementValue("doctoServico.tpDocumentoServico") != '') {
				setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
				setDisabled("doctoServico.idDoctoServico", true, null, false);
			}
			
		}
		return retorno;
	}
	
	function filialDataLoad_cb(data){
		var retorno = filialOrigem_sgFilial_exactMatch_cb(data);

		return retorno;
	}
	
	function meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler() {
	
	 	meioTransporteRodoviario2_nrFrotaOnChangeHandler();
	 	
	 	if (document.getElementById("meioTransporteRodoviario2.nrFrota").value=="") {
	 		document.getElementById("meioTransporteRodoviario.idMeioTransporte").value="";
	 		resetValue(document.getElementById("meioTransporteRodoviario.idMeioTransporte")); 
	 	}
	}
	
	function resetForm(doc){
	
		/** Reseta todos campos da tela */
		cleanButtonScript(doc);
	
		setDisabled("btnConsultar", false);
		setDisabled("newButton", false);
		setDisabled("exportarExcel", false);
	}
	
		function validaFiltros(){
		
		if(getElementValue("cliente.idCliente") == "" 
			&& getElementValue("tpModal") == ""
			&& getElementValue("tpFrete") == "" 
			&& getElementValue("tipoSeguro.idTipoSeguro") == "" 
			&& getElementValue("reguladoraSeguro.idReguladora") == "" 
			&& getElementValue("seguradora.idSeguradora") == "" 
			&& getElementValue("meioTransporteRodoviario.idMeioTransporte") == "" 
			&& getElementValue("filialOrigem.idFilial") == ""  
			&& getElementValue("filialDestino.idFilial") == ""
			&& getElementValue("dtInicioViagem") == ""
			&& getElementValue("dtFimViagem") == ""){
				alert("LMS-00055- " + i18NLabel.getLabel("LMS-00055"));
			return false;
		}
		
		return findButtonScript("averbacoes", document.getElementById("formAverbacao"));
		
	}

	function removeAverbacoes(){
		var mapCriteria = new Array();	   
		setNestedBeanPropertyValue(mapCriteria, "ids", averbacoesGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.seguros.manterAverbacoesAction.removeByIds", "removeItem", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	function removeItem_cb(data, error, eventObj){
		if(error == undefined){
			averbacoesGridDef.executeLastSearch(true);
			showSuccessMessage();
			atualizaTela(eventObj);
		}else{
			alert(error);	
		}
	}
	
	function atualizaTela(eventObj){
		if (eventObj == undefined) {
			eventObj = {name:"storeItemButton"};
		}	

		var tab = getTab(document, false);
		if (tab) {
			eventObj.src = tab.tabGroup.selectedTab;
		}
   		initWindowScript(document.parentWindow, eventObj);
	}
	
	function emitirReport() {
		if(getElementValue("cliente.idCliente") == "" 
			&& getElementValue("tpModal") == ""
			&& getElementValue("tpFrete") == "" 
			&& getElementValue("tipoSeguro.idTipoSeguro") == "" 
			&& getElementValue("reguladoraSeguro.idReguladora") == "" 
			&& getElementValue("seguradora.idSeguradora") == "" 
			&& getElementValue("meioTransporteRodoviario.idMeioTransporte") == "" 
			&& getElementValue("filialOrigem.idFilial") == ""  
			&& getElementValue("filialDestino.idFilial") == ""
			&& getElementValue("dtInicioViagem") == ""
			&& getElementValue("dtFimViagem") == ""){
				alert("LMS-00055- " + i18NLabel.getLabel("LMS-00055"));
			return false;
		}
		executeReportWithCallback('lms.seguros.manterAverbacoesAction.executeReport', 'openReportWithLocator', document.forms[0]);
	}
	
	/**
	* Seta a descrição Modal para ser usada no cabeçalho do relatório
	*/
	function setaDescricaoModal(obj){
		setElementValue("dsModal",obj.options[obj.selectedIndex].text);    
	}
	
	/**
	* Seta a descrição Tipo de Frete para ser usada no cabeçalho do relatório
	*/
	function setaDescricaoTpFrete(obj){
		setElementValue("dsFrete",obj.options[obj.selectedIndex].text);
	}

</script>