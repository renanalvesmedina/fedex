<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.densidadeService">
	<adsm:form action="/expedicao/manterDensidades" idProperty="idDensidade">
		<adsm:combobox property="tpDensidade" domain="DM_DENSIDADES" label="codigo" />
				<adsm:textbox maxLength="5" size="7" mask="##0.00" minValue="0,01" dataType="decimal" property="vlFator" labelWidth="20%" width="30%" label="fatorMultiplicacao" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="densidade" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="densidade" idProperty="idDensidade" defaultOrder="tpDensidade, vlFator" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn title="codigo" property="tpDensidade" isDomain="true" width="30%" />
		<adsm:gridColumn title="fatorMultiplicacao" property="vlFator" dataType="decimal" width="40%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="30%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
