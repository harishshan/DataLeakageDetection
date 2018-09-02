package com.sansoft.dld.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sansoft.dld.controllers.LoginController;
import com.sansoft.dld.entity.Login;

public class LoginDAO implements ILoginDAO{

	private SessionFactory sessionFactory;
	
	private final Logger logger = LoggerFactory.getLogger(LoginDAO.class);
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Login findByUsernameAndPassword(Login login) {
		Session session = null;
		try{
			logger.info("LoginDAO findByUsernameAndPassword method");
			session = this.sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Login.class);
			criteria.add(Restrictions.eq("username", login.getUsername()));
			criteria.add(Restrictions.eq("password", login.getPassword()));
			List<Login> loginList=criteria.list();
			return loginList.get(0);
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
			return null;
		}finally{
			if(session!=null && (session.isConnected() || session.isOpen())){
				session.close();
			}
		}
	}
	
	
}
