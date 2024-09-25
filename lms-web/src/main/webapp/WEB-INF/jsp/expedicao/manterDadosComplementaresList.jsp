<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/manterDadosComplementares">
		<adsm:combobox property="tipoProduto" optionLabelProperty="value" optionProperty="0" service="" label="tipoProduto" />
		<adsm:combobox property="naturezaProduto" optionLabelProperty="value" optionProperty="0" service="" label="naturezaProduto" />
		<adsm:textbox maxLength="80" dataType="text" property="descricao" label="descricao" size="83" width="85%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true">
		<adsm:gridColumn title="tipoProduto" property="tipoProduto" width="25%" />
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto" width="25%" />
		<adsm:gridColumn title="descricao" property="descricao" width="50%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
