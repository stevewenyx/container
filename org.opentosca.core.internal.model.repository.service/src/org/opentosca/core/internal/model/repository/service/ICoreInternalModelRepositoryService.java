package org.opentosca.core.internal.model.repository.service;

import java.util.List;

import javax.xml.namespace.QName;

import org.opentosca.core.model.csar.id.CSARID;
import org.opentosca.model.tosca.TDefinitions;

/**
 * This interface provides methods to to store and retrieve TOSCAs /
 * Definitions.
 * 
 * Copyright 2012 IAAS University of Stuttgart <br>
 * <br>
 * 
 * @author Matthias Fetzer - fetzerms@studi.informatik.uni-stuttgart.de
 * @author Rene Trefft - trefftre@studi.informatik.uni-stuttgart.de
 * 
 */
public interface ICoreInternalModelRepositoryService {
	
	// /**
	// * Stores a TOSCA file / Definitions.
	// *
	// * @param toscaFile to store
	// * @return If storing was successful the Definitions ID of the TOSCA file,
	// * otherwise <code>null</code>.
	// */
	// public QName storeTOSCA(File toscaFile);
	
	// /**
	// * @param definitionsID
	// * @return IDs of all service templates contained in Definitions with ID
	// * <code>definitionsID</code>.
	// */
	
	// /**
	// *
	// * @param csarID
	// * @param definitionsID
	// * @return IDs of all service templates contained in Definitions with ID
	// * <code>definitionsID</code> and CSAR <code>csarID</code>.
	// */
	// public List<QName> getServiceTemplateIDs(CSARID csarID, QName
	// definitionsID);
	
	// /**
	// * Deletes all stored Definitions / TOSCAs.
	// *
	// * @return Number of deleted Definitions.
	// */
	// public int deleteAllDefinitions();
	
	// /**
	// * Deletes a Definitions / TOSCA.
	// *
	// * @param definitionsID of Definitions to delete
	// * @return <code>true</code> if deletion was successful, otherwise
	// * <code>false</code>.
	// */
	// public boolean deleteDefinitions(QName definitionsID);
	
	/**
	 * 
	 * @return IDs of all Definitions of CSAR <code>csarID</code>.
	 */
	public List<QName> getAllDefinitionsIDs(CSARID csarID);
	
	/**
	 * @param definitionsID
	 * @return Definitions with ID <code> definitionsID</code> of CSAR
	 *         <code>csarID</code>. If it doesn't exist <code>null</code>.
	 */
	public TDefinitions getDefinitions(CSARID csarID, QName definitionsID);
	
}
