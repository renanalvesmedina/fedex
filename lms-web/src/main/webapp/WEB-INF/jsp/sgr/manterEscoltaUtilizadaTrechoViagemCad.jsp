<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sgr/manterEscoltaUtilizadaTrechoViagem">
		<adsm:lookup dataType="text" property="controleCargas" label="controleCargas" size="20" width="40%" action="/carregamento/manterControleCargas" cmd="list" service="" required="true" />
		<adsm:textbox dataType="JTDate" property="data" label="data" required="false" labelWidth="12%" width="33%" disabled="true" />

		<adsm:textbox property="veiculo" label="veiculo" dataType="text" size="6%" width="8%" maxLength="8" disabled="true" />
		<adsm:lookup dataType="text" property="placa" size="10%" action="/contratacaoVeiculos/manterMeiosTransporte" service="" width="32%" disabled="true" />
		<adsm:textbox property="semiReboque" label="semiReboque" dataType="text" size="6%" width="8%" labelWidth="12%" maxLength="8" disabled="true" />
		<adsm:lookup dataType="text" property="placaReboque" size="10%" action="/contratacaoVeiculos/manterMeiosTransporte" service="" width="25%" disabled="true" />

		<adsm:lookup property="origemEscolta.id" label="origemEscolta" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="3" maxLength="3" labelWidth="15%" width="85%" required="true" >
			<adsm:propertyMapping modelProperty="origemEscolta.id" formProperty="nomeOrigemEscolta" />
			<adsm:textbox dataType="text" property="nomeOrigemEscolta" size="50" maxLength="50" disabled="false" />
		</adsm:lookup>

		<adsm:lookup property="destinoEscolta.id" label="destinoEscolta" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="3" maxLength="3" labelWidth="15%" width="85%" required="true" >
			<adsm:propertyMapping modelProperty="destinoEscolta.id" formProperty="nomeDestinoEscolta" />
			<adsm:textbox dataType="text" property="nomeDestinoEscolta" size="50" maxLength="50" disabled="false" />
		</adsm:lookup>

		<adsm:combobox property="moeda" label="valor" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="USD|REAL" labelWidth="15%" width="12%" />
		<adsm:textbox property="valor" dataType="currency" size="20" width="50%" maxLength="20" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="salvar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>