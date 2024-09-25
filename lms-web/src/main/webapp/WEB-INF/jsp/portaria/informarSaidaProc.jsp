<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	
	function saidaPageLoad_cb(){
		onPageLoad_cb();
		
		if (dialogArguments) {
			setElementValue("idControleTemp", dialogArguments.document.getElementById("idControleTemp").value);
			setElementValue("idFilial", dialogArguments.document.getElementById("filial.idFilial").value);
		}		

		findDadosSaida();
		
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
<adsm:window title="informarSaidas" onPageLoadCallBack="saidaPageLoad">
	<adsm:form action="/portaria/informarSaida" idProperty="idMeioTransporte">

	<td colspan="100" >
		
	<div id="principal" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>	
	
		<adsm:hidden property="idControleCarga"/>
		<adsm:hidden property="nrControleCarga"/>
		<adsm:hidden property="sgFilial"/>
		<adsm:hidden property="idOrdemSaida"/>
		<adsm:hidden property="idControleEntSaidaTerceiro"/>
		<adsm:hidden property="idControleTemp"/>
		<adsm:hidden property="tpSaida"/>
		<adsm:hidden property="tpControleCarga"/>
		<adsm:hidden property="idFilial"/>
		
		<adsm:textbox dataType="text" property="dsTipoMeioTransporte" size="20" maxLength="20" label="tipoMeioTransporte" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" />
		
		<adsm:textbox dataType="text" property="nrFrotaTransportado" label="meioTransporte" size="8" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" >		
	        <adsm:textbox dataType="text" property="nrIdentificadorTransportado" size="20" disabled="true" cellStyle="vertical-Align:bottom"/>
		</adsm:textbox>
				
		<adsm:textbox dataType="text" property="nrFrotaReboque" label="semiReboque" size="8" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" >
			<adsm:textbox dataType="text" property="nrIdentificadorReboque" size="20" disabled="true" cellStyle="vertical-align:bottom"/>
        </adsm:textbox>
		
		<adsm:textbox dataType="text" property="nrIdentificacao" label="motorista" size="21" labelWidth="22%" width="78%" disabled="true" cellStyle="vertical-Align:bottom" >		  
        	<adsm:textbox dataType="text" property="nmPessoa" size="30" disabled="true" cellStyle="vertical-Align:bottom" />        
        </adsm:textbox>
        
        </table>
	</div>	
	<div id="lacre" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>
		
        <adsm:listbox property="lacres" label="lacres" labelWidth="22%" boxWidth="120" optionProperty="idLacreControleCarga" optionLabelProperty="nrLacres" size="5"/>
        
		</table>	
	</div>
	<div id="outros" style="display:;border:none;">
		<script>
			document.write(geraColunas());
		</script>
		
		<adsm:section caption="informacoesDaSaida" width="80"/>
		
		<adsm:textbox dataType="text" property="dhSaida" size="20" maxLength="20" label="dataHoraSaida" labelWidth="22%" width="78%" disabled="true" />

		</table>	
	</div>	
	<div id="quilometragem" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>
		<adsm:textbox dataType="decimal" mask="###,###" property="nrQuilometragem" size="20" maxLength="6" label="quilometragem" labelWidth="22%"  width="25%"/>

		<adsm:checkbox property="blVirouHodometro" label="virouHodometro" labelWidth="21%" width="30%"/>		

		<adsm:textarea property="obSaida" maxLength="500" label="observacao" columns="70" labelWidth="22%" width="78%"/>
		</table>
	</div>
		
	</table></div>
	</td></tr></table> 
	
		<adsm:buttonBar>
			<adsm:button id="confirmar" caption="confirmar" onclick="desabilitaConfirmar();"/>
			<adsm:button id="cancelar" caption="cancelar" onclick="cancela()" />
			<adsm:button caption="fechar" onclick="self.close();" disabled="false" id="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function afterConfirmar_cb(data, error){
		setDisabled("confirmar", false);
		if (error != undefined) {
			//Jira LMS-4124
			if (error.substring(0, 4) == "ERRO") {
				cancela();
			} else {
				alert(error);
			}
		} else {
			cancela();
		}
	}

	function cancela() {
		dialogArguments.loadGrids();
		window.close();
	}

	function desabilitaConfirmar() {
		setDisabled("confirmar", true);

		var sdo = createServiceDataObject(
				"lms.portaria.informarSaidaAction.executeConfirmaSaida",
				"afterConfirmar", buildFormBeanFromForm(document.forms[0]));
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	function findDadosSaida() {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", getElementValue("idControleTemp"));
		setNestedBeanPropertyValue(data, "idFilial", getElementValue("idFilial"));
		var sdo = createServiceDataObject("lms.portaria.informarSaidaAction.findDadosSaida", "preencheDadosSaida", data);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	function preencheDadosSaida_cb(data, error) {
		onDataLoad_cb(data, error);

		if (data != undefined) {

			var tpControleCarga = getNestedBeanPropertyValue(data, "tpControleCarga");

			var idControleCarga = getNestedBeanPropertyValue(data, "idControleCarga");
			setElementValue("idControleCarga", idControleCarga);

			var blInformaKmPortaria = getNestedBeanPropertyValue(data, "blInformaKmPortaria");

			if (idControleCarga != undefined) {
				document.getElementById("lacre").style.display = "";

				if (tpControleCarga == "C") {
					document.getElementById("quilometragem").style.display = "";
					if (blInformaKmPortaria == "true") {
						document.getElementById("nrQuilometragem").required = "true";
						setFocus("nrQuilometragem");
					} else {
						setDisabled("nrQuilometragem", true);
						setDisabled("obSaida", true);
						setDisabled("blVirouHodometro", true);
						setFocus("cancelar", false);
					}
				} else {
					setFocus("cancelar", false);
				}
			} else {
				setFocus("cancelar", false);
			}

			verificarMeioTransporteParaAuditoria(getElementValue("idFilial"),
					data.idMeioTransporte);
		}
	}

	function verificarMeioTransporteParaAuditoria(idFilial, idMeioTransporte) {
		if (getElementValue("idControleCarga") != undefined
				&& getElementValue("idControleCarga") != "") {
			var data = new Array();
			setNestedBeanPropertyValue(data, "idFilial", idFilial);
			setNestedBeanPropertyValue(data, "idMeioTransporte",
					idMeioTransporte);
			var sdo = createServiceDataObject(
					"lms.portaria.informarSaidaAction.executeFindMeioTransporteAuditoria",
					"retornoAuditoria", data);
			xmit({
				serviceDataObjects : [ sdo ]
			});
		}
	}

	function retornoAuditoria_cb(data, error) {
		if (error != undefined) {
			alert(error);
			cancela();
		} else if (data != undefined && data._value != undefined) {
			alert(data._value);
			cancela();
		}
	}
</script>