<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.horarioCorteClienteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterHorariosCorteColetaEntrega">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	

		<adsm:combobox property="tpHorario" domain="DM_TIPO_HORARIO_CORTE" label="tipoServico" style="width:250px"/>
		<adsm:combobox property="servico" optionLabelProperty="dsServico" optionProperty="idServico" 
			service="lms.configuracoes.servicoService.find" label="servico" style="width:260px"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="horarioCorteCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="horarioCorteCliente" idProperty="idHorarioCorteCliente" gridHeight="200" unique="true" onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow" rows="13">
		<adsm:gridColumn title="tipoServico" property="tpHorario" isDomain="true" width="13%" />
		<adsm:gridColumn title="servico" property="servico.dsServico" width="25%" />
		<adsm:gridColumn title="uf" property="unidadeFederativaOrigem.sgUnidadeFederativa" width="3%" />
		<adsm:gridColumn title="filial" property="filialOrigem.sgFilial" width="6%" />
		<adsm:gridColumn title="municipio" property="municipioOrigem.nmMunicipio" width="20%" />
		<adsm:gridColumn title="horaInicial" property="hrInicial" dataType="JTTime" width="10%"/>
		<adsm:gridColumn title="horaFinal" property="hrFinal" width="10%" dataType="JTTime"/>
		<adsm:gridColumn title="horasAplicadas" property="nrHorasAplicadas" dataType="integer" width="13%"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.municipios.horarioCorteClienteService.validatePermissaoUsuarioLogado", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";
	
	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){

		var permissao = document.getElementById("permissao");
		
		if( permissao.value != "true" ){
			setDisabled("removeButton",true);
			return false;
		}
	}
	
	/**
	* Esta função deve executar exatamente a mesma tarefa que a função myOnSelectRow.
	*/
	function myOnSelectAll(){
		return myOnSelectRow();
	}

</script>