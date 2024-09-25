<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';
		return colunas;
	}
	
	
	
</script>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="210" idProperty="idDoctoServico">
		<td colspan="100" >

		<div id="principal" style="display:;border:none;">
			<script>
				document.write(geraColunas());
			</script>
			
			<adsm:textbox dataType="text" property="cotacao" label="cotacao" size="25" disabled="true" labelWidth="25%" width="25%"/>
			
			<adsm:textbox dataType="text" property="tabelaPreco" label="tabelaUtilizada" size="30" disabled="true" labelWidth="25%" width="25%"/>
			
			<adsm:textbox dataType="text" property="dsDivisaoCliente" label="divisao" size="25" disabled="true" labelWidth="25%" width="25%"/>
			</table>
		</div>
		
		<div id="calcDoctoServico" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>
		    <adsm:section caption="calculoDocumentoServico"/>
		    	<adsm:hidden property="idConhecimento"/>
				<adsm:textbox dataType="currency" property="vlMercadoria" label="valorMercadoria" size="25" disabled="true" labelWidth="25%" width="25%"/>
				<adsm:textbox dataType="text" style="text-align:right" property="vlMercadoriaReemb" label="valorMercadoriaReembolso" size="25" disabled="true" labelWidth="25%" width="25%"/>
				<adsm:textbox dataType="decimal" property="psReal" label="pesoReal" mask="###,###,##0.000" size="25" disabled="true" labelWidth="25%" width="25%" unit="kg"/>
				<adsm:textbox dataType="decimal" property="psAforado" label="pesoCubado" mask="###,###,##0.000" size="25" disabled="true" labelWidth="25%" width="25%" unit="kg"/>
				<adsm:textbox dataType="decimal" property="psReferenciaCalculo" label="pesoCalculo" mask="###,###,##0.000" size="25" disabled="true" labelWidth="25%" width="25%" unit="kg"/>
				<adsm:textbox dataType="integer" property="qtVolumes" label="quantidadeVolumes" size="25" disabled="true" labelWidth="25%" width="25%"/>
				<adsm:textbox dataType="integer" property="qtNF" label="quantidadeNotas" size="25" disabled="true" labelWidth="25%" width="25%"/>
		</table>
	  </div>
	  
	  <div id="calcDoctoServicoIntern" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>	
	    <adsm:section caption="calculoDocumentoServicoInternacional"/>
	    	<adsm:hidden property="idConhInter" />
			<adsm:textbox dataType="text" property="vlMercadoriaI" label="valorMercadoria" size="25" disabled="true" labelWidth="25%" width="25%" style="text-align:right"/>
			<adsm:textbox dataType="text" property="vlFreteExterno" label="valorFreteExterno" size="25" disabled="true" labelWidth="25%" width="25%" style="text-align:right"/>
			<adsm:textbox dataType="decimal" property="psRealI" label="pesoBruto" mask="###,###,##0.000" size="25" disabled="true" labelWidth="25%" width="25%" unit="kg"/>
			<adsm:textbox dataType="decimal" property="psLiquido" label="pesoLiquido" mask="###,###,##0.000" size="25" disabled="true" labelWidth="25%" width="25%" unit="kg"/>
			<adsm:textbox dataType="decimal" property="vlVolume" label="volume" size="25" disabled="true" labelWidth="25%" width="25%" unit="m3"/>
		</table>
	  </div>	
	  
	</adsm:form>   
</adsm:window> 
<script>
function findPaginatedDadosCalculo(){
	
	var idDoctoServico = parent.document.getElementById("idDoctoServico").value;
	var data = new Array();
	setNestedBeanPropertyValue(data,"idDoctoServico",idDoctoServico);
	
	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findDadosCalculoFrete", "onDataLoadFreteCalculo", {idDoctoServico:idDoctoServico}));
  	xmit();
}

function onDataLoadFreteCalculo_cb(data,exception){
	onDataLoad_cb(data,exception);
	var idConhecimento = getNestedBeanPropertyValue(data,"idConhecimento");
	var idConhInter = getNestedBeanPropertyValue(data,"idConhInter");
	showCalculoAgrupamentos(idConhecimento != undefined,idConhInter != undefined);
	
}

function showCalculoAgrupamentos(valueBooleanConh,valueBooleanConhI) {
	var value = valueBooleanConh ? "" : "none";
	document.getElementById("calcDoctoServico").style.display = value;
	
	var value = valueBooleanConhI ? "" : "none";
	document.getElementById("calcDoctoServicoIntern").style.display = value;
}

			







</script>  