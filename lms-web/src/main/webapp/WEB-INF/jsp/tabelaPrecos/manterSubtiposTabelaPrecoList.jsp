<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.subtipoTabelaPrecoService">
	<adsm:form action="/tabelaPrecos/manterSubtiposTabelaPreco"  idProperty="idSubtipoTabelaPreco">
		<adsm:combobox property="tpTipoTabelaPreco" label="tipoTabela" domain="DM_TIPO_TABELA_PRECO" width="20%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="50%" />
		<adsm:textbox dataType="text" property="tpSubtipoTabelaPreco" label="subTipo" maxLength="1" size="4" width="20%"/>
		<adsm:textbox dataType="text" property="dsSubtipoTabelaPreco" label="identificacao" maxLength="60" size="60" width="50%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="subtipoTabelaPreco"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idSubtipoTabelaPreco" property="subtipoTabelaPreco" gridHeight="200" unique="true" defaultOrder="tpTipoTabelaPreco,tpSubtipoTabelaPreco" rows="13">
		<adsm:gridColumn title="tipoTabela" property="tpTipoTabelaPreco" width="15%" isDomain="true"/>
		<adsm:gridColumn title="subTipo" property="tpSubtipoTabelaPreco" width="15%" />
		<adsm:gridColumn title="identificacao" property="dsSubtipoTabelaPreco" width="55%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="15%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>