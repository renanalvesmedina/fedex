<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirDivergenciasLMSCorporativo">

		<adsm:hidden property="sgFilial"/>
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirDivergenciasLMSCorporativoAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="75%"
					 labelWidth="25%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			
			<adsm:textbox dataType="text" 
						  property="filial.pessoa.nmFantasia" 
						  size="60" 
						  maxLength="60" 
						  disabled="true" 
						  serializable="true"/>
						  
		</adsm:lookup>
		
		<adsm:textbox property="emissaoAte" 
					  label="posicaoAte" 
					  serializable="true" 
					  labelWidth="25%"
					  width="75%"
					  dataType="JTDate"
					  disabled="true"/>
		
		<adsm:combobox  property="tpFormatoRelatorio" 
						required="true"
						defaultValue="pdf"
						label="formatoRelatorio" 
						domain="DM_FORMATO_RELATORIO"
						labelWidth="25%" width="75%"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirDivergenciasLMSCorporativoAction" disabled="false"/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
		</adsm:buttonBar>
	</adsm:form>
	
</adsm:window>

<script>

	//chamado no inicio da tela
	function initWindow(evento){
		setDisabled('btnLimpar', false);
	}


	/**
	* Function que limpa os campos da tela e desabilita todos os campos.
	*
	* chamado por: botão limpar
	*/
	function limpar(){
		cleanButtonScript();
		valoresDefault();
	}


function myOnPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data, erro);
	valoresDefault();
}


/* Função que busca os valores padrão da tela */
function valoresDefault(){
	_serviceDataObjects = new Array();
	
	// Busca a filial do usuário logado
	addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirDivergenciasLMSCorporativoAction.findFilialUsuario",
		"setFilialUsuario", 
		new Array()));
		
	// Busca a data atual menos um dia
	addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirDivergenciasLMSCorporativoAction.getDataAtualMenosUmDia",
		"setDataAtualMenosUmdia", 
		new Array()));

	xmit(false);	

}


function setFilialUsuario_cb(data, error) {
	setElementValue('filial.idFilial', data.idFilial);
	setElementValue('filial.sgFilial', data.sgFilial);
	setElementValue('sgFilial', data.sgFilial);
	setElementValue('filial.pessoa.nmFantasia', data.nmFantasia);
	
	if (data.blDisableFilial == "true"){
		setDisabled("filial.idFilial", true);
	} else {
		setDisabled("filial.idFilial", false);
	}
}

function setDataAtualMenosUmdia_cb(data, error){
	setElementValue("emissaoAte", setFormat(getElement("emissaoAte"), data.emissaoAte));
}

</script>