<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoadCallBack="myOnPageLoad">
	<script>
		function myOnPageLoad_cb(d,e,o,x){
			var u = new URL(parent.location.href);
			setElementValue("idMonitoramentoMensagem", u.parameters["idMonitoramentoMensagemConteudo"]);
			var idMonitoramentoMensagem = u.parameters["idMonitoramentoMensagemConteudo"];
			onPageLoad_cb(d,e,o,x);
			
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findConteudoMensagem", "findConteudo",{idMonitoramentoMensagem:idMonitoramentoMensagem});
			xmit({serviceDataObjects:[sdo]});
		}
		
		function findConteudo_cb(data, error){
			if(error) {
				alert(error);
				closeCalculo();
				return false;
			}
			
			var gridFrete = getElement("fatura.dataTable").gridDefinition;
			gridFrete.resetGrid();
			gridFrete.onDataLoad_cb(data, error);
		}
		
		function myOnRowClick(){
			return false;
		}
	</script>
	<adsm:form action="/contasReceber/manterFaturas" service=""	idProperty="idMonitoramentoMensagem">
		<adsm:hidden property="idMonitoramentoMensagem"/>
		<adsm:grid 	property="fatura" title="conteudoMensagem" onRowClick="myOnRowClick();" idProperty="idParcela" selectionMode="none" 
					showRowIndex="false" autoAddNew="false" gridHeight="420"	showGotoBox="false" showPagging="false"
					showTotalPageCount="false" scrollBars="vertical">
					
					<adsm:gridColumn title="conteudo"
							image=""
							openPopup="true" 				
							property="dcMensagem"			    
							width="100%"	
							popupDimension="790,496" 	
							dataType="text"/>
			
		</adsm:grid>
		<adsm:buttonBar>
			<adsm:button caption="fechar" onclick="self.close();"
				disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>