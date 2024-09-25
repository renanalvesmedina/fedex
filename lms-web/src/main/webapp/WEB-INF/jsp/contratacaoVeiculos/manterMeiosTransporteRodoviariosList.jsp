<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarMeiosTransporteRodoviarios">
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporteRodoviarios" height="106">

		<adsm:lookup service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="filialOrigem" size="5" maxLength="5" labelWidth="21%" width="10%" action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="filial" size="22" disabled="true" width="19%"/>

		<adsm:combobox property="tipo" label="tipoVinculo" service="" optionLabelProperty="" optionProperty="" prototypeValue="Agregado|Eventual|Próprio" labelWidth="21%" width="60%"/>

		<adsm:textbox dataType="text" property="placa" label="placa" maxLength="20" size="20" labelWidth="21%" width="34%"/>
		<adsm:textbox dataType="text" property="frota" label="frota" maxLength="20" size="20" labelWidth="18%" width="27%"/>

		<adsm:combobox property="tipo" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" prototypeValue="Rodoviário|Aéreo|Ferroviário|Maritmo" labelWidth="21%" width="60%"/>

		<adsm:combobox property="marca" label="marca" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="21%" width="34%"/>
		<adsm:combobox property="modelo" label="modelo" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="18%" width="27%"/>

		<adsm:textbox dataType="text" property="anoFabricacao" label="anoFabricacao" maxLength="20" size="20" labelWidth="21%" width="34%"/>
		<adsm:textbox dataType="text" property="quantidadePortas" label="quantidadePortas" maxLength="20" size="20" labelWidth="18%" width="27%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="180" unique="true" scrollBars="horizontal">
		<adsm:gridColumn title="filial" property="filial" width="200" />
		<adsm:gridColumn title="frota" property="frota" width="80" />
		<adsm:gridColumn title="placa" property="ident" width="100" />
		<adsm:gridColumn title="tipoVinculo" property="vin" width="80" />
		<adsm:gridColumn title="tipoMeioTransporte" property="meioTra" width="100" />
		<adsm:gridColumn title="marca" property="marca" width="100" />
		<adsm:gridColumn title="modelo" property="modelo" width="90" />
		<adsm:gridColumn title="ano" property="ano" width="50" align="right"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>