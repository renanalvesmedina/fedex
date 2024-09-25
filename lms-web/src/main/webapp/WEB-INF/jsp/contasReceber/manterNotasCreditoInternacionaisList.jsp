<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>
	<adsm:form action="/contasReceber/manterNotasCreditoInternacionais">
        
		<adsm:lookup action="/municipios/manterFiliais"  service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" 
			label="filialFaturamento" labelWidth="22%" size="5" maxLength="5" width="30%" >
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="20" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" label="numero" labelWidth="20%" property="numero" size="10" maxLength="10" width="28%" />

		<adsm:textbox label="faturaOriginal" labelWidth="22%" dataType=	"text" property="filialRomaneio" width="7%" size="4" />
		<adsm:lookup dataType="integer" service="" action="/contasReceber/manterFaturas" property="romaneio.id" width="23%" size="10" />

        <adsm:combobox property="situacaoAprovacao" label="situacaoAprovacao" service="" 
				prototypeValue="Em aprovação|Aprovado|Cancelado" 
				width="28%" labelWidth="20%" optionLabelProperty="" optionProperty="" >
		</adsm:combobox>


        <adsm:lookup action="/configuracoes/manterClientes"  service="" dataType="integer" property="cliente.id" criteriaProperty="cliente.codigo" 
			label="clienteResponsavel" size="14" maxLength="5" width="78%" labelWidth="22%" >
			<adsm:propertyMapping modelProperty="cliente" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="77" maxLength="50" disabled="true"/>
		</adsm:lookup>

        <adsm:range label="dataEmissao" labelWidth="22%" width="30%">
			<adsm:textbox dataType="JTDate" property="emissaoInicial" size="10" />
	    	<adsm:textbox dataType="JTDate" property="emissaoFinal"  size="10"/>
		</adsm:range>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true">
		<adsm:gridColumn title="notaCredito" property="notaCredito" />
		<adsm:gridColumn title="dataEmissao" property="dataEmissao" />
		<adsm:gridColumn title="situacaoAprovacao" property="situacaoAprovacao" />
		<adsm:gridColumn title="faturaOriginal" property="faturaOriginal" />
		<adsm:gridColumn title="valorTotalNota" property="valorTotalNota" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>