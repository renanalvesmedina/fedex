<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myPageLoadCallBack"> 

	<adsm:form action="/contasReceber/emitirChequesTransferidosFilialCobranca">

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filial" 
					 size="3" 
					 maxLength="3" 
					 width="40%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="30" disabled="true" serializable="true"/>
		</adsm:lookup>		

		<adsm:textbox label="dataTransferencia" dataType="JTDate" property="dataTransferencia" labelWidth="20%" width="20%"/>

	    <adsm:combobox property="tpFormatoRelatorio" 
					   label="formatoRelatorio"  
					   labelWidth="20%"
					   required="true"
					   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirChequesTransferidosFilialCobrancaAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
	setaDataAtual();
}

function setaDataAtual() {

	_serviceDataObjects = new Array();
	
	addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirChequesTransferidosFilialCobrancaAction.findDataAtual",
		"setDataAtual", 
		new Array()));

       xmit(false);	
}

function setDataAtual_cb(data, error) {
	setElementValue('dataTransferencia', data._value);
}

</script>