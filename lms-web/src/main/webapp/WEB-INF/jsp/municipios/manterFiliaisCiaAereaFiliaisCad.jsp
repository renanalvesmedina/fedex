<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("filialCiaAerea.aeroporto.pessoa.nmPessoa",true);
		setDisabled("filialCiaAerea.pessoa.nmPessoa",true);
		setDisabled("ciaFilialMercurio.filial.pessoa.nmFantasia",true);
		setDisabled("ciaFilialMercurio.empresa.pessoa.nmPessoa",true);
		///setDisabled("ciaFilialMercurio.idCiaFilialMercurio",true); 
		setDisabled("ciaFilialMercurio.idCiaFilialMercurio",document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").masterLink);
		setDisabled("__buttonBar:0.removeButton",true);
		// setFocus(document.getElementById("filialCiaAerea.pessoa.nrIdentificacao"));
		if (document.getElementById("filialCiaAerea.pessoa.nrIdentificacao").masterLink == "true") {
			setDisabled("filialCiaAerea.idFilialCiaAerea",true);
			setDisabled("filialCiaAerea.pessoa.nrIdentificacao",true);
			//setElementValue(document.getElementById("ciaFilialMercurio.empresa.idEmpresa"),document.getElementById("filialCiaAerea.empresa.idEmpresa").value);
			// setFocus(document.getElementById("ciaFilialMercurio.idCiaFilialMercurio"));
		}
	}
	
	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function pageLoad_cb(data,exception) {
	   	onDataLoad_cb(data,exception);	
		comportamentoDetalhe(data);
	}
		
</script>
<adsm:window service="lms.municipios.filialMercurioFilialCiaService" >
	<adsm:form action="/municipios/manterFiliaisCiaAereaFiliais" idProperty="idFilialMercurioFilialCia"
			onDataLoadCallBack="pageLoad" service="lms.municipios.filialMercurioFilialCiaService.findByIdDetalhamento" >
		<adsm:hidden property="flag" serializable="false" value="01"/>

		<adsm:lookup dataType="text" property="filialCiaAerea" service="lms.municipios.filialCiaAereaService.findLookup"
				action="/municipios/manterFiliaisCiaAerea" idProperty="idFilialCiaAerea" criteriaProperty="pessoa.nrIdentificacao" 
				relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				onchange="return filialCiaAereaChange();"
				label="filialCiaAerea2" size="20" maxLength="20" width="40%" exactMatch="true" required="true" >
				
			<adsm:propertyMapping relatedProperty="filialCiaAerea.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="filialCiaAerea.aeroporto.idAeroporto" modelProperty="aeroporto.idAeroporto"/>
			<adsm:propertyMapping relatedProperty="filialCiaAerea.aeroporto.sgAeroporto" modelProperty="aeroporto.sgAeroporto"/>
			<adsm:propertyMapping relatedProperty="filialCiaAerea.aeroporto.pessoa.nmPessoa" modelProperty="aeroporto.pessoa.nmPessoa"/>
	
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.empresa.idEmpresa"
					modelProperty="empresa.idEmpresa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.empresa.pessoa.nmPessoa"
					modelProperty="empresa.pessoa.nmPessoa" blankFill="false" />
	
			<adsm:propertyMapping criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtiva" modelProperty="empresa.tpSituacao"/>
	
			<adsm:propertyMapping criteriaProperty="filialCiaAerea.aeroporto.idAeroporto" modelProperty="aeroporto.idAeroporto" />
			<adsm:propertyMapping criteriaProperty="filialCiaAerea.aeroporto.sgAeroporto" modelProperty="aeroporto.sgAeroporto" />
			<adsm:propertyMapping criteriaProperty="filialCiaAerea.aeroporto.pessoa.nmPessoa" modelProperty="aeroporto.pessoa.nmPessoa" />
	    	
	    	<adsm:textbox dataType="text" property="filialCiaAerea.pessoa.nmPessoa" size="20" disabled="true" serializable="false" />
	    </adsm:lookup>
	    
		<adsm:textbox dataType="text" property="ciaFilialMercurio.empresa.pessoa.nmPessoa" label="ciaAerea" size="35" disabled="true" serializable="false" width="30%"/>				
		<adsm:hidden property="ciaFilialMercurio.empresa.idEmpresa" />
		<adsm:hidden property="filialCiaAerea.empresa.idEmpresa" />		
				
		<adsm:hidden property="filialCiaAerea.aeroporto.idAeroporto" />
		<adsm:hidden property="filialCiaAerea.aeroporto.sgAeroporto" />
		
		<adsm:textbox dataType="text" property="filialCiaAerea.aeroporto.pessoa.nmPessoa"
				label="aeroporto" size="37" disabled="true" serializable="false" width="85%"/>
				
		<adsm:lookup dataType="text" property="ciaFilialMercurio" service="lms.municipios.ciaFilialMercurioService.findLookup"
				action="/municipios/manterCiasAereasFiliais" idProperty="idCiaFilialMercurio" criteriaProperty="filial.sgFilial" disabled="false"
				label="filial" size="3" maxLength="3" width="85%" minLengthForAutoPopUpSearch="3" exactMatch="true" required="true"
				onchange="return ciaFilialMercurioChange();" >
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.empresa.idEmpresa"
					modelProperty="empresa.idEmpresa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.empresa.pessoa.nmPessoa"
					modelProperty="empresa.pessoa.nmPessoa" blankFill="false" />
			<adsm:hidden property="ciaFilialMercurio.filial.idFilial"/>

			<adsm:propertyMapping criteriaProperty="tpSituacaoAtiva" modelProperty="empresa.tpSituacao"/>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
	    	<adsm:textbox dataType="text" property="ciaFilialMercurio.filial.pessoa.nmFantasia"
					size="30" disabled="true" serializable="false" />
	    </adsm:lookup>
		<adsm:hidden property="tpSituacaoAtiva" value="A" serializable="false" />
		
		<adsm:textbox dataType="integer" property="nrOrdemUso" label="ordemUsoAeroporto" mask="##"
				size="10" maxLength="2" labelWidth="15%" width="85%" cellStyle="vertical-align:bottom" />
				
		<adsm:range label="vigencia" labelWidth="15%" width="35%" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        <adsm:buttonBar>
			<adsm:storeButton id="salvar" 
					service="lms.municipios.filialMercurioFilialCiaService.storeMap" callbackProperty="afterStore" />
			<adsm:newButton id="__botaoNovo" />
			<adsm:removeButton id="excluir" />
		</adsm:buttonBar> 
	</adsm:form>
</adsm:window>
<script>

	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton")  {
			estadoNovo();
			setFocusOnFirstFocusableField(document);
		}			
	}
	
	/*onchange*/
	function filiaCiaAereaChange() {
		/*	
		var flag = filialCiaAerea_pessoa_nrIdentificacaoOnChangeHandler();
			if (getElementValue("filialCiaAerea.pessoa.nrIdentificacao") == "")
				disableFilial();
			return flag;
		*/
	}
	
	function disableFilial() {
		resetValue("ciaFilialMercurio.idCiaFilialMercurio");
		setDisabled("ciaFilialMercurio.idCiaFilialMercurio",true);
	}
	
	/*filiaCiaAereaLookup*/
	function filiaCiaAereaLookup_cb(data, exception) {
		alert ('filiaCiaAereaLookup');
		
		var isFilialCiaAereaEmpty = (getNestedBeanPropertyValue(data,":0.idFilialCiaAerea") == undefined);
		if (isFilialCiaAereaEmpty)
			document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").value = "";
		if(document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").masterLink == "false")	
			setDisabled("ciaFilialMercurio.idCiaFilialMercurio",isFilialCiaAereaEmpty);
		
		if (data != undefined && data.length == 1) {
			var nrFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrFormatado);
		}
		
		return lookupExactMatch({e:document.getElementById("filialCiaAerea.idFilialCiaAerea"), data:data, callBack:'setaNrIdentificacaoLikeEnd'});
	}
	
	function setaNrIdentificacaoLikeEnd_cb(data) {
	    if (data != undefined && data.length == 1) {
			var nrFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrFormatado);
		}
		
		return empresa_pessoa_nrIdentificacao_likeEndMatch_cb(data);
	}	
	
	/*onPopupSetValue*/
	function filiaCiaAereaPopup(data) {
		/*
		alert ('filiaCiaAereaPopup');	

		if (getNestedBeanPropertyValue(data,"idFilialCiaAerea") != getElementValue("filialCiaAerea.idFilialCiaAerea"))			
			disableFilial();*/
		return true;
	} 
		
	function comportamentoDetalhe(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("__buttonBar:0.removeButton",false);
			setFocusOnFirstFocusableField(document);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setDisabled("salvar",false);
			setDisabled("dtVigenciaFinal",false);
			setDisabled("nrOrdemUso",false);
			setFocusOnFirstFocusableField(document);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setFocusOnNewButton();
		}
	}
	
	function afterStore_cb(data,error) {
		store_cb(data,error);
		if (error == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
	}
	
	function limpaCiaAerea(){	
		setElementValue(document.getElementById("ciaFilialMercurio.empresa.idEmpresa"),"");
		setElementValue(document.getElementById("ciaFilialMercurio.empresa.pessoa.nmPessoa"),"");
	}
	
	function validaLookups() {
		if (getElementValue("filialCiaAerea.pessoa.nrIdentificacao") == "" && 
				getElementValue("ciaFilialMercurio.filial.sgFilial") == "")
			limpaCiaAerea();
	}
	
	function filialCiaAereaChange() {
		validaLookups();
		return filialCiaAerea_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function ciaFilialMercurioChange() {
		validaLookups();
		return ciaFilialMercurio_filial_sgFilialOnChangeHandler()
	}
	
</script>