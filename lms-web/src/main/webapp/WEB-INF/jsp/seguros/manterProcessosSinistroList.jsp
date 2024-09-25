<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterProcessosSinistroAction" onPageLoadCallBack="pageLoad" onPageLoad="loadData">

	<adsm:form action="/seguros/manterProcessosSinistro" height="123" idProperty="idProcessoSinistro">	
			
		<adsm:textbox property="nrProcessoSinistro" label="processoSinistro" dataType="text" width="40%" maxLength="30"/>
		
		<adsm:combobox
			property="situacaoProcessoSinistro"
			label="situacao"
			domain="DM_SIT_PROCESSO_SINISTRO"
			onlyActiveValues="true"
			labelWidth="15%"
			width="30%"
		/>
		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" labelWidth="15%" width="40%" serializable="false" 
					   service="lms.seguros.manterProcessosSinistroAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="limpaManifesto();
					   			 return changeDocumentWidgetType({
					   		documentTypeElement:this, 
					   		filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
					   		documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
					   		documentGroup:'MANIFESTO',
					   		actionService:'lms.seguros.manterProcessosSinistroAction'}); " >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false"
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
						 onchange="limpaManifesto();
						 		   return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	});" >						 	
			</adsm:lookup>

			<adsm:lookup dataType="integer" onDataLoadCallBack="retornoManifesto"
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" popupLabel="pesquisarManifesto"
						 afterPopupSetValue="manifestoAfterPopupSetValue"
						 onchange="return manifestoNrManifestoOrigem_OnChange();"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" >						 
			</adsm:lookup>
		</adsm:combobox>
		
		<!-- LMS-6152 -->
		<adsm:combobox property="doctoServico.tpDocumentoServico"
			label="documentoServico" labelWidth="15%" width="30%"
			service="lms.seguros.manterProcessosSinistroAction.findTpDoctoServico"
			optionProperty="value" optionLabelProperty="description"
			onchange="return changeDocumentWidgetType({
         documentTypeElement:this, 
         filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
         documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
         parentQualifier:'',
         documentGroup:'SERVICE',
         actionService:'lms.seguros.manterProcessosSinistroAction'
        });">

			<adsm:lookup dataType="text"
				property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial"
				criteriaProperty="sgFilial" service="" disabled="true" action=""
				size="3" maxLength="3" picker="false"
				onDataLoadCallBack="enableDoctoServico"
				onchange="return changeDocumentWidgetFilial({
       filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
       documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
       });" />

			<adsm:lookup dataType="integer" property="doctoServico"
				idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
				service="" action="" onDataLoadCallBack="retornoDocumentoServico"
				popupLabel="pesquisarDocumentoServico" size="10" maxLength="8"
				serializable="true" disabled="true" mask="00000000" />

		</adsm:combobox>

		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:hidden property="manifesto.tpStatusManifesto" serializable="false"/>
		<adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" serializable="false" />
		<adsm:hidden property="manifesto.tpManifestoEntrega" serializable="false"/>
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />


		<adsm:hidden property="filialByIdFilialOrigem.nmFilial"/>
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.seguros.manterProcessosSinistroAction.findLookupFilial" action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 onchange="return onFilialControleCargaChange();" onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="15%" width="40%" size="3" maxLength="3" picker="false" serializable="false">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.nmFilial" modelProperty="pessoa.nmFantasia" />
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.seguros.manterProcessosSinistroAction.findLookupControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="onControleCargaPopupSetValue" 
						 popupLabel="pesquisarControleCarga"
						 maxLength="8" size="8" mask="00000000" disabled="false">

				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false" inlineQuery="false"/>				
			</adsm:lookup>
		</adsm:lookup>
		
		
		
		<adsm:lookup label="meioTransporte" labelWidth="15%" width="30%" maxLength="6" size="6"
		             dataType="text" 
		             picker="false"
		             property="meioTransporteRodoviario2" 
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
					 service="lms.sgr.manterSMPAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onchange="meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler()"> 

			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteRodoviario.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  relatedProperty="meioTransporteRodoviario.nrIdentificador"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  criteriaProperty="meioTransporteRodoviario.nrIdentificador" disable="false"/> 
			
			<adsm:lookup dataType="text" 
			             property="meioTransporteRodoviario" 
			             idProperty="idMeioTransporte" 
			             criteriaProperty="nrIdentificador"
						 service="lms.sgr.manterSMPAction.findLookupMeioTransporte" 
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="23" picker="true">
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteRodoviario2.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteRodoviario2.nrFrota" disable="false"/> 
			</adsm:lookup>
		</adsm:lookup>
		
		
		
		<adsm:range label="dataSinistro" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" property="dhSinistroInicial" picker="true"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhSinistroFinal" picker="true" />
		</adsm:range>
		
		<adsm:combobox property="tipoSinistro.idTipoSinistro" label="tipoSinistro" width="40%" optionLabelProperty="dsTipo" optionProperty="idTipoSinistro" service="lms.seguros.manterProcessosSinistroAction.findComboTipoProcessoSinistro"  />

		<adsm:combobox property="localSinistro" label="localSinistro" labelWidth="15%" width="30%" domain="DM_LOCAL_SINISTRO" renderOptions="true" />

		<adsm:combobox property="tipoSeguro.idTipoSeguro" label="tipoSeguro" optionLabelProperty="sgTipo" optionProperty="idTipoSeguro" service="lms.seguros.manterProcessosSinistroAction.findComboTipoSeguro" labelWidth="15%" width="35%" />
 		
		<adsm:lookup service="lms.seguros.manterProcessosSinistroAction.findLookupRodovia" dataType="text" property="rodovia" idProperty="idRodovia" criteriaProperty="sgRodovia" label = "rodovia" size="10" maxLength="10" width="85%" action="/municipios/manterRodovias" >
			<adsm:propertyMapping relatedProperty="rodovia.dsRodovia" modelProperty="dsRodovia" />
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="26" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:lookup property="filialSinistro" idProperty="idFilial" required="false" criteriaProperty="sgFilial" maxLength="3" service="lms.seguros.manterProcessosSinistroAction.findLookupFilial" dataType="text" label="filialSinistro" size="3" action="/municipios/manterFiliais"  width="85%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="false" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
			<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
			<adsm:hidden property="flagBuscaEmpresaUsuarioLogado"      value="true" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
		</adsm:lookup>
		
		<adsm:lookup label="aeroporto" service="lms.seguros.manterProcessosSinistroAction.findLookupAeroporto" action="municipios/manterAeroportos" 
			 	 	 dataType="text" property="aeroporto" idProperty="idAeroporto" labelWidth="15%" width="85%" size="3" 
			 	 	 maxLength="3" criteriaProperty="sgAeroporto" required="false">
 	 	    <adsm:propertyMapping relatedProperty="aeroporto.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox property="aeroporto.pessoa.nmPessoa" dataType="text" size="30" maxLength="30" disabled="true"/>	 	    	      
	 	</adsm:lookup>

		
		<adsm:lookup dataType="text" property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio"
	             action="/municipios/manterMunicipios" service="lms.seguros.manterProcessosSinistroAction.findLookupMunicipio"
                 maxLength="60" size="30" minLengthForAutoPopUpSearch="3" exactMatch="false" 
                 label="municipio" labelWidth="15%" width="85%" required="false">
                <adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
        </adsm:lookup>


		<adsm:lookup property="unidadeFederativa"  idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.seguros.manterProcessosSinistroAction.findLookupUnidadeFederativa" dataType="text" required="false"
					 labelWidth="15%" width="35%" label="uf" size="3" maxLength="3" 
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="25" serializable="false" disabled="true" />
		</adsm:lookup>		

		
		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.seguros.manterProcessosSinistroAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 label="cliente" size="20" maxLength="20" serializable="true" labelWidth="15%" width="85%">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<!-- LMS-6152 -->
		<adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuarioResponsavel"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="20" 
					 maxLength="20"	
					 width="85%"		 					 
					 service="lms.seguros.manterProcessosSinistroAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="nmUsuario" disabled="true" maxLength="45" size="50"/>
		</adsm:lookup>	
		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="processoSinistro"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		 
		
	</adsm:form>
	<adsm:grid property="processoSinistro" 
			idProperty="idProcessoSinistro" 
			service="lms.seguros.manterProcessosSinistroAction.findPaginatedCustom"
			rowCountService="lms.seguros.manterProcessosSinistroAction.getRowCountCustom"
			selectionMode="none" 
			gridHeight="161" 
			rows="8"
			unique="true" 
			scrollBars="horizontal" >
			
		<adsm:gridColumn property="nrProcessoSinistro"  dataType="text" title="numeroProcesso" width="135"/>

		<adsm:gridColumn property="nrFrota"  dataType="text" title="veiculo" width="50" align="left" />
		<adsm:gridColumn property="nrIdentificador" title="" width="70" align="left" />

		<adsm:gridColumn property="dhSinistro" title="dataHoraSinistro" width="130" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="tipoSinistro" title="tipoSinistro" width="100" />
		<adsm:gridColumn property="tipoSeguro" title="tipoSeguro" width="100" />
		<adsm:gridColumn property="municipio" title="municipio" width="180" />
		<adsm:gridColumn property="uf" title="uf" width="40" />
		
		<adsm:gridColumn property="valorMercadoria" title="valorMercadoria" dataType="currency" mask="###,###,##0.00" width="90" />
		<adsm:gridColumn property="valorPrejuizo" title="valorPrejuizo" dataType="currency" mask="###,###,##0.00" width="90" />
		<adsm:gridColumn property="valorIndenizado" title="valorIndenizado" dataType="currency" mask="###,###,##0.00" width="90" />
		
		<adsm:gridColumn property="usuarioResponsavel"  dataType="text" title="usuarioResponsavel" width="180"/>
		
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>

<script>
	function initWindow(e) {
		if (e.name == 'cleanButton_click') {
			limparTagManifesto();
		}
		if (document.location.search.match("mode=lookup")==null){
            setVisibility('btnFechar', false);		      
		}
	}
	
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);

		if (getNrProcessoSinistro()) {
			var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
			processoSinistro_cb(fb);
			setElementValue("idProcessoSinistro", '');
		} else {
			disableControleCarga(true);
		}
		mostraEscondeBotaoFechar();
	}
	
	function loadData() {
		onPageLoad();
	}
	
	function gerarEmailsMonitoramento() {
		var sdo = createServiceDataObject("lms.seguros.monitoramentoProcessoSinistroService.executeMonitoramento", "gerarEmailsMonitoramento", null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function gerarEmailsMonitoramento_cb(data, error)  {
		if (data._exception==undefined) {
			showSuccessMessage();
		} else {
			alert(error);
		}
	}
	
	function getNrProcessoSinistro() {
		var url = new URL(parent.location.href);
		return url.parameters["nrProcessoSinistro"];
	}
	
	function loadData_cb(data, error) {
		setElementValue("nrProcessoSinistro", getNrProcessoSinistro());
		onPageLoad();
	}
	
	function mostraEscondeBotaoFechar(){
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}	
	}
	
	function meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler() {
	
	 	meioTransporteRodoviario2_nrFrotaOnChangeHandler();
	 	
	 	if (document.getElementById("meioTransporteRodoviario2.nrFrota").value=="") {
	 		document.getElementById("meioTransporteRodoviario.idMeioTransporte").value="";
	 		resetValue(document.getElementById("meioTransporteRodoviario.idMeioTransporte")); 
	 		// document.getElementById("meioTransporteByIdMeioTransporte.nrIdentificador").value="";
	 	}
	}
	
	/*******************************************************************************************************************
	 * Início Manifesto
	 *******************************************************************************************************************/

	/**
	 * Limpa os campos relacionados ao manifesto
	 */
	function limparCamposRelacionadosManifesto() {
		resetValue('controleCarga.idControleCarga');
		resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
		limparTagManifesto();
		limparDadosFrota();
	}
	
	/**
	 * Limpa os dados informados na tag manifesto
	 */
	function limparTagManifesto() {
		resetValue("manifesto.tpManifesto");
		resetValue("manifesto.idManifesto");
		resetValue("manifesto.filialByIdFilialOrigem.idFilial");
		resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
		desabilitaTagManifesto(true);
		disableControleCarga(true);
	}
	
	function limpaManifesto() {
		resetValue('manifesto.idManifesto');
		disableControleCarga(true);
	}
	
	function desabilitaTagManifesto(valor) {
		if (valor == true) {
			setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
			setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', true);
		}
		else {
			if (getElementValue('manifesto.idManifesto') != "" || getElementValue('manifesto.tpManifesto') != "")
				setDisabled('manifesto.filialByIdFilialOrigem.idFilial', false);
			if (getElementValue('manifesto.idManifesto') != "" || getElementValue('manifesto.filialByIdFilialOrigem.idFilial') != "")
				setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', false);
		}
	}
	
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
	
	/**
	 * Busca os dados relacionados ao manifesto.
	 */
	function buscarManifesto(idManifesto) {
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findManifestoByRNC", 
					"resultado_buscarManifesto", {idManifesto:idManifesto});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Povoa os campos com os dados retornados da busca em manifesto
	 */
	function resultado_buscarManifesto_cb(data, error) {
		if (data != undefined) {
	
			setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.idControleCarga"));
			setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.nrControleCarga"));
			setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', getNestedBeanPropertyValue(data,"0:controleCarga.filialByIdFilialOrigem.idFilial"));
			setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data,"0:controleCarga.filialByIdFilialOrigem.sgFilial"));
			setElementValue('meioTransporteRodoviario.idMeioTransporte', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdTransportado.idMeioTransporte"));
			setElementValue('meioTransporteRodoviario2.nrFrota', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdTransportado.nrFrota"));
			setElementValue('meioTransporteRodoviario.nrIdentificador', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
			format(document.getElementById("controleCarga.nrControleCarga"));
			disableControleCarga(false);
		}
	}
	
	/**
	 * Busca os dados relacionados ao Manifesto/Controle de cargas
	 */
	function buscarManifestoControleCargas(idDoctoServico) {
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findManifestoComControleCargas", 
					"resultado_buscarManifestoControleCargas", {idDoctoServico:idDoctoServico});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Povoa os campos com os dados retornados da busca em Manifesto/Controle de cargas
	 */
	function resultado_buscarManifestoControleCargas_cb(data, error) {
		if (data == undefined) {
			return;
		}
		setElementValue('controleCarga.idControleCarga', data.idControleCarga);
		setElementValue('controleCarga.nrControleCarga', data.nrControleCarga); 
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.sgFilialControleCarga); 
		
		setElementValue('manifesto.idManifesto', data.idManifesto);
		setElementValue('manifesto.filialByIdFilialOrigem.sgFilial', data.sgFilialManifesto);
		setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', data.nrManifesto);
		setElementValue('manifesto.tpManifesto', data.tpManifesto);
	
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', data.veiculoFrota);
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', data.veiculoPlaca);
	
		if (getElementValue('manifesto.tpManifesto') != "") {
			changeDocumentWidget({documentDefinition:eval(getElementValue("manifesto.tpManifesto") + "_MANIFESTO_DOCUMENT_WIDGET_DEFINITION"), 
				  filialElement:document.getElementById("manifesto.filialByIdFilialOrigem.idFilial"), 
				  documentNumberElement:document.getElementById("manifesto.manifestoViagemNacional.idManifestoViagemNacional"), 
				  actionService:"lms.seguros.manterProcessosSinistroAction"
				  });  
		}
	
		format(document.getElementById("controleCarga.nrControleCarga"));
		format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	}
	
	
	function disableControleCarga(disable) {
		setDisabled('controleCarga.nrControleCarga', disable);
		if (disable==true)	{
			resetValue('controleCarga.idControleCarga');
			resetValue('controleCarga.filialByIdFilialOrigem.idFilial');			
		}
	}
	
	function manifestoNrManifestoOrigem_OnChange() {
		var r = manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
		if (getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
			resetaControleCarga();
			resetValue('manifesto.idManifesto');
		}
		return r;
	}
	
	function manifestoAfterPopupSetValue(data) {
		setDisabled('manifesto.manifestoViagemNacional.nrManifestoOrigem', false);
		var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		setElementValue('manifesto.idManifesto', idManifesto);
		buscarManifesto(idManifesto);
	}
		
	function enableManifestoManifestoViagemNacioal_cb(data) {
	   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
	      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	   }
	}
	
	function carregaManifesto(data) {
		if ( getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega") != undefined ) {
			setElementValue('manifesto.tpManifesto', 'EN');
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega"));
			document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "00000000";
			format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
		else
		if ( getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem") != undefined) {
			setElementValue('manifesto.tpManifesto', 'VN');
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem"));
			document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "00000000";
			format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
		else
		if ( getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt") != undefined) {
			setElementValue('manifesto.tpManifesto', 'VI');
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt"));
			document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "000000";
			format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
	}
	
	function resetaControleCarga() {
		resetValue('manifesto.idManifesto');
		resetValue('controleCarga.idControleCarga');
		resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
		limparDadosFrota();
	}
	
	function limparDadosFrota() {
		resetValue('meioTransporteRodoviario2.nrFrota');
		resetValue('meioTransporteRodoviario.nrIdentificador');
		resetValue('meioTransporteRodoviario.idMeioTransporte'); 
	}
	/*******************************************************************************************************************
	 * Término Manifesto
	 *******************************************************************************************************************/


	/*************************************************************************************
	 * Controle de Cargas
	 *************************************************************************************/
	function onFilialControleCargaChange() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			return true;
			
		} else {
			disableNrControleCarga(false);
			return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
		}
	}
	

	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.nrControleCarga", disable);
	}
	
	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
			
	function onNrFrotaChange(elem) {
		var r = meioTransporteRodoviario2_meioTransporte_nrFrotaOnChangeHandler();
		if(getElementValue("meioTransporteRodoviario2.nrFrota") == "")   {
			setElementValue("meioTransporteRodoviario.nrIdentificador", "");
		}
		
		return r;
	}

	function onFilialMeioTransporteDataLoad() {
	
	}
	
	function onListagemShow() {
		var tabGroup = getTabGroup(document);
		tabGroup.setDisabledTab("documentos", false);
		tabGroup.setDisabledTab("custosAdicionais", true);
		tabGroup.setDisabledTab("fotos", true);			
		
	}
	
	function onControleCargaPopupSetValue(data) {
    	setDisabled('controleCarga.idControleCarga', false);
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.filialByIdFilialOrigem.sgFilial);				
	}
	

	/**
	 * javaScripts para a 'tag documents'
	 */
	function enableDoctoServico_cb(data) {
		var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if (r == true) {
			setDisabled("doctoServico.idDoctoServico", false);
			setFocus(document.getElementById("doctoServico.nrDoctoServico"));
		}
	}

	function retornoDocumentoServico_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
	}
	
</script>
