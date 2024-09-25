<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas" idProperty="idFatura">
		<adsm:hidden property="idFatura" />
		
		<adsm:grid 	property="fatura" title="eventos" idProperty="idFatura" selectionMode="none" 
					showRowIndex="false" autoAddNew="false" gridHeight="400"	showGotoBox="false" showPagging="false"
					showTotalPageCount="false" scrollBars="vertical">
					
					<adsm:gridColumn title="dtTratativa" 					property="dtTratativa"			    width="16%"		dataType="text"/>
					<adsm:gridColumn title="usuarioTratativa" 					property="usuarioTratativa"			    width="16%"		dataType="text"/>
					<adsm:gridColumn title="motivoInadimplencia" 					property="motivoInadimplencia"			    width="16%"		dataType="text"/>
					<adsm:gridColumn title="dtPrevistaSolucao" 					property="dtPrevistaSolucao"			    width="16%"		dataType="text"/>
					<adsm:gridColumn title="planoAcao" 					property="planoAcao"			    width="16%"		dataType="text"/>
					<adsm:gridColumn title="parecerMatriz" 					property="parecerMatriz"			    width="16%"		dataType="text"/>
					
			
		</adsm:grid>
	</adsm:form>
</adsm:window>

<script>
		function myOnPageLoad_cb(d, e, o, x) {
			var u = new URL(parent.location.href);
			setElementValue("idFatura", u.parameters["idFatura"]);
			var idFatura = u.parameters["idFatura"];
			var data = new Array();
			setNestedBeanPropertyValue(data, "idFatura", idFatura);
			
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findTratativas","tratativaPB",data);
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
		
	</script>