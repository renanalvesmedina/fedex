<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/contasReceber/manterNotasCreditoInternacionais">

        <adsm:textbox label="faturaOriginal" labelWidth="22%" dataType=	"text" property="filialRomaneio" width="7%" size="4" />
		<adsm:lookup dataType="integer" service="" action="/contasReceber/manterFaturas" 
				property="romaneio.id" width="23%" size="10" required="true"/>

		<adsm:textbox labelWidth="20%" dataType="text" label="valorTotalNota" property="valorTotalNota" size="10" maxLength="18" width="28%" required="true" />

        <adsm:lookup action="/municipios/manterFiliais"  service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="filialFaturamento" labelWidth="22%" size="5" maxLength="5" width="30%" disabled="true">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="20" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" label="numero" labelWidth="20%" property="numero" size="10" maxLength="10" width="28%" disabled="true"/>
        <adsm:textbox labelWidth="22%" width="30%" label="dataEmissao" dataType="JTDate" property="emissaoInicial" size="10" required = "true"/>

		<adsm:textbox dataType="text" label="valorIVA" labelWidth="20%" property="valorIVA" size="10" maxLength="10" width="28%" required="true"/>

        <adsm:combobox property="situacaoAprovacao" label="situacaoAprovacao" service="" 
				prototypeValue="Em aprovação|Aprovado|Cancelado" 
				width="30%" labelWidth="22%" optionLabelProperty="" optionProperty="" disabled="true" >
		</adsm:combobox>

 		<adsm:combobox property="situacaoNota" label="situacaoNota" service="" 
				prototypeValue="Digitado|Emitido|Em Boleto|Em Redeco|Liquidado|Cancelado a Transmitir|Cancelado|Inutilizado" 
				width="28%" labelWidth="20%" optionLabelProperty="" optionProperty="" required="true">
		</adsm:combobox>

		<adsm:textarea label="descricao" property="descricao" labelWidth="22%" width="78%" maxLength="80" 
			columns="100" rows="2" required="true"/>

		<adsm:section caption="dadosFaturaOriginal" />
		
		<adsm:lookup action="/configuracoes/manterClientes"  service="" dataType="integer" property="cliente.id" criteriaProperty="cliente.codigo" 
			label="clienteResponsavel" size="14" maxLength="5" width="78%" labelWidth="22%" disabled="true">
			<adsm:propertyMapping modelProperty="cliente" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="77" maxLength="50" disabled="true"/>
		</adsm:lookup>
			
		<adsm:lookup action="/municipios/manterFiliais"  service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" 
			label="filialCobrancaCliente" labelWidth="22%" size="5" maxLength="5" width="30%" disabled="true">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="20" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox labelWidth="20%" dataType="text" label="numeroPreImpresso" property="numeroPreImpresso" size="10" maxLength="10" width="28%" disabled="true"/>

		<adsm:lookup action="/municipios/manterFiliais"  service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" 
			label="filialCobranca" labelWidth="22%" size="5" maxLength="5" width="30%" disabled="true">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="20" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox property="cedente" label="cedente" service="" 
				prototypeValue="" width="28%" labelWidth="20%"
				optionLabelProperty="" optionProperty="" disabled="true"/>

		<adsm:combobox label="modal" property="modal" labelWidth="22%" service="" 
				prototypeValue="Aéreo|Rodoviário" width="30%"
				optionLabelProperty="" optionProperty="" disabled="true"/>
		<adsm:combobox property="abrangencia" label="abrangencia" service="" 
				prototypeValue="Nacional|Internacional" width="28%" labelWidth="20%"
				optionLabelProperty="" optionProperty="" disabled="true"/>

		<adsm:combobox property="divisao" label="divisao" labelWidth="22%" service="" 
				prototypeValue="Produtos farmacêuticos|Produtos hospitalares" width="30%"
				optionLabelProperty="" optionProperty="" disabled="true"/>

		
		<adsm:combobox property="formaAgrupamento" label="formaAgrupamento" service="" 
				prototypeValue="Tipo de frete|Natureza da mercadoria" width="28%" labelWidth="20%"
				optionLabelProperty="" optionProperty="" disabled="true"/>
		<adsm:combobox property="tipoAgrupamento" label="tipoAgrupamento" service="" 
				prototypeValue="CIF|FOB" width="30%" labelWidth="22%"
				optionLabelProperty="" optionProperty="" disabled="true"/>

		<adsm:combobox property="situacaoFatura" label="situacaoFatura" service="" 
				prototypeValue="Digitado|Emitido|Em Boleto|Em Redeco|Liquidado|Cancelado a Transmitir|Cancelado|Inutilizado" 
				width="28%" labelWidth="20%" optionLabelProperty="" optionProperty="" disabled="true">
				<a href="#" onclick="javascript:window.open('manterNotasCreditoInternacionais.do?cmd=descricao','descricao','width=500,height=130')"><img alt="editar" src="../images/edit.gif" border="0"/></a>
		</adsm:combobox>

		<adsm:textbox label="cotacao" labelWidth="22%" dataType="text" property="moeda" width="7%" size="4" disabled="true"/>
		<adsm:textbox property="dataCotacao" dataType="text" size="7" width="9%" disabled="true"/>
		<adsm:lookup property="cotacao" action="/configuracoes/manterCotacoesMoedas" dataType="currency" service="" size="5" width="14%" />

		<adsm:textbox labelWidth="20%" dataType="JTDate" label="dataEmissao" property="dataEmissao" size="8" maxLength="20" width="28%" disabled="true"/>
		<adsm:textbox labelWidth="22%" dataType="JTDate" label="dataVencimento" property="dataVencimento" size="8" maxLength="20" width="30%" disabled="true"/>
		<adsm:textbox labelWidth="20%" dataType="text" label="dataLiquidacao" property="dataLiquidacao" size="10" maxLength="20" width="28%" disabled="true"/>
		<adsm:textbox labelWidth="22%" dataType="text" label="recibo" property="recibo" size="10" maxLength="10" width="30%" disabled="true"/>
		<adsm:complement label="relacaoCobranca" labelWidth="20%" width="28%">
			<adsm:textbox dataType="text" property="filialRelacao" size="3" width="4%" disabled="true" maxLength="3"/>
			<adsm:textbox dataType="text" property="numeroRelacao" size="6" width="9%" disabled="true" maxLength="10"/>
		</adsm:complement>

		<adsm:section caption="totaisImpostos" />

		<adsm:textbox labelWidth="22%" dataType="text" label="qtdeTotalDocumentos" property="qtdeTotalDocumentos" size="10" maxLength="6" width="14%" disabled="true"/>
		<adsm:textbox labelWidth="18%" dataType="text" label="valorIVA" property="" size="10" maxLength="20" width="14%" disabled="true"/>
		<adsm:textbox labelWidth="18%" dataType="text" label="valorTotalFrete" property="valorTotalFrete" size="10" maxLength="18" width="14%" disabled="true"/>
		<adsm:textbox labelWidth="22%" dataType="text" label="valorTitulo" property="valorTotalRecebido" size="10" maxLength="18" width="14%" disabled="true"/>
		<adsm:textbox labelWidth="18%" dataType="text" label="valorJurosCalc" property="valorJurosCalc" size="10" maxLength="18" width="46%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button caption="emitir" action="/contasReceber/manterBoletos" cmd="main"/>
			<adsm:button caption="relacaoCobranca" action="/contasReceber/pesquisarRelacoesCobranca" cmd="main"/>
			<adsm:button caption="redeco" action="/contasReceber/manterRedeco" cmd="main"/>
			<adsm:button caption="reciboOficial" action="/contasReceber/manterReciboOficial" cmd="main"/>
            <adsm:button caption="cancelar"/>
			<adsm:button caption="novo" />
			<adsm:button caption="salvar" />
			<adsm:button caption="excluir" />
		</adsm:buttonBar>

	</adsm:form>	
</adsm:window>