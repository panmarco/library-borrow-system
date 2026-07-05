package tw.com.marco.web.library.service;

import tw.com.marco.core.pojo.AllData;

public interface LibraryService {

	AllData getAllData();

	AllData borrowBook(Integer userId, Integer inventoryId);

	AllData returnBook(Integer userId, Integer inventoryId);
}
