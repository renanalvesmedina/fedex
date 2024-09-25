<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.tarifaPrecoService">
	<adsm:form action="/tabelaPrecos/manterTarifasPreco" idProperty="idTarifaPreco">
		<adsm:textbox dataType="text" property="cdTarifaPreco" label="tarifa" maxLength="5" size="7" labelWidth="20%" width="30%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="20%" width="30%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tarifaPreco"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTarifaPreco" property="tarifaPreco" gridHeight="200" unique="true" defaultOrder="cdTarifaPreco" rows="14">
		<adsm:gridColumn title="tarifa" property="cdTarifaPreco" width="25%" />
		<adsm:gridColumn dataType="integer" title="quilometragemInicialAnterior" property="nrKmInicial" width="15%" />
		<adsm:gridColumn dataType="integer" title="quilometragemFinalAnterior" property="nrKmFinal" width="15%" />
		<adsm:gridColumn dataType="integer" title="quilometragemInicialAtual" property="nrKmInicialAtual" width="15%" />
		<adsm:gridColumn dataType="integer" title="quilometragemFinalAtual" property="nrKmFinalAtual" width="15%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="15%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>