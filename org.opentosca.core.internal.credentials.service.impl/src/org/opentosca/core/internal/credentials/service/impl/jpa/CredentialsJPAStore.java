package org.opentosca.core.internal.credentials.service.impl.jpa;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.opentosca.core.internal.credentials.service.impl.CoreInternalCredentialsServiceImpl;
import org.opentosca.core.model.credentials.Credentials;
import org.opentosca.exceptions.UserException;
import org.opentosca.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages credentials in the database by using Eclipse Link (JPA).<br />
 * <br />
 * Copyright 2013 IAAS University of Stuttgart<br />
 * <br />
 * 
 * @author Rene Trefft - rene.trefft@developers.opentosca.org
 * 
 */
public class CredentialsJPAStore {
	
	private final static Logger LOG = LoggerFactory.getLogger(CredentialsJPAStore.class);
	
	/**
	 * JDBC-URL to the OpenTOSCA database. It will be created if it does not
	 * exist yet.
	 * 
	 * @see org.opentosca.settings.Settings
	 */
	private final String DB_URL = "jdbc:derby:" + Settings.getSetting("databaseLocation") + ";create=true";
	
	/**
	 * JPA EntityManager and Factory. These variables are global, as we do not
	 * want to create a new EntityManager / Factory each time a method is
	 * called.
	 */
	private EntityManagerFactory emf;
	private EntityManager em;
	
	
	/**
	 * Initializes JPA.
	 */
	private void initJPA() {
		if (this.em == null) {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put(PersistenceUnitProperties.JDBC_URL, this.DB_URL);
			this.emf = Persistence.createEntityManagerFactory("Credentials", properties);
			this.em = this.emf.createEntityManager();
		}
	}
	
	/**
	 * Destructor. This method is called when the garbage collector destroys the
	 * class. We will then manually close the EntityManager / Factory and pass
	 * control back.
	 */
	@Override
	protected void finalize() throws Throwable {
		this.em.close();
		this.emf.close();
		super.finalize();
	}
	
	/**
	 * Persists the credentials {@code credentials}.
	 * 
	 * @param credentials
	 * 
	 * @return Generated ID of credentials.
	 * @throws UserException if {@code credentials} are already stored according
	 *             to unique constraints defined in {@link Credentials}.
	 */
	public long storeCredentials(Credentials credentials) throws UserException {
		
		String storageProviderID = credentials.getStorageProviderID();
		
		CredentialsJPAStore.LOG.debug("Storing credentials for storage provider \"{}\"...", storageProviderID);
		
		this.initJPA();
		
		try {
			
			this.em.getTransaction().begin();
			this.em.persist(credentials);
			this.em.getTransaction().commit();
			
		} catch (Exception exc) {
			// check if exception chain contains exception that indicates a
			// constraint violation
			for (Throwable excCause = exc.getCause(); excCause != null; excCause = excCause.getCause()) {
				if (excCause instanceof SQLIntegrityConstraintViolationException) {
					throw new UserException("Credentials for storage provider \"" + storageProviderID + "\" are already stored.");
				}
			}
		}
		
		long credentialsID = credentials.getID();
		
		CredentialsJPAStore.LOG.debug("Storing credentials for storage provider \"{}\" completed. Generated ID of credentials: \"{}\"", storageProviderID, credentialsID);
		
		return credentialsID;
	}
	
	/**
	 * @see CoreInternalCredentialsServiceImpl#getCredentials(String, String)
	 */
	public Credentials getCredentials(Long credentialsID) throws UserException {
		
		CredentialsJPAStore.LOG.debug("Retrieving credentials \"{}\"...", credentialsID);
		
		Credentials credentials = null;
		
		this.initJPA();
		
		Query getCredentialsQuery = this.em.createNamedQuery(Credentials.getCredentialsByID);
		getCredentialsQuery.setParameter("id", credentialsID);
		
		try {
			credentials = (Credentials) getCredentialsQuery.getSingleResult();
			CredentialsJPAStore.LOG.debug("Credentials \"{}\" were found.", credentialsID);
			return credentials;
		} catch (NoResultException exc) {
			throw new UserException("Credentials \"" + credentialsID + "\" were not found.");
		}
		
	}
	
	/**
	 * @see CoreInternalCredentialsServiceImpl#getAllCredentialsOfStorageProvider(String)
	 */
	public Set<Credentials> getAllCredentialsOfStorageProvider(String storageProviderID) {
		
		CredentialsJPAStore.LOG.debug("Retrieving all credentials for storage provider \"{}\"...", storageProviderID);
		
		this.initJPA();
		
		Query getAllCredentialsByStorageProviderIDQuery = this.em.createNamedQuery(Credentials.getAllCredentialsByStorageProviderID);
		getAllCredentialsByStorageProviderIDQuery.setParameter("storageProviderID", storageProviderID);
		
		@SuppressWarnings("unchecked")
		List<Credentials> allCredentialsOfStorageProvider = getAllCredentialsByStorageProviderIDQuery.getResultList();
		
		CredentialsJPAStore.LOG.debug("{} credentials for storage provider \"{}\" were found.", allCredentialsOfStorageProvider.size(), storageProviderID);
		
		return new HashSet<Credentials>(allCredentialsOfStorageProvider);
		
	}
	
	/**
	 * @see CoreInternalCredentialsServiceImpl#getCredentialsIDs()
	 */
	public Set<Long> getCredentialsIDs() {
		
		CredentialsJPAStore.LOG.debug("Retrieving IDs of all stored credentials...");
		
		this.initJPA();
		
		Query getCredentialsIDsQuery = this.em.createNamedQuery(Credentials.getCredentialsIDs);
		
		@SuppressWarnings("unchecked")
		List<Long> credentialsIDs = getCredentialsIDsQuery.getResultList();
		
		CredentialsJPAStore.LOG.debug("{} credentials ID(s) were found.", credentialsIDs.size());
		
		return new HashSet<Long>(credentialsIDs);
		
	}
	
	/**
	 * @see CoreInternalCredentialsServiceImpl#getAllCredentials()
	 */
	public Set<Credentials> getAllCredentials() {
		
		CredentialsJPAStore.LOG.debug("Retrieving all credentials...");
		
		this.initJPA();
		
		Query getAllCredentialsQuery = this.em.createNamedQuery(Credentials.getAllCredentials);
		
		@SuppressWarnings("unchecked")
		List<Credentials> allCredentials = getAllCredentialsQuery.getResultList();
		
		CredentialsJPAStore.LOG.debug("{} credentials were found.", allCredentials.size());
		
		return new HashSet<Credentials>(allCredentials);
		
	}
	
	/**
	 * Deletes credentials {@code credentialsID}.
	 * 
	 * @param credentialsID of credentials.
	 * 
	 * @throws UserException if credentials to delete were not found.
	 */
	public void deleteCredentials(long credentialsID) throws UserException {
		
		CredentialsJPAStore.LOG.debug("Deleting credentials \"{}\"...", credentialsID);
		
		this.initJPA();
		
		this.em.getTransaction().begin();
		Query removeCredentialsQuery = this.em.createNamedQuery(Credentials.removeCredentialsByID);
		removeCredentialsQuery.setParameter("id", credentialsID);
		int numDeletedCredentials = removeCredentialsQuery.executeUpdate();
		this.em.getTransaction().commit();
		
		if (numDeletedCredentials > 0) {
			CredentialsJPAStore.LOG.debug("Deleting credentials \"{}\" completed.", credentialsID);
		} else {
			throw new UserException("Credentials \"" + credentialsID + "\" to delete were not found.");
		}
		
	}
	
	/**
	 * Deletes all stored credentials.
	 */
	public void deleteAllCredentials() {
		
		CredentialsJPAStore.LOG.debug("Deleting all credentials...");
		
		this.initJPA();
		
		this.em.getTransaction().begin();
		Query removeAllCredentialsQuery = this.em.createNamedQuery(Credentials.removeAllCredentials);
		int numDeletedCredentials = removeAllCredentialsQuery.executeUpdate();
		this.em.getTransaction().commit();
		
		CredentialsJPAStore.LOG.debug("Deleted {} credentials.", numDeletedCredentials);
		
	}
	
}
