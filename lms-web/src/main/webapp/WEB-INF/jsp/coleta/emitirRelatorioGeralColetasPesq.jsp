<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/coleta/emitirRelatorioGeralColetas">
		<adsm:lookup property="filial.id" label="filial" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" width="85%" required="true" >
			<adsm:propertyMapping modelProperty="filial.id" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" required="true"/>
		</adsm:lookup>

		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:multicheckbox property="status" label="status" texts="aberta|transmitida|manifestada|executada|cancelada" width="85%" cellStyle="vertical-align:bottom;" />
		<adsm:label key="espacoBranco" width="15%" style="border:none;"/>
		<adsm:multicheckbox property="status"  texts="aguardandoDescarga|emDescarga|noTerminal|finalizada" width="85%" cellStyle="vertical-align:bottom;" />

		<adsm:lookup property="rota" label="rota" action="/municipios/consultarRotasViagem" service="" dataType="text" width="85%"/>
			
		<adsm:lookup property="regiao" label="regiao" action="/municipios/manterRegioesColetaEntregaFiliais" service="" dataType="text" width="85%"/>

		<adsm:combobox property="servico" label="servico" width="85%" optionLabelProperty="label" service="" optionProperty=""/>

		<adsm:lookup property="cliente.id" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" width="85%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		<adsm:textbox property="frota" label="veiculo" dataType="text" size="6%" maxLength="8" labelWidth="15%" width="23%" >
			<adsm:lookup dataType="text" property="placa" size="10%" action="/contratacaoVeiculos/manterMeiosTransporte" service="" />
		</adsm:textbox>
		<adsm:lookup property="destino.id" label="destino" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" width="85%" >
			<adsm:propertyMapping modelProperty="destino.id" formProperty="nomeDestino"/>
			<adsm:textbox dataType="text" property="nomeDestino" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		<adsm:combobox property="tipoColeta" label="tipoColeta" width="85%" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="Normal|Devolução|Coleta direta|Aeroporto"/>
		<adsm:combobox property="agruparPor" label="agruparPor" width="85%" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="Rota|Cliente|Destino|Veículo"/>
		<adsm:buttonBar freeLayout="false">
			<adsm:reportViewerButton caption="rota" service="coleta/emitirRelatorioGeralColetasRota.jasper"/>
			<adsm:reportViewerButton caption="cliente" service="coleta/emitirRelatorioGeralColetasCliente.jasper"/>
			<adsm:reportViewerButton caption="destino" service="coleta/emitirRelatorioGeralColetasDestino.jasper"/>
			<adsm:reportViewerButton caption="veiculo" service="coleta/emitirRelatorioGeralColetasVeiculo.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>