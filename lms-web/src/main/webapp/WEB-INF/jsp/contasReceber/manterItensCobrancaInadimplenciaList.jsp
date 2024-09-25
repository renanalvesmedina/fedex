<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>
	<adsm:form action="/contasReceber/manterItensCobrancaInadimplencia">

		<adsm:textbox label="cliente" property="cliente" dataType="text" size="40" maxLength="50" disabled="true"/>
		<adsm:textbox label="responsavel" property="responsavel" dataType="text" size="40" maxLength="50" disabled="true"/>

		<adsm:textbox label="descricao" property="descricao" dataType="text" size="40" maxLength="60" width="85%" disabled="true"/>

		<adsm:textbox label="fatura" property="filialRomaneio" dataType="text" size="3" maxLength="3" width="5%"/>
		<adsm:lookup property="" action="/contasReceber/manterFaturas" dataType="text" service="" size="10" maxLength="10" width="40%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true">
		<adsm:gridColumn title="fatura" property="romaneio" width="100%"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>