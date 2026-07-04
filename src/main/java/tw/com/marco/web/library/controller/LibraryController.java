package tw.com.marco.web.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.com.marco.core.entity.Inventory;
import tw.com.marco.core.entity.User;
import tw.com.marco.core.pojo.RecordResult;
import tw.com.marco.web.library.dao.LibraryDao;
import tw.com.marco.web.library.service.LibraryService;

@RestController
@RequestMapping("library")
public class LibraryController {
	@Autowired
	private LibraryService service;
	@Autowired
	private LibraryDao dao;

	@GetMapping("manage")
	public List<Inventory> manage() {
		return dao.selectInventoryByStatus();
	}

	@PostMapping("/rent/{inventoryId}")
	public RecordResult rent(@PathVariable Integer inventoryId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			RecordResult result = new RecordResult();
			result.setMessage("未登入");
			result.setSuccessful(false);
			return result;
		}
		return service.borrowBook(user.getUserId(), inventoryId);
	}

	@PutMapping("/return/{inventoryId}")
	public RecordResult returnBook(@PathVariable Integer inventoryId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			RecordResult result = new RecordResult();
			result.setMessage("未登入");
			result.setSuccessful(false);
			return result;
		}
		return service.returnBook(user.getUserId(), inventoryId);
	}

}
