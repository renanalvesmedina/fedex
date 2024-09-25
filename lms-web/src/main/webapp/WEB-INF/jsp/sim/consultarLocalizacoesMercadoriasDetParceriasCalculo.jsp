<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="210" idProperty="idDoctoServico">
		<adsm:textbox dataType="text" property="dsNaturezaProduto" label="naturezaMercadoria" size="25" disabled="true" labelWidth="25%" width="25%"/>
		<adsm:textbox dataType="text" property="valorMercadoria" label="valorMercadoria" style="text-align:right" size="25" disabled="true" labelWidth="25%" width="25%"/>
		<adsm:textbox dataType="decimal" property="pesoReal" label="pesoReal" size="25" disabled="true" labelWidth="25%" width="25%" unit="kg" mask="###,###,###.0,00"/>
		<adsm:textbox dataType="decimal" property="psAforado" label="pesoCubado" size="25" disabled="true" labelWidth="25%" width="25%" unit="kg" mask="###,###,###.0,00"/>
		<adsm:textbox dataType="integer" property="qtVolumes" label="quantidadeVolumes2" size="25" disabled="true" labelWidth="25%" width="25%"/>
		<adsm:textbox dataType="integer" property="qtNfs" label="quantidadeNotas" size="25" disabled="true" labelWidth="25%" width="25%"/>
	</adsm:form>   
</adsm:window>   
<script>
function findDadosCalculoAbaParcerias(){
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
	
    _serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findDadosCalculoByIdConhecimento", "onDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
}

</script>