<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/digitarMIC">
		<adsm:lookup action="/municipios/manterFiliais" service="" dataType="integer" property="filialOrigem.codigo" criteriaProperty="empresa.name" label="filialOrigem" size="5" maxLength="5" labelWidth="20%" width="34%" required="true">
		   	<adsm:propertyMapping modelProperty="filialOrigem.codigo" formProperty="filialOrigem.nome"/>
            <adsm:textbox dataType="text" property="filialOrigem.nome" size="25" maxLength="45" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup action="/municipios/manterFiliais" service="" dataType="integer" property="filialOrigem.codigo" criteriaProperty="empresa.name" label="filialDestino" size="5" maxLength="5" labelWidth="14%" width="32%" required="true">
		   	<adsm:propertyMapping modelProperty="filialDestino.codigo" formProperty="filialDestino.nome"/>
            <adsm:textbox dataType="text" property="filialDestino.nome" size="23" maxLength="45" disabled="true"/>
        </adsm:lookup>

		<adsm:combobox property="tipoMIC" label="tipoMIC" service="" optionLabelProperty="" optionProperty="" prototypeValue="MIC|MIC/DTA" labelWidth="20%" width="34%" required="true"/>
		<adsm:textbox property="numeroMIC" dataType="text" label="numeroMIC04" size="10" maxLength="10" labelWidth="14%" width="31%" disabled="true" required="true"/>

		<adsm:textbox property="cnpjFilialEmitente" dataType="text" label="cnpjFilialEmitente" size="10" maxLength="10" labelWidth="20%" width="34%" required="true" disabled="true"/>
		<adsm:checkbox property="transitoAduaneiro" label="transitoAduaneiro03" labelWidth="19%" width="26%"/>

	    <adsm:textbox dataType="date" label="dataEmissao06" labelWidth="20%" property="dataEmissao" width="70%" required="true"/>

       <adsm:lookup action="/municipios/manterMunicipios" service="" dataType="text" property="municipio.codigo" criteriaProperty="municipio.descricao" label="municipioPartida07" size="20" maxLength="20" labelWidth="20%" width="70%" required="true">
		   	<adsm:propertyMapping modelProperty="municipio.codigo" formProperty="municipio.complemento"/>
            <adsm:textbox dataType="text" property="municipio.complemento" size="40" maxLength="20"/>
        </adsm:lookup> 

       <adsm:lookup action="/municipios/manterMunicipios"  service="" dataType="text" property="municipio.codigo" criteriaProperty="municipio.descricao" label="municipioDestino08" size="20" maxLength="20"  labelWidth="20%" width="70%" required="true">
		   	<adsm:propertyMapping modelProperty="municipio.codigo" formProperty="municipio.complemento"/>
            <adsm:textbox dataType="text" property="municipio.complemento" size="40" maxLength="20" />
        </adsm:lookup> 

       <adsm:lookup action="/contratacaoVeiculos/manterMeiosTransporte"  service="" dataType="text" property="caminhao.codigo" criteriaProperty="caminhao.descricao" label="caminhaoOriginal09" size="20" maxLength="20" labelWidth="20%" width="70%" required="true">
		   	<adsm:propertyMapping modelProperty="municipio.codigo" formProperty="municipio.complemento"/>
            <adsm:textbox dataType="text" property="municipio.complemento" size="40" maxLength="30" disabled="true"/>
        </adsm:lookup> 

		<adsm:textbox dataType="text" property="proprietario.nome" label="proprietario10" size="46" width="60%" labelWidth="20%" maxLength="30" disabled="true" required="true"/>

		<adsm:lookup action="/contratacaoVeiculos/manterMeiosTransporte" service="" dataType="integer" property="proprietario.codigo" criteriaProperty="proprietario.nome" label="semiReboque15" size="20" maxLength="5" labelWidth="20%" width="70%" >
		   	<adsm:propertyMapping modelProperty="proprietario.codigo" formProperty="proprietario.nome"/>
            <adsm:textbox dataType="text" property="proprietario.nome" size="40" maxLength="30" disabled="true"/>
        </adsm:lookup>

	    <adsm:textbox dataType="text" label="numeroLacres37" labelWidth="20%" property="numeroLacres" size="80" width="70%"/>

	    <adsm:lookup action="/municipios/consultarRotasViagem" service="" dataType="integer" property="destinatario.codigo" criteriaProperty="destinatario.nome" label="rotas40" size="80" maxLength="100" width="65%" labelWidth="20%" required="true"/>
		<adsm:label key="branco" style="border:none;" width="19%" />
		<adsm:label key="branco" style="border:none;" width="1%" />
        <adsm:textarea maxLength="600" property="dadosDestinatario" labelWidth="1%" width="70%" columns="82" rows="4" required="true"/>

		<adsm:lookup action="/contratacaoVeiculos/manterMotoristas" service="" dataType="integer" property="motorista.codigo" criteriaProperty="motorista.nome" label="motorista" size="5" maxLength="5" labelWidth="20%" width="70%" required="true">
		   	<adsm:propertyMapping modelProperty="motorista.codigo" formProperty="motorista.nome"/>
            <adsm:textbox dataType="text" property="motorista.nome" size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>

	    <adsm:textbox dataType="text" label="numeroDTA" labelWidth="20%" width="34%" property="numeroLacres" size="10"/>
		<adsm:combobox property="situacao" label="situacao" service="" optionLabelProperty="" optionProperty="" prototypeValue="Ativo|Inativo" labelWidth="14%" width="31%" disabled="true"/>
		
		<adsm:buttonBar lines="1">	
			<adsm:button caption="incluirExcluirCRT2" action="/expedicao/incluirExcluirCRT" cmd="main"/>
			<adsm:button caption="salvar" />
			<adsm:button caption="cancelarMIC" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   