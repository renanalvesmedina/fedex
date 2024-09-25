<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	
	/**
	 * Ao carregar dados na tela, há algumas regras de comportamento.
	 */
	function meioTranspMotLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		
		if (data != undefined) {
			comportamentoDetalhe(data);
		}	
	}
	
	function comportamentoDetalhe(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			desabilitarMTAndMot();
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

<adsm:window service="lms.contratacaoveiculos.meioTranspRodoMotoristaService" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporteRodoviariosMotorista" idProperty="idMeioTranspRodoMotorista"
			service="lms.contratacaoveiculos.meioTranspRodoMotoristaService.findByIdDetalhamento"
			onDataLoadCallBack="meioTranspMotLoad" >

		<adsm:hidden property="motorista.tpVinculo" />
		<adsm:hidden property="meioTransporteRodoviario.meioTransporte.tpVinculo" />

		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		
		<adsm:lookup
			dataType="text" 
			property="motorista" 
			idProperty="idMotorista"
			service="lms.contratacaoveiculos.motoristaService.findLookupMotorista"
			action="/contratacaoVeiculos/manterMotoristas" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="motorista" 
			labelWidth="23%" 
			width="77%" 
			size="20" 
			maxLength="20" 
			required="true"
			exactMatch="false"
			minLengthForAutoPopUpSearch="5">
			<adsm:propertyMapping criteriaProperty="motorista.tpVinculo" modelProperty="tpVinculo" />
			<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" 
					modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="35" disabled="true" serializable="false" />
		</adsm:lookup>
		 
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte"
				service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="23%" width="77%" size="8" serializable="false" maxLength="6" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
			
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte"
					service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="20" required="true" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:range label="vigencia" labelWidth="23%" width="60%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton id="salvar" callbackProperty="afterStore" service="lms.contratacaoveiculos.meioTranspRodoMotoristaService.storeMap" />
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
		setDisabled("motorista.pessoa.nmPessoa",true);
		setDisabled("excluir",true);
	}
	
	function desabilitarMTAndMot() {
		setDisabled("meioTransporteRodoviario.idMeioTransporte",true);
		setDisabled("motorista.idMotorista",true);
		setDisabled("meioTransporteRodoviario2.idMeioTransporte",true);
		setDisabled("motorista.pessoa.nmPessoa",true);
	}
	
	/**
	 * Ao detalhar, desabilitar Meio de Transporte e proprietário.
	 */
	function initWindow(obj) {
		if (obj.name != "gridRow_click" && obj.name != "storeButton") {
			estadoNovo();
			setDisabled("meioTransporteRodoviario.idMeioTransporte",document.getElementById("meioTransporteRodoviario.idMeioTransporte").masterLink);
			setDisabled("motorista.idMotorista",document.getElementById("motorista.idMotorista").masterLink);
			setDisabled("meioTransporteRodoviario2.idMeioTransporte",document.getElementById("meioTransporteRodoviario2.idMeioTransporte").masterLink);
			setFocusOnFirstFocusableField();
		} else {
			desabilitarMTAndMot();
		}
	}
	
	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		
		// Se vier da tela de Meio de Transporte com vínculo Próprio, só permite motorista Funcionário.
		document.getElementById("motorista.tpVinculo").masterLink = true;
		var tpVinculoMeioTransporte = getElementValue("meioTransporteRodoviario.meioTransporte.tpVinculo");
		if (tpVinculoMeioTransporte == 'P') {
			setElementValue("motorista.tpVinculo",'F');
		} else {
			resetValue("motorista.tpVinculo");
		}
	}
	
</script>
