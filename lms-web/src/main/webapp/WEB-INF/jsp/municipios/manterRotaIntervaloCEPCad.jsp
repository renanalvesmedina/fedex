<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterRotaIntervaloCEPAction" onPageLoadCallBack="rotaIntervaloCepPageLoad">

	<adsm:form action="/municipios/manterRotaIntervaloCEP" service="lms.municipios.manterRotaIntervaloCEPAction.findById" idProperty="idRotaIntervaloCep" onDataLoadCallBack="rotaIntervaloCepDataLoad">
	
		<adsm:combobox labelWidth="20%" width="70%" property="blAtendimentoTemporario" label="lbAtendimentoTemporario" domain="DM_SIM_NAO" required="true"/>	
	
		<adsm:hidden property="tpEmpresa" value="M" serializable="false"></adsm:hidden>	    
		<adsm:lookup dataType="text"
					 property="rotaColetaEntrega.filial"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.municipios.manterRotaIntervaloCEPAction.findLookupFilial"
    				 label="filial" 
    				 size="3" 
    				 required="true"
    				 maxLength="3" 
    				 labelWidth="20%"  
    				 width="30%" 
    				 exactMatch="true" 
    				 action="/municipios/manterFiliais">
         	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />         
         	<adsm:propertyMapping criteriaProperty="tpEmpresa"  modelProperty="empresa.tpEmpresa" />
         	<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
	    </adsm:lookup>

		<adsm:lookup label="rotaColetaEntrega2" property="rotaColetaEntrega" 
			 		 idProperty="idRotaColetaEntrega" 
			 		 criteriaProperty="nrRota"
			 		 required="true"
					 action="/municipios/manterRotaColetaEntrega" 
					 service="lms.municipios.manterRotaIntervaloCEPAction.findLookupRotaColetaEntrega" 
					 dataType="integer" size="3" maxLength="3" labelWidth="20%" width="80%" >
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="rotaColetaEntrega.filial.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="rotaColetaEntrega.filial.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:lookup>		

		<adsm:lookup service="lms.municipios.municipioService.findLookupMunicipio"  labelWidth="20%" dataType="text" property="municipio"
					 criteriaProperty="nmMunicipio" idProperty="idMunicipio" label="municipio" size="35" maxLength="50" width="35%"
					 action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="2" exactMatch="false" cellStyle="vertical-align:bottom;" required="true"
					 onPopupSetValue="municipioPopup" 
					 onDataLoadCallBack="municipioDataLoadExact" 
					 onchange="return municipioOnChange(this)">
				<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="municipioFiliais.filial.idFilial"/>
				<adsm:propertyMapping criteriaProperty="blAtendimentoTemporario" modelProperty="blAtendimentoTemporario"/>
				<adsm:hidden property="municipioFilial.dtVigenciaInicial"/>
				<adsm:hidden property="municipioFilial.dtVigenciaFinal"/>
		</adsm:lookup>
				
		<adsm:textarea property="dsBairro" label="bairro" rows="2" columns="60" maxLength="120"  labelWidth="20%" width="80%" />

        <adsm:textbox property="nrOrdemOperacao" required="true"  labelWidth="20%" width="30%" maxLength="3" size="3" label="ordemOperacao" dataType="integer" />
		
        <adsm:range label="cep" required="false" labelWidth="20%" width="30%">
             <adsm:textbox dataType="text" property="nrCepInicial" size="8" maxLength="8" onchange="return validaIntervaloCeps(this)"/>
             <adsm:textbox dataType="text" property="nrCepFinal" size="8" maxLength="8" onchange="return validaIntervaloCeps(this)"/>
        </adsm:range>
        <adsm:hidden property="intervaloCep"/>
        
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="cliente" 
			maxLength="20" 
			property="cliente" 
			service="lms.municipios.manterRotaIntervaloCEPAction.findLookupCliente" 
			size="20" 
			labelWidth="20%" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
				
			<adsm:hidden property="cliente.pessoa.nrIdentificacaoFormatado"/>
				
		</adsm:lookup>

		<adsm:combobox service="lms.municipios.manterRotaIntervaloCEPAction.findEnderecoByIdCliente" 
					   labelWidth="20%" 
					   width="30%" 
					   boxWidth="200"
					   required="false" 
					   optionProperty="idEnderecoPessoa" 
					   optionLabelProperty="dsEndereco" 
					   property="enderecoCliente.idEnderecoPessoa"
					   onDataLoadCallBack="setAdressIfUnique"
					   label="endereco">
			<adsm:propertyMapping criteriaProperty="cliente.idCliente" modelProperty="pessoa.idPessoa" />

		</adsm:combobox>
        
		<adsm:combobox property="tpGrauRisco" required="true" labelWidth="20%" width="30%" label="grauRisco" domain="DM_GRAU_RISCO" />	
		
		<adsm:combobox property="tipoDificuldadeAcesso.idTipoDificuldadeAcesso" labelWidth="20%" width="30%" boxWidth="210" label="dificuldadeAcesso"
				service="lms.municipios.manterRotaIntervaloCEPAction.findTipoDificuldadeAcesso"	onlyActiveValues="true"
				optionLabelProperty="dsTipoDificuldadeAcesso" optionProperty="idTipoDificuldadeAcesso" />

		<adsm:textbox dataType="JTTime" property="hrCorteSolicitacao" labelWidth="20%" cellStyle="vertical-align:bottom" label="horaCorteSolicitacaoColeta" required="true" width="30%"/>
		
		<adsm:textbox dataType="JTTime" property="hrCorteExecucao" labelWidth="20%" cellStyle="vertical-align:bottom" label="horaCorteParaColeta" width="30%"/>	

		<adsm:range label="vigencia" labelWidth="20%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
				
		<adsm:buttonBar>
			<adsm:button caption="horariosTransito" id="horariosTransito" action="municipios/manterHorariosRestricaoAcesso" cmd="main">
				<adsm:linkProperty src="rotaColetaEntrega.nrRota" target="rotaIntervaloCep.rotaColetaEntrega.nrRota"/>
				<adsm:linkProperty src="rotaColetaEntrega.filial.pessoa.nmFantasia" target="rotaIntervaloCep.rotaColetaEntrega.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="idRotaIntervaloCep" target="rotaIntervaloCep.idRotaIntervaloCep"/>
				<adsm:linkProperty src="intervaloCep" target="rotaIntervaloCep.intervaloCep"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacaoFormatado" target="cliente.pessoa.nrIdentificacaoFormatado"/>
			</adsm:button>
			<adsm:storeButton callbackProperty="storeRotaIntervalo"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idOperacaoServicoLocaliza" autoSearch="false" showGotoBox="true" onRowClick="rowClick"
				service="lms.municipios.manterRotaIntervaloCEPAction.findOperacaoServicoLocalizacao" 
				showPagging="false" property="operacaoServicoLocaliza" gridHeight="120" unique="true" scrollBars="vertical" selectionMode="none">
			<adsm:gridColumn title="tipoOperacao" property="tpOperacao" isDomain="true"/>
			<adsm:gridColumn title="servico" property="servico.dsServico" width="220" />	
			<adsm:gridColumn title="dom" property="blDomingo" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="seg" property="blSegunda" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="ter" property="blTerca" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="qua" property="blQuarta" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="qui" property="blQuinta" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="sex" property="blSexta" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="sab" property="blSabado" width="30" renderMode="image-check"/> 
			<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate"/> 
			<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="90" dataType="JTDate"/> 

	</adsm:grid>
</adsm:window>   
<script>
		
	function rowClick() {
		return false;
	}
	
	function loadGridOperacaoServico(idMunicipio){
		
		var idFilial = getElementValue("rotaColetaEntrega.filial.idFilial");

		if (idMunicipio != undefined && idMunicipio != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "municipioFilial.municipio.idMunicipio", idMunicipio);
			setNestedBeanPropertyValue(data, "municipioFilial.filial.idFilial", idFilial);
			operacaoServicoLocalizaGridDef.executeSearch(data);
		} else 
			operacaoServicoLocalizaGridDef.resetGrid();
	}
	
	function rotaIntervaloCepPageLoad_cb(data){
		onPageLoad_cb(data);

		if (!document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink == "true" || document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink == undefined) {
			var sdo = createServiceDataObject("lms.municipios.manterRotaIntervaloCEPAction.findDataSession","dataSession",null);
			xmit({serviceDataObjects:[sdo]});
		}
	
	}
	
	function municipioOnChange(obj){
		var retorno = municipio_nmMunicipioOnChangeHandler();
		if (obj.value == ''){
			loadGridOperacaoServico();
		}
		
		return retorno;
	}
	
	function municipioDataLoadExact_cb(data){
		lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:"municipioDataLoadLikeEnd"});
      	
      	if (data != undefined){
      		loadGridOperacaoServico(getNestedBeanPropertyValue(data, ":0.idMunicipio"));
      	}
        
    }
    
    function municipioDataLoadLikeEnd_cb(data){
		municipio_nmMunicipio_likeEndMatch_cb(data);
        if (data != undefined){
      		loadGridOperacaoServico(getNestedBeanPropertyValue(data, ":0.idMunicipio"));
      	}
    }
	
	function municipioPopup(data){

        if (data != undefined){
      		loadGridOperacaoServico(getNestedBeanPropertyValue(data, "idMunicipio"));
      	}
      	
      	return true;
    }
    
	function storeRotaIntervalo_cb(data, error){
		store_cb(data, error);
		
		if (error == undefined && data != undefined){			
            comportamentoVigencia(data);      
            setFocusOnNewButton();
		}
	}
	
	function rotaIntervaloCepDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		
		if(getElementValue("idRotaIntervaloCep")== "" )
			document.getElementById("enderecoCliente.idEnderecoPessoa").selectedIndex = 1;
		else
			setElementValue("enderecoCliente.idEnderecoPessoa", getNestedBeanPropertyValue(data, "enderecoCliente.idEnderecoPessoa"));	
			
		loadGridOperacaoServico(getNestedBeanPropertyValue(data,"municipio.idMunicipio"));
		comportamentoVigencia(data);							
		focoVigencia(data.acaoVigenciaAtual);
		
	}
		

	function estadoNovo(){
	
		setDisabled("blAtendimentoTemporario", false);
		setDisabled("rotaColetaEntrega.filial.idFilial", false);
		setDisabled("rotaColetaEntrega.idRotaColetaEntrega", false);
		setDisabled("cliente.idCliente", false);	
		setDisabled("enderecoCliente.idEnderecoPessoa", false);	
	
		setDisabled("municipio.idMunicipio", false);
		setDisabled("dsBairro", false);
		setDisabled("nrCepInicial", false);
		setDisabled("nrCepFinal", false);
		setDisabled("nrOrdemOperacao", false);
		setDisabled("tpGrauRisco", false);
		setDisabled("tipoDificuldadeAcesso.idTipoDificuldadeAcesso", false);
		setDisabled("hrCorteSolicitacao", false);
		setDisabled("hrCorteExecucao", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setFocusOnFirstFocusableField();
	}
	
	function focoVigencia(acaoVigenciaAtual) {
		if (acaoVigenciaAtual == 2) {
			setFocusOnNewButton();
		} else {
			setFocusOnFirstFocusableField();
		}
	}
	
	function comportamentoVigencia(data){
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
	
		// 0 = VIGENCIA INICIAL > HOJE
		// PODE TUDO
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
		
		// 1 = VIGENCIA INICIAL <= HOJE E 
		//     VIGENCIA FINAL   >= HOJE
		//     DESABILITA VIGENCIA INICIAL		 
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("__buttonBar:0.storeButton", false);
			setDisabled("dtVigenciaFinal", false);
			setDisabled("horariosTransito", false);
			
		// 2 = VIGENCIA INICIAL <= HOJE E 
		//     VIGENCIA FINAL   < HOJE
		//     DESABILITA TUDO, EXCETO O BOTÃO NOVO
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("horariosTransito", false);
		}
	}
	
	function initWindow(event){
		if ("tab_click" == event.name || "newButton_click" == event.name){
			writeDataSession();
			operacaoServicoLocalizaGridDef.resetGrid();	
			estadoNovo();	
		}
		if ("removeButton" == event.name){
			operacaoServicoLocalizaGridDef.resetGrid();	
		}
	}	

	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	function dataSession_cb(data) {		
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			
		writeDataSession();
	}

	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("rotaColetaEntrega.filial.idFilial",idFilial);
			setElementValue("rotaColetaEntrega.filial.sgFilial",sgFilial);
			setElementValue("rotaColetaEntrega.filial.pessoa.nmFantasia",nmFilial);
		}		
			
		setFocusOnFirstFocusableField(document);			
	}
	
	function validaIntervaloCeps(campo){
		var msg = "";
		var cepInicial = document.getElementById('nrCepInicial').value;
		var cepFinal = document.getElementById('nrCepFinal').value;	
		
		if ((cepInicial != '' && cepFinal != '') &&
			cepInicial > cepFinal){
			alert('Valor final menor que valor inicial!');
			setFocus(campo);
			return false;								
		} else return true;
	}

	function setAdressIfUnique_cb(data) {
		enderecoCliente_idEnderecoPessoa_cb(data);
		if (data != undefined && data.length == 1)
			document.getElementById("enderecoCliente.idEnderecoPessoa").selectedIndex = 1;
	}
</script>