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

public class AgentDAO implements IAgentDAO {

	private SessionFactory sessionFactory;
	
	private final Logger logger = LoggerFactory.getLogger(AgentDAO.class);
	
	public void save(Agent agent) {
		Session session=null;
		Transaction tx=null;
		try{
			logger.info("AgentDAO save method");
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(agent);
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

	public List<Agent> getAgentList() {
		Session session=null;
		List<Agent> agentList = new ArrayList<Agent>();
		try{
			logger.info("AgentDAO getAgentList method");
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Agent.class);
			agentList=criteria.list();			 
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
		return agentList;
	}
	
	public List<Agent> getAgentByName(String agentname) {
		Session session=null;
		List<Agent> agentList = new ArrayList<Agent>();
		try{
			logger.info("AgentDAO getAgentList method");
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Agent.class);
			criteria.add(Restrictions.eq("agentname", agentname));
			agentList=criteria.list();			 
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
		return agentList;
	}
	
	public void activate(int id){
		Session session=null;
		Transaction tx=null;
		List<Agent> agentList = new ArrayList<Agent>();
		try{
			logger.info("AgentDAO activate method");
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Agent.class);
			criteria.add(Restrictions.eq("id", id));
			agentList=criteria.list();
			for(Agent agent : agentList){
				agent.setActive(true);
				session.save(agent);
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
	
	public void deactivate(int id){
		Session session=null;
		Transaction tx=null;
		List<Agent> agentList = new ArrayList<Agent>();
		try{
			logger.info("AgentDAO deactivate method");
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Agent.class);
			criteria.add(Restrictions.eq("id", id));
			agentList=criteria.list();
			for(Agent agent : agentList){
				agent.setActive(false);
				session.update(agent);
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
}
