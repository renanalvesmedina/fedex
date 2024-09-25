<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/manterDadosComplementares">
		<adsm:combobox property="tipoProduto" optionLabelProperty="value" optionProperty="0" service="" label="tipoProduto" required="true"/>
		<adsm:combobox property="naturezaProduto" optionLabelProperty="value" optionProperty="0" service="" label="naturezaProduto" required="true"/>
		<adsm:textbox maxLength="80" dataType="text" property="descricao" label="descricao" required="true" width="85%" size="83"/>
		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>