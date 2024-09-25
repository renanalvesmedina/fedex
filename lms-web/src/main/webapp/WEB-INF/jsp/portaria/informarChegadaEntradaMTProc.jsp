<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	
	function chegadaPageLoad_cb(){
		onPageLoad_cb();
		
		if (dialogArguments) {
			setElementValue("idControleTemp", dialogArguments.document.getElementById("idControleTemp").value);
			setElementValue("idFilial", dialogArguments.document.getElementById("filial.idFilial").value);
		}
		
		setDisabled("encaminhar", false);
		setDisabled("cancelar", false);
		setDisabled("encaminharEstacionamento", false);
		findDadosChegada();
		
		window.returnValue = window;
	}
	
		/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';

		return colunas;	
	}	
</script>
<adsm:window title="entradaMeiosTransporteTitulo" onPageLoadCallBack="chegadaPageLoad">
	<adsm:form action="/portaria/selecionarMeiosTransporteChegada" idProperty="idMeioTransporte">
	
	<td colspan="100" >
		
	<div id="principal" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>	
	
		<adsm:hidden property="idControleCarga"/>
		<adsm:hidden property="idControleTrecho"/>
		<adsm:hidden property="versao"/>
		<adsm:hidden property="idOrdemSaida"/>
		<adsm:hidden property="idControleEntSaidaTerceiro"/>
		<adsm:hidden property="idControleTemp"/>
		<adsm:hidden property="tpChegada"/>
		<adsm:hidden property="tpControleCarga"/>
		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="idReboque"/> 
		
		<adsm:textbox dataType="text" property="dsTipoMeioTransporte" size="20" maxLength="20" label="tipoMeioTransporte" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" required="true"/>
		
		<adsm:textbox dataType="text" property="nrFrotaTransportado" label="meioTransporte" size="15" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom">		
	        <adsm:textbox dataType="text" property="nrIdentificadorTransportado" size="20" disabled="true" cellStyle="vertical-Align:bottom" required="true"/>
        </adsm:textbox>

		<adsm:textbox label="semiReboque" labelWidth="22%" width="78%" size="15" maxLength="20" dataType="text" property="nrFrotaReboque" disabled="true" cellStyle="vertical-Align:bottom">		
	        <adsm:textbox  size="20" dataType="text" property="nrIdentificadorReboque" disabled="true" cellStyle="vertical-Align:bottom" />
        </adsm:textbox>

		<adsm:textbox dataType="text" property="nrIdentificacao" label="motorista" size="15" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" required="true" >
        	<adsm:textbox dataType="text" property="nmPessoa" size="40" disabled="true" cellStyle="vertical-Align:bottom" />
      	</adsm:textbox>

		<adsm:textbox dataType="text" property="sgFilial" label="controleCarga" size="4" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" >
        	<adsm:textbox dataType="integer" property="nrControleCarga" mask="00000000" size="10" disabled="true" cellStyle="vertical-Align:bottom" />
      	</adsm:textbox>

		<adsm:textbox dataType="text" property="dsRota" label="rota" size="25" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" />

		</table>
	</div>	
	<div id="lacre" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>
		<adsm:listbox property="lacres" label="lacres" labelWidth="22%" boxWidth="120" optionProperty="idLacreControleCarga" optionLabelProperty="nrLacres" size="5"/>
		</table>
	</div>
	<div id="informacoesChegada" style="display:;border:none;">
		<script>
			document.write(geraColunas());
		</script>
			
		<adsm:section caption="informacoesChegada" width="80"/>
		
		<adsm:textbox dataType="JTDateTimeZone" picker="false" property="dhChegada" size="20" maxLength="20" label="dataHoraChegada" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-align:bottom;" required="true"/>		
		
		</table> 
	</div>
	<div id="quilometragem" style="display:none;border:none;">
	<script>
		document.write(geraColunas());
	</script>
	
 		<adsm:textbox dataType="decimal" maxLength="6" property="nrQuilometragem" size="20"  label="quilometragem" labelWidth="22%" width="28%"
			      	  mask="###,###"/> 		
 		<adsm:checkbox property="blVirouHodometro" label="virouHodometro" labelWidth="15%"/> 		
		<adsm:textarea property="obChegada" maxLength="50" label="observacao" columns="60" labelWidth="22%" width="78%"/>
	</table>
	</div>
	<div id="outros" style="display:;border:none;">
	<script>
		document.write(geraColunas()); 
	</script>
		<adsm:section caption="definirAreaDescargaEstacionamento" width="80"/>
		
		<adsm:combobox property="doca.idDoca" optionProperty="idDoca" optionLabelProperty="dsDoca" label="doca" labelWidth="22%" 
					   autoLoad="false"	width="78%" service="lms.portaria.informarChegadaAction.findDocas" boxWidth="200" /> 		
				
 		<adsm:combobox property="box.idBox" onDataLoadCallBack="boxesDataLoad" optionProperty="idBox" 
 					   optionLabelProperty="dsBox" service="lms.portaria.informarChegadaAction.findBoxes" 
 					   label="box" labelWidth="22%" width="78%" boxWidth="200">
 			<adsm:propertyMapping criteriaProperty="idMeioTransporte" modelProperty="idMeioTransporte"/>
 			<adsm:propertyMapping criteriaProperty="idFilial" modelProperty="idFilial"/>
 			<adsm:propertyMapping criteriaProperty="doca.idDoca" modelProperty="idDoca"/>
 			<adsm:propertyMapping criteriaProperty="tpControleCarga" modelProperty="tpControleCarga"/>
 		</adsm:combobox>
 		</table></div>
	</td></tr></table>

		<adsm:buttonBar>
			<adsm:button id="encaminhar" caption="encaminhar" onclick="encaminha();"/>
			<adsm:button id="encaminharEstacionamento" caption="encaminharEstacionamento" boxWidth="220" onclick="encaminhaEstacionamento();" />
			<adsm:button id="cancelar" caption="cancelar" onclick="cancela()" />
			<adsm:button caption="fechar" onclick="self.close();" disabled="false" id="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function cancela(){
		dialogArguments.loadGrids();
		window.close();
	}
	
	function encaminha(){
		document.getElementById("doca.idDoca").required = "true";
		document.getElementById("box.idBox").required = "true";
		
		storeButtonScript('lms.portaria.informarChegadaAction.executeEncaminhar', 'encaminhar', document.forms[0]);		
	}
	
	function encaminhaEstacionamento(){
		setDisabled("encaminharEstacionamento", true);
		document.getElementById("doca.idDoca").required = "false";
		document.getElementById("box.idBox").required = "false";
		
		storeButtonScript('lms.portaria.informarChegadaAction.executeEncaminhar', 'encaminhar', document.forms[0]);	
	}
	
	function encaminhar_cb(data, error){
		if (error != undefined) {
			//Jira LMS-4124
			if (error.substring(0, 4) == "ERRO") {
				cancela();
			} else {
				alert(error);
				return false;
			}
		} else {
			cancela();
		}
	}
	
	function findDadosChegada(){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", getElementValue("idControleTemp"));
		setNestedBeanPropertyValue(data, "idFilial", getElementValue("idFilial"));
		var sdo = createServiceDataObject("lms.portaria.informarChegadaAction.findDadosChegada",
											"preencheDadosChegada",data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	function preencheDadosChegada_cb(data, error){
		onDataLoad_cb(data, error);
		
		if (data != undefined){
			var tpChegada = getNestedBeanPropertyValue(data, "tpChegada");
			if (tpChegada != "V") {
				document.getElementById("quilometragem").style.display = "";	
				var blInformaKmPortaria = getNestedBeanPropertyValue(data, "blInformaKmPortaria");
				if (blInformaKmPortaria == "true") {
					document.getElementById("nrQuilometragem").required = "true";
				} else {
					setDisabled("nrQuilometragem", true);
					setDisabled("obChegada", true);		
					setDisabled("blVirouHodometro", true);					
				}			
			} else {
				document.getElementById("lacre").style.display = "";
			}

			var idMeioTransporte = getNestedBeanPropertyValue(data, "idMeioTransporte");
			loadDocas(idMeioTransporte);
			
			setFocusOnFirstFocusableField();
		}
	}

	function loadDocas(idMeioTransporte){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", getElementValue("idFilial"));
		setNestedBeanPropertyValue(data, "idMeioTransporte", idMeioTransporte);
		setNestedBeanPropertyValue(data, "tpControleCarga", getElementValue("tpControleCarga"));
		
		var sdo = createServiceDataObject("lms.portaria.informarChegadaAction.findDocas",
											"docasDataLoad",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function docasDataLoad_cb(data){
		doca_idDoca_cb(data);
		
		if (data != undefined && data.length > 0) {
			if (data.length == 1 || data[0].isPreferencial == "true"){
				document.getElementById("doca.idDoca").selectedIndex = 1;
				notifyElementListeners({e:document.getElementById("doca.idDoca")});
			}
		}
	}

	function boxesDataLoad_cb(data){
		box_idBox_cb(data);
		
		if (data != undefined && data.length > 0) {
			if (data.length == 1 || data[0].isPreferencial == "true"){
				document.getElementById("box.idBox").selectedIndex = 1;			
			}
		}
	}

</script>