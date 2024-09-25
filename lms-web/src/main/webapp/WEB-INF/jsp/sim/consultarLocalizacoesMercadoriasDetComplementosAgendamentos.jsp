<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="130" idProperty="idDoctoServico" service="lms.sim.consultarLocalizacoesMercadoriasAction.findAgendamentoByIdAgendamento">
	
		<adsm:textbox width="30%" dataType="text" size="3" property="sgFilial" label="filial" labelWidth="20%" disabled="true">
		<adsm:textbox dataType="text" property="nmFantasia" size="30" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox width="30%" size="25" dataType="text" property="tpAgendamento" label="tipoAgendamento" disabled="true" labelWidth="20%"/>
		<adsm:textbox width="30%" size="25" dataType="text" property="nmUsuarioCriacao" label="registradoPor" disabled="true" labelWidth="20%"/>
		<adsm:textbox width="30%" size="25" dataType="text" property="dhContato" label="dataHoraContato" disabled="true" labelWidth="20%"/>
		<adsm:textbox width="30%" size="25" dataType="text" property="nmContato" label="contato" disabled="true" labelWidth="20%"/>
		<adsm:textbox width="30%" size="25" dataType="text" property="nrTelefone" label="telefone" disabled="true" labelWidth="20%"/>
		<adsm:textbox width="30%" size="25" dataType="integer" property="nrRamal" label="ramal" disabled="true" labelWidth="20%"/>
		<adsm:textbox width="30%" size="25" dataType="text" property="tpSituacaoAgendamento" label="situacao" disabled="true" labelWidth="20%" />
		<adsm:textarea property="obAgendamentoEntrega" maxLength="50"  label="observacao" columns="104" labelWidth="20%" width="78%" disabled="true"/>
		<adsm:textbox width="30%" size="25" dataType="JTDate" property="dtAgendamento" label="dataAgendamento" disabled="true" labelWidth="20%"/>
		<adsm:textbox width="30%" size="25" dataType="text" property="dsTurno" label="turno" disabled="true" labelWidth="20%"/>

		<adsm:textbox width="10%" size="4" dataType="JTTime" property="hrPreferenciaInicial" label="preferencia" disabled="true" labelWidth="20%" unit="ate"/>
		<adsm:textbox width="15%" size="4" dataType="JTTime" property="hrPreferenciaFinal" disabled="true"/>
		<adsm:checkbox property="blCartao" label="cartaoCredito" labelWidth="20%" width="78%" disabled="true"/>
		<adsm:textbox width="30%" size="40" dataType="text" property="dsMotivoAgendamento" label="motivoAlteracaoCancelamento" disabled="true" labelWidth="20%" cellStyle="vertical-align:bottom;"/>
		<adsm:textbox width="30%" size="25" dataType="text" property="dhCancelamento" label="dataHoraCancelamento" disabled="true" labelWidth="20%" cellStyle="vertical-align:bottom;" />
		<adsm:textbox width="30%" size="25" dataType="text" property="nmUsuarioCancel" label="canceladoPor" disabled="true" labelWidth="20%"/>
	</adsm:form>
        <%--adsm:section caption="historico"/--%>
			<adsm:grid property="agendamentoEntrega" scrollBars="vertical" idProperty="idAgendamentoEntrega" showPagging="false" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedAgendamentosByDoctoServico" showGotoBox="false" selectionMode="none" unique="false" gridHeight="40" detailFrameName="complementosAgendamentos" >
					<adsm:gridColumn width="150" title="tipoAgendamento" property="tpAgendamento" align="left" isDomain="true"/>
					<adsm:gridColumn width="150" title="dataHoraContato" property="dhContato" dataType="JTDateTimeZone" align="center"/>
					<adsm:gridColumn width="50" title="contato" property="nmContato" dataType="text"/>
					<adsm:gridColumn width="150" title="agendamento" property="agendamento" align="center" dataType="text"/>
					<adsm:gridColumn width="50"  title="cartao" property="blCartao" renderMode="image-check" />
					<adsm:gridColumn width="100"  title="situacao" property="tpSituacaoAgendamento" isDomain="true" align="left"/>
			</adsm:grid>
	
</adsm:window>   
<script>


function findAgendamentos(){
	newButtonScript();
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
 	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findAgendamentosByDoctoServico", "onDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
  	
  	var data = new Array();
  	setNestedBeanPropertyValue(data,"idDoctoServico", idDoctoServico);
  	agendamentoEntregaGridDef.executeSearch(data);
}  	
</script>