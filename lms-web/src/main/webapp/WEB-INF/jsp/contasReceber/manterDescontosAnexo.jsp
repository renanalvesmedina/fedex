<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterDescontosAction" >

	<adsm:form idProperty="idDescontoAnexo"  
				action="/contasReceber/manterDescontos"  
				service="lms.contasreceber.manterDescontosAction.findAnexos"  >
				
		<adsm:masterLink idProperty="idDesconto" showSaveAll="true">
			<adsm:masterLinkItem property="descontoDsConcatenado" label="desconto"/>
		</adsm:masterLink>

		<adsm:textbox 
					property="dsAnexo" 
					dataType="text" 
					label="descricao" 
					size="20" 
					labelWidth="18%" 
					width="82%" 
					required="true" />

		<adsm:textbox property="dcArquivo" 
					dataType="file"
					label="arquivo"
					labelWidth="18%"
					width="82%"
					size="60"
					required="true"
					serializable="true" />
					
		<adsm:combobox property="blEnvAnexoQuestFat" 
					label="incluirQuestionamento" 
					domain="DM_SIM_NAO" 
					labelWidth="18%" 
					width="82%" 
					renderOptions="true"
					required="true"
					defaultValue="S"/>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" id="salvarItemMdaButton"  
							  service="lms.contasreceber.manterDescontosAction.storeAnexo" />
			<adsm:newButton caption="limpar"/>	
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="descontoAnexo" idProperty="idDescontoAnexo" selectionMode="check" gridHeight="82" 
			   unique="true" rows="8" detailFrameName="anexo"
			   service="lms.contasreceber.manterDescontosAction.findPaginatedDescontoAnexo" autoSearch="false"
			   rowCountService="lms.contasreceber.manterDescontosAction.getRowCountDescontoAnexo">
			   
			   <adsm:gridColumn property="descricao" dataType="text" title="descricao" width="50%" />
			   <adsm:gridColumn property="dhinclusao" dataType="JTDateTimeZone" title="dataHoraDeInclusao" width="20%" />
			   <adsm:gridColumn property="nmusuario" dataType="text" title="usuario" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirItem" service="lms.contasreceber.manterDescontosAction.removeByIdsAnexoDesconto" />
		</adsm:buttonBar>
	</adsm:grid>	
	
</adsm:window>
<script>

function initWindow(eventObj){		
	    if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
		    addServiceDataObject(createServiceDataObject("lms.contasreceber.manterDescontosAction.clearSessionItens",
	                null,
	                null));
			xmit(false);
	    }
}

</script>