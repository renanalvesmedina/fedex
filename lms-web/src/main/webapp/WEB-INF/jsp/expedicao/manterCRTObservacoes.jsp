<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnLoadPage">
	<adsm:form
		action="/expedicao/manterCRTObservacoes">

		<adsm:section
			caption="observacoes22"
			width="60"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:textarea
			maxLength="500"
			property="observacaoDoctoServicos.dsObservacaoDoctoServico"
			width="99%"
			columns="80"
			rows="10"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:combobox
			domain="DM_LOCAL_ENTREGA_CRT"
			property="tpEntregarEm"
			onDataLoadCallBack="tpEntregarEmOnDataLoad"
			label="entregarEm"
			width="81%"
			labelWidth="18%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:textbox
			label="parceiroEntrega"
			property="dsParceiroEntrega"
			dataType="text"
			size="52"
			maxLength="60"
			labelWidth="18%"
			width="81%"/>

		<adsm:buttonBar
			freeLayout="true">
			<adsm:button
				id="btnSalvar"
				disabled="false"
				onclick="salvar()"
				caption="salvar"/> 
			<adsm:button
				onclick="self.close()"
				id="btnFechar"
				disabled="false"
				caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function salvar(){
		var service = 'lms.expedicao.manterCRTObservacoesAction.storeInSession';
		var data = buildFormBeanFromForm(document.forms[0]);
		var sdo = createServiceDataObject(service, 'storeInSession', data);
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function storeInSession_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}

		self.close();
	}
	/************************************************************\
	*
	\************************************************************/
	var ModoTela = {
		INCLUSAO : 'INCLUSAO'
		,VISUALIZACAO : 'VISUALIZACAO'
		/************************************************************\
		*
		\************************************************************/
		,setModo : function (modo){
			switch(modo){
				case this.INCLUSAO:
					this.setModoInclusao();
					break;
				case this.VISUALIZACAO:
					this.setModoVisualizacao();
					break;
				default:
					alert("Modo[" + modo + "] não reconhecido.");
					break;
			}
		}
		/************************************************************\
		*
		\************************************************************/
		,setModoInclusao : function (){
			this.controlDisabledButtons(true);
			setFocusOnFirstFocusableField();
		}
		/************************************************************\
		*
		\************************************************************/
		,setModoVisualizacao : function (){
			this.controlDisabledButtons(false);
			setFocus('btnFechar', false);
		}
		/************************************************************\
		*
		\************************************************************/
		,controlDisabledButtons : function (isInclusao){
			var isDisable = !isInclusao;
			setDisabled(document, isDisable);
			setDisabled('btnSalvar', isDisable);
			setDisabled('btnFechar', false);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnLoadPage(){
		onPageLoad();
	}
	/************************************************************\
	*
	\************************************************************/
	function populateWindow_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}
		
		if(data && data != undefined){
			setElementValue('dsParceiroEntrega', data.ctoInternacional.dsParceiroEntrega);
			setElementValue('tpEntregarEm', data.ctoInternacional.tpEntregarEm);

			if(data.ctoInternacional.observacaoDoctoServicos){
				setElementValue('observacaoDoctoServicos.dsObservacaoDoctoServico', data.ctoInternacional.observacaoDoctoServicos.dsObservacaoDoctoServico);
			}
		}

		var modo = dialogArguments.MODO_TELA;
		setIsEditavel(modo == 'INCLUSAO');
	}
	/************************************************************\
	*
	\************************************************************/
	function setIsEditavel(isEditavel){
		var modo = isEditavel ? ModoTela.INCLUSAO : ModoTela.VISUALIZACAO;
		ModoTela.setModo(modo);
	}
	/************************************************************\
	*
	\************************************************************/
	function tpEntregarEmOnDataLoad_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}
		tpEntregarEm_cb(data);

		var service = 'lms.expedicao.manterCRTObservacoesAction.findCtoInternacionalInSession';
		var sdo = createServiceDataObject(service, 'populateWindow', {});
		xmit({serviceDataObjects:[sdo]});
	}
</script>