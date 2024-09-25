<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterFaturasAction">
	<adsm:form action="/contasReceber/manterFaturas" service="lms.contasreceber.manterFaturasAction.findAnexos" idProperty="idFaturaAnexo">
	
		<adsm:masterLink idProperty="idFatura" showSaveAll="true">			
			<adsm:masterLinkItem property="nrFaturaFormatada" label="fatura"
				itemWidth="45" />
			<adsm:masterLinkItem property="dtVencimento" label="dataVencimento"
				itemWidth="45" />
			<adsm:masterLinkItem property="tpFreteDescription" label="tipoFrete"
				itemWidth="45" />
			<adsm:masterLinkItem property="servico.dsServico" label="servico"
				itemWidth="45" />			
		</adsm:masterLink>	
	
		<adsm:textbox dataType="text" label="anexos" property="dsAnexo" size="42" maxLength="60" required="true" labelWidth="22%" width="50%"/>	
		<adsm:textbox labelWidth="22%"
					  label="arquivo"
					  property="dcArquivo"
					  blobColumnName="DC_ARQUIVO"
					  tableName="FATURA_ANEXO"
					  primaryKeyColumnName="ID_FATURA_ANEXO"
					  primaryKeyValueProperty="idFaturaAnexo"
					  dataType="file"
					  width="78%"
					  size="60"
					  required="true"
					  serializable="true"/>

		<adsm:combobox property="blEnvAnexoQuestFat" label="questionamentoFaturas" domain="DM_SIM_NAO" labelWidth="22%" 
			width="33%" boxWidth="150" required="true" defaultValue="S"> 
		</adsm:combobox>		
	
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" id="salvarItemMdaButton"  
							  service="lms.contasreceber.manterFaturasAction.storeAnexo" />
							  
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>		
	<adsm:grid property="faturaAnexo" idProperty="idFaturaAnexo" selectionMode="check" gridHeight="82" 
			   unique="true" rows="8" detailFrameName="anexos"
			   service="lms.contasreceber.manterFaturasAction.findPaginatedFaturaAnexo" autoSearch="false"
			   rowCountService="lms.contasreceber.manterFaturasAction.getRowCountFaturasAnexo">
			   
			   <adsm:gridColumn property="descricao" dataType="text" title="descricao" width="30%" />
			   <adsm:gridColumn property="dhinclusao" dataType="JTDateTimeZone" title="dataHoraDeInclusao" width="15%" />
			   <adsm:gridColumn property="nmusuario" dataType="text" title="usuario" width="25%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirItem" service="lms.contasreceber.manterFaturasAction.removeByIdsAnexoFatura" />
		</adsm:buttonBar>
	</adsm:grid>	

</adsm:window>
<script>

function initWindow(eventObj){		
	    if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
		    addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.clearSessionItens",
	                null,
	                null));
			xmit(false);
	    }
}

</script>