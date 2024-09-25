<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarClientes">
	<adsm:form action="/df01">

		<adsm:combobox label="identificacao" property="identificacao" optionLabelProperty="a" optionProperty="0" service="" prototypeValue="CPF/CNPJ|CUIT/DNI|RUT|RUC" labelWidth="19%" width="12%"/>
		<adsm:textbox dataType="text" property="numeroIdentificacao" maxLength="20" size="17" width="19%"/>

		<adsm:textbox dataType="text" size="53" property="nome" label="nomeRazaoSocial" maxLength="50" labelWidth="19%" width="81%" />
		<adsm:textbox dataType="text" property="nomeFantasia" label="nomeFantasia" labelWidth="19%" width="31%" maxLength="50" size="36"/>
		<adsm:textbox dataType="text" property="numeroConta" label="numeroConta" labelWidth="20%" width="30%" maxLength="12" size="15"/>
		<adsm:combobox property="tipoCliente" optionLabelProperty="value" optionProperty="0" service="" label="tipoCliente" labelWidth="19%" width="31%" prototypeValue="Especial|Eventual|Potencial"/>
		<adsm:combobox property="situacao" optionLabelProperty="value" optionProperty="0" service="" label="situacao" labelWidth="20%" width="30%" prototypeValue="Ativo|Inativo"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true">
		<adsm:gridColumn title="identificacao" property="tipoIdentificacao" width="10%" />
		<adsm:gridColumn title="nome" property="nome" width="20%" />
		<adsm:gridColumn title="nomeFantasia" property="nomeFantasia" />
		<adsm:editColumn title="numeroConta" property="numero" width="160" field="ComboBox"/>
		<adsm:gridColumn title="tipoCliente" property="tipoCliente" />
		<adsm:editColumn title="situacao" property="tipo" width="160" field="TextBox" dataType="Date" />
		<adsm:buttonBar>
			<adsm:button caption="novo" onClick="javascript:window.open('cadastrarPedidoColeta.do?cmd=cadastrarClientes','cadastrarClientesColeta','width=690,height=260')" />
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
