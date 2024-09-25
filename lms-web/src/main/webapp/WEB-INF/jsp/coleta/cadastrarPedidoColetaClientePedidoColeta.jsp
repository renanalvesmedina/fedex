<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="cadastrarPedidoColetaCliente">

	<!-- Para quando sairem os botões temporários:    lines="1" na button bar > -->
	<!-- Para quando sairem os botões temporários:    height="388"            > -->
	<adsm:form action="/coleta/cadastrarPedidoColetaCliente" height="390" >

		<adsm:textbox label="numero" property="numero" dataType="integer" disabled="true" labelWidth="21%" width="79%"/>

		<adsm:lookup label="cliente" idProperty="" dataType="text" property="cliente" service="" action="/vendas/manterDadosIdentificacao" cmd="list" width="79%" labelWidth="21%" size="18" maxLength="18" disabled="true" required="true">
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox label="contato" property="contato" dataType="text" required="true" labelWidth="21%" width="29%"/>
		<adsm:textbox label="telefone" property="ddd" dataType="integer" size="3" maxValue="3" labelWidth="15%" width="6%"/>
		<adsm:lookup property="telefone" idProperty="" dataType="integer" disabled="false" required="true" action="/coleta/cadastrarPedidoColeta" cmd="selecionarTelefone" service="" width="23%"/>

		<adsm:combobox label="tipo" property="tipo" optionLabelProperty="" optionProperty="" service="" prototypeValue="Normal|Devolução|Coleta direta|Aeroporto" width="79%" labelWidth="21%" required="true"/>

		<adsm:textbox label="disponibilidadeColeta" property="dataDisponibilidade" dataType="JTDate" required="true" width="15%" labelWidth="21%"/>
		<adsm:textbox                               property="horaDisponibilidade" dataType="JTTime" required="true" width="14%"/>

		<adsm:textbox label="horarioLimite" property="horarioLimite" dataType="JTTime" required="true"/>

		<adsm:textbox label="horarioCorte" property="horarioCorte" dataType="JTTime" labelWidth="21%" width="29%" disabled="true"/>

		<adsm:textbox label="dataPrevColeta" property="dataPrevisaoColeta" dataType="JTDate" disabled="true"/>

		<adsm:textarea  label="endereco" property="endereco" maxLength="300" columns="90" rows="3" labelWidth="21%" width="79%" disabled="true" required="true">
			<a href="#"  onclick="javascript:window.open('cadastrarPedidoColeta.do?cmd=selecionarEndereco','selecionarEndereco','width=770,height=250')"><img alt="Cadastrar Pedido Coleta" src="../images/lookup-picker.gif" border="0"/></a>
			<!-- TODO: Isso não deve ser assim na construção. Verificar a possibilidade de renderizar uma textarea na lookup. -->
		</adsm:textarea>

		<adsm:lookup property="municipio" idProperty="" label="municipio" action="/municipios/manterMunicipios" service="" dataType="text" size="35" maxLength="50" labelWidth="21%" width="79%" disabled="true" />		

		<adsm:combobox label="valorTotal" property="moeda" optionLabelProperty="" optionProperty="" service="" disabled="true" labelWidth="21%" width="12%"/>
		<adsm:textbox property="valorTotal" dataType="currency"	disabled="true" width="17%" size="16" />

		<adsm:textbox label="volumes" property="volumes" dataType="integer" disabled="true" labelWidth="15%" width="35%"/>

		<adsm:textbox label="peso" property="peso" dataType="weight" disabled="true" labelWidth="21%" width="79%" unit="kg" size="6" maxLength="10" />

		<adsm:textarea label="observacoes" maxLength="300" property="observacoes" columns="90" rows="4" labelWidth="21%" width="79%"/>
 
		<adsm:combobox label="servicosAdicionais" optionLabelProperty="" optionProperty="" property="servicosAdicionais" service="" labelWidth="21%" width="29%">
			<adsm:listbox property="list" optionLabelProperty="" optionProperty="" service="" size="8" boxWidth="200"/>
		</adsm:combobox>

		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>