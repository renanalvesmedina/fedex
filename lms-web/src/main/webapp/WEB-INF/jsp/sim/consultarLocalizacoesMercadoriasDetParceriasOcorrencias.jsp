<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="220">
		<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" showPaging="false" unique="true" gridHeight="180">
			<adsm:gridColumn width="30%" title="dataHora" property="dataHora" align="center"/>
			<adsm:gridColumn width="35%" title="ocorrencia" property="ocorrencia" align="left"/>
			<adsm:gridColumn width="35%" title="observacao" property="observacao" align="left"/>
		</adsm:grid>
	</adsm:form>
</adsm:window>   