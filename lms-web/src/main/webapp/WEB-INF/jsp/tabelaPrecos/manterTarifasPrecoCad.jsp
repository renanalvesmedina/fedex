<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.tarifaPrecoService">
	<adsm:form action="/tabelaPrecos/manterTarifasPreco" idProperty="idTarifaPreco">
		<adsm:textbox labelWidth="20%" dataType="text" property="cdTarifaPreco" label="tarifa" maxLength="5" size="7" required="true" width="30%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="20%" width="30%" required="true" />
		<adsm:textbox labelWidth="20%" minValue="1" smallerThan="nrKmFinal" dataType="integer" property="nrKmInicial" label="quilometragemInicialAnterior" maxLength="10" width="30%" size="10"/>
		<adsm:textbox labelWidth="20%" minValue="1" biggerThan="nrKmInicial" dataType="integer" property="nrKmFinal" label="quilometragemFinalAnterior" maxLength="10" width="30%" size="10"/>
		<adsm:textbox labelWidth="20%" minValue="1" smallerThan="nrKmFinalAtual" dataType="integer" property="nrKmInicialAtual" label="quilometragemInicialAtual" maxLength="10" width="30%" size="10"/>
		<adsm:textbox labelWidth="20%" minValue="1" biggerThan="nrKmInicialAtual" dataType="integer" property="nrKmFinalAtual" label="quilometragemFinalAtual" maxLength="10" width="30%" size="10"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>