<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.action.registrarRecebimentosEntregaDocumentoAction" >
	<adsm:form action="/seguros/registrarRecebimentosEntregaDocumento" idProperty="idDoctoProcessoSinistro">
	
		<adsm:hidden property="processoSinistro.idProcessoSinistro"/>		
		<adsm:textbox label="numeroProcesso" property="processoSinistro.nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />
		
		<adsm:combobox property="tpEntregaRecebimento" label="acao" domain="DM_ENTREGA_RECEBIMENTO" labelWidth="16%" width="82%" required="true" renderOptions="true"/>

		<adsm:textbox property="nrProtocolo" dataType="integer" label="protocolo" maxLength="10" size="15" labelWidth="16%" width="82%"/>
		<adsm:textbox property="nrDocumento" dataType="text" label="documento" maxLength="15" size="15" labelWidth="16%" width="82%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="doctoProcessoSinistro"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="doctoProcessoSinistro" idProperty="idDoctoProcessoSinistro" selectionMode="check" scrollBars="horizontal" gridHeight="200" rows="10" defaultOrder="nrProtocolo, dhCadastroDocumento">
		<adsm:gridColumn width="115" title="acao" property="tpEntregaRecebimento" isDomain="true"/>
		<adsm:gridColumn width="140" title="data" property="dhCadastroDocumento" dataType="JTDateTimeZone" align="center"/>
		<adsm:gridColumn width="150" title="tipoDocumento" property="tipoDocumentoSeguro.dsTipo" />
		<adsm:gridColumn width="100" title="documento" property="nrDocumento" align="left"/>
		<adsm:gridColumn width="150" title="entregaRecebimento" property="nmRecebedor"/>
		<adsm:gridColumn width="100" title="protocolo" property="nrProtocolo" align="right"/>
		<adsm:gridColumn width="140" title="dataEmissaoProtocolo" property="dhEmissaoProtocolo" dataType="JTDateTimeZone" align="center"/>		
		<adsm:buttonBar> 
			<adsm:button onclick="onReportButtonClick(this);" caption="emitirProtocolo" />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">

	document.getElementById("processoSinistro.idProcessoSinistro").masterLink = "true";
	document.getElementById("processoSinistro.nrProcessoSinistro").masterLink = "true";
	
	function onReportButtonClick(button) {

		var strService = 'lms.seguros.report.emitirProtocoloDocEntreguesAction.executeReport';
		var strCallBack = 'openPdf';
		var form = button.form;

		var data = new Array(); 
		var tpEntregaRecebimento = document.getElementById("tpEntregaRecebimento");
		
		//data.selectedIds = doctoProcessoSinistroGridDef.getSelectedIds()["ids"];
		setNestedBeanPropertyValue(data, 'selectedIds', doctoProcessoSinistroGridDef.getSelectedIds()["ids"]);
		// data.tpEntregaRecebimento = tpEntregaRecebimento[tpEntregaRecebimento.selectedIndex].value;
		setNestedBeanPropertyValue(data, 'tpEntregaRecebimento', tpEntregaRecebimento[tpEntregaRecebimento.selectedIndex].value);
		
		setNestedBeanPropertyValue(data, 'processoSinistro.idProcessoSinistro', document.getElementById("processoSinistro.idProcessoSinistro").value);
		
		var sdo = createServiceDataObject(strService, strCallBack, data); 
		
		executeReportWindowed(sdo, 'pdf');
	
	}

</script>
