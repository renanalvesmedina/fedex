<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="onPageLoadCustom" >
	<adsm:form action="/freteCarreteiroColetaEntrega/emitirFretesPagosCarreteiro">
		 <adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="periodoPagamento"/>
			<adsm:include key="periodoEmissao"/>
		</adsm:i18nLabels>
		
		
		<adsm:hidden property="filialSigla" serializable="true"/>		
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" label="filial" 
				size="3" maxLength="3" width="100%" action="/municipios/manterFiliais" 
				service="lms.fretecarreteirocoletaentrega.emitirFretesPagosCarreteiroAction.findLookupFilial" 
				labelWidth="20%">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="filialSigla" modelProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
		<adsm:lookup service="lms.fretecarreteirocoletaentrega.emitirFretesPagosCarreteiroAction.findLookupProprietario"
				idProperty="idProprietario" 
                property="proprietario" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
                label="proprietario" size="21" maxLength="20" 
				width="80%" labelWidth="20%" action="/contratacaoVeiculos/manterProprietarios" dataType="text" required="false">
	         <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	         <adsm:propertyMapping relatedProperty="proprietarioNrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
	         <adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" maxLength="60" disabled="true"/>
	         <adsm:hidden property="proprietarioNrIdentificacao" serializable="true"/>
	    </adsm:lookup>
        
		<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" 
				idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrFrota"
				service="lms.fretecarreteirocoletaentrega.emitirFretesPagosCarreteiroAction.findLookupMeioTransporteRodoviario" 
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" 
				label="meioTransporte" labelWidth="20%" width="80%" size="8" maxLength="6"
				cellStyle="vertical-Align:bottom" picker="false" >
				
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="true" />
		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />		
			<adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
			
			<adsm:lookup dataType="text" property="meioTransporteRodoviario2"
					idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrIdentificador"
					service="lms.fretecarreteirocoletaentrega.emitirFretesPagosCarreteiroAction.findLookupMeioTransporteRodoviario"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo"
					serializable="false" 
					size="20" maxLength="25" picker="true" cellStyle="vertical-Align:bottom" >
				
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />		
				<adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
				
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" />
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="true" />
			   		
	        </adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="identificacaoMeioTransporte" serializable="true"/>
		<adsm:hidden property="identificacaoMeioTransporte" serializable="true"/>
		<adsm:hidden property="meioTransporteNrFrota" serializable="true"/>	
		<adsm:hidden property="meioTransporteNrFrota" serializable="true"/>
		<adsm:hidden property="tpMeioTransporte" value="R" serializable="false"/>
		
		
		<adsm:combobox onchange="setaDescricaoTpRecibo(this)" property="tpSituacaoRecibo" domain="DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE" label="situacaoRecibo" labelWidth="20%" width="80%"/>
		<adsm:hidden property="descricaoTpRecibo"/>
		
		<adsm:range label="periodoEmissao" labelWidth="20%" width="80%" >
			<adsm:textbox dataType="JTDate" property="periodoEmissaoInicial"/> 
			<adsm:textbox dataType="JTDate" property="periodoEmissaoFinal"/>
		</adsm:range>
		<adsm:range label="periodoPagamento" labelWidth="20%" width="80%" >
			<adsm:textbox dataType="JTDate" property="periodoPagamentoInicial"/> 
			<adsm:textbox dataType="JTDate" property="periodoPagamentoFinal"/>
		</adsm:range>
		
		<adsm:combobox required="true" property="moedaPais.idMoedaPais" optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" label="converterParaMoeda" service="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findMoedaPais" labelWidth="20%" width="80%" >
			<adsm:propertyMapping relatedProperty="descricaoMoeda" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="idMoedaDestino" modelProperty="moeda.idMoeda"/>
			<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="pais.idPais" />
		</adsm:combobox>
		
		<adsm:hidden property="descricaoMoeda" serializable="true"/>	
		<adsm:hidden property="idPaisDestino" serializable="true"/>
		<adsm:hidden property="idMoedaDestino" serializable="true"/>	
		<adsm:hidden property="dsSimbolo" serializable="true"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="20%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.fretecarreteirocoletaentrega.emitirFretesPagosCarreteiroAction" />
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script>

	function validateTab() {
		if (validateTabScript(document.forms)) {
			if ((getElementValue("periodoPagamentoInicial") != "" || getElementValue("periodoPagamentoFinal") != "")||
					(getElementValue("periodoEmissaoInicial") != "" || getElementValue("periodoEmissaoFinal") != "")) 
				return true;
			else
				alert(i18NLabel.getLabel("LMS-00013") + '' + i18NLabel.getLabel("periodoEmissao") + ', ' + i18NLabel.getLabel("periodoPagamento")+'.');	
		}
		return false;			
	}

	function onPageLoadCustom_cb(data,exception) {
		onPageLoad_cb(data,exception);
		if (exception == undefined) {
			findInfoUsuario();
		}
	}

	function setaDescricaoTpRecibo(field){
		if(getElementValue("tpSituacaoRecibo") != "" && getElementValue("tpSituacaoRecibo") != null){
        	var descricao = document.getElementById("tpSituacaoRecibo").options[document.getElementById("tpSituacaoRecibo").selectedIndex].text;
        	setElementValue("descricaoTpRecibo",descricao);
		}
		
	}

	function findInfoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(
	   		createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirFretesPagosCarreteiroAction.findInfoUsuarioLogado",
	   			"findInfoUsuario",
	   			""));
	  	xmit();
	}
	
	// Variável global para não precisar encontrar a filial do usuário toda vez que clicar no botão limpar.
	var dadosUsuario = undefined;
	function findInfoUsuario_cb(data, exception){
		// se dados foram retornados e nenhuma excessão foi gerada
		if (data != undefined && exception == undefined) {
			dadosUsuario = data;
			setaInformacoesUsuario(dadosUsuario)
		}
	}
	
	function setaInformacoesUsuario(data) {
		if(data != undefined){
			// dados de filial
			setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filialSigla", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
			setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
			
			// dados de moeda
			setElementValue("moedaPais.idMoedaPais", data.idMoedaPais);
			setElementValue("idMoedaDestino", data.idMoeda);
			setElementValue("dsSimbolo", data.dsSimbolo);
			setElementValue("descricaoMoeda", data.siglaSimbolo);
			setElementValue("idPaisDestino",data.idPais);
		}
	}

	/* Ocorre quando acessa a tela ou quando clica-se no botão limpar.
	 */
	function initWindow(eventObj){
		setaInformacoesUsuario(dadosUsuario);
	}

</script>