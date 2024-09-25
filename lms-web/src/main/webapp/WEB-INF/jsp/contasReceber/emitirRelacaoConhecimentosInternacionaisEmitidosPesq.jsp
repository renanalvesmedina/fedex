<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/contasReceber/emitirRelacaoConhecimentosInternacionaisEmitidos">

		<adsm:range label="emissao" labelWidth="17%" width="35%" required="true">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" />
		</adsm:range>

		<adsm:hidden property="sgFilialOrigem"/>
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialOrigem" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialOrigem" 
					 size="3" 
					 maxLength="3" 
					 width="28%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialOrigem.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilialOrigem"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="25" maxLength="25" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="sgFilialDestino"/>
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialDestino" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialDestino" 
					 size="3" 
					 maxLength="3" 
					 width="35%"
					 labelWidth="17%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialDestino.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilialDestino"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="25" maxLength="25" disabled="true" serializable="true"/>
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
					 width="28%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialCobranca.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilialCobranca"/>
			<adsm:textbox dataType="text" property="filialCobranca.pessoa.nmFantasia" size="25" maxLength="25" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:lookup label="clienteRemetente"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupClientes" 
					 dataType="text"
					 property="clienteRemetente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="17"
					 maxLength="20" 
					 onDataLoadCallBack="formataRemetenteOnDataLoad"
					 width="83%"
					 serializable="true"
					 labelWidth="17%"
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="clienteRemetente.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="clienteRemetente.pessoa.nmPessoa" disabled="true" size="60"/>
		</adsm:lookup>

		<adsm:lookup label="clienteDestinatario"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupClientes" 
					 dataType="text"
					 property="clienteDestinatario" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="17"
					 maxLength="20" 
					 onDataLoadCallBack="formataDestinatarioOnDataLoad"
					 width="83%"
					 serializable="true"
					 labelWidth="17%"
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="clienteDestinatario.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="clienteDestinatario.pessoa.nmPessoa" disabled="true" size="60"/>
		</adsm:lookup>

		<adsm:hidden property="moeda.dsSimbolo" serializable="true"/>

		<adsm:combobox property="moeda.idMoeda" label="moedaExibicao" optionProperty="idMoeda" 
			optionLabelProperty="siglaSimbolo"
			service="lms.contasreceber.emitirRelacaoContaFreteAction.findMoedaPaisCombo" 
			labelWidth="17%"
			width="35%" boxWidth="80" required="true" serializable="true"
			onDataLoadCallBack="setMoedaPadrao">
			<adsm:propertyMapping relatedProperty="moeda.dsSimbolo" modelProperty="siglaSimbolo"/>			
		</adsm:combobox>

        <adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="20%"
    				   width="28%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRelacaoConhecimentosInternacionaisEmitidosAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script>
	function formataRemetenteOnDataLoad_cb(data, error) {
		clienteRemetente_pessoa_nrIdentificacao_exactMatch_cb(data);

		if (error == null && data[0] != null) {
			setElementValue("clienteRemetente.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
			setElementValue("clienteRemetente.pessoa.nmPessoa", data[0].pessoa.nmPessoa);
		}
	}
	
	function formataDestinatarioOnDataLoad_cb(data, error) {
		clienteDestinatario_pessoa_nrIdentificacao_exactMatch_cb(data);		

		if (error == null && data[0] != null) {
			setElementValue("clienteDestinatario.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
			setElementValue("clienteDestinatario.pessoa.nmPessoa", data[0].pessoa.nmPessoa);
		}
	}
	
	function initWindow(eventObj){
	
		if(eventObj.name == 'tab_load' || eventObj.name == 'cleanButton_click') {
			
			_serviceDataObjects = new Array();
			
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacaoConhecimentosInternacionaisEmitidosAction.findMoedaUsuario",
				"setMoedaUsuario", 
				new Array()));
			
	        xmit(false);

			_serviceDataObjects = new Array();
	     
	   }
	        
	}
	
	function setMoedaUsuario_cb(data, error) {
		setElementValue('moeda.idMoeda', data.idMoeda);
		setElementValue('moeda.dsSimbolo', data.siglaSimbolo);
	}
	
	function setMoedaPadrao_cb(data){

			moeda_idMoeda_cb(data);
			_serviceDataObjects = new Array();
			
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacaoConhecimentosInternacionaisEmitidosAction.findMoedaUsuario",
				"setMoedaUsuario", 
				new Array()));
		
	        xmit(false);

	}

</script>