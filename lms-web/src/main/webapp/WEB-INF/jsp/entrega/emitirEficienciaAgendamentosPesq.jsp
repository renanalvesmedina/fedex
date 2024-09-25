<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/entrega/emitirEficienciaAgendamentos">
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="6" maxLength="3" labelWidth="20%" width="11%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/> 
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeFilial" size="18" maxLength="50" disabled="true" width="69%" required="true" />
		<adsm:combobox property="servico.nome" label="servico" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:lookup service="" dataType="text" property="cliente.id" criteriaProperty="cliente.codigo" label="remetente" size="6" maxLength="50" labelWidth="20%" width="11%" action="/vendas/manterClientes" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="nomeCliente" size="18" maxLength="50" disabled="true" width="69%" />
		<adsm:lookup service="" dataType="text" property="cliente.id" criteriaProperty="cliente.codigo" label="destinatario" size="6" maxLength="50" labelWidth="20%" width="11%" action="/vendas/manterClientes" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="nomeCliente" size="18" maxLength="50" disabled="true" width="69%" />
		<adsm:combobox property="agendamentoEntrega.tipoAgendamento" label="tipoAgendamento" prototypeValue="Tentativa|Agendamento por horário|Agendamento por turno|Reagendamento" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:range label="periodo" labelWidth="20%" width="80%" required="true" >
			<adsm:textbox dataType="date" property="periodoInicial"/> 
			<adsm:textbox dataType="date" property="periodoFinal"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/entrega/emitirEficienciaAgendamentos.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>