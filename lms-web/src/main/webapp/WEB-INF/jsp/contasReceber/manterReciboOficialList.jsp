<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>
	<adsm:form action="/contasReceber/manterReciboOficial">
		<adsm:lookup action="/configuracaoes/manterFiliais" service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="filialCobranca" size="15" maxLength="15" width="80%" labelWidth="20%">
			<adsm:propertyMapping modelProperty="numFilial" formProperty="numFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" disabled="true"/>		
		</adsm:lookup>

        <adsm:lookup service="" dataType="integer" property="cliente.id" criteriaProperty="cliente.codigo" label="cliente" size="15" maxLength="15" labelWidth="20%" width="80%" action="" >
			<adsm:propertyMapping modelProperty="nome" formProperty="cliente"/> 
			<adsm:textbox property="cliente" dataType="text" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:textbox dataType="text" property="numeroRecibo" label="numeroRecibo" size="10" maxLength="10" labelWidth="20%" width="25%"/>		
        
		<adsm:range label="dataEmissao" labelWidth="20%" width="35%">
			<adsm:textbox dataType="JTDate" property="dataEmissaoInicial" picker="true"/>
	    	<adsm:textbox dataType="JTDate" property="dataFinalFinal" picker="true"/>
		</adsm:range>

		<adsm:combobox label="situacao" property="situacao" optionProperty="" optionLabelProperty="" service="" prototypeValue="Digitado|Emitido|Recebido|Cancelado|Inutilizado" labelWidth="20%" width="25%"/>

        <adsm:combobox label="situacaoAprovacao" property="situacaoAprovacao" optionProperty="" optionLabelProperty="" service="" prototypeValue="" labelWidth="20%" width="30%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true">
		<adsm:gridColumn width="15%" title="filialCobranca" property="filialCobranca"/>
		<adsm:gridColumn width="15%" title="numeroRecibo" property="numeroFilial"/>
		<adsm:gridColumn width="15%" title="dataEmissao" property="dataEmissao"/>
		<adsm:gridColumn width="15%" title="situacao" property="situacao"/>
		<adsm:gridColumn width="18%" title="totalReceber" property="valorTotal"/>
        <adsm:gridColumn width="18%" title="valorTotalRecebido" property="valorTotal"/>

	</adsm:grid>
	<adsm:buttonBar> 
		<adsm:button caption="excluir"/>
	</adsm:buttonBar>
</adsm:window>