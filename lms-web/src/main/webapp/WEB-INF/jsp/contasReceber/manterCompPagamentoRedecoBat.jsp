<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.creditoBancarioAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-36378"/>
	</adsm:i18nLabels>

	<adsm:form action="/contasReceber/manterCompPagamentoRedeco">
		<adsm:hidden property="nrBanco"/>
		<adsm:hidden property="idBanco"/>
		<adsm:hidden property="idRedeco"/>

		<adsm:lookup idProperty="idFilial" 
			property="filial" 
			criteriaProperty="sgFilial"
			service="lms.contasreceber.manterBoletosAction.findLookupFilial" 
			dataType="text"
			label="filial" 
			size="5" 
			maxLength="3" 
			action="/municipios/manterFiliais" 
			minLengthForAutoPopUpSearch="3" 
			exactMatch="true">
			
			<adsm:propertyMapping 
				relatedProperty="filial.pessoa.nmFantasia" 
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox 
				dataType="text" 
				property="filial.pessoa.nmFantasia" 
				size="30" 
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup 
			dataType="integer" 
			property="agenciaBancaria.banco" 
			idProperty="idBanco"
			service="lms.contasreceber.manterBoletosAction.findLookupBanco" 
			label="banco" 
			size="3" 
			maxLength="3"
			allowInvalidCriteriaValue="true"
		    serializable="false"
			criteriaProperty="nrBanco" 
			action="/configuracoes/manterBancos">
 				
			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				formProperty="agenciaBancaria.banco.nmBanco"/> 
				
			<adsm:propertyMapping 
				modelProperty="nrBanco" 
				formProperty="nrBanco"/> 

			<adsm:propertyMapping 
				modelProperty="idBanco" 
				formProperty="idBanco"/> 

				
			<adsm:textbox 
				property="agenciaBancaria.banco.nmBanco" 
				dataType="text" 
				size="20" 
				maxLength="30" 
				disabled="true" 
				serializable="false"/>
				
		</adsm:lookup>


		<adsm:range label="dtCredito">
			<adsm:textbox property="dataCreditoInicial" dataType="JTDate"/>
			<adsm:textbox property="dataCreditoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:range label="dtAlteracao">
			<adsm:textbox property="dataAlteracaoInicial" dataType="JTDate"/>
			<adsm:textbox property="dataAlteracaoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:combobox label="modalidade" property="modalidade" domain="DM_TP_MODALIDADE_CRED_BANC" boxWidth="200" renderOptions="true"/>

		<adsm:combobox label="origemRegistro" property="origemRegistro" domain="DM_TP_ORIGEM_CRED_BANC" boxWidth="200" renderOptions="true" />

		<adsm:range label="valorCredito" >
			<adsm:textbox property="vlCreditoInicial" dataType="currency" size="10"/>
			<adsm:textbox property="vlCreditoFinal" dataType="currency" size="10"/>
		</adsm:range>

		<adsm:range label="valorSaldo" >
			<adsm:textbox property="vlSaldoInicial" dataType="currency" size="10"/>
			<adsm:textbox property="vlSaldoFinal" dataType="currency" size="10"/>
		</adsm:range>

		<adsm:textbox maxLength="40" size="43" dataType="text" property="cpfCnpj" label="cpfCnpj" />
		
		<adsm:textbox label="numeroBoleto" property="nrBoleto" dataType="text" size = "43" />

		<adsm:textbox maxLength="40" size="43" dataType="text" property="nomeRazaoSocial" label="nomeRazaoSocial" />
		
		<adsm:textbox maxLength="40" size="43" dataType="text" property="observacoes" label="observacoes" />

		 <adsm:section caption="saldo"/>
		 <adsm:textbox maxLength="16" size="18" disabled="true" property="vlSomaSaldo" label="total" dataType="currency" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="composicao"/>
			<adsm:button caption="limpar" onclick="limpaCampos()" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idRemetenteOtd"
		property="composicao" 
		unique="true"
		rowCountService="lms.contasreceber.creditoBancarioAction.getRowCountCreditoBancarioLote" 
		service="lms.contasreceber.creditoBancarioAction.findPaginatedCreditoBancarioLote"
		gridHeight="130"
		rows="24" 
		onDataLoadCallBack="setSaldoTotal"
		scrollBars="both"
		selectionMode="none"
		onRowClick="onRowClick">
		
		<adsm:gridColumn title="filial" property="filial.sgFilial" dataType="text" width="80" />
		<adsm:gridColumn title="banco" property="banco.nmBanco" dataType="text" width="120" />
		<adsm:gridColumn title="dtCredito" property="dtCredito" dataType="JTDate" width="100" />
		<adsm:gridColumn title="vlCredito" property="vlCredito" dataType="currency" width="100" />
		<adsm:gridColumn title="saldo" property="saldo" dataType="currency" width="100" />
		<adsm:gridColumn title="modalidade" property="tpModalidade" isDomain="true" width="100" />
		<adsm:gridColumn title="origem" property="tpOrigem" isDomain="true" width="80" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="100" />
		<adsm:gridColumn title="cpfCnpj" property="dsCpfCnpj" dataType="text" width="100" />
		<adsm:gridColumn title="nomeRazaoSocial" property="dsNomeRazaoSocial" dataType="text" width="100" />
		<adsm:gridColumn title="numeroBoleto" property="dsBoleto" dataType="text" width="100" />
		<adsm:gridColumn title="observacoes" property="obCreditoBancario" dataType="text" width="120"/>
		<adsm:gridColumn title="dataHoraAlteracao" dataType="JTDateTimeZone" property="dhAlteracao" width="100" />
		<adsm:gridColumn title="usuarioAlteracao" property="usuario.usuarioADSM.nmUsuario" dataType="text" width="100" />
		
	</adsm:grid>

	<adsm:buttonBar>
		<adsm:button property="gridImportar" caption="alocarCreditos" onclick="alocarCreditos()" disabled="false" buttonType="button"/>
	</adsm:buttonBar>

</adsm:window>

<script type="text/javascript">


	var gridList;
	var idRedeco;
	
	function myOnPageLoad() {

		onPageLoad();

		/**Seta os campos abaixo como mastelink para que não sejam limpos pelos eventos*/
		getElement("filialByIdFilial.sgFilial").masterLink = "true";

	}

	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);		
		findFilialUsuario();

	}

	/**Obtem informações da aba de listagem*/
	function obtemInfoRedeco() {
		/**Através da função tabGroup obtem a tab com o id pesq (Listagem)*/
		var tabGroup = getTabGroup(this.document);
		/**Obtem a tab listagem*/
		var tabPes = tabGroup.getTab("pesq");
		/**Seta as informações do Redeco nos campos */
		idRedeco = getElementValue(tabPes.getElementById("redeco.idRedeco"));
 
	}

	function findFilialUsuario(){
		if (getElement("filial.idFilial").masterLink == undefined){
			var dados = new Array();
	        
	        var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findFilialSessao", "findFilialUsuario", dados);
	        xmit({serviceDataObjects:[sdo]});
        }	
		
	}

	function findFilialUsuario_cb(data,erro){
		if (erro == undefined){
			setElementValue("filial.idFilial", data.filialByIdFilial.idFilial);
			setElementValue("filial.sgFilial", data.filialByIdFilial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.filialByIdFilial.pessoa.nmFantasia);
		}			

		if(data.filialByIdFilial.sgFilial != 'MTZ'){
			setDisabled('filial.idFilial',true);
		}else{
			setDisabled('filial.idFilial',false);
		}
		
	}

	function limpaCampos() {
		cleanButtonScript(this.document);
		cleanGrid();
		findFilialUsuario();
		
	}
	
	function alocarCreditos(data) {
		obtemInfoRedeco();
		setElementValue("idRedeco", idRedeco);
		var dados = new Array();
		var formFilter = buildFormBeanFromForm(document.forms[0]);
		
		var sdo = createServiceDataObject("lms.contasreceber.creditoBancarioAction.executeAlocarCreditos", "alocarCreditos", formFilter);
        xmit({serviceDataObjects:[sdo]});
		
	}

	function alocarCreditos_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
			
		}
		
		var processados = data.processados;
		var vlUtilizado = CurrencyFormat(data.vlUtilizado);
		var mensagem = getI18nMessage("LMS-36378");
		mensagem = mensagem.replace("{0}", processados);
		mensagem = mensagem.replace("{1}", vlUtilizado); 
		alert(mensagem);
		refreshGrid();
		
	}
	
	function onRowClick() {
		return false;
		
	}

	function refreshGrid() {
		var data = new Object();
		var form = buildFormBeanFromForm(window.document.forms[0]);
		composicaoGridDef.executeSearch(form, true);

	}
	
	function cleanGrid() {
		composicaoGridDef.resetGrid();

	}

	function setSaldoTotal_cb(data, erro) {
		if (erro == undefined) {
			gridList = data.list;
			if(gridList != undefined) {
				var somaSaldo = data.list[0].vlSomaSaldo;
				setElementValue("vlSomaSaldo", CurrencyFormat(somaSaldo));
				
			} else {
				setElementValue("vlSomaSaldo", "0,00");
				
			}
		}

	}

	function CurrencyFormat(number) {
		number = number.replace(',', '.') 
		var number = number.toString(), 
    		dollars = number.split('.')[0], 
    		cents = (number.split('.')[1] || '') +'00';
    		
		dollars = dollars.split('').reverse().join('')
        	.replace(/(\d{3}(?!$))/g, '$1.')
        	.split('').reverse().join('');
    		
    	return dollars + ',' + cents.slice(0, 2);
    	
	}
	
</script>
