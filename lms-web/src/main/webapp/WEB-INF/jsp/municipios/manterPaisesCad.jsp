<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.paisService">
	<adsm:form idProperty="idPais" action="/municipios/manterPaises">

		<adsm:textbox
			label="sigla"
			property="sgPais"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="15%"
			width="35%"
			required="true"
			onchange="validaSigla(this)"
		/>

		<adsm:textbox
			dataType="text"
			property="sgResumida"
			onchange="validaSigla(this)"
			label="siglaResumida"
			size="2"
			labelWidth="15%"
			width="35%"
			maxLength="2"
		/>

		<adsm:textbox
			label="nome"
			property="nmPais"
			dataType="text"
			size="30"
			maxLength="60"
			labelWidth="15%"
			width="35%"
			required="true"
		/>

		<adsm:textbox
			dataType="integer"
			property="cdIso"
			label="numeroPais"
			size="3"
			maxLength="3"
			labelWidth="15%"
			width="35%"
		/>

		<adsm:combobox
			label="zona"
			property="zona.idZona"
			optionProperty="idZona"
			optionLabelProperty="dsZona"
			service="lms.municipios.zonaService.find"
			boxWidth="200"
			onlyActiveValues="true"
			required="true"
		/>

		<adsm:combobox
			label="buscaEndereco"
			property="tpBuscaEndereco"
			domain="DM_TIPO_BUSCA_ENDERECO"
			labelWidth="15%"
			width="35%"
			boxWidth="140"
			required="true"
		/> 

		<adsm:combobox
			label="situacao"
			property="tpSituacao"
			boxWidth="80"
			domain="DM_STATUS"
			required="true"
			labelWidth="15%"
			width="35%"
		/>

		<adsm:hidden property="dsTpSituacaoPais"/>
		<adsm:checkbox label="cepOpcional" property="blCepOpcional" labelWidth="15%" width="35%"/>
		<adsm:checkbox label="cepDuplicado" property="blCepDuplicado" labelWidth="15%" width="35%"/>
		<adsm:checkbox label="cepAlfanumerico" property="blCepAlfanumerico" labelWidth="15%" width="35%"/>

		<adsm:textbox
			label="codigoBacen"
			property="nrBacen"
			dataType="integer"
			size="15"
			maxLength="10"
			labelWidth="15%"
			width="35%"
		/>

		<adsm:buttonBar>
			<adsm:button caption="permissos" action="/municipios/manterPermissosEmpresaPaises" cmd="main">
				<adsm:linkProperty src="nmPais" target="pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="idPais" target="pais.idPais" disabled="true"/>
			</adsm:button>
			<adsm:button caption="moedas" action="/configuracoes/manterMoedasPaises" cmd="main">
				<adsm:linkProperty src="nmPais" target="pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="idPais" target="pais.idPais" disabled="true"/>
			</adsm:button>
			<adsm:button caption="feriados" action="/municipios/manterFeriados" cmd="main">
				<adsm:linkProperty src="nmPais" target="pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="idPais" target="pais.idPais" disabled="true"/>
			</adsm:button>
			<adsm:button caption="unidadesFederativasButton" action="/municipios/manterUnidadesFederativas" cmd="main" >
				<adsm:linkProperty src="nmPais" target="pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="sgPais" target="pais.sgPais" disabled="true"/> 
				<adsm:linkProperty src="idPais" target="pais.idPais" disabled="true"/>
				<adsm:linkProperty src="dsTpSituacaoPais" target="pais.tpSituacao.description" disabled="true"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function validaSigla(obj) {
		var sigla = obj;
		sigla.value = sigla.value.toUpperCase();
	}
</script>