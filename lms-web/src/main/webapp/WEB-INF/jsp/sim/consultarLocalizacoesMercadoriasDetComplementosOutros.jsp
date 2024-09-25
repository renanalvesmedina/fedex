<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="208" idProperty="idDoctoServico">
			<adsm:textbox width="30%" size="22" dataType="text" property="dhInclusao" label="dataHoraInclusao" disabled="true" labelWidth="20%"/>
			<adsm:textbox width="30%" size="35" dataType="text" property="nmUsuarioInclusao" label="incluidoPor" disabled="true" labelWidth="20%"/>
			<adsm:textbox width="80%" size="3" dataType="text" property="filialInc" label="filial" disabled="true" labelWidth="20%">
			<adsm:textbox dataType="text" property="nmFantasiaInc" size="30" disabled="true"/>
			</adsm:textbox>

			<adsm:textbox width="30%" size="22" dataType="text" property="dhAlteracao" label="dataHoraAlteracao" disabled="true" labelWidth="20%"/>
			<adsm:textbox width="30%" size="35" dataType="text" property="nmUsuarioAlteracao" label="alteradoPor" disabled="true" labelWidth="20%"/>
			
			<adsm:textbox width="80%" size="3" dataType="text" property="filialAlt" label="filial" disabled="true" labelWidth="20%">
			<adsm:textbox dataType="text" property="nmFantasiaAlt" size="30" disabled="true"/>
			</adsm:textbox>
			
			<adsm:textbox dataType="integer" property="idConhecimento" disabled="true" label="preConhecimento" width="80%" labelWidth="20%"/>
			
			<adsm:textbox width="30%" size="30" dataType="text" property="indicadorCooperacao" label="indicadorCooperacao" disabled="true" labelWidth="20%"/>
			<adsm:textbox width="30%" size="30" dataType="text" property="liberacao" label="liberacaoFaturamento" disabled="true" labelWidth="20%"/>
			
		<adsm:section caption="EDI"/>		
			<adsm:textbox width="30%" size="15" dataType="text" property="documento" label="geracaoDocumento" disabled="true" labelWidth="20%"/>
			<adsm:textbox width="30%" size="15" dataType="text" property="cooperacao" label="geracaoCooperada" disabled="true" labelWidth="20%"/>
			<adsm:textbox width="30%" size="15" dataType="text" property="entrega" label="geracaoEntrega" disabled="true" labelWidth="20%"/>
			<adsm:textbox width="30%" size="15" dataType="text" property="ocorrencia" label="geracaoOcorrencia" disabled="true" labelWidth="20%"/>
			<adsm:checkbox width="30%" property="indicadorEDICliente" label="indicadorEDICliente" disabled="true" labelWidth="20%"/>
	</adsm:form>
</adsm:window> 
<script>
function findComplOutros(){
	 cleanButtonScript(this.document);	
	 var idDoctoServico = parent.document.getElementById("idDoctoServico").value;
	 _serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findComplementosOutros", "onDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
}  	
</script>  