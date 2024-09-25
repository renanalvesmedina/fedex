<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
	
	/**
	 * Ao carregar dados na tela, há algumas regras de comportamento.
	 */
	function meioTranspPropLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		
		if (data != undefined) {		
			comportamentoDetalhe(data);
		}
	}
	
	function comportamentoDetalhe(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			desabilitarMTAndProp();
			setDisabled("excluir",false);
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 1) {
		    setDisabled(document,true);
		    setDisabled("dtVigenciaFinal",false);
		    setDisabled("novo",false);
		    setDisabled("salvar",false);
		    setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 2) {
		    setDisabled(document,true);
		    setDisabled("novo",false);
		    setFocusOnNewButton();
		}
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
    }			
</script>

<adsm:window service="lms.contratacaoveiculos.meioTranspProprietarioService" >
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporteProprietarios" idProperty="idMeioTranspProprietario"
			service="lms.contratacaoveiculos.meioTranspProprietarioService.findByIdDetalhamento"
			onDataLoadCallBack="meioTranspPropLoad" >
			
		<adsm:hidden property="dontFillFilial" value="true" />
		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>
		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
				service="lms.contratacaoveiculos.proprietarioService.findLookup"
				action="/contratacaoVeiculos/manterProprietarios" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				label="proprietario" labelWidth="15%" width="19%" size="20" maxLength="20" 
				onDataLoadCallBack="proprietarioDataLoad" onPopupSetValue="proprietarioPopup" required="true">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping criteriaProperty="dontFillFilial" modelProperty="dontFillFilial" />
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="50" width="55%" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte"
				service="lms.contratacaoveiculos.meioTransporteService.findLookup" picker="false" maxLength="6"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota"
				label="meioTransporte" labelWidth="15%" width="85%" size="8" serializable="false" >
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
					service="lms.contratacaoveiculos.meioTransporteService.findLookup" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
					size="20" required="true" >
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />	
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			</adsm:lookup>			
		</adsm:lookup>
		
		
					

		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:buttonBar>
			<adsm:storeButton id="salvar" callbackProperty="afterStore" service="lms.contratacaoveiculos.meioTranspProprietarioService.storeMap" />
			<adsm:newButton id="novo" />
			<adsm:removeButton id="excluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("excluir",true);
		setDisabled("proprietario.pessoa.nmPessoa",true);
	}
	
	function desabilitarMTAndProp() {
		setDisabled("meioTransporte.idMeioTransporte",true);
		setDisabled("proprietario.idProprietario",true);
		setDisabled("meioTransporte2.idMeioTransporte",true);
	}	

	/**
	 * Ao detalhar, desabilitar Meio de Transporte e proprietário.
	 */
	function initWindow(obj) {
		if (obj.name != "gridRow_click" && obj.name != "storeButton" ) {
			estadoNovo();
			setDisabled("meioTransporte.idMeioTransporte",document.getElementById("meioTransporte.idMeioTransporte").masterLink);
			setDisabled("proprietario.idProprietario",document.getElementById("proprietario.idProprietario").masterLink);
			setDisabled("meioTransporte2.idMeioTransporte",document.getElementById("meioTransporte2.idMeioTransporte").masterLink);
			setFocusOnFirstFocusableField();
		} else {
			desabilitarMTAndProp();
		}		
	}

	/**
	 * Formata o campo Nr Identificacao no retorno da lookup
	 */
	function proprietarioDataLoad_cb(data){
		proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setElementValue("proprietario.pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}
	
	/**
	 * Formata o campo Nr Identificacao no retorno da lookup
	 */
	function proprietarioPopup(data){
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}

</script>  