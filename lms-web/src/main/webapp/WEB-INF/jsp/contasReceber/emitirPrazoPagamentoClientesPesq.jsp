<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>

	<adsm:form action="/contasReceber/emitirPrazoPagamentoClientes">

		<adsm:hidden property="sgFilial"/>
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filial" 
					 size="3" 
					 maxLength="3" 
					 width="80%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="60" maxLength="60" disabled="true" serializable="true"/>
		</adsm:lookup>

        <adsm:combobox property="modal" label="modal" domain="DM_MODAL" labelWidth="20%" width="30%" />

        <adsm:combobox property="abrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="20%" width="30%" />

  	    <adsm:textbox label="prazoSuperior" property="prazo" dataType="integer" size="10" maxLength="5" labelWidth="20%" width="30%"/>		

 		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="20%"
    				   width="30%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirPrazoPagamentoClientesAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script>

    function initWindow(event){
		if (event.name == "tab_load" || event.name == "cleanButton_click") {
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirMaioresDevedoresAction.findFilialUsuario",
				"setFilialUsuario", 
				new Array()));
			xmit(false);	
		}
	}

	function setFilialUsuario_cb(data, error) {
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue('filial.sgFilial', data.sgFilial);
		setElementValue('sgFilial', data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
	}
</script>