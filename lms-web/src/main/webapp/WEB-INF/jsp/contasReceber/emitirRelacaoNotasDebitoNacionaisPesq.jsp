<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.emitirRelacaoNotasDebitoNacionaisAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirRelacaoNotasDebitoNacionais">

		<adsm:hidden property="filialFaturamento.siglaFilial" serializable="true"/>
		<adsm:hidden property="filialFaturamento.pessoa.nmPessoa" serializable="true"/>
		
        <adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.emitirRelacaoNotasDebitoNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialFaturamento" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialFaturamento" 
					 size="3" 
					 maxLength="3" 
					 labelWidth="20%"
					 width="80%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialFaturamento.nmFilial"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialFaturamento.pessoa.nmPessoa"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="filialFaturamento.siglaFilial"/>
			
			<adsm:textbox dataType="text" property="filialFaturamento.nmFilial" size="50" maxLength="50" disabled="true" serializable="false"/>
			
		</adsm:lookup>

		<adsm:hidden property="filialCobranca.siglaFilial" serializable="true"/>
		
		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.emitirRelacaoNotasDebitoNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialCobranca" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label = "filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="80%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialCobranca.pessoa.nmPessoa"/>			
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="filialCobranca.siglaFilial"/>
			
			<adsm:textbox dataType="text" property="filialCobranca.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
			
		</adsm:lookup>

		<adsm:range label="periodoEmissao" labelWidth="20%" width="30%">
			<adsm:textbox property="emissaoIni" dataType="JTDate"/>
			<adsm:textbox property="emissaoFim" dataType="JTDate"/>
		</adsm:range>
		
		<adsm:combobox label="situacao" 
					   property="situacaoCobranca" 
					   service="" 
					   domain="DM_STATUS_RECIBO_FRETE"
					   labelWidth="20%"
					   width="30%"/>
					   
		<adsm:combobox property="tpSituacaoCobranca" 
					   label="estadoCobranca" 
					   labelWidth="20%"
  					   width="30%" 
					   domain="DM_STATUS_COBRANCA_DOCTO_SERVICO"/>					   
			
		<adsm:combobox property="tpFormatoRelatorio" 
					   required="true"
					   label="formatoRelatorio" 
					   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"
					   labelWidth="20%" width="30%"/>
			

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRelacaoNotasDebitoNacionaisAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		retornoFindFilialUsuarioLogado();
	}
	
	function initWindow(e){
		if (e.name == "cleanButton_click"){
			retornoFindFilialUsuarioLogado();
		}
	}
	
	function retornoFindFilialUsuarioLogado(){
		_serviceDataObjects = new Array();
		var dados = new Array();	
        addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacaoNotasDebitoNacionaisAction.findFilialUsuarioLogado",
			"retornoFindFilialUsuarioLogado", 
			dados));

        xmit(false);
	}
	
	function retornoFindFilialUsuarioLogado_cb(data,error){
		
		if (error != undefined){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		setElementValue("filialCobranca.siglaFilial", data.filialCobranca.sgFilial);
		setElementValue("filialCobranca.sgFilial", data.filialCobranca.sgFilial);
		setElementValue("filialCobranca.idFilial", data.filialCobranca.idFilial);
		setElementValue("filialCobranca.pessoa.nmPessoa", data.filialCobranca.pessoa.nmPessoa);
		
	}
</script>