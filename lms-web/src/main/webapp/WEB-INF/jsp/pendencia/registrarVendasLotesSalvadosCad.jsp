<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/registrarVendasLotesSalvados">

		<adsm:textbox dataType="text" label="lote" property="lote" labelWidth="17%" width="33%" disabled="true" />
		<adsm:textbox dataType="JTDate" label="dataGeracao" property="dataGeracao" labelWidth="18%" width="32%" disabled="true"/>

		<adsm:textbox dataType="integer" label="quantidadeVolumes" property="quantidadeVolumes" labelWidth="17%" width="33%" disabled="true" />
		<adsm:combobox label="valorTotalMercadoria" property="moedaValorTotal" optionLabelProperty="" optionProperty="" service="" labelWidth="18%" width="32%" disabled="true" >
			<adsm:textbox property="valorTotalMercadoria" dataType="currency" disabled="true"/>
		</adsm:combobox>

		<adsm:textbox dataType="JTDate" label="dataVenda" property="nomeModulo" labelWidth="17%" width="33%" required="true" />
		<adsm:textbox dataType="JTDate" label="dataEntrega" property="nomeModulo" labelWidth="18%" width="32%" />

		<adsm:combobox label="valorPago" property="moedaValorPago" optionLabelProperty="" optionProperty="" service="" labelWidth="17%" width="83%" required="true" >
			<adsm:textbox property="valorPago" dataType="currency" />
		</adsm:combobox>

		<adsm:lookup property="comprador.id" label="comprador" dataType="text" criteriaProperty="" action="/vendas/manterDadosIdentificacao" service="" labelWidth="17%" width="83%" required="true" >
			<adsm:propertyMapping modelProperty="comprador.id" formProperty="nomeComprador"/>
			<adsm:textbox property="nomeComprador" dataType="text" size="50" maxLength="50" />
		</adsm:lookup>

		<adsm:textarea  label="endereco" property="endereco" maxLength="300" columns="90" rows="3" labelWidth="17%" width="83%" disabled="true" required="true" >
			<a href="#"  onclick="javascript:window.open('registrarVendasLotesSalvados.do?cmd=selecionarEndereco','selecionarEndereco','width=770,height=250')"><img alt="endereco" src="../images/lookup-picker.gif" border="0"/></a>
		</adsm:textarea>

		<adsm:textbox label="telefone" property="ddd" dataType="integer" size="3" maxValue="3" labelWidth="17%" width="33%" >
			<adsm:lookup property="telefone" dataType="integer" action="/coleta/cadastrarPedidoColeta" cmd="selecionarTelefone" service="" />
		</adsm:textbox>
		<adsm:textbox dataType="text" label="contato" property="contato" labelWidth="18%" width="32%" required="true" />

		<adsm:combobox label="formaPagamento" property="formaPagamento" optionLabelProperty="" optionProperty="" service="" prototypeValue="Dinheiro|Cheque|Depósito em Conta" labelWidth="17%" width="33%" required="true" />

		<adsm:textbox dataType="date" label="dataVencimento" property="data" labelWidth="18%" width="32%" />

		<adsm:buttonBar>
			<adsm:button caption="salvar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
