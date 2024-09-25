<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoadCallBack="myOnPageLoad">
	<script>
		function myOnPageLoad_cb(d,e,o,x){
			var u = new URL(parent.location.href);
			setElementValue("idFatura", u.parameters["idFatura"]);
			var idFatura = u.parameters["idFatura"];
			onPageLoad_cb(d,e,o,x);
			
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findRecebimentosParciais", "findRecebimentosParciais",{idFatura:idFatura});
			xmit({serviceDataObjects:[sdo]});
		}
		
		function findRecebimentosParciais_cb(data, error){
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
	<adsm:form action="/contasReceber/manterFaturas" service=""	idProperty="idFatura">
		<adsm:hidden property="idFatura"/>
		<adsm:grid 	property="fatura" title="recebimentosParciais" onRowClick="myOnRowClick();" idProperty="idParcela" selectionMode="none" 
					showRowIndex="false" autoAddNew="false" gridHeight="70"	showGotoBox="false" showPagging="false"
					showTotalPageCount="false" scrollBars="vertical">
					
					<adsm:gridColumn title="filial" 				property="relacao.sgFilial"					width="5%"		dataType="text"/>
					<adsm:gridColumn title="numero" 				property="relacao.nrRelacao"				width="7%"		dataType="integer"/>
					<adsm:gridColumn title="valorPagamento"			property="relacao.vlPagamento"				width="10%"		dataType="currency"/>
					<adsm:gridColumn title="valorJuro" 				property="relacao.vlJuros"					width="10%"		dataType="currency"/>
					<adsm:gridColumn title="valorDesconto" 			property="relacao.vlDesconto"				width="10%"		dataType="currency"/>
					<adsm:gridColumn title="dataPagamento" 			property="relacao.dtPagamento"				width="10%"		dataType="JTDate"/>
					<adsm:gridColumn title="banco" 					property="relacao.nmBanco"					width="15%"		dataType="text"/>
					<adsm:gridColumn title="dtEnvioContabilidade" 	property="relacao.dtEnvioContabilidade"		width="13%"		dataType="JTDate"/>
					<adsm:gridColumn title="loteJde" 				property="relacao.nrLoteContabilJDE"		width="10%"		dataType="text"/>
					<adsm:gridColumn title="observacao" 			property="relacao.obRelacao"				width="10%"		dataType="text"/>
			
		</adsm:grid>
		<adsm:buttonBar>
			<adsm:button caption="fechar" onclick="self.close();"
				disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>