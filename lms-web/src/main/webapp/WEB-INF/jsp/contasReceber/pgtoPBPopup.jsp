<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas" idProperty="idFatura">
		<adsm:hidden property="idFatura" />
		<adsm:hidden property="filialUsuarioLogado" />
		<adsm:hidden property="idRecebimento"/>
		
		<adsm:textbox  property="dtRecebimento" label="dataRecebimento" dataType="JTDate" labelWidth="18%" width="100%" required="true" disabled="true"/>
		<adsm:textbox  property="valorRecebimento" label="valorRecebimento" dataType="currency" maxLength="10"  labelWidth="18%"  width="100%" required="true" disabled="true"/>
		<adsm:textarea property="observacoes" label="observacoes" maxLength="500" rows="2" columns="40"  labelWidth="18%" width="100%" disabled="true"/>
		<adsm:textbox  property="nmUsuarioAlteracao" label="usuarioAlteracao" dataType="text" labelWidth="18%" width="100%" disabled="true"/>
		<adsm:textbox  property="dhCadastro" dataType="JTDateTimeZone" label="dataAlteracao" width="32%" maxLength="20" labelWidth="18%" disabled="true"/>
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="botaoSalvar"
					 onclick="storeButtonScript('lms.contasreceber.manterFaturasAction.storePgtoPB', 'store', this.form);" 
					 disabled="true" />
			<adsm:button caption="fechar" onclick="self.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
		function myOnPageLoad_cb(d, e, o, x) {
			var u = new URL(parent.location.href);
			setElementValue("idFatura", u.parameters["idFatura"]);
			var idFatura = u.parameters["idFatura"];
			var data = new Array();
			setNestedBeanPropertyValue(data, "idFatura", idFatura);
			
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.checkPgtoPB","checkPgtoPB",data);
			xmit({serviceDataObjects:[sdo]});
			
		}

		
		function checkPgtoPB_cb(data, erros) {
			var temFatura = getNestedBeanPropertyValue(data, "fatura");
			var filialMTZ = getNestedBeanPropertyValue(data, "filialMTZ");
			var usuario = getNestedBeanPropertyValue(data, "usuario");
			var idRecebimento = getNestedBeanPropertyValue(data, "idRecebimento");
			var valorRecebimento =  getNestedBeanPropertyValue(data, "valorRecebimento");
			var observacoes =  getNestedBeanPropertyValue(data, "observacoes");
			var dtRecebimento =  getNestedBeanPropertyValue(data, "dtRecebimento");
			var dtAlteracao =  getNestedBeanPropertyValue(data, "dtAlteracao");
			setElementValue("nmUsuarioAlteracao", usuario);
			setElementValue("idRecebimento", idRecebimento);
			setElementValue("valorRecebimento", setFormat(getElement("valorRecebimento"),valorRecebimento));
			setElementValue("observacoes", observacoes);
			setElementValue("dtRecebimento", dtRecebimento);
			setElementValue("dhCadastro", dtAlteracao);
			
			if ( temFatura === "true" && filialMTZ === "true" ){
				setDisabled("dtRecebimento", false);
				setDisabled("valorRecebimento", false);
				setDisabled("observacoes", false);
				setDisabled("botaoSalvar", false);
			}else{
				setDisabled("dtRecebimento", true);
				setDisabled("valorRecebimento", true);
				setDisabled("observacoes", true);
				setDisabled("botaoSalvar", true);
			}
		}
		

		function store_cb(dados, erros) {
			if (erros == undefined) {
				window.close();
			} else
				alert(erros);
		}
	</script>