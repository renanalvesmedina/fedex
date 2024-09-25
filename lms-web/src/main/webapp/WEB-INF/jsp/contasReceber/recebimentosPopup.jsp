<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas" idProperty="idFatura">
		<adsm:hidden property="idFatura" />
		
		<adsm:grid 	property="fatura" title="recebimentos" idProperty="idFatura" selectionMode="none" 
					showRowIndex="false" autoAddNew="false" gridHeight="400"	showGotoBox="false" showPagging="false"
					showTotalPageCount="false" scrollBars="vertical" onRowClick="myOnRowClick();">
					
					<adsm:gridColumn title="filial" property="sgFilial"			    width="12%"		dataType="text"/>
					<adsm:gridColumn title="numeroRedeco" property="nrRedeco"			    width="12%"		dataType="integer"/>
					<adsm:gridColumn title="situacao" property="tpSituacaoRedeco"			    width="12%"		dataType="text"/>
					<adsm:gridColumn title="dataRecebimento" property="dtRecebimento"			    width="12%"		dataType="JTDate"/>
					<adsm:gridColumn title="dataLiquidacao" property="dtLiquidacao"			    width="12%"		dataType="JTDate"/>
					<adsm:gridColumn title="finalidade" property="tpFinalidade"			    width="12%"		dataType="text"/>
					<adsm:gridColumn title="vlRecebidoRedeco" property="vlRedeco"			    width="16%"		dataType="currency"/>
					<adsm:gridColumn title="vlRecebidoFatura" property="vlFatura"			    width="16%"		dataType="currency"/>
		</adsm:grid>
		<adsm:buttonBar>
			<adsm:button caption="fechar" onclick="self.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
		function myOnPageLoad_cb(d, e, o, x) {
			var u = new URL(parent.location.href);
			setElementValue("idFatura", u.parameters["idFatura"]);
			var idFatura = u.parameters["idFatura"];
			var data = new Array();
			setNestedBeanPropertyValue(data, "idFatura", idFatura);
			
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findRecebimentos","tratativaPB",data);
			xmit({serviceDataObjects:[sdo]});
			
		}

		function tratativaPB_cb(data, erros) {
			if(erros) {
				alert(erros);
				closeCalculo();
				return false;
			}
			
			var gridFrete = getElement("fatura.dataTable").gridDefinition;
			gridFrete.resetGrid();
			gridFrete.onDataLoad_cb(data, erros);
		}
		
		function myOnRowClick() {
			return false;
		}
		
	</script>