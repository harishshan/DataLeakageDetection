package com.sansoft.dld.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sansoft.dld.entity.Detector;

public class DetectorDAO implements IDetectorDAO {

	private SessionFactory sessionFactory;
	
	private final Logger logger = LoggerFactory.getLogger(FilesDAO.class);
	
	public void save(Detector detector) {
		Session session=null;
		Transaction tx=null;
		try{
			logger.info("DetectorDAO save method");
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(detector);
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

	public List<Detector> getDetectorList() {
		Session session=null;
		List<Detector> detectorList = new ArrayList<Detector>();
		try{
			logger.info("DetectorDAO getDetectorList method");
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Detector.class);
			detectorList=criteria.list();			 
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
		return detectorList;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
