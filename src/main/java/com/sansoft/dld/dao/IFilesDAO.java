package com.sansoft.dld.dao;

import java.util.List;

import com.sansoft.dld.entity.Files;

public interface IFilesDAO {
	public void save(Files files);
	public List<Files> getFilesList();
	public List<Files> getFilesByAgentnameFilename(String agentname, String filename);
	public void lock(int id);	
	public void unlock(int id);
}
