package com.sansoft.dld.dao;

import java.util.List;

import com.sansoft.dld.entity.Detector;

public interface IDetectorDAO {
	public void save(Detector detector);
	public List<Detector> getDetectorList();
}
