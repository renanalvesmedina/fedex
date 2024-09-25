<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.densidadeService">
	<adsm:form action="/expedicao/manterDensidades" idProperty="idDensidade">
		<adsm:combobox property="tpDensidade" domain="DM_DENSIDADES" label="codigo" required="true"/>
		<adsm:textbox maxLength="5" size="7" mask="##0.00" minValue="0.01" dataType="decimal" property="vlFator" labelWidth="20%" width="30%" label="fatorMultiplicacao" required="true"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" />
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>