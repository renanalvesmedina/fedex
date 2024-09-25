<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.vol.manterEquipamentosAction.findDataSession","dataSession",data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Carrega os dados de filial do usuario logado
	 */
	var dataUsuario;
	function dataSession_cb(data, error) {
		dataUsuario = data;
		fillDataUsuario();
		onPageLoad_cb(data,error);
	}
	
	/**
	 * Faz o callBack do carregamento da pagina
	 */
	function loadPage_cb(data, error) {
		setDisabled("filial.idFilial", false);
		document.getElementById("filial.sgFilial").disabled=false;
		document.getElementById("filial.sgFilial").focus;
	}
	
	/**
	 * Retorna o parametro 'mode' que contem o modo em que a tela esta sendo utilizada.
	 * Caso mode seja igual a 'lookup' significa que a tela esta sendo aberta por uma lookup.
	 */
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}

	function initWindow(eventObj) {
		
		if (eventObj.name == "cleanButton_click") {
			fillDataUsuario();
			
			filial_sgFilialOnChangeHandler();	
			
			setFocus(document.getElementById("filial.sgFilial"));
		}
		 
	}

	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia); 
		}
	}
    
	
</script>
<adsm:window service="lms.vol.manterEquipamentosAction" onPageLoadCallBack="pageLoad">
	<adsm:form  action="/vol/manterEquipamentos">
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
	    <adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
	    <adsm:hidden property="tpAcesso" serializable="false" value="F"/>
		<adsm:lookup label="filial" width="8%" labelWidth="15%" 
				     property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.manterEquipamentosAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3" required="true">
		    <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>        
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />	
        	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" /> 
        	
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true" width="50%"/>
        </adsm:lookup>
		
		<adsm:lookup service="lms.vol.manterEquipamentosAction.findLookupModelo" dataType="text" property="volModeloseqp" idProperty="idModeloeqp"
				criteriaProperty="dsNome" label="modelo" size="20" maxLength="30"
				action="/vol/manterModelos" width="30%" />
				
		<adsm:lookup service="lms.vol.manterEquipamentosAction.findLookupOperadora" dataType="text" property="volOperadorasTelefonia" 
				idProperty="idOperadora" criteriaProperty="pessoa.nmPessoa" label="operadora" size="20" maxLength="30"
				action="/vol/manterOperadoras" width="30%" />
		
		<adsm:lookup service="lms.vol.manterEquipamentosAction.findLookupUso" dataType="text" property="volTiposUso" 
				idProperty="idTiposUso" criteriaProperty="dsNome" label="uso" size="20" maxLength="30"
				action="/vol/manterTiposUso" width="30%" />
		
		 <adsm:lookup label="meioTransporte" labelWidth="15%" width="80%" picker="false"
                      property="meioTransporte"
                      idProperty="idMeioTransporte"
                      criteriaProperty="nrFrota"
                      action="/contratacaoVeiculos/manterMeiosTransporte"
                      service="lms.vol.manterEquipamentosAction.findLookupMeioTransporte" 
                      dataType="text"
                      size="8" 
                      maxLength="6"
                      exactMatch="true"
                      required="false"
          >

              <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" />
              <!--  Criteria por nrIdentificador -->        
              <adsm:lookup 
                         property="meioTransporte2"
                         idProperty="idMeioTransporte"
                         criteriaProperty="nrIdentificador"
                         action="/contratacaoVeiculos/manterMeiosTransporte"
                         service="lms.vol.manterEquipamentosAction.findLookupMeioTransporte" 
                         dataType="text"
                         size="30" 
                         maxLength="25"
                         exactMatch="false"
                         serializable="false"
                         minLengthForAutoPopUpSearch="5"
              >

                  <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota" />
                  <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />      
             </adsm:lookup>
        </adsm:lookup>		
		
		<adsm:textbox property="numero" label="numero" dataType="integer" maxLength="15" size="16" width="30%"/>		
				
		<adsm:combobox property="tarifa" label="tpTarifa" width="35%" domain="DM_TP_TARIFA"
						autoLoad="true" onlyActiveValues="false"/>				
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="volEquipamentos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
		
	</adsm:form>

	<adsm:grid property="volEquipamentos" idProperty="idEquipamento" selectionMode="check" 
			   rows="8" gridHeight="160" unique="true" scrollBars="horizontal"
			   service="lms.vol.manterEquipamentosAction.findPaginatedEquipamentos" 
			   rowCountService="lms.vol.manterEquipamentosAction.getRowCountEquipamentos"
			   onPopulateRow="populateRow">
		<adsm:gridColumn property="numero" title="numero" width="100" align="right"/>
		<adsm:gridColumn property="modelo.dsModelo" title="modelo" width="150" />
		<adsm:gridColumn property="frota.nrFrota" title="meioTransporte" width="50" />
		<adsm:gridColumn property="frota.nrIdentificador" title="" width="80" align="right"/>
		<adsm:gridColumn property="filial.sgFilial" title="filial" width="50" />
		<adsm:gridColumn property="operadora.nmOperadora" title="operadora" width="150" />
		<adsm:gridColumn property="uso.dsUso" title="uso" width="100" />
		<adsm:gridColumn property="versao" title="versao" width="100" />
		<adsm:gridColumn property="dtAtualizacao" title="atualizacao" width="100" dataType="JTDateTimeZone"/>
		<adsm:gridColumn align="center" property="retiradoImg" title="retirado" width="100" image="/images/bandeira_verde.gif" link="javascript:volEquipamentos_cb"/>		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
  function volEquipamentos_cb(searchFilters) {
	 
  }

	function populateRow(tr,data) {
		var indice;
		var tab = getTab(this.document.forms[0].document);
		if (tab != null) {
			indice = 10;
		} else {
			indice = 9;
		}
		if (data.retirado!=0)
			tr.children[indice].innerHTML = tr.children[indice].innerHTML.replace("verde","vermelha"); 
	}
</script>
