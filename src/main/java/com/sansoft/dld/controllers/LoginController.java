package com.sansoft.dld.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sansoft.dld.dao.DetectorDAO;
import com.sansoft.dld.dao.IAgentDAO;
import com.sansoft.dld.dao.IFilesDAO;
import com.sansoft.dld.dao.ILoginDAO;
import com.sansoft.dld.entity.Agent;
import com.sansoft.dld.entity.Detector;
import com.sansoft.dld.entity.Files;
import com.sansoft.dld.entity.Login;
import com.sansoft.dld.util.JsonUtil;
import com.sansoft.dld.util.MailUtil;


@Controller
public class LoginController {

	@Autowired
	private ILoginDAO loginDAO;
	
	@Autowired
	private IAgentDAO agentDAO;
	
	@Autowired
	private IFilesDAO filesDAO;
	
	@Autowired
	private MailUtil mailUtil;
	
	@Autowired
	private DetectorDAO detectorDAO;
	
	@Autowired
	private JsonUtil jsonUtil;
	
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try
		{
			String username=request.getParameter("username")!=null?request.getParameter("username"):"";
			String password=request.getParameter("password")!=null?request.getParameter("password"):"";
			logger.info("Login Servlet Username:"+username+" Password:"+password);			
			Login login=new Login();
			login.setUsername(username);
			login.setPassword(password);
			login = loginDAO.findByUsernameAndPassword(login);
			if(login!=null){
				session.setAttribute("loginUser",login);
				return "distributerpage";
			} else {
				return "unauthorised";
			}					
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
			return "unauthorised";
		}
	}
	
	@RequestMapping(value="/createAgent", method = RequestMethod.POST)
	public String createAgent(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try
		{
			logger.info("Create Agent");
			String agentname=request.getParameter("agentname")!=null?request.getParameter("agentname"):"";
			String firstname=request.getParameter("firstname")!=null?request.getParameter("firstname"):"";
			String lastname=request.getParameter("lastname")!=null?request.getParameter("lastname"):"";
			String emailid=request.getParameter("emailid")!=null?request.getParameter("emailid"):"";
			String phonenumber=request.getParameter("phonenumber")!=null?request.getParameter("phonenumber"):"";
			Agent agent=new Agent();
			agent.setAgentname(agentname);
			agent.setFirstname(firstname);
			agent.setLastname(lastname);
			agent.setEmailid(emailid);
			agent.setPhonenumber(phonenumber);
			agent.setActive(true);
			agentDAO.save(agent);
			return "registersuccess";
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
			return "error";
		}
	}
	@RequestMapping(value="/getAgentList", method = RequestMethod.GET)
	public @ResponseBody JsonArray getAgentList(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		JsonArray jsonArray= new JsonArray();
		try
		{
			logger.info("Get Agent List");
			List<Agent> agentList=agentDAO.getAgentList();			
			for(Agent agent : agentList){
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", agent.getId());
				jsonObject.addProperty("agentname", agent.getAgentname());
				jsonObject.addProperty("firstname", agent.getFirstname());
				jsonObject.addProperty("lastname", agent.getLastname());
				jsonObject.addProperty("emailid", agent.getEmailid());
				jsonObject.addProperty("phonenumber", agent.getPhonenumber());
				jsonObject.addProperty("active", agent.isActive());
				jsonArray.add(jsonObject);				
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
		}
		return jsonArray;
	}
	@RequestMapping(value="/getDetectorList", method = RequestMethod.GET)
	public @ResponseBody JsonArray getDetectorList(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		JsonArray jsonArray= new JsonArray();
		try
		{
			logger.info("Get Detector List");
			List<Detector> detectorList=detectorDAO.getDetectorList();			
			for(Detector detector : detectorList){
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", detector.getId());
				jsonObject.addProperty("agentname", detector.getAgentname());
				jsonObject.addProperty("filename", detector.getFilename());
				jsonArray.add(jsonObject);				
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
		}
		return jsonArray;
	}
	
	@RequestMapping(value="/getFilesList", method = RequestMethod.GET)
	public @ResponseBody JsonArray getFilesList(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		JsonArray jsonArray= new JsonArray();
		try
		{
			logger.info("Get Files List");
			List<Files> filesList=filesDAO.getFilesList();			
			for(Files files : filesList){				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", files.getId());
				jsonObject.addProperty("agentname", files.getAgentname());
				jsonObject.addProperty("field", files.getField());
				jsonObject.addProperty("subject", files.getSubject());
				jsonObject.addProperty("filename", files.getFilename());
				jsonObject.addProperty("date", files.getDate().toString());
				jsonObject.addProperty("lock", files.isLock());
				jsonObject.addProperty("filetype", files.getFiletype());
				jsonObject.addProperty("size", files.getSize());
				jsonArray.add(jsonObject);				
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
		}
		return jsonArray;
	}
	
	@RequestMapping(value="/activate/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonArray activate(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id") int id)
	{
		JsonArray jsonArray= new JsonArray();
		try
		{
			logger.info("Activate Agent");
			agentDAO.activate(id);
			List<Agent> agentList=agentDAO.getAgentList();			
			for(Agent agent : agentList){
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", agent.getId());
				jsonObject.addProperty("agentname", agent.getAgentname());
				jsonObject.addProperty("firstname", agent.getFirstname());
				jsonObject.addProperty("lastname", agent.getLastname());
				jsonObject.addProperty("emailid", agent.getEmailid());
				jsonObject.addProperty("phonenumber", agent.getPhonenumber());
				jsonObject.addProperty("active", agent.isActive());
				jsonArray.add(jsonObject);				
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
		}
		return jsonArray;
	}
	@RequestMapping(value="/lock/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonArray lock(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id") int id)
	{
		JsonArray jsonArray= new JsonArray();
		try
		{
			logger.info("Lock Files");
			filesDAO.lock(id);
			logger.info("Get Files List");
			List<Files> filesList=filesDAO.getFilesList();			
			for(Files files : filesList){				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", files.getId());
				jsonObject.addProperty("agentname", files.getAgentname());
				jsonObject.addProperty("field", files.getField());
				jsonObject.addProperty("subject", files.getSubject());
				jsonObject.addProperty("filename", files.getFilename());
				jsonObject.addProperty("date", files.getDate().toString());
				jsonObject.addProperty("lock", files.isLock());
				jsonObject.addProperty("filetype", files.getFiletype());
				jsonObject.addProperty("size", files.getSize());
				jsonArray.add(jsonObject);				
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
		}
		return jsonArray;
	}
	@RequestMapping(value="/unlock/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonArray unlock(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id") int id)
	{
		JsonArray jsonArray= new JsonArray();
		try
		{
			logger.info("Lock Files");
			filesDAO.unlock(id);
			logger.info("Get Files List");
			List<Files> filesList=filesDAO.getFilesList();			
			for(Files files : filesList){				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", files.getId());
				jsonObject.addProperty("agentname", files.getAgentname());
				jsonObject.addProperty("field", files.getField());
				jsonObject.addProperty("subject", files.getSubject());
				jsonObject.addProperty("filename", files.getFilename());
				jsonObject.addProperty("date", files.getDate().toString());
				jsonObject.addProperty("lock", files.isLock());
				jsonObject.addProperty("filetype", files.getFiletype());
				jsonObject.addProperty("size", files.getSize());
				jsonArray.add(jsonObject);				
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
		}
		return jsonArray;
	}
	
	@RequestMapping(value="/deactivate/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonArray deactivate(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable("id") int id)
	{
		JsonArray jsonArray= new JsonArray();
		try
		{
			logger.info("Deactivate Agent");
			agentDAO.deactivate(id);
			List<Agent> agentList=agentDAO.getAgentList();			
			for(Agent agent : agentList){
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", agent.getId());
				jsonObject.addProperty("agentname", agent.getAgentname());
				jsonObject.addProperty("firstname", agent.getFirstname());
				jsonObject.addProperty("lastname", agent.getLastname());
				jsonObject.addProperty("emailid", agent.getEmailid());
				jsonObject.addProperty("phonenumber", agent.getPhonenumber());
				jsonObject.addProperty("active", agent.isActive());
				jsonArray.add(jsonObject);				
			}
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
		}
		return jsonArray;
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody String uploadFileHandler(@RequestParam("agentname") String agentname, @RequestParam("uploadFile") MultipartFile file, @RequestParam("field") String field,@RequestParam("subject") String subject) {
		try{
			logger.info("Upload Files");
			if (!file.isEmpty()) {
	            try {
	                byte[] bytes = file.getBytes();
	                String rootPath = System.getProperty("catalina.home");
	                File dir = new File(rootPath + File.separator + "uploadFiles");
	                if (!dir.exists())
	                    dir.mkdirs();
	                String fileFullName = dir.getAbsolutePath() + File.separator +  file.getOriginalFilename();
	                File serverFile = new File(fileFullName);	                
	                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
	                stream.write(bytes);
	                stream.close();	
	                FileStore store1 = java.nio.file.Files.getFileStore(serverFile.toPath());
	    			boolean supported1 = store1.supportsFileAttributeView(UserDefinedFileAttributeView.class);
	    			System.out.println(store1.name() + " --- " + supported1);
	    			UserDefinedFileAttributeView udfav = java.nio.file.Files.getFileAttributeView(serverFile.toPath(),	UserDefinedFileAttributeView.class);
	    			for (String name : udfav.list()) {
	    				ByteBuffer buf = ByteBuffer.allocate(udfav.size(name));
	    				udfav.read(name, buf);
	    				buf.flip();
	    				String value = Charset.defaultCharset().decode(buf).toString();
	    				System.out.println(udfav.size(name) + " " + name + ":" + value);
	    			}
	    			
	    			int written = udfav.write("file.description", Charset.defaultCharset().encode(agentname));
	    			for (String name : udfav.list()) {
	    				System.out.println(udfav.size(name) + " " + name);
	    				ByteBuffer buf = ByteBuffer.allocate(udfav.size(name));
	    				udfav.read(name, buf);
	    				buf.flip();
	    				String value = Charset.defaultCharset().decode(buf).toString();
	    				System.out.println(udfav.size(name) + " " + name + ":" + value);
	    			}
	                String sender = "",receiver="";
	                List<Agent> agentList=agentDAO.getAgentByName(agentname);
	                for(Agent agent: agentList){
	                	if(agent.isActive()){
	                		sender="meganathanlosser@gmail.com";
	                		receiver=agent.getEmailid();
	                	} else {
	                		return "Filed to Share the file,due to agent is inactive";
	                	}
	                }
	                Files files = new Files();
	                files.setAgentname(agentname);
	                files.setField(field);
	                files.setSubject(subject);
	                files.setFilename(fileFullName);
	                files.setDate(new Date());
	                files.setFiletype(serverFile.getPath().substring(fileFullName.lastIndexOf(".")+1));
	                files.setSize((int)serverFile.length());
	                filesDAO.save(files);        
	                mailUtil.sendMail(sender,receiver,"File Shared ","File shared can be downloaded with the help of the following url http://localhost:8080/dld/OTP?filename="+file.getOriginalFilename()+"&agentname="+agentname+" . kindly keep the files shared confidentaly");
	                logger.info("Server File Location="+ serverFile.getAbsolutePath());
	                return "You successfully uploaded file=" + file.getName();
	            } catch (Exception e) {
	                return "You failed to upload " + file.getName() + " => " + e.getMessage();
	            }
	        } else {
	            return "You failed to upload " + file.getName()+ " because the file was empty.";
	        }
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
			return ex.getMessage();
		}
        
    }
	@RequestMapping(value = "/checkFile", method = RequestMethod.POST)
    public @ResponseBody String checkFileHandler(@RequestParam("uploadFile") MultipartFile file) {
		try{
			logger.info("Upload Files");
			if (!file.isEmpty()) {
	            try {
	                byte[] bytes = file.getBytes();
	                String rootPath = System.getProperty("catalina.home");
	                File dir = new File(rootPath + File.separator + "tempFiles");
	                if (!dir.exists())
	                    dir.mkdirs();
	                String fileFullName = dir.getAbsolutePath() + File.separator +  file.getOriginalFilename();
	                File serverFile = new File(fileFullName);	                
	                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
	                stream.write(bytes);
	                stream.close();
	                FileStore store1 = java.nio.file.Files.getFileStore(serverFile.toPath());
	    			boolean supported1 = store1.supportsFileAttributeView(UserDefinedFileAttributeView.class);
	    			System.out.println(store1.name() + " --- " + supported1);
	    			UserDefinedFileAttributeView udfav = java.nio.file.Files.getFileAttributeView(serverFile.toPath(),	UserDefinedFileAttributeView.class);
	    			for (String name : udfav.list()) {
	    				ByteBuffer buf = ByteBuffer.allocate(udfav.size(name));
	    				udfav.read(name, buf);
	    				buf.flip();
	    				String value = Charset.defaultCharset().decode(buf).toString();
	    				System.out.println(udfav.size(name) + " " + name + ":" + value);
	    			}
	    			int length =(int)serverFile.length();
	    			String agentnames="";
	    			List<Files> filesList=filesDAO.getFilesList();
	    			for(Files f:filesList){
	    				if(f.getSize()==length){
	    					agentnames +=f.getAgentname(); 
	    				}
	    			}
	                return "AgentName:"+agentnames;
	            } catch (Exception e) {
	                return "You failed to upload " + file.getName() + " => " + e.getMessage();
	            }
	        } else {
	            return "You failed to upload " + file.getName()+ " because the file was empty.";
	        }
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
			return ex.getMessage();
		}
        
    }
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam("filename") String filename, @RequestParam("agentname") String agentname, @RequestParam("OTP") String otp,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		try{
			if(!otp.equals(""+session.getAttribute("OTP"))){
				String errorMessage = "Sorry. OTP not match";
	            OutputStream outputStream = response.getOutputStream();
	            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
	            outputStream.close();
	            Detector detector = new Detector();
	            detector.setAgentname(agentname);
	            detector.setFilename(filename);
	            detectorDAO.save(detector);
	            return;
			}
			logger.info("download Files");
			File file =null;
			List<Files> filesList = filesDAO.getFilesByAgentnameFilename(agentname, filename);
			for(Files files : filesList){
				file = new File(files.getFilename());
				if(!files.getAgentname().equals(agentname)){
					String errorMessage = "Sorry. Your are not autherised agent to access this file";
					OutputStream outputStream = response.getOutputStream();
		            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
		            outputStream.close();
		            Detector detector = new Detector();
		            detector.setAgentname(agentname);
		            detector.setFilename(filename);
		            detectorDAO.save(detector);
		            return;
				}
			}	
	        if(!file.exists()){
	            String errorMessage = "Sorry. The file you are looking for does not exist";
	            OutputStream outputStream = response.getOutputStream();
	            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
	            outputStream.close();
	            return;
	        }
	        //if(file.getName().endsWith(".docx")){
	        	//File destination = new File(file.getAbsolutePath().replace(".docx", new Date().getTime()+".docx"));
	        	//file = setAgentNameAtDocumnet(file, destination, agentname);            	
            //}
	        System.out.println("Download Started");
	        FileStore store1 = java.nio.file.Files.getFileStore(file.toPath());
			boolean supported1 = store1.supportsFileAttributeView(UserDefinedFileAttributeView.class);
			System.out.println(store1.name() + " --- " + supported1);
			UserDefinedFileAttributeView udfav = java.nio.file.Files.getFileAttributeView(file.toPath(),	UserDefinedFileAttributeView.class);
			for (String name : udfav.list()) {
				ByteBuffer buf = ByteBuffer.allocate(udfav.size(name));
				udfav.read(name, buf);
				buf.flip();
				String value = Charset.defaultCharset().decode(buf).toString();
				System.out.println(udfav.size(name) + " " + name + ":" + value);
			}
			/*
			int written = udfav.write("file.description", Charset.defaultCharset().encode(agentname));
			for (String name : udfav.list()) {
				System.out.println(udfav.size(name) + " " + name);
				ByteBuffer buf = ByteBuffer.allocate(udfav.size(name));
				udfav.read(name, buf);
				buf.flip();
				String value = Charset.defaultCharset().decode(buf).toString();
				System.out.println(udfav.size(name) + " " + name + ":" + value);
			}*/
	        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
	        if(mimeType==null){
	            System.out.println("mimetype is not detectable, will take default");
	            mimeType = "application/octet-stream";
	        }	         
	        logger.info("mimetype : "+mimeType);
	        FileInputStream inStream = new FileInputStream(file);
	        response.setContentType(mimeType);
	        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
	        response.setContentLength((int)file.length());	 
	        OutputStream outStream = response.getOutputStream();	         
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;	         
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }	         
	        inStream.close();
	        outStream.close(); 
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
		}
        
    }
	@RequestMapping(value = "/OTP", method = RequestMethod.GET)
    public @ResponseBody String OTP(@RequestParam("filename") String filename, @RequestParam("agentname") String agentname, HttpServletRequest request, HttpSession session) {
		try{
			Random r = new Random();
			int otp= r.nextInt(10000);
			session.setAttribute("OTP", otp);
			String sender="meganathanlosser@gmail.com";
			String receiver="";
			List<Agent> agentList=agentDAO.getAgentByName(agentname);
            for(Agent agent: agentList){
            	if(agent.isActive()){            		
            		receiver=agent.getEmailid();
            	} else {
            		return "Filed to Share the file,due to agent is inactive";
            	}
            }
			mailUtil.sendMail(sender,receiver,"OTP ","OTP for downloading file:"+otp);
			String output= "<html><head><link rel='stylesheet' href='css/otp.css' type='text/css'></head><body><div id='login'><center>OTP sent to your registered mail ID</center><form action='downloadFile' method='get'><fieldset><p>"
						+ "<input type='text' required  name='OTP' placeholder='Enter OTP'><input type='hidden' name='agentname' required value='"+agentname+"'><input type='hidden' name='filename' required value='"+filename+"'>"
				   		+ "</p><input type='submit' value='submit'></fieldset></form></div></body></html>";
			return output;
		}catch(Exception ex){
			logger.error(ex.toString(),ex);
			return "Error on generating OTP";
		}
        
    }

	public ILoginDAO getLoginDAO() {
		return loginDAO;
	}

	public void setLoginDAO(ILoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	public IAgentDAO getAgentDAO() {
		return agentDAO;
	}

	public void setAgentDAO(IAgentDAO agentDAO) {
		this.agentDAO = agentDAO;
	}

	public IFilesDAO getFilesDAO() {
		return filesDAO;
	}

	public void setFilesDAO(IFilesDAO filesDAO) {
		this.filesDAO = filesDAO;
	}

	public MailUtil getMailUtil() {
		return mailUtil;
	}

	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public JsonUtil getJsonUtil() {
		return jsonUtil;
	}

	public void setJsonUtil(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}

	public DetectorDAO getDetectorDAO() {
		return detectorDAO;
	}

	public void setDetectorDAO(DetectorDAO detectorDAO) {
		this.detectorDAO = detectorDAO;
	}
	public File setAgentNameAtDocumnet(File source, File destination, String agentname){
		try{
			XWPFDocument doc = new XWPFDocument(OPCPackage.open(source));
			XWPFParagraph lastParagraph = doc.getLastParagraph();
			List<XWPFRun> runs =lastParagraph.getRuns();
			runs.get(runs.size() - 1).setText(runs.get(runs.size() - 1).getText(0)+"AgentName:"+agentname);					
		    doc.write(new FileOutputStream(destination));
		} catch(Exception ex){
			logger.error(ex.toString(),ex);
			return source;
		}
		return destination;
	}
}
