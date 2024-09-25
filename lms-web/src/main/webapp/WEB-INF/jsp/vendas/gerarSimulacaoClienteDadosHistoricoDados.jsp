<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/gerarSimulacaoCliente">
		<adsm:range label="periodo" labelWidth="15%" width="80%">
             <adsm:textbox dataType="date" property="inicioVigencia" required="true" picker="true" />
             <adsm:textbox dataType="date" property="fimVigencia" picker="true"/>
        </adsm:range>
		<adsm:combobox property="tipoFrete" optionLabelProperty="" optionProperty="" service="" labelWidth="15%" width="80%" prototypeValue="CIF|FOB" label="tipoFrete" />
		<adsm:combobox property="ufOrigem" optionLabelProperty="" optionProperty="" service="" labelWidth="15%" width="35%" label="ufOrigem" />
		<adsm:combobox property="ufDestino" optionLabelProperty="" optionProperty="" service="" labelWidth="15%" width="35%" label="ufDestino" />
		<adsm:lookup action="/municipios/manterFiliais" service="" dataType="integer" property="filialOrigem.codigo" criteriaProperty="empresa.name" label="filialOrigem" size="5" maxLength="5" labelWidth="15%" width="35%">
		   	<adsm:propertyMapping modelProperty="filialOrigem.codigo" formProperty="filial.nome"/>
            <adsm:textbox dataType="text" property="filialOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup action="/municipios/manterFiliais" service="" dataType="integer" property="filialDestino.codigo" criteriaProperty="empresa.name" label="filialDestino" size="5" maxLength="5" labelWidth="15%" width="35%">
		   	<adsm:propertyMapping modelProperty="filialOrigem.codigo" formProperty="filial.nome"/>
            <adsm:textbox dataType="text" property="filialOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup action="/municipios/manterAeroportos" service="" dataType="integer" property="aeroportoOrigem.codigo" criteriaProperty="aeroporto.name" label="aeroportoOrigem" size="5" maxLength="5" width="35%" labelWidth="15%" >
		   	<adsm:propertyMapping modelProperty="aeroportoOrigem.codigo" formProperty="aeroportoOrigem.nome"/>
            <adsm:textbox dataType="text" property="aeroportoOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup action="/municipios/manterAeroportos" service="" dataType="integer" property="aeroportoOrigem.codigo" criteriaProperty="aeroporto.name" label="aeroportoDestino" size="5" maxLength="5" width="35%" labelWidth="15%" >
		   	<adsm:propertyMapping modelProperty="aeroportoOrigem.codigo" formProperty="aeroportoOrigem.nome"/>
            <adsm:textbox dataType="text" property="aeroportoOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>

	    <adsm:checkbox label="dadosConhecimentos" property="dadosConhecimentos" labelWidth="30%" width="70%" />
	    <adsm:checkbox label="dadosNFTransporte" property="dadosNFTransporte" labelWidth="30%" width="70%" />

		<adsm:buttonBar>
			<adsm:button caption="salvarDadosHistorico"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>