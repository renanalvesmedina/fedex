<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sgr/manterEscoltaUtilizadaTrechoViagem">
		<adsm:range label="periodo" width="80%">
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:lookup property="rota" label="rota" action="/municipios/consultarRotasViagem" service="" dataType="text" size="30" maxLength="40" width="40%" />
		<adsm:lookup dataType="text" property="controleCargas" label="controleCargas" size="20" width="30%" action="/carregamento/manterControleCargas" cmd="list" service="" />

		<adsm:textbox property="veiculo" label="veiculo" dataType="text" size="6%" width="8%" maxLength="8"  />
		<adsm:lookup dataType="text" property="placa" size="10%" action="/contratacaoVeiculos/manterMeiosTransporte" service="" width="32%"/>
		<adsm:textbox property="semiReboque" label="semiReboque" dataType="text" size="6%" width="8%" maxLength="8" />
		<adsm:lookup dataType="text" property="placaReboque" size="10%" action="/contratacaoVeiculos/manterMeiosTransporte" service="" width="22%" />

		<adsm:lookup property="origemEscolta.id" label="origemEscolta" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="3" maxLength="3" labelWidth="15%" width="85%" >
			<adsm:propertyMapping modelProperty="origemEscolta.id" formProperty="nomeOrigemEscolta" />
			<adsm:textbox dataType="text" property="nomeOrigemEscolta" size="50" maxLength="50" disabled="false"/>
		</adsm:lookup>

		<adsm:lookup property="destinoEscolta.id" label="destinoEscolta" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="3" maxLength="3" labelWidth="15%" width="85%" >
			<adsm:propertyMapping modelProperty="destinoEscolta.id" formProperty="nomeDestinoEscolta" />
			<adsm:textbox dataType="text" property="nomeDestinoEscolta" size="50" maxLength="50" disabled="false"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" selectionMode="none" gridHeight="200" unique="true" scrollBars="horizontal">
		<adsm:gridColumn property="controleCargas" title="controleCargas" width="120" />
		<adsm:gridColumn property="data" title="data" width="80" align="center"/>
		<adsm:gridColumn property="veiculo" title="veiculo" width="110" />
		<adsm:gridColumn property="semiReboque" title="semiReboque" width="110" />
		<adsm:gridColumn property="origem" title="origem" width="150" />
		<adsm:gridColumn property="destino" title="destino" width="150" />
		<adsm:gridColumn property="valor" title="valor" width="90" mask="###,###,###,###,##0.00" align="right" unit="reais" />
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
