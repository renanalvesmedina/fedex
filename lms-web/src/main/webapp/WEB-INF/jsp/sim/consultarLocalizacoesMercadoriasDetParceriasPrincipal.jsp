<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="220" idProperty="idDoctoServico">
		<adsm:textbox label="cooperada" labelWidth="10%" width="14%" size="17" maxLength="20" dataType="text" property="nrIdentificacaoCooperada" disabled="true"/>
        <adsm:textbox width="21%" size="25" dataType="text" property="nmFantasiaCooperada" disabled="true"/>
        
		<adsm:textbox label="conhecimento" labelWidth="10%" width="5%" size="3" maxLength="3" dataType="text" property="sgFilialConhecimento" disabled="true"/>
        <adsm:textbox width="10%" size="8" dataType="integer" property="nrCtoCooperada" disabled="true" mask="00000000"/>
			
		<adsm:textbox dataType="text" size="22" property="dhEmissao" label="emissao" labelWidth="10%" width="20%" disabled="true"/>
		<adsm:textbox dataType="text" size="20" property="vlFrete" style="text-align:right" label="valorFrete" labelWidth="10%" width="20%" disabled="true"/>

		<adsm:textbox width="20%" labelWidth="18%" size="20" dataType="text" property="tpConhecimento" label="indicadorCooperacao" disabled="true"/>

		
		<adsm:textbox width="12%" labelWidth="10%" size="8" dataType="integer" property="volumes" label="volumes" disabled="true"/>
		<adsm:textbox width="15%" labelWidth="10%" size="14" dataType="decimal" property="pesoReal" label="pesoReal" unit="kg" disabled="true" mask="###,###,###.0,00"/>
		<adsm:textbox width="15%" labelWidth="10%" size="14" dataType="decimal" property="psAforado" label="pesoCubado" unit="kg" disabled="true" mask="###,###,###.0,00"/>

		<adsm:textbox width="17%" size="20" labelWidth="12%" dataType="text" style="text-align:right" property="valorMercadoria" label="valorMercadoria2" disabled="true"/>
		
		<adsm:textbox width="10%" labelWidth="7%" size="15" dataType="text" property="dsNaturezaProduto" label="natureza" disabled="true"/>

		<adsm:textbox label="remetente" labelWidth="10%" width="14%" size="17" maxLength="20" dataType="text" property="nrIdentificacaoRem" disabled="true"/>
        <adsm:textbox width="26%" size="30" dataType="text" property="nmPessoaRem" disabled="true"/>
        
		<adsm:textbox label="destinatario" labelWidth="10%" width="14%" size="17" maxLength="20" dataType="text" property="nrIdentificacaoDest" disabled="true" />
        <adsm:textbox width="26%" size="30" dataType="text" property="nmPessoaDest" disabled="true"/>

		<adsm:section caption="entrega"/>
			<adsm:textbox label="previsao" labelWidth="10%" width="20%" size="11" maxLength="10" dataType="JTDate" property="dtPrevisaoEntrega" disabled="true"/>
	        	
			<adsm:textbox label="dataEntrega" labelWidth="15%" width="20%" size="22" maxLength="10" dataType="text" property="dtEntrega" disabled="true"/>
	        	
			<adsm:textbox labelWidth="10%" width="90%" size="35" dataType="text" property="dsLocalizacaoMercadoria" label="localizacao" disabled="true"/>
	</adsm:form>
</adsm:window>  
<script>

function findByIdDetalhamentoAbaParcerias(){
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
	
    _serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findCooperadaByIdConhecimento", "onDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
}
</script>