package com.sansoft.dld.dao;

import java.util.List;

import com.sansoft.dld.entity.Agent;

public interface IAgentDAO {
	public void save(Agent agent);
	
	public List<Agent> getAgentList();
	
	public List<Agent> getAgentByName(String agentname);
	
	public void activate(int id);
	
	public void deactivate(int id);
}
