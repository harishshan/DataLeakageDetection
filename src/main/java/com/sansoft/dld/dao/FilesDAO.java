package com.sansoft.dld.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sansoft.dld.entity.Agent;
import com.sansoft.dld.entity.Files;

public class FilesDAO implements IFilesDAO {

	private SessionFactory sessionFactory;
	
	private final Logger logger = LoggerFactory.getLogger(FilesDAO.class);
	
	public void save(Files files) {
		Session session=null;
		Transaction tx=null;
		try{
			logger.info("FilesDAO save method");
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(files);
			tx.commit();
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
			if(session!=null && (session.isConnected() || session.isOpen()) && tx!=null){
				tx.rollback();
			}
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
	}
	public List<Files> getFilesList() {
		Session session=null;
		List<Files> filesList = new ArrayList<Files>();
		try{
			logger.info("AgentDAO getAgentList method");
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Files.class);
			filesList=criteria.list();			 
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
		return filesList;
	}
	public List<Files> getFilesByAgentnameFilename(String agentname, String filename) {
		Session session=null;
		List<Files> filesList = new ArrayList<Files>();
		try{
			logger.info("AgentDAO getAgentList method");
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Files.class);
			criteria.add(Restrictions.eq("agentname", agentname));
			criteria.add(Restrictions.like("filename", "%"+filename));
			filesList=criteria.list();	 
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
		return filesList;
	}
	
	public void lock(int id){
		Session session=null;
		Transaction tx=null;
		List<Files> filesList = new ArrayList<Files>();
		try{
			logger.info("FilesDAO lock method");
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Files.class);
			criteria.add(Restrictions.eq("id", id));
			filesList=criteria.list();
			for(Files files : filesList){
				files.setLock(true);
				session.save(files);
			}
			tx.commit();
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
			if(session!=null && (session.isConnected() || session.isOpen()) && tx!=null){
				tx.rollback();
			}
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
	}
	
	public void unlock(int id){
		Session session=null;
		Transaction tx=null;
		List<Files> filesList = new ArrayList<Files>();
		try{
			logger.info("FilesDAO unlock method");
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Files.class);
			criteria.add(Restrictions.eq("id", id));
			filesList=criteria.list();
			for(Files files : filesList){
				files.setLock(false);
				session.save(files);
			}
			tx.commit();
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
			if(session!=null && (session.isConnected() || session.isOpen()) && tx!=null){
				tx.rollback();
			}
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
