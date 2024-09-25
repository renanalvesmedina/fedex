<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/contasReceber/emitirRelacaoDocumentosFaturados">

		<adsm:hidden property="sgFilialFaturamento"/>
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialFaturamento" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialFaturamento" 
					 size="3" 
					 maxLength="3" 
					 width="33%"
					 labelWidth="17%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialFaturamento.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilialFaturamento"/>
			<adsm:textbox dataType="text" property="filialFaturamento.pessoa.nmFantasia" size="25" maxLength="25" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="sgFilialCobranca"/>
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialCobranca" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="35%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialCobranca.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilialCobranca"/>
			<adsm:textbox dataType="text" property="filialCobranca.pessoa.nmFantasia" size="25" maxLength="25" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:combobox label="tipo" property="tipo" domain="DM_TIPO_DOCUMENTO_PENDENTE" labelWidth="17%" width="33%"/>

		<adsm:range label="dataEmissao" width="35%">
			<adsm:textbox property="dtEmissaoInicial" dataType="JTDate" />
			<adsm:textbox property="dtEmissaoFinal" dataType="JTDate" />
		</adsm:range>

		<adsm:combobox label="situacao" property="situacao" domain="DM_SITUACAO_DOCUMENTO_PENDENTE" defaultValue="T" labelWidth="17%" width="33%" required="true"/>

		<adsm:hidden property="moeda.dsSimbolo" serializable="true"/>

		<adsm:combobox property="moeda.idMoeda" label="moedaExibicao" optionProperty="idMoeda" 
			optionLabelProperty="siglaSimbolo"
			service="lms.contasreceber.emitirRelacaoContaFreteAction.findMoedaPaisCombo" 
			boxWidth="85" required="true" serializable="true" onchange="atualizarDsSimbolo();">	
		</adsm:combobox>

		<adsm:hidden property="moeda.dsSimbolo"/>

		<adsm:lookup label="cliente"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupClientes" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 width="80%"
					 serializable="true"
					 labelWidth="17%"
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="50"/>
		</adsm:lookup>

		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="17%"
    				   width="28%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRelacaoDocumentosFaturadosAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>
<script>

	function myOnPageLoad_cb(d,e){
		onPageLoad_cb(d,e);
		
		getMoedaSessao();
	}
	
	function initWindow(){
		getMoedaSessao();
		atualizarDsSimbolo();
	}
	
	function atualizarDsSimbolo(){
		setElementValue("moeda.dsSimbolo", document.getElementById("moeda.idMoeda").options[document.getElementById("moeda.idMoeda").options.selectedIndex].text);
	}

	function getMoedaSessao(){	
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacaoDocumentosFaturadosAction.findMoedaSessao", "getMoedaSessao"));
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacaoDocumentosFaturadosAction.findFilialSessao", "onDataLoad"));
		xmit(false);
	}
	
	function getMoedaSessao_cb(d,e){
		if (e == undefined){
			setElementValue("moeda.idMoeda", d.moeda.idMoeda);
			setElementValue("moeda.dsSimbolo", d.moeda.siglaSimbolo);
		}
	}
</script>