<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/contasReceber/emitirRelacoesCobrancas">

		<adsm:hidden property="sgFilial"/>

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="40%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:textbox label="numeroRelacao" dataType="integer" property="nrRelacaoCobranca" size="10" maxLength="10" labelWidth="25%" width="15%"/>
        
        <adsm:checkbox property="soTotais" label="soTotais" labelWidth="20%" width="40%" />
        
        <adsm:checkbox property="listarDocumentosServicos" label="listarDocumentosServicos" labelWidth="25%" width="15%" />

		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="20%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRelacoesCobrancasAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script>
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		findFilialSessao();
	}

	function initWindow(){
		findFilialSessao();
	}
	
	function findFilialSessao(){
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacoesCobrancasAction.findFilialSessao", "findFilialSessao", null)); 
		xmit(false);
	}

	function findFilialSessao_cb(d,e,o,x){
		if (e == undefined) {	
			onDataLoad_cb(d,e,o,x);			
		}
	}	
</script>