<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/freteCarreteiroViagem/manterReajustesEspecificosTabelasFrete">
		<adsm:textbox dataType="text" property="descricao" label="descricao" size="40" labelWidth="20%" width="80%" disabled="true" />
		<adsm:combobox property="" label="tipoReajuste" prototypeValue="Percentual|Valor" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" />
		<adsm:textbox dataType="text" property="percentualReajuste" label="percentualReajuste" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="text" property="valorReajuste" label="valorReajuste" labelWidth="20%" width="30%"/>
		<adsm:section caption="criteriosEspecificos" />
		<adsm:combobox property="empresa.id" label="empresa" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" />
		<adsm:combobox property="regional.id" label="regional" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" />
		<adsm:combobox property="filial.id" label="filial" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" />
		<adsm:combobox property="tipoRota" label="tipoRota" prototypeValue="Expressa|Eventual" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%"/>
		<adsm:lookup service="" dataType="text" property="rota.id" criteriaProperty="rota.codigo" label="rota" size="32" maxLength="120" labelWidth="20%" width="30%" action="/municipios/consultarRotasViagem" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeRota" /> 
        </adsm:lookup>
		<adsm:combobox property="tipoMeioTransporte.id" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" />
		<adsm:lookup service="" dataType="text" property="meioTransporte.id" criteriaProperty="meioTransporte.codigo" label="meioTransporte" size="5" maxLength="10" labelWidth="20%" width="11%" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" >
        	<adsm:propertyMapping modelProperty="meioTransporte" formProperty="meioTransporte"/> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="meioTransporte" size="15" maxLength="50" disabled="true" width="19%"/>		
		<adsm:lookup service="" dataType="text" property="proprietario.id" criteriaProperty="proprietario.codigo" label="proprietario" size="5" maxLength="10" labelWidth="20%" width="11%" action="/contratacaoVeiculos/manterProprietarios" cmd="list" >
        	<adsm:propertyMapping modelProperty="proprietario" formProperty="proprietario"/> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="proprietario" size="20" maxLength="50" disabled="true" width="69%" />
		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   