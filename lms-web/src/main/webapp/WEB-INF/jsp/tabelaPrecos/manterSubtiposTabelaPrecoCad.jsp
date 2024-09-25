<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.subtipoTabelaPrecoService">
	<adsm:form action="/tabelaPrecos/manterSubtiposTabelaPreco" idProperty="idSubtipoTabelaPreco">
		<adsm:combobox property="tpTipoTabelaPreco" label="tipoTabela" domain="DM_TIPO_TABELA_PRECO" width="20%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="50%" required="true" />
		<adsm:textbox dataType="text" property="tpSubtipoTabelaPreco" label="subTipo" maxLength="1" size="4" required="true" width="20%"/>
		<adsm:textbox dataType="text" property="dsSubtipoTabelaPreco" label="identificacao" maxLength="60" size="60" width="50%"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form> 
</adsm:window>