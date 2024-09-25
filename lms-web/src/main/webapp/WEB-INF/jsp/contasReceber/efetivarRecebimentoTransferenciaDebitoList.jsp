<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/contasReceber/efetivarRecebimentoTransferenciaDebito">

		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.findLookupFilial" 
					 property="filialByIdFilialOrigem" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 dataType="text" 
					 label = "filialOrigem" 
					 size="3" 
					 maxLength="3" 
					 labelWidth="20%" width="35%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="35" disabled="true" 
					serializable="false"/>			
		</adsm:lookup>	

		<adsm:textbox dataType="integer" property="nrTransferencia" size="15" maxLength="15" label="numeroTransferencia" 
				labelWidth="20%" width="25%"/>		

		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.findLookupFilial" 
					 property="filialByIdFilialDestino" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 dataType="text" 
					 label = "filialDestino" 
					 size="3" 
					 maxLength="3" 
					 labelWidth="20%" width="35%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" size="35" 
					disabled="true" serializable="false"/>			
		</adsm:lookup>	

        <adsm:textbox label="dataEmissao" property="dtEmissao" dataType="JTDate" labelWidth="20%" width="25%"/>

        <adsm:combobox label="origemTransferencia" property="tpOrigem" domain="DM_ORIGEM"
        		labelWidth="20%" width="35%"/>

        <adsm:combobox label="situacaoTransferencia" property="tpSituacaoTransferencia" domain="DM_STATUS_TRANSFERENCIA"
        		labelWidth="20%" width="25%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="debitos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="debitos" idProperty="idTransferencia" rows="11" onRowClick="tabSetDisabled(false);"
			selectionMode="none"	    
			disableMarkAll="true"
			service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.findPaginatedTransferenciaDebito"
			rowCountService="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.getRowCountTransferenciaDebito">
			
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialOrigem" property="sgFilialOrigem" width="35" dataType="text"/>
			<adsm:gridColumn title="" property="filialOrigem" width="140" dataType="text"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="numeroTransferencia" property="nrTransferencia" width="90" dataType="integer"/>
		<adsm:gridColumn title="dataEmissao" property="dtEmissao" width="60" dataType="JTDate"/>
		
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="35" dataType="text"/>
			<adsm:gridColumn title="" property="filialDestino" width="140" dataType="text"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="origemTransferencia" property="tpOrigem" isDomain="true" width="110"/>
		
		<adsm:gridColumn title="situacaoTransferencia" property="tpSituacaoTransferencia" isDomain="true" width="" dataType="text"/>
		
	</adsm:grid>

	<adsm:buttonBar>
	</adsm:buttonBar>
</adsm:window>
<script>
	function myOnPageLoad_cb(d,e){
		onPageLoad_cb(d,e);
		loadFilialDestino();
	}


	function initWindow(evento) {
		tabSetDisabled(true);
		
		
		if (evento.name == "cleanButton_click"){	
			loadFilialDestino();
		}			
	}
	
	function loadFilialDestino(){
		var sdo = createServiceDataObject("lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.findFilialSessao",
			"onDataLoad");
		xmit({serviceDataObjects:[sdo]});	
	}
	
	function tabSetDisabled(disable) {
		var tabGroup = getTabGroup(this.document);
 		tabGroup.setDisabledTab("cad", disable);	
 		tabGroup.setDisabledTab("proc", disable);	
	}
</script>
