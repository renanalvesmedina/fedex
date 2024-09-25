<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">

	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="220" idProperty="idDoctoServico">
	
		<adsm:textbox dataType="text" size="3" property="sgFilialOr" label="numero" labelWidth="18%" width="32%" disabled="true">	
		<adsm:textbox dataType="integer" size="12" property="nrManifesto" disabled="true" mask="0000000000"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="text" size="22" property="dhEmissao" label="dataEmissao" labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:textbox dataType="text" size="3" property="sgFilialOrPC" label="pedidoColeta" labelWidth="18%" width="32%" disabled="true">
		<adsm:textbox dataType="integer" mask="00000000" size="12" property="nrColeta" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="JTDate" size="25" property="dtPrevisaoColeta" label="dataPrevisao" labelWidth="18%" width="32%" disabled="true"/>
		<adsm:textbox dataType="text" size="25" property="tpPedidoColeta" label="tipo" labelWidth="18%" width="32%" disabled="true"/>
		<adsm:textbox dataType="text" size="25" property="tpModoPedidoColeta" label="modoColeta" labelWidth="18%" width="32%" disabled="true"/>
		<adsm:textbox dataType="text" size="25" property="dhColetaDisponivel" label="dataExecucao" labelWidth="18%" width="32%" disabled="true"/>
		
	</adsm:form>
</adsm:window>   
<script>
	function findManifestoColetaByIdDocto(){
		var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
		
	   _serviceDataObjects = new Array();
   		addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findManifestoColetaByIdDoctoServico", "onDataLoad", {idDoctoServico:idDoctoServico}));
  		xmit();
  	
	}


</script>