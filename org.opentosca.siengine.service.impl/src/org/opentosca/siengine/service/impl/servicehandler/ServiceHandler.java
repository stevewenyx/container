package org.opentosca.siengine.service.impl.servicehandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentosca.core.endpoint.service.ICoreEndpointService;
import org.opentosca.instancedata.service.IInstanceDataService;
import org.opentosca.siengine.plugins.service.ISIEnginePluginService;
import org.opentosca.toscaengine.service.IToscaEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class that handles all needed services for SIEngine.<br>
 * <br>
 * 
 * Copyright 2013 IAAS University of Stuttgart <br>
 * <br>
 * 
 * In this class the from the SIEngine needed services are binded an unbinded.
 * 
 * 
 * @see ISIEnginePluginService
 * @see IToscaEngineService
 * @see ICoreEndpointService
 * 
 * @author Michael Zimmermann - zimmerml@studi.informatik.uni-stuttgart.de
 * 
 */

public class ServiceHandler {
	
	public static Map<String, ISIEnginePluginService> pluginServices = Collections.synchronizedMap(new HashMap<String, ISIEnginePluginService>());
	public static IInstanceDataService instanceDataService,
			oldInstanceDataService;
	public static ICoreEndpointService endpointService, oldEndpointService;
	public static IToscaEngineService toscaEngineService,
			oldToscaEngineService;
	
	private final static Logger LOG = LoggerFactory.getLogger(ServiceHandler.class);
	
	
	/**
	 * Bind EndpointService.
	 * 
	 * @param endpointService - The endpointService to register.
	 */
	public void bindEndpointService(ICoreEndpointService endpointService) {
		if (endpointService != null) {
			if (ServiceHandler.endpointService == null) {
				ServiceHandler.endpointService = endpointService;
			} else {
				ServiceHandler.oldEndpointService = endpointService;
				ServiceHandler.endpointService = endpointService;
			}
			
			ServiceHandler.LOG.debug("Bind Endpoint Service: {} bound.", endpointService.toString());
		} else {
			ServiceHandler.LOG.error("Bind Endpoint Service: Supplied parameter is null!");
		}
		
	}
	
	/**
	 * Unbind EndpointService.
	 * 
	 * @param endpointService - The endpointService to unregister.
	 */
	public void unbindEndpointService(ICoreEndpointService endpointService) {
		if (ServiceHandler.oldEndpointService == null) {
			endpointService = null;
		} else {
			ServiceHandler.oldEndpointService = null;
		}
		
		ServiceHandler.LOG.debug("Unbind Endpoint Service unbound.");
	}
	
	/**
	 * Bind ToscaEngineService
	 * 
	 * @param toscaEngineService
	 */
	public void bindToscaService(IToscaEngineService toscaEngineService) {
		if (toscaEngineService != null) {
			if (ServiceHandler.toscaEngineService == null) {
				ServiceHandler.toscaEngineService = toscaEngineService;
			} else {
				ServiceHandler.oldToscaEngineService = toscaEngineService;
				ServiceHandler.toscaEngineService = toscaEngineService;
			}
			
			ServiceHandler.LOG.debug("Bind ToscaEngineService: {} bound.", toscaEngineService.toString());
		} else {
			ServiceHandler.LOG.error("Bind ToscaEngineService: Supplied parameter is null!");
		}
	}
	
	/**
	 * Unbind ToscaEngineService
	 * 
	 * @param toscaEngineService
	 */
	public void unbindToscaService(IToscaEngineService toscaEngineService) {
		if (ServiceHandler.oldToscaEngineService == null) {
			toscaEngineService = null;
		} else {
			ServiceHandler.oldToscaEngineService = null;
		}
		
		ServiceHandler.LOG.debug("Unbind ToscaEngineService unbound.");
	}
	
	/**
	 * Bind InstanceDataService
	 * 
	 * @param instanceDataService
	 */
	public void bindInstanceDataService(IInstanceDataService instanceDataService) {
		if (instanceDataService != null) {
			if (ServiceHandler.instanceDataService == null) {
				ServiceHandler.instanceDataService = instanceDataService;
			} else {
				ServiceHandler.oldInstanceDataService = instanceDataService;
				ServiceHandler.instanceDataService = instanceDataService;
			}
			
			ServiceHandler.LOG.debug("Bind InstanceDataServiceInterface: {} bound.", ServiceHandler.instanceDataService.toString());
		} else {
			ServiceHandler.LOG.error("Bind InstanceDataServiceInterface: Supplied parameter is null!");
		}
	}
	
	/**
	 * Unbind InstanceDataServiceInterface
	 * 
	 * @param instanceDataService
	 */
	public void unbindInstanceDataService(IInstanceDataService instanceDataService) {
		if (ServiceHandler.oldInstanceDataService == null) {
			instanceDataService = null;
		} else {
			ServiceHandler.oldInstanceDataService = null;
		}
		
		ServiceHandler.LOG.debug("Unbind InstanceDataServiceInterface unbound.");
	}
	
	/**
	 * Bind SIPluginServices and store them in local HashMap.
	 * 
	 * @param plugin - A SIPluginServices to register.
	 */
	public void bindPluginService(ISIEnginePluginService plugin) {
		if (plugin != null) {
			
			List<String> types = plugin.getSupportedTypes();
			
			for (String type : types) {
				ServiceHandler.pluginServices.put(type, plugin);
				ServiceHandler.LOG.debug("Bound SI-Plugin Service: {} for Type: {}", plugin.toString(), type);
			}
			
		} else {
			ServiceHandler.LOG.error("Bind SI-Plugin Service: Supplied parameter is null!");
		}
	}
	
	/**
	 * Unbind SIPluginServices and delete them from Map.
	 * 
	 * @param plugin - A SIPluginServices to unregister.
	 */
	public void unbindPluginService(ISIEnginePluginService plugin) {
		if (plugin != null) {
			
			List<String> types = plugin.getSupportedTypes();
			
			for (String type : types) {
				Object deletedObject = ServiceHandler.pluginServices.remove(type);
				if (deletedObject != null) {
					ServiceHandler.LOG.debug("Unbound SI-Plugin Service: {} for Type: {}", plugin.toString(), type);
				} else {
					ServiceHandler.LOG.debug("SI-Plug-in {} could not be unbound, because it is not bound!", plugin.toString());
				}
			}
		}
		
		else {
			ServiceHandler.LOG.error("Unbind Plugin Service: Supplied parameter is null!");
		}
	}
}
