package tw.com.marco.web.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import tw.com.marco.core.entity.User;
import tw.com.marco.core.pojo.AllData;
import tw.com.marco.web.library.service.LibraryService;

@RestController
@RequestMapping("library")
public class LibraryController {
	@Autowired
	private LibraryService service;

	@GetMapping("manage")
	public AllData manage(@SessionAttribute(required = false) User user) {
		return service.getAllData(user.getUserId());
	}

	@PostMapping("/rent/{inventoryId}")
	public AllData rent(@PathVariable Integer inventoryId, @SessionAttribute(required = false) User user) {
		if (user == null) {
			AllData allData = new AllData();
			allData.setMessage("未登入");
			allData.setSuccessful(false);
			return allData;
		}
		return service.borrowBook(user.getUserId(), inventoryId);
	}

	@PostMapping("/return/{inventoryId}")
	public AllData returnBook(@PathVariable Integer inventoryId, @SessionAttribute(required = false) User user) {
		if (user == null) {
			AllData allData = new AllData();
			allData.setMessage("未登入");
			allData.setSuccessful(false);
			return allData;
		}
		return service.returnBook(user.getUserId(), inventoryId);
	}
}
