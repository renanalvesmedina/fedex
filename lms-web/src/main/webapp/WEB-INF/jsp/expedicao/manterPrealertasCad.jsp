<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterPrealertasAction">

	<adsm:i18nLabels>
		<adsm:include key="LMS-04527"/>
		<adsm:include key="LMS-04531"/>
	</adsm:i18nLabels>

	<adsm:form idProperty="idPreAlerta" action="/expedicao/manterPrealertas" onDataLoadCallBack="validateData">
		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte" />
		<adsm:hidden property="motorista.idMotorista" />
		<adsm:hidden property="isConfirmed" />
		<adsm:hidden property="rotaColetaEntrega.idRotaColetaEntrega" />
		<adsm:hidden property="awb.filialByIdFilialDestino.idFilial" />
		<adsm:hidden property="awb.filialByIdFilialDestino.sgFilial" />
		<adsm:hidden property="awb.filialByIdFilialDestino.pessoa.nmFantasia" />
		<adsm:hidden property="awb.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" />
		<adsm:hidden property="awb.clienteByIdClienteDestinatario.pessoa.idPessoa" />
		<adsm:hidden property="awb.clienteByIdClienteDestinatario.pessoa.nmPessoa" />
	
		<adsm:textbox dataType="integer" labelWidth="22%" width="28%" maxLength="8"
			property="nrPreAlerta" 
			label="numeroPrealerta" 
			disabled="true"/>
		 <adsm:combobox label="numeroAWB" property="tpStatusAwb"
					   domain="DM_LOOKUP_AWB" 
					   labelWidth="18%" width="32%"
					   defaultValue="E" renderOptions="true" disabled="true">
        
			<adsm:lookup property="ciaFilialMercurio.empresa" 
						 idProperty="idEmpresa"
						 dataType="text"
						 criteriaProperty="sgEmpresa"
				 		 criteriaSerializable="true"
						 service="lms.expedicao.manterPrealertasAction.findLookupSgCiaAerea"
						 action="" 	
						 size="3" maxLength="3"						 
					 	 picker="false"
					 	 disabled="true">
			</adsm:lookup>

	        <adsm:lookup dataType="integer" size="13" maxLength="11" 
	        	property="awb"
	        	idProperty="idAwb"
	        	criteriaProperty="nrAwb"
	        	criteriaSerializable="true"
	 			service="lms.expedicao.manterPrealertasAction.findLookupAwb"
				action="expedicao/consultarAWBs"
				onDataLoadCallBack="awbOnDataLoadCallBack"
				onPopupSetValue="findAwb_cb"
				disabled="true">
				
				<adsm:propertyMapping modelProperty="tpStatusAwb" criteriaProperty="tpStatusAwb" disable="true" />
				<adsm:propertyMapping modelProperty="ciaFilialMercurio.empresa.idEmpresa" criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" disable="true" />
				
	        </adsm:lookup>
	    </adsm:combobox>

		<adsm:textbox dataType="text" labelWidth="22%" width="28%" maxLength="20"
			property="dsVoo" 
			label="numeroVoo" 
			required="false"
			disabled="true"/>
		<adsm:hidden property="blVooConfirmadoRef" serializable="true"/>
		<adsm:checkbox labelWidth="18%" width="32%"
			property="blVooConfirmado" 
			label="confirmarEmbarque"/>
		<adsm:textbox dataType="JTDateTimeZone" labelWidth="22%" width="28%"
			property="dhSaida" 
			label="dataDeEmbarque" 
			required="true"/>
		<adsm:textbox dataType="JTDateTimeZone" labelWidth="18%" width="32%"
			property="dhChegada" 
			label="dataPrevistaChegada" 
			required="true"/>

		<adsm:section caption="visualizacaoPrealerta"/>
		<adsm:hidden property="usuario.idUsuario"/>
		<adsm:complement label="funcionario" labelWidth="22%" width="38%">
			<adsm:textbox dataType="text" size="12"
				property="usuario.nrMatricula"
                serializable="false" 
                disabled="true"/>
			<adsm:textbox dataType="text" size="30"
				property="usuario.nmUsuario"
				serializable="false" 
				disabled="true"/>
		</adsm:complement>
		<adsm:textbox dataType="JTDateTimeZone" labelWidth="20%" width="20%" size="16"
			label="data" 
			property="dhRecebimentoMens" 
			disabled="true"
			picker="false"/>

		<adsm:buttonBar>
			<adsm:storeButton id="saveButton" callbackProperty="storePreAlerta"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script type="text/javascript">
	/*
	* Valida se usuario pode editar o registro.
	*/
	function validateData_cb(data, error) {
		onDataLoad_cb(data, error);
		if (error != undefined) {
			alert(error);
			return;
		}
		/* Armazena Referencia original do objeto */
		setElementValue("blVooConfirmadoRef", data.blVooConfirmado)
		setDisabled("saveButton", (data.isEditavel != 'true'));
		setElementValue("isConfirmed", false);
	}
	
	function getInfoParceira(){
		var service = 'lms.expedicao.manterPrealertasAction.findAwbById';
		storeButtonScript(service, 'getInfoParceira', document.forms[0]);	
	}
	
	function getInfoParceira_cb(data, error){
		if(error != undefined){
			alert(error)
			return false;
		}
		
		setElementValue("awb.filialByIdFilialDestino.idFilial", data.filialByIdFilialDestino.idFilial);
		setElementValue("awb.filialByIdFilialDestino.sgFilial" , data.filialByIdFilialDestino.sgFilial);
		setElementValue("awb.filialByIdFilialDestino.pessoa.nmFantasia" , data.filialByIdFilialDestino.pessoa.nmFantasia);
		setElementValue("awb.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" , data.clienteByIdClienteDestinatario.pessoa.nrIdentificacao);
		setElementValue("awb.clienteByIdClienteDestinatario.pessoa.idPessoa" , data.clienteByIdClienteDestinatario.pessoa.idPessoa);
		setElementValue("awb.clienteByIdClienteDestinatario.pessoa.nmPessoa" , data.clienteByIdClienteDestinatario.pessoa.nmPessoa);
		
		showPopUpGerarPedidoColetaAeroportoParceira();
		if(getElementValue("meioTransporteByIdTransportado.idMeioTransporte") != null && getElementValue("meioTransporteByIdTransportado.idMeioTransporte") != ""
			&& getElementValue("motorista.idMotorista") != null && getElementValue("motorista.idMotorista") != ""
			&& getElementValue("rotaColetaEntrega.idRotaColetaEntrega") != null && getElementValue("rotaColetaEntrega.idRotaColetaEntrega") != ""){
			store();
		}
	}
	
	/*
	* CallBack do store: Caso Voo Confirmado, chama tela de Cadastro Pedido de Coleta.
	*/
	function storePreAlerta_cb(data, error) {
		
		if(error != undefined){
			if(error.substring(0,8) == "parceira"){
				if(getElementValue("blVooConfirmado")){
					findManifestoColeta();
				}else{
					if(confirm("LMS-04531: " + i18NLabel.getLabel("LMS-04531"))){
						setElementValue("isConfirmed", true);
						store();
					}
				}
			}else{
				alert(error);
			}
			return false;
		}
		
		store_cb(data, error);
		/* Se deve Limpar campos da Visualizacao do PreAlerta */
		if (data.cleanUser != undefined && data.cleanUser == 'true') {
			resetValue("usuario.idUsuario");
			resetValue("usuario.nrMatricula");
			resetValue("usuario.nmUsuario");
			resetValue("dhRecebimentoMens");
		}
		/* Verifica se houve Exceção no envio do e-mail */
		if (data.exception != undefined) {
			alert(data.exception);
		}
		/* Verifica se deve chamar Pedido de Coleta */
		var blVooConfirmado = getElementValue("blVooConfirmado");
		var blVooConfirmadoRef = getElementValue("blVooConfirmadoRef");
		/** Armazena nova referencia */
		setElementValue("blVooConfirmadoRef", blVooConfirmado);
		setElementValue("isConfirmed", false);
	}
	
	function findManifestoColeta(){
		var service = 'lms.expedicao.manterPrealertasAction.findPedidoColetaJaInseridoByAwb';
		storeButtonScript(service, 'findPedidoColetaJaInseridoByAwb', document.forms[0]);
	}
	
	function findPedidoColetaJaInseridoByAwb_cb(data, error){
		if(error != undefined){
			alert(error);
			return false;
		}
		
		if(data._value == "true"){
			alert(i18NLabel.getLabel("LMS-04527"));
			return false;
		}else{
			setElementValue("meioTransporteByIdTransportado.idMeioTransporte", "");
			setElementValue("motorista.idMotorista", "");
			setElementValue("rotaColetaEntrega.idRotaColetaEntrega", "");
			getInfoParceira();
		}
	}
	
	function store(){
		var service = 'lms.expedicao.manterPrealertasAction.store';
		storeButtonScript(service, 'storePreAlerta', document.forms[0]);
	}
	
	function showPopUpGerarPedidoColetaAeroportoParceira(){
		var parameters = '&idFilialDestinoAwb=' + getElementValue("awb.filialByIdFilialDestino.idFilial") + 
				'&sgFilialDestinoAwb=' + getElementValue("awb.filialByIdFilialDestino.sgFilial") + 
				'&nmFilialDestinoAwb=' + getElementValue("awb.filialByIdFilialDestino.pessoa.nmFantasia") +
				'&nrIdentificacaoPessoa=' + getElementValue("awb.clienteByIdClienteDestinatario.pessoa.nrIdentificacao") + 
				'&idPessoa=' + getElementValue("awb.clienteByIdClienteDestinatario.pessoa.idPessoa") +
				'&nmPessoa=' + getElementValue("awb.clienteByIdClienteDestinatario.pessoa.nmPessoa");
		showModalDialog('expedicao/gerarPedidoColetaAeroportoParceira.do?cmd=main' + parameters,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:470px;dialogHeight:160px;');
	}
	
</script>