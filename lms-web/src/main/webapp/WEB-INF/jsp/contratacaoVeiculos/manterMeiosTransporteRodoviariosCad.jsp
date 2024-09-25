<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporteRodoviarios" height="358">
		<adsm:lookup service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="5" maxLength="5" labelWidth="21%" width="10%" action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="filial" size="22" disabled="true" width="19%"/>

		<adsm:combobox property="tipo" label="tipoVinculo" service="" optionLabelProperty="" optionProperty="" prototypeValue="Agregado|Eventual|Próprio" labelWidth="21%" width="60%"/>

		<adsm:textbox dataType="text" property="placa" label="placa" maxLength="20" size="20" labelWidth="21%" width="34%"/>
		<adsm:textbox dataType="text" property="frota" label="frota" maxLength="30" size="30" labelWidth="18%"  width="27%"/>

		<adsm:textbox dataType="text" property="situacaoAtual" label="situacaoAtual" maxLength="20" size="20" labelWidth="21%" width="60%" disabled="true"/>
		<adsm:combobox property="tipo" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" prototypeValue="Rodoviário|Aéreo|Ferroviário|Maritmo" labelWidth="21%" width="60%"/>

		<adsm:combobox property="marca" label="marca" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="21%" width="34%"/>
		<adsm:combobox property="modelo" label="modelo" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="18%" width="27%"/>

		<adsm:textbox dataType="text" property="anoFabricacao" label="anoFabricacao" maxLength="20" size="20" labelWidth="21%" width="34%"/>
		<adsm:textbox dataType="text" property="quantidadePortas" label="quantidadePortas" maxLength="20" size="20" labelWidth="18%" width="27%"/>

		<adsm:textbox dataType="text" property="capacidadeM3" label="capacidade" unit="m3" maxLength="20" size="20" labelWidth="21%" width="34%"/>
		<adsm:textbox dataType="text" property="capacidadeKg" label="capacidade" unit="kg" maxLength="20" size="20" labelWidth="18%" width="27%"/>

		<adsm:textbox dataType="file" property="foto" label="foto" maxLength="12" size="30" labelWidth="21%" width="60%" />

		<adsm:textbox dataType="text" property="quantidadeEixos" label="quantidadeEixos" maxLength="10" size="10" labelWidth="21%" width="60%"/>

		<adsm:combobox property="operadoraMCT" label="operadoraMCT" service="" optionLabelProperty="" optionProperty="" labelWidth="21%" width="34%"/>
		<adsm:textbox dataType="text" property="rastreadorNum" label="rastreadorNum" maxLength="20" size="20" width="27%" labelWidth="18%"/>

		<adsm:complement labelWidth="21%" width="60%" label="celular">
                  <adsm:textbox dataType="text" property="ddd" size="4" maxLength="4"/>
                  <adsm:textbox dataType="text" property="numeroCel" size="10" maxLength="10"/>
        </adsm:complement>

		<adsm:checkbox property="possuiSemParar" label="possuiSemParar" width="34%" labelWidth="21%"/>
		<adsm:checkbox property="controleTAG" label="controleTAG" width="27%" labelWidth="18%"/>

		<adsm:lookup service="" dataType="integer" property="cavalo.id" criteriaProperty="cavalo.codigo" label="cavalo" size="5" maxLength="5" labelWidth="21%" width="10%" action="/contratacaoVeiculos/manterMeiosTransporteRodoviarios">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="carreta" size="22" disabled="true" width="19%"/>

		<adsm:section caption="informacoesDocumentacao"/>

		<adsm:textbox dataType="text" property="codigoRENAVAM" label="codigoRENAVAM" width="34%" labelWidth="21%"/>
		<adsm:textbox dataType="text" property="certificado" label="certificado" width="27%" labelWidth="18%"/>

		<adsm:textbox dataType="text" property="chassi" label="chassi" width="60%" labelWidth="21%"/>

		<adsm:textbox dataType="text" property="bilheteSeguro" label="bilheteSeguro" width="34%" labelWidth="21%"/>
		<adsm:textbox dataType="text" property="vencimentoSeguro" label="vencimentoSeguro" width="27%" labelWidth="18%"/>

		<adsm:textbox dataType="JTDate" picker="true" size="8" maxLength="8" label="dataEmissaoDocumento" property="dataEmissaoDocumento" width="60%" labelWidth="21%"/>

		<adsm:lookup service="" dataType="text" property="pais.id" criteriaProperty="pais.codigo" label="paisEmplacamento" size="30" action="/municipios/manterPaises" width="34%" labelWidth="21%"/>
		<adsm:combobox property="ufEmplacamento" label="ufEmplacamento" service="" optionLabelProperty="" optionProperty="" width="27%" labelWidth="18%"/>

		<adsm:combobox property="municipioEmplacamento" label="municipioEmplacamento" service="" optionLabelProperty="" optionProperty="" width="34%" labelWidth="21%"/>

		<adsm:section caption="caracteristicasGerais"/>

		<adsm:combobox property="corCarroceria" label="corCarroceria" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="21%" width="60%"/>
		<adsm:combobox property="corCabine" label="corCabine" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="21%" width="60%"/>
		<adsm:combobox property="tipoCarroceria" label="tipoCarroceria" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="21%" width="60%"/>

		<adsm:textbox dataType="text" property="radio" label="radio" maxLength="20" size="20" width="60%" labelWidth="21%"/>
		<adsm:textbox dataType="text" property="celular" label="celular" maxLength="20" size="20" width="60%" labelWidth="21%"/>
		<adsm:textbox dataType="text" property="rastreadorNum" label="rastreadorNum" maxLength="20" size="20" width="60%" labelWidth="21%"/>
		<adsm:checkbox property="possuiPlataforma" label="possuiPlataforma" width="60%" labelWidth="21%"/>

		<adsm:buttonBar lines="2">
			<adsm:button caption="permissos" action="/contratacaoVeiculos/manterPaisPermissionadoMeiosTransporte" cmd="main" breakBefore="true"/>
			<adsm:button caption="motoristas" action="/contratacaoVeiculos/manterMeiosTransporteRodoviariosMotorista" cmd="main" boxWidth="80"/>
			<adsm:button caption="liberacoes" action="/contratacaoVeiculos/manterLiberacoesReguladora" cmd="main" boxWidth="80"/>
			<adsm:button caption="servicos" action="/contratacaoVeiculos/manterServicosMeioTransporte" cmd="main" boxWidth="70"/>
			<adsm:button caption="bloqueios" action="/contratacaoVeiculos/manterBloqueiosMotoristaProprietario" cmd="main" boxWidth="80"/>
			<adsm:button caption="eventos" action="contratacaoVeiculos/consultarEventosMeiosTransporte" cmd="main" boxWidth="70"/>
			<adsm:button caption="solicitacoesContratacao" action="/contratacaoVeiculos/manterSolicitacoesContratacao" cmd="main" boxWidth="180"/>

			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
