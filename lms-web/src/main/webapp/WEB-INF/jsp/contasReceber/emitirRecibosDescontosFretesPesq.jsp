<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirRecibosDescontosFretes">

		<adsm:hidden property="sgFilial"/>
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirRecibosDescontosFretesAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 required="true"
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="80%"
					 labelWidth="20%"
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

		<adsm:textbox label="numeroRecibo" dataType="integer" property="numeroRecibo" size="10" maxLength="10" 
				labelWidth="20%" width="30%"/>
		
		<adsm:combobox  property="tpFormatoRelatorio" 
						required="true"
						defaultValue="pdf"
						label="formatoRelatorio" 
						domain="DM_FORMATO_RELATORIO"
						labelWidth="20%" width="30%"/>
						
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRecibosDescontosFretesAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	
	</adsm:form>
	
</adsm:window>

<script>

function myOnPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data, erro);
	
	setFilialUsuario();
}

function initWindow(eventObj){
	if (eventObj.name == "cleanButton_click"){
		setFilialUsuario();
	}
}

function setFilialUsuario(){
	setElementValue("tpFormatoRelatorio", "pdf");
	
	_serviceDataObjects = new Array();

	addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRecibosDescontosFretesAction.findFilialUsuario",
		"setFilialUsuario", 
		new Array()));

	xmit(false);	
}

function setFilialUsuario_cb(data, error) {
	setElementValue('filial.idFilial', data.idFilial);
	setElementValue('filial.sgFilial', data.sgFilial);
	setElementValue('sgFilial', data.sgFilial);
	setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
}

</script>