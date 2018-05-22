package com.omniacom.omniapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.omniacom.omniapp.service.StorageService;

@CrossOrigin
@RestController
public class UploadController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Autowired
	StorageService storageService;

	List<String> files = new ArrayList<String>();

	@GetMapping("/upload-controller")
	public ModelAndView index(Model model) {
		return new ModelAndView("upload");
	}

	@PostMapping("/upload-file")
	public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
		String message = "";
		System.out.println("upoad in .......................................");
		try {

			storageService.store(file);
			files.add(file.getOriginalFilename());

			return responseLinkData("http://localhost:8080/files/" + file.getOriginalFilename());
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	private ResponseEntity<HashMap<String, String>> responseLinkData(final String filename) {
		return ResponseEntity.ok(new HashMap<String, String>() {
			{
				put("link", filename);
			}
		});
	}

	@GetMapping("/getallfiles")
	public ResponseEntity<List<String>> getListFiles(Model model) {
		List<String> fileNames = files
				.stream().map(fileName -> MvcUriComponentsBuilder
						.fromMethodName(UploadController.class, "getFile", fileName).build().toString())
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(fileNames);
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.loadFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	/*
	 * @GetMapping("/files/") protected void doGet(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException { String
	 * filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
	 * File file = new File("E:\\Plateforme\\Workspace\\Doceo_spring\\upload-dir",
	 * filename); response.setHeader("Content-Type",
	 * getServletContext().getMimeType(filename));
	 * response.setHeader("Content-Length", String.valueOf(file.length()));
	 * response.setHeader("Content-Disposition", "inline; filename=\"" +
	 * file.getName() + "\""); Files.copy(file.toPath(),
	 * response.getOutputStream()); }
	 */

}
