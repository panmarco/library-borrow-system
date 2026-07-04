package tw.com.marco.web.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.marco.core.entity.BorrowingRecord;
import tw.com.marco.core.pojo.RecordResult;
import tw.com.marco.web.library.dao.LibraryDao;
import tw.com.marco.web.library.service.LibraryService;

@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {
	@Autowired
	private LibraryDao dao;

	@Override
	public RecordResult borrowBook(Integer userId, Integer inventoryId) {
		List<BorrowingRecord> records = dao.selectAllBorrowingRecords();
		Integer update = dao.updateInventoryStatus(inventoryId, "已借閱");
		Integer insert = dao.insertBorrowingRecord(userId, inventoryId);
		RecordResult result = new RecordResult();
		if (update < 1 || insert < 1) {
			result.setSuccessful(false);
			result.setMessage("系統異常:借閱失敗");
			result.setData(records);
			return result;
		}
		records = dao.selectAllBorrowingRecords();
		result.setSuccessful(true);
		result.setMessage("借閱成功");
		result.setData(records);
		return result;
	}

	@Override
	public RecordResult returnBook(Integer userId, Integer inventoryId) {
		List<BorrowingRecord> records = dao.selectAllBorrowingRecords();
		Integer update1 = dao.updateInventoryStatus(inventoryId, "在庫");
		Integer update2 = dao.updateReturnTime(userId, inventoryId);

		RecordResult result = new RecordResult();
		if (update1 < 1 || update2 < 1) {
			result.setSuccessful(false);
			result.setMessage("系統異常:歸還失敗");
			result.setData(records);
			return result;
		}
		records = dao.selectAllBorrowingRecords();
		result.setSuccessful(true);
		result.setMessage("歸還成功");
		result.setData(records);
		return result;
	}
}
