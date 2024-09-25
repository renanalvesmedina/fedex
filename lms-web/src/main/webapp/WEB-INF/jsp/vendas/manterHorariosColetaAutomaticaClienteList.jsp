<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.coletaAutomaticaClienteService" onPageLoadCallBack="myPageLoad">

	<adsm:form action="/vendas/manterHorariosColetaAutomaticaCliente">
	
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
	
		<adsm:hidden property="cliente.idCliente"/>
		
		<adsm:textbox dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" width="13%" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" maxLength="60" disabled="true" width="61%" serializable="false"/>
		</adsm:textbox>

		<adsm:combobox property="tpDiaSemana" label="diaSemana" width="85%" domain="DM_DIAS_SEMANA"/>
		
		<adsm:combobox property="servico.idServico"
					   label="servico"
					   boxWidth="240"
					   optionLabelProperty="dsServico"
					   optionProperty="idServico"
					   service="lms.configuracoes.servicoService.find"/>
					   
		<adsm:combobox property="naturezaProduto.idNaturezaProduto"
					   label="naturezaProduto"
					   boxWidth="200"
					   optionLabelProperty="dsNaturezaProduto"
					   optionProperty="idNaturezaProduto"
					   service="lms.expedicao.naturezaProdutoService.find"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="coletaAutomaticaCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid gridHeight="200" unique="true" idProperty="idColetaAutomaticaCliente" property="coletaAutomaticaCliente" 
			   defaultOrder="tpDiaSemana, hrChegada" onSelectRow="myOnSelectRow" onSelectAll="myOnSelectAll" rows="12">
		<adsm:gridColumn title="diaSemana"   property="tpDiaSemana" width="20%" isDomain="true"/>
		<adsm:gridColumn title="horaInicial" property="hrChegada"   width="17%"  dataType="JTTime"/>
		<adsm:gridColumn title="horaFinal"   property="hrSaida"     width="17%"  dataType="JTTime"/>
		<adsm:gridColumn title="servico"     property="servico.dsServico" width="23%" dataType="text"/>
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="23%" dataType="text"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script>

    // Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
    document.getElementById("permissao").masterLink = "true";
    
	/**
	* Criada para validar o acesso do usuário 
	* logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.coletaAutomaticaClienteService.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	* Criada para validar o acesso do usuário 
	* logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}
	
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
