package com.sansoft.dld.dao;

import com.sansoft.dld.entity.Login;

public interface ILoginDAO {	
	public Login findByUsernameAndPassword(Login login);
}
