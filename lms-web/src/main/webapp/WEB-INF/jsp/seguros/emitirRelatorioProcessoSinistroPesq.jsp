<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
 
<adsm:window service="lms.seguros.emitirRelatorioProcessoSinistroAction" >
	<adsm:form action="/seguros/emitirRelatorioProcessoSinistro" id="reportForm">
	
		<adsm:range label="periodo" width="85%">
			<adsm:textbox dataType="JTDate" property="dtSinistroInicial"/>
			<adsm:textbox dataType="JTDate" property="dtSinistroFinal"/>
		</adsm:range>
		
		<adsm:lookup dataType="text" property="doctoServico.clienteByIdCliente" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupCliente" 
					 label="cliente" width="85%" maxLength="20" size="20">
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdCliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdCliente.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup dataType="text" property="grupoEconomico" idProperty="idGrupoEconomico" criteriaProperty="dsGrupoEconomico"
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupGrupoEconomico" action="/vendas/manterGruposEconomicos"
					 label="grupoEconomico" width="85%" size="60" maxLength="60" minLengthForAutoPopUpSearch="3" exactMatch="false" criteriaSerializable="true"/>

		<adsm:lookup label="processoSinistro" 
					property="processoSinistro" 
					idProperty="idProcessoSinistro" 
					criteriaProperty="nrProcessoSinistro"
					criteriaSerializable="true"
					disabled="false"
					mask="99999/9999/9999"
					picker="true" 	
					popupLabel="pesquisarProcessoSinistro"				
					dataType="text"	
					onPopupSetValue="onProcessoSinistroPopupSetValue"
					action="/seguros/manterProcessosSinistro" cmd="list"
					service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupProcessoSinistro"
					onDataLoadCallBack="retornoProcessoSinistro"
					width="85%">
					
			<adsm:propertyMapping relatedProperty="tipoSeguro.idTipoSeguro" modelProperty="processoSinistro.tipoSeguro.idTipoSeguro"/>		
		</adsm:lookup>
		
		
		<adsm:lookup dataType="text" property="rota" idProperty="idRota" criteriaProperty="dsRota"
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupRota" 
					 action="/municipios/manterPostosPassagemRotasViagem" cmd="list" 
		 			 label="rota" width="85%" size="60" maxLength="60" criteriaSerializable="true"/>
		
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />
		
		<adsm:hidden property="manifesto.dsManifesto"/>		
		<adsm:combobox label="manifesto" serializable="true" 
		               property="manifesto.tpManifesto" 
					   service="lms.seguros.emitirRelatorioProcessoSinistroAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="onChangeForSetHiddenValue(this, 'manifesto.dsManifesto'); return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
						   documentGroup:'MANIFESTO',
						   actionService:'lms.seguros.emitirRelatorioProcessoSinistroAction'
						   });" >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 criteriaSerializable="true"
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false" 
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
						 onchange="return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	}); "
						 />

			<adsm:lookup dataType="integer" 
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" popupLabel="pesquisarManifesto"
						 criteriaSerializable="true"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" />
		</adsm:combobox>
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifesto" />
		 			 
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupFilial" action="/municipios/manterFiliais"
					 popupLabel="pesquisarFilial"
					 onchange="return sgFilialControleCargaOnChangeHandler();" onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" size="3" maxLength="3" picker="false" serializable="false" criteriaSerializable="true">
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 popupLabel="pesquisarControleCarga"
						 onDataLoadCallBack="loadNrControleCarga" onPopupSetValue="enableNrControleCarga"
						 maxLength="8" size="8" mask="00000000" criteriaSerializable="true">
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"/>				
				<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false"/>
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporteFake" criteriaProperty="nrFrota"
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onchange="meioTransporteNrFrotaOnChangeHandler()" picker="false"
					 label="meioTransporte" maxLength="6" size="6" serializable="false" criteriaSerializable="true">
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificador"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporte.nrIdentificador" disable="false"/> 
			<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"
						 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupMeioTransporte" 
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="25" picker="true" criteriaSerializable="true">
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporte.nrFrota"/> 
			</adsm:lookup>
		</adsm:lookup>
		
		
		<adsm:hidden property="servico.dsModal"/>
		<adsm:combobox property="servico.tpModal" label="modal" domain="DM_MODAL" onchange="onChangeForSetHiddenValue(this, 'servico.dsModal')" renderOptions="true"/>
		
		<adsm:hidden property="servico.dsAbrangencia"/>
		<adsm:combobox property="servico.tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" onchange="onChangeForSetHiddenValue(this, 'servico.dsAbrangencia')" renderOptions="true"/>
		
		<adsm:hidden property="tipoSinistro.dsTipo"/>
		<adsm:combobox property="tipoSinistro.idTipo" optionLabelProperty="dsTipo" optionProperty="idTipo" 
					   service="lms.seguros.emitirRelatorioProcessoSinistroAction.findTipoSinistro"  
					   label="tipoSinistro" width="85%" onchange="onChangeForSetHiddenValue(this, 'tipoSinistro.dsTipo')"/>
					   
		<adsm:hidden property="tipoSeguro.sgTipo"/>
		<adsm:combobox property="tipoSeguro.idTipoSeguro" optionLabelProperty="sgTipo" optionProperty="idTipoSeguro" 
					   service="lms.seguros.emitirRelatorioProcessoSinistroAction.findTipoSeguro"  
					   label="tipoSeguro" width="85%" onchange="onChangeForSetHiddenValue(this, 'tipoSeguro.sgTipo')"/>

		<adsm:hidden property="naturezaProduto.dsNaturezaProduto" />
		<adsm:combobox property="naturezaProduto" optionProperty="idNaturezaProduto" optionLabelProperty="dsNaturezaProduto" 
					   service="lms.seguros.emitirRelatorioProcessoSinistroAction.findNaturezaProduto" 
					   label="naturProduto" width="85%" onchange="onChangeForSetHiddenValue(this, 'naturezaProduto.dsNaturezaProduto')"/>

		<adsm:hidden property="filial.regionalFiliais.dsRegional" />
		<adsm:combobox property="filial.regionalFiliais.idRegionalFilial" optionProperty="idRegional" optionLabelProperty="siglaDescricao"
					   service="lms.seguros.emitirRelatorioProcessoSinistroAction.findRegional" 
					   label="regional" width="85%" onchange="onChangeForSetHiddenValue(this, 'filial.regionalFiliais.dsRegional')"/>

		<adsm:hidden property="reguladoraSeguro.pessoa.tpIdentificacao"/>		
		<adsm:lookup dataType="text" property="reguladoraSeguro" idProperty="idReguladora" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
		   		  	 action="/seguros/manterReguladorasSeguro" 
		   		  	 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupReguladoraSeguro" 
		   		  	 label="reguladora" width="85%" size="20" maxLength="20" criteriaSerializable="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa"  relatedProperty="reguladoraSeguro.pessoa.nmPessoa"/>
			<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao"  relatedProperty="reguladoraSeguro.pessoa.tpIdentificacao"/>
			
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="reguladoraSeguro.pessoa.nmPessoa" disable="false"/>
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" criteriaProperty="reguladoraSeguro.pessoa.nrIdentificacao" disable="false"/>
			
			<adsm:textbox dataType="text" property="reguladoraSeguro.pessoa.nmPessoa" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup property="rodovia" dataType="text" idProperty="idRodovia" criteriaProperty="sgRodovia" 
					 action="/municipios/manterRodovias" 
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupRodovia" 
					 label="rodovia" size="10" maxLength="10" width="85%"  minLengthForAutoPopUpSearch="3" exactMatch="false" criteriaSerializable="true">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="60" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup property="aeroporto" dataType="text" idProperty="idAeroporto" criteriaProperty="sgAeroporto"
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupAeroporto" 
					 action="/municipios/manterAeroportos" 
					 label="aeroporto"  width="85%" size="3" maxLength="3" criteriaSerializable="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="aeroporto.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="aeroporto.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 label="filialSinistro" size="3" maxLength="3" width="85%" criteriaSerializable="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio" dataType="text" 
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupMunicipio" 
					 action="/municipios/manterMunicipios" 
					 label="municipio" size="60" maxLength="60" width="85%"  minLengthForAutoPopUpSearch="3" exactMatch="false" criteriaSerializable="true"/>

		<adsm:hidden property="municipio.unidadeFederativa.sgUnidadeFederativa" />
		<adsm:combobox property="municipio.unidadeFederativa.idUnidadeFederativa" optionProperty="idUnidadeFederativa" optionLabelProperty="sgUnidadeFederativa"
					   service="lms.seguros.emitirRelatorioProcessoSinistroAction.findUnidadeFederativa" 
					   label="uf" width="85%" onchange="onChangeForSetHiddenValue(this, 'municipio.unidadeFederativa.sgUnidadeFederativa')"/>
		
		<adsm:combobox property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO"
		               defaultValue="pdf" renderOptions="true"
		               label="formatoRelatorio" required="true"/>
		
		<adsm:buttonBar>
			<adsm:button id="detalhado" caption="visualizar" onclick="callReportViewer('detalhado')"/>
			<adsm:button id="resumido" caption="visualizarResumido" onclick="callReportViewer('resumido')"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
		<script>
			function lms_22010() {
				alert("<adsm:label key='LMS-22010'/>");
			}
		</script>
	</adsm:form>
</adsm:window>

<script>

	function initWindow(eventObj) {
	
		disableReportButtons(false);
		
		if (eventObj.name == "tab_load"){
			disableNrControleCarga(true);
		} else if (eventObj.name == "cleanButton_click"){
			disableNrControleCarga(true);
			setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", true);
		}
	}

	//##################################
    // Comportamentos apartir de objetos
	//##################################

	/**
	 * Chama a funcao de geracao do relatorio, apartir do relatorio em questao.
	 *
	 * @param reportStyle stilo desejado, pode ser 'detalhado' ou 'resumido'. 
	 */
	function callReportViewer(reportStyle) {
	
		if ((validateTabScript(document.getElementById("reportForm")))==false) return false;
		if ((getElementValue("processoSinistro.idProcessoSinistro")!='') || 
		   ((getElementValue("dtSinistroInicial")!='') && (getElementValue("dtSinistroFinal")!='') && 
		    (getElementValue("tipoSinistro.idTipo")!='') && (getElementValue("tipoSeguro.idTipoSeguro")!=''))) {
	
			var dataObject = buildFormBeanFromForm(document.getElementById("reportForm"));
			
			var service = null;
			if (reportStyle == 'resumido') {
				service = 'lms.seguros.emitirRelatorioProcessoSinistroAction.executeProcessoSinistroResumido';
			} else {
				service = 'lms.seguros.emitirRelatorioProcessoSinistroAction.executeProcessoSinistroDetalhado';
			}
			var sdo = createServiceDataObject(service, 'openPdf', dataObject); 
			executeReportWindowed(sdo, getElementValue('tpFormatoRelatorio'));
			
		} else { 
			lms_22010();
		}
		
		return false;
	}
	
	/**
	 * Controle para o objeto de sigla de filial do objeto 'controleCarga'
	 */	
	function sgFilialControleCargaOnChangeHandler() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}
	
	
	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) {
			disableNrControleCarga(false);
		}
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	function loadNrControleCarga_cb(data, error) {
		controleCarga_nrControleCarga_exactMatch_cb(data);
		if (data[0]!=undefined) {
			document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value=data[0].filialByIdFilialOrigem.sgFilial;
		}
	}
	
	function nrControleCargaChangeHandler() {
		if (getElementValue("controleCarga.nrControleCarga")=="") {
			var idFilial = getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
			var sgFilial = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");
			resetValue(this.document);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
		} 
		return controleCarga_nrControleCargaOnChangeHandler();
	}
	
	function disableNrControleCarga(disable) {
		document.getElementById("controleCarga.nrControleCarga").disabled = disable;
	}
	
	function enableNrControleCarga(data){
		if (data.nrControleCarga!=undefined) {
			disableNrControleCarga(false);
		} else {
			disableNrControleCarga(true);
		}
	}
	
	//##########################################
	// Funções referentes as tags do manifesto
	//##########################################	
	
	/**
	 * Quando o "Manifesto" for informado
	 */
	function retornoManifesto_cb(data) {
		var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
		if (r == true) {
			var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
			setElementValue('manifesto.idManifesto', idManifesto);
			buscarManifesto(idManifesto);
		}
	}
	
	function enableManifestoManifestoViagemNacioal_cb(data) {
	   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
	      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	   }
	}
	
	function manifestoNrManifestoOrigem_retornoPopup(data) {
		if (data != undefined && data.length > 0) {
			if (getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
				resetValue('manifesto.idManifesto');
			}
			var idManifesto = getElementValue('manifesto.idManifesto');
			if (idManifesto != undefined && idManifesto != '') {
				buscarManifesto(idManifesto);
			}
		}
	}
	
	/**
	 * Busca os dados relacionados ao manifesto.
	 */
	function buscarManifesto(idManifesto) {
		var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findManifestoByRNC", "resultado_buscarManifesto", {idManifesto:idManifesto});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Povoa os campos com os dados retornados da busca em manifesto
	 */
	function resultado_buscarManifesto_cb(data, error) {
		if (data != undefined) {
		
		}
	}
	
	/**
	 * Funcoes para limpar o nrIndentificador dos veiculos
	 */
	function meioTransporteNrFrotaOnChangeHandler() {
	 	meioTransporte_nrFrotaOnChangeHandler();
	 	if (document.getElementById("meioTransporte.nrFrota").value=="") {
	 		document.getElementById("meioTransporte.idMeioTransporte").value="";
	 		document.getElementById("meioTransporte.nrIdentificador").value="";
	 	}
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################

	function onChangeForSetHiddenValue(combo, strHidden) {
		if (combo.options[combo.selectedIndex].value!="") {
			setElementValue(strHidden, combo.options[combo.selectedIndex].text);	
		}
	}
	
	/**
	 * Reseta os dados da tela
	 */
	function resetView(){
		resetValue(this.document);
	}
	
	/**
	 * Disabled buttons
	 *
	 * @param disable 'true' ou 'false' para o caso de ele ser abilitado ou nao.
	 */
	function disableReportButtons(disable) {
		document.getElementById("resumido").disabled = false;
		document.getElementById("detalhado").disabled = false;
	}

	function onProcessoSinistroPopupSetValue(data, error) {
		if (data!=undefined) {			
			if (data.idProcessoSinistro!=undefined) {
				var vet = new Array();				
				vet.idProcessoSinistro = data.idProcessoSinistro;
			    var sdo = createServiceDataObject("lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupProcessoSinistro", "onProcessoSinistroPopupSetValue", vet);
			    xmit({serviceDataObjects:[sdo]});
			}
		}
	}
	
	//Necessário para setar o sgTipo no campo hidden quando se busca um processo de sinistro pela tela lookup.
	function onProcessoSinistroPopupSetValue_cb(data, error){
		processoSinistro_nrProcessoSinistro_exactMatch_cb(data);
		onChangeForSetHiddenValue(document.getElementById('tipoSeguro.idTipoSeguro'), 'tipoSeguro.sgTipo');
	}
	
	//Necessário para setar o sgTipo no campo hidden quando se busca um processo de sinistro diretamente pela lookup.
	function retornoProcessoSinistro_cb(data, error){
		processoSinistro_nrProcessoSinistro_exactMatch_cb(data);
		if (data[0] && data[0].processoSinistro){
			setElementValue("processoSinistro.idProcessoSinistro", data[0].idProcessoSinistro);
			setElementValue("processoSinistro.nrProcessoSinistro", data[0].nrProcessoSinistro);
			setElementValue("tipoSeguro.idTipoSeguro", data[0].processoSinistro.tipoSeguro.idTipoSeguro);
			onChangeForSetHiddenValue(document.getElementById('tipoSeguro.idTipoSeguro'), 'tipoSeguro.sgTipo');
		}
	}

</script>