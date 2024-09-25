<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("btnRemove",true);
		setDisabled("__prioridades",true);
		setDisabled("filial.pessoa.nmFantasia",true);
		setDisabled("filial.idFilial",document.getElementById("filial.idFilial").masterLink == "true");
		document.getElementById("tpUso").value = 'A' ;
		 
		if (document.getElementById("filial.idFilial").masterLink == "true")
			setFocus(document.getElementById("empresa.idEmpresa"));
		else
			setFocus(document.getElementById("filial.sgFilial"));
	}
	
	/**
	 * Habilitar campos se o registro estiver vigente.
	 */
	function habilitarCampos() {
		setDisabled("nrPrestacaoContas",false);
		setDisabled("dsIdentificadorCiaAerea",false);
		setDisabled("vlIdentificadorCiaAerea",false);
		setDisabled("tpUso",false);
		setDisabled("observacaoCiaAerea.idObservacaoCiaAerea",false);
		setDisabled("dtVigenciaFinal",false);
	}
	
	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function dataLoadCustom_cb(data,exception) {
		onDataLoad_cb(data,exception);
		comportamentoDetalhe(data);
	}
	
	function sugereVigenciaPopUp(data) {
	      var idFilial = getNestedBeanPropertyValue(data,"idFilial");
	      sugereVigencia(idFilial);
	}
	
	function sugereVigenciaDataLoad_cb(data, erro) {
		if (erro != undefined) {
			alert(erro);
			return false;
		}
		
		if (data != undefined && data != null) {
		    filial_sgFilial_exactMatch_cb(data);
		    var idFilial = getNestedBeanPropertyValue(data,":0.idFilial");
		    sugereVigencia(idFilial);
		}
	}
	
	function sugereVigencia(idFilial){
		if (idFilial == undefined || idFilial == null)
			return;
         var sdo = createServiceDataObject("lms.municipios.filialService.findFilialComHistoricosFuturos", "sugereVigencia",
         {idFilial:idFilial});
         xmit({serviceDataObjects:[sdo]});
	}

	function sugereVigencia_cb(data, erro) {
	     if (erro != undefined) {
	  	   alert(erro);
	  	   return false;
	     }
         setElementValue("dtVigenciaInicial", data.dtPrevisaoOperacaoInicial);
	}
	
	function ciaFilialMercurioOnDataLoad_cb(data,exception){
		onPageLoad_cb(data, exception);
		document.getElementById("tpUso").value = 'A';
	}
	
	
</script>
<adsm:window service="lms.municipios.manterCiasAereasFiliaisAction" onPageLoadCallBack="ciaFilialMercurioOnDataLoad">
	<adsm:form action="/municipios/manterCiasAereasFiliais" idProperty="idCiaFilialMercurio" height="390"
			onDataLoadCallBack="dataLoadCustom" service="lms.municipios.manterCiasAereasFiliaisAction.findById" >
		<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>
		<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" property="filial" idProperty="idFilial"
				criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" required="true"
				action="/municipios/manterFiliais" labelWidth="21%" width="75%" exactMatch="true" onPopupSetValue="sugereVigenciaPopUp" onDataLoadCallBack="sugereVigenciaDataLoad">
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
         	<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true"/>
	    </adsm:lookup>

		<adsm:combobox property="empresa.idEmpresa" optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa" onlyActiveValues="true"
				labelWidth="21%" width="79%" label="ciaAerea" required="true" service="lms.municipios.empresaService.findCiaAerea" boxWidth="205">
				<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
		</adsm:combobox>		
		<adsm:hidden property="empresa.pessoa.nmPessoa" />
		
		<adsm:textbox dataType="integer" property="nrPrestacaoContas" label="numeroPrestacaoContas" maxLength="6" size="7"
				labelWidth="21%" width="79%" />
		<adsm:textbox dataType="text" property="dsIdentificadorCiaAerea" label="identificadorCiaAerea" maxLength="20" size="20"
				labelWidth="21%" width="29%" />
		<adsm:textbox dataType="integer" property="vlIdentificadorCiaAerea" label="valorIdentificador" maxLength="15" size="15"
				labelWidth="21%" width="29%" /> 
				
		<adsm:combobox property="tpUso" label="status" domain="DM_STATUS_AEROPORTO" labelWidth="21%" width="79%" required="true" /> 
		
		<adsm:combobox property="observacaoCiaAerea.idObservacaoCiaAerea" label="observacao" service="lms.municipios.observacaoCiaAereaService.findByOrder"
				optionLabelProperty="dsObservacaoCiaAerea" optionProperty="idObservacaoCiaAerea" boxWidth="395"
				labelWidth="21%" width="79%" onlyActiveValues="true" required="true" />
		<adsm:range label="vigencia" labelWidth="21%" width="79%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
        </adsm:range>

		<adsm:buttonBar>
			<adsm:button caption="prioridadeEmbarqueFilial" id="__prioridades" action="municipios/manterFiliaisCiaAereaFiliais" cmd="main" boxWidth="365" >
				<adsm:linkProperty src="idCiaFilialMercurio" target="ciaFilialMercurio.idCiaFilialMercurio" />
				<adsm:linkProperty src="filial.idFilial" target="ciaFilialMercurio.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="ciaFilialMercurio.filial.sgFilial" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="ciaFilialMercurio.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="empresa.idEmpresa" target="ciaFilialMercurio.empresa.idEmpresa" disabled="true"/>
				<adsm:linkProperty src="empresa.pessoa.nmPessoa" target="ciaFilialMercurio.empresa.pessoa.nmPessoa" />
			</adsm:button>
			<adsm:storeButton id="btnStore" service="lms.municipios.manterCiasAereasFiliaisAction.store" callbackProperty="afterStore" />
			<adsm:newButton id="btnNew" />
			<adsm:removeButton id="btnRemove" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton") {
			estadoNovo();
		}
	}
	
	function comportamentoDetalhe(data, isStore) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("btnRemove",false);
			setFocusOnFirstFocusableField(document);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			setDisabled("btnNew",false);
			setDisabled("btnStore",false);
			setDisabled("btnRemove",true);
			habilitarCampos();
			setDisabled("tpUso",true);
			setDisabled("observacaoCiaAerea.idObservacaoCiaAerea",true);
			setFocusOnFirstFocusableField(document);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("btnNew",false);
			setFocusOnNewButton();
		}
		
		setDisabled("__prioridades",false);
	}
		
	function afterStore_cb(data,error) {
		store_cb(data,error);
		if (error == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
	}

</script>