<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="220" idProperty="idDoctoServico">
			<adsm:textbox size="27" dataType="text" property="nmUsuario" label="usuarioInclusao" disabled="true" labelWidth="20%"  width="30%"/>
			<adsm:textbox size="22" dataType="text" property="dhInclusao" label="dataHoraInclusao" disabled="true" labelWidth="20%" width="30%"/>
			<adsm:textbox width="80%" size="27" dataType="text" property="tpModal" label="modal" disabled="true" labelWidth="20%"/>
	</adsm:form>
</adsm:window>  
<script>
function findOutrosAbaParcerias(){
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
	
    _serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findOutrosByIdConhecimento", "onDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
}

</script> 