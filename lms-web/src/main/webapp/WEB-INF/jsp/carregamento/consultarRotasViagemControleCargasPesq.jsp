<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/carregamento/consultarRotasViagemControleCargas" height="130">
		<adsm:combobox property="tipoRota" label="tipoRota" width="85%" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="Expressa|Eventual" />

		<adsm:lookup service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="filiais" size="5" maxLength="10" width="85%"  action="/municipios/manterFiliais" required="true">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
	        <adsm:listbox property="" size="3" prototypeValue="" optionLabelProperty="2|1"  optionProperty="" service="false" boxWidth="100" />
		</adsm:lookup>

		<adsm:lookup property="origemId" label="origem" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" width="85%" >
			<adsm:propertyMapping modelProperty="origemId" formProperty="nomeOrigem"/>
			<adsm:textbox dataType="text" property="nomeOrigem" size="57" maxLength="50" disabled="true"/>
		</adsm:lookup>
	
		<adsm:lookup property="destinoId" label="destino" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" width="85%" >
			<adsm:propertyMapping modelProperty="destinoId" formProperty="nomeDestino"/>
			<adsm:textbox dataType="text" property="nomeDestino" size="57" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="JTTime" property="horaInicial" label="horarioSaida" width="7%" />
		<adsm:label key="ate" width="3%"/>
		<adsm:textbox dataType="JTTime" property="horaFinal" width="75%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" selectionMode="none" scrollBars="vertical" showPagging="false">
		<adsm:gridColumn title="rota" property="rota" width="17%"/>
		<adsm:gridColumn title="tipo" property="tipo" width="15%"/>
		<adsm:gridColumn title="horarioSaida" property="horarioSaida" width="15%" align="center"/>
		<adsm:gridColumn title="tempoViagem" property="tempoViagem" width="15%" align="center"/>
		<adsm:gridColumn title="distancia" property="distancia" width="10%" align="right"/>
		<adsm:gridColumn title="seg" property="segunda" width="4%"/>
		<adsm:gridColumn title="ter" property="terca" width="4%"/>
		<adsm:gridColumn title="qua" property="quarta" width="4%"/>
		<adsm:gridColumn title="qui" property="quinta" width="4%"/>
		<adsm:gridColumn title="sex" property="sexta" width="4%"/>
		<adsm:gridColumn title="sab" property="sabado" width="4%"/>
		<adsm:gridColumn title="dom" property="domingo" width="4%"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>