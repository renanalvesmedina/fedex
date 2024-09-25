<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contasReceber/manterReciboOficial">

		<adsm:lookup action="/configuracaoes/manterFiliais" service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="filialCobranca" size="15" maxLength="15" width="80%" labelWidth="20%" disabled="true">
			<adsm:propertyMapping modelProperty="numFilial" formProperty="numFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" disabled="true"/>		
		</adsm:lookup>

        <adsm:lookup service="" dataType="integer" property="cliente.id" criteriaProperty="cliente.codigo" label="cliente" size="15" maxLength="15" labelWidth="20%" width="80%" action="" disabled="true">
			<adsm:propertyMapping modelProperty="nome" formProperty="cliente"/> 
			<adsm:textbox property="cliente" dataType="text" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:textbox dataType="text" property="numeroRecibo" label="numeroRecibo" size="10" maxLength="10" labelWidth="20%" width="25%" disabled="true"/>		

		<adsm:textbox dataType="JTDate" property="dataEmissao" label="dataEmissao" width="32%" labelWidth="23%" picker="true" disabled="true"/>

		<adsm:combobox label="situacao" property="situacao" optionProperty="" optionLabelProperty="" service="" prototypeValue="Digitado|Emitido|Recebido|Cancelado|Inutilizado" labelWidth="20%" width="25%" required="true"/>

        <adsm:combobox label="situacaoAprovacao" property="situacaoAprovacao" optionProperty="" optionLabelProperty="" service="" prototypeValue="" labelWidth="23%" width="32%" disabled="true"/>

		<adsm:textbox dataType="text" property="totalReceber"  label="totalReceber" maxLength="18" size="18" width="25%" labelWidth="20%" required="true" disabled="true"/>

        <adsm:textbox label="cotacao" labelWidth="23%" dataType="text" property="moeda" width="7%" size="4" disabled="true"/>
		<adsm:textbox property="dataCotacao" dataType="text" size="7" width="9%" disabled="true"/>
		<adsm:lookup property="cotacao" action="/configuracoes/manterCotacoesMoedas" dataType="currency" service="" size="5" width="12%" disabled="true" />

        <adsm:textbox dataType="text" property="valorTotalRecebido"  label="valorTotalRecebido" maxLength="18" size="18" width="25%" labelWidth="20%" required="true" disabled="true"/>
        <adsm:textbox dataType="decimal" property="diferencaCambialCotacao" label="diferencaCambialCotacao" labelWidth="23%" width="30%" disabled="true"/>

		<adsm:textarea width="85%" columns="100" rows="3" maxLength="255" property="observacaoRecebimento" label="observacao" labelWidth="20%" />		

		<adsm:buttonBar>
            <adsm:button caption="emitirRecibo"/>
			<adsm:button caption="cancelar"/>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>