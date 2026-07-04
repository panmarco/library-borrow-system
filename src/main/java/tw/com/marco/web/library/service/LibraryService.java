package tw.com.marco.web.library.service;

import tw.com.marco.core.pojo.RecordResult;

public interface LibraryService {

	RecordResult borrowBook(Integer userId, Integer inventoryId);

	RecordResult returnBook(Integer userId, Integer inventoryId);
}
