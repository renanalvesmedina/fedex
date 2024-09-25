<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.causaNaoConformidadeService" >
	<adsm:form action="/rnc/manterCausasNaoConformidade" idProperty="idCausaNaoConformidade" >
		<adsm:textbox property="dsCausaNaoConformidade" label="descricao" dataType="text" size="60" maxLength="50" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="causasNaoConformidade"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="causasNaoConformidade" idProperty="idCausaNaoConformidade" defaultOrder="dsCausaNaoConformidade:asc" selectionMode="check" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn property="dsCausaNaoConformidade" title="descricao" width="90%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
