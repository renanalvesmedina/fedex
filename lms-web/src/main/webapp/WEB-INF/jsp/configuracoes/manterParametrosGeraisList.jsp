<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.parametroGeralService">
	<adsm:form action="/configuracoes/manterParametrosGerais" idProperty="idParametroGeral">
		<adsm:textbox dataType="text" width="85%" maxLength="60" property="nmParametroGeral" label="nome" size="60"/>
		<adsm:textbox dataType="text" size="60" width="85%" maxLength="60" property="dsParametro" label="descricao"/>
				<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parametrosGerais"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idParametroGeral" defaultOrder="nmParametroGeral" property="parametrosGerais" selectionMode="check" gridHeight="200" rows="13">
		<adsm:gridColumn title="nome" property="nmParametroGeral" width="30%" />
		<adsm:gridColumn title="descricao" property="dsParametro" width="45%" />
		<adsm:gridColumn title="tamanho" property="nrTamanho" width="10%" dataType="integer" />
		<adsm:gridColumn title="formato" property="tpFormato" isDomain="true" width="15%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>