<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	// detalha o recibo indenizacao, para o workflow
	function executaDetalhamentoWorkflow(idProcessoWorkflow) {
		var data = new Object();
		data.idProcessoWorkflow = idProcessoWorkflow;
		var sdo = createServiceDataObject("lms.indenizacoes.consultarReciboIndenizacaoAction.findByIdReciboIndenizacao", "dataLoad", data);
		xmit({serviceDataObjects:[sdo]});
		
		var tabGroup = getTabGroup(this.document);
		tabGroup._tabsIndex[0].setDisabled(true);
		
		setDisabled(document, true);
	}
</script>

<adsm:window service="lms.indenizacoes.consultarReciboIndenizacaoAction" >
	<adsm:form action="/indenizacoes/consultarReciboIndenizacao" height="240" idProperty="idReciboIndenizacao"  service="lms.indenizacoes.consultarReciboIndenizacaoAction.findByIdReciboIndenizacao" onDataLoadCallBack="dataLoad">

		<adsm:complement label="rim" labelWidth="25%" width="75%" separator="branco">
			<adsm:textbox property="sgFilialRecibo" dataType="text" size="3" disabled="true"/>
			<adsm:textbox property="nrReciboIndenizacao" dataType="integer" disabled="true" mask="00000000" />
		</adsm:complement>			

		<adsm:textbox label="status" property="tpStatusIndenizacao" dataType="text" disabled="true" labelWidth="25%" width="75%" size="22"/>
		
		<adsm:textbox label="situacaoAprovacao" property="tpSituacaoWorkflow" dataType="text" disabled="true" labelWidth="25%" width="75%" size="22"/>
		
		<adsm:textbox label="tipoIndenizacao" property="tpIndenizacao" dataType="text" disabled="true" labelWidth="25%" width="75%" size="22"/>

		<adsm:textbox label="dataEmissao" property="dtEmissao" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />
		
		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="25%" width="75%" disabled="true" />
		<adsm:hidden property="nrReciboComposto"/>
		<adsm:hidden property="idPendencia"/>
		
		<adsm:textbox property="tipoSeguro" dataType="text" label="tipoSeguro" disabled="true" labelWidth="25%" width="75%" size="22"/>
		
		<adsm:combobox label="naoSegurado" property="blSegurado" domain="DM_SIM_NAO" required="false" width="75%" labelWidth="25%" disabled="true" renderOptions="true"/>
 		
		<adsm:complement label="valorIndenizacao" width="75%" labelWidth="25%" separator="branco">
			<adsm:textbox property="sgSimboloMoeda" dataType="text" size="8" disabled="true"/>			
			<adsm:textbox property="vlIndenizacao" dataType="currency" size="18" disabled="true"/>		
		</adsm:complement>	

		<adsm:textbox label="volumesIndenizados" property="qtVolumesIndenizados" dataType="integer" required="false" width="75%" labelWidth="25%" maxLength="6" size="6" disabled="true"/>
		
		<adsm:textbox label="notaFiscalDebitoCliente" property="nrNotaFiscalDebitoCliente" dataType="integer" width="75%" labelWidth="25%" maxLength="10" size="10" disabled="true"/>

		<adsm:combobox label="salvados" property="blSalvados" domain="DM_SIM_NAO" required="false" width="75%" labelWidth="25%" disabled="true" renderOptions="true"/>
		
		<adsm:textarea label="observacao" property="obReciboIndenizacao" maxLength="1000" columns="50" rows="3" width="75%" labelWidth="25%" disabled="true"/>
		
        <%-- ============================================================ --%>
		<adsm:combobox label="beneficiario" property="tpBeneficiarioIndenizacao" domain="DM_BENEFICIARIO_INDENIZACAO"  boxWidth="85" labelWidth="25%" width="75%" required="false" disabled="true" renderOptions="true">
            <adsm:textbox dataType="text" 
            			  property="beneficiario" 
            			  serializable="false" 
            			  size="67"
            			  maxLength="50" disabled="true"/>
		</adsm:combobox>
        <%-- ============================================================ --%>		
		<adsm:combobox label="favorecido" property="tpFavorecidoIndenizacao" domain="DM_BENEFICIARIO_INDENIZACAO"  boxWidth="85" labelWidth="25%" width="75%" required="false" disabled="true" renderOptions="true">
            <adsm:textbox dataType="text" 
		      			  property="favorecido"             			  
            			  serializable="false" 
            			  size="67" 
            			  maxLength="50" disabled="true"/>
		</adsm:combobox>
        <%-- ============================================================ --%>		
        
		<adsm:combobox label="formaPagamento" property="tpFormaPagamento" domain="DM_FORMA_PAGAMENTO_INDENIZACAO" required="false" width="75%" labelWidth="25%" disabled="true" renderOptions="true"/>

		<adsm:textbox label="dataProgramadaPagamento" property="dtProgramadaPagamento" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />		
		
		<adsm:textbox dataType="integer"
					 property="banco.nrBanco" 
					 label="banco" size="5" maxLength="3"
					 required="false"
					 labelWidth="25%"
				  	 disabled="true"
					 width="75%"
					 >
			<adsm:textbox property="banco.nmBanco" dataType="text" size="50" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="integer" 
					 property="agenciaBancaria.nrAgenciaBancaria" 
					 label="agencia" maxLength="4" size="7" 
					 disabled="true"
					 required="false"
					 labelWidth="25%"
					 width="75%" 
					 >			
			<adsm:textbox property="agenciaBancaria.nmAgenciaBancaria" dataType="text" size="50" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:complement label="numeroConta" labelWidth="25%" width="75%" separator="branco">
			<adsm:textbox label="numeroConta" dataType="integer" property="nrContaCorrente" maxLength="15" size="14" required="fasle" minValue="0"  disabled="true"/>
			<adsm:textbox dataType="text" property="nrDigitoContaCorrente" maxLength="2"  style="width:30px;"        required="false"  disabled="true"/>
		</adsm:complement>
		
		<adsm:textbox label="parcelas" property="qtParcelasBoletoBancario" dataType="integer" size="2" maxLength="2" width="75%" labelWidth="25%" disabled="true" />
		
		<adsm:textbox label="dataLiberacaoPgto" property="dtLiberacaoPgto" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />
		
		<adsm:textbox label="dataDevolucaoBanco" property="dtDevolucaoBanco" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />
		<adsm:textbox label="dataPagamento" property="dtPagamentoEfetuado" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />				

		<adsm:buttonBar freeLayout="true">
					<adsm:button caption="historicoAprovacao" id="btnFluxoAprovacao"
				onclick="showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');"/>
		</adsm:buttonBar>		
		
	</adsm:form>
	<adsm:grid property="filialDebitada" 
				idProperty="idFilialDebitada"  
				unique="true" 
				rows="3" 
				gridHeight="155"
				selectionMode="none"
				service="lms.indenizacoes.consultarReciboIndenizacaoAction.findPaginatedFilialDebitada"		
				rowCountService="lms.indenizacoes.consultarReciboIndenizacaoAction.getRowCountFilialDebitada"
				onRowClick="onRowClick"
	>		
		<adsm:gridColumn property="sgFilial"             title="filial"             width="140"/>
		<adsm:gridColumn property="pcDebitado"           title="percentualDebitado" width="110" align="right"/>
		<adsm:gridColumn property="sgFilialReembolsada"  title="filialReembolsada"  width="110"/>		
		<adsm:gridColumn property="sgSimboloVlReembolso" title="valorReembolso"     width="70"  align="left"/>
		<adsm:gridColumn property="vlReembolso"          title=""                   width="140"  align="right" dataType="currency"/>
		<adsm:gridColumn property="dtReembolso"          title="dataReembolso"      width="" dataType="JTDate"/>
		<adsm:buttonBar/>
	</adsm:grid>
	
	
	
</adsm:window>
<script>

	function onRowClick() {
		return false;
	}

	function dataLoad_cb(data, error) {	
		onDataLoad_cb(data, error);
		if(error==undefined && getElementValue('idReciboIndenizacao')!=''){
			// habilita a aba de documentos		
			tabSetDisabled('documentos', false);			
			// se salvados, então habilida a aba mda
			if (getElementValue('blSalvados')=='S') {
				tabSetDisabled('mda', false);
			} else {
				tabSetDisabled('mda', true);
			}		
						
			if (getElementValue('qtParcelasBoletoBancario')!='' && getElementValue('qtParcelasBoletoBancario') > 0 ) {
				tabSetDisabled('parcelas', false);
			} else {
				tabSetDisabled('parcelas', true);
			}
			
			// Seta campo hidden utilizado para barra verde (masterlink) em outras abas
			setElementValue("nrReciboComposto", getElementValue("sgFilialRecibo") + " " + setFormat(document.getElementById("nrReciboIndenizacao"), getElementValue("nrReciboIndenizacao")));
			
			executeSearch();			
		}	
	}
		
	function executeSearch() {
		if (getElementValue('idReciboIndenizacao')!='') {
			var data = new Array();
			data.idReciboIndenizacao = getElementValue('idReciboIndenizacao');
			filialDebitadaGridDef.executeSearch(data);	
		}	
	}	

	// init window
	function initWindow(evento) {
		if (evento.name == "newButton_click" || evento.name=="tab_click" ) {
			if (evento.name == "newButton_click" || (evento.name=="tab_click" && evento.src.tabGroup.oldSelectedTab.properties.id=="pesq")) { 
				newMaster();
				tabSetDisabled('mda', true);
				tabSetDisabled('documentos', true);
				tabSetDisabled('parcelas', true);
			} 
		} 
	}
		
	// executa o new master
	function newMaster() {
		var service = "lms.indenizacoes.consultarReciboIndenizacaoAction.newMaster";
		var callback = "newMaster";
		data = new Array();
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject(service, null, data)); 
		xmit(remoteCall); 		
	}
									
	function tabSetDisabled(tab, disable) {
		var tabGroup = getTabGroup(this.document);
 		tabGroup.setDisabledTab(tab, disable);	
	}
	
</script>